package org.springblade.modules.task.service.impl;

import org.apache.commons.lang.StringUtils;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.springblade.modules.metamodel.utils.Neo4jUtil;
import org.springblade.modules.task.dto.Page;
import org.springblade.modules.task.entity.*;
import org.springblade.modules.task.mapper.DispatchMapper;
import org.springblade.modules.task.mapper.MyMapper;
import org.springblade.modules.task.mapper.ProductHardwareMapper;
import org.springblade.modules.task.service.IQuestionService;
import org.springblade.modules.task.service.ITaskNodeMapService;
import org.springblade.modules.task.service.ITaskService;
import org.springblade.modules.task.vo.TaskInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.PUT;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service("taskServiceImpl")
@Transactional
public class TaskServiceImpl implements ITaskService{
	@Autowired
	private ITaskNodeMapService taskNodeMapService;
	@Autowired
	private Neo4jUtil neo4jUtil;

	@Autowired
	private DispatchMapper dispatchMapper;
	@Autowired
	private ProductHardwareMapper productHardwareMapper;
	@Autowired
	private IQuestionService questionService;

	@Autowired
	private MyMapper myMapper;

	/**
	 * 根据任务ID与选择的四类（输入、输出、资源、约束）返回相应实体数据
	 * @param taskId
	 * @param type 返回的结果以什么形式展示 form/table/file
	 * @return  [{
	 * 			  "type":"table",
	 * 			  "title":"实物装备",
	 * 			  "columns":[{"prop":"productName","label":"故障产品名称"},{"prop":"reason",”label“:”故障原因“}]
	 * 			  "data":[{"productName":"键盘","reason":"人为损坏"},{"productName":"鼠标","reason":"接触不良"}]
	 * 			  },
	 *            {
	 *  		  "type":"form",
	 *  		  "title":"任务要求",
	 *  		  "data":[{"label":"调度单号","content":"2019-ADK9B-1511"},{"label":"详情","content":"这是一段详情"}]
	 * 	  		  },
	 *            {
	 * 	     	  "type":"file",
	 * 	  		  "title":"雷达数据",
	 * 	  		  "data":[{"url":"D:/xxx"},{"url":"D:/xxx"]
	 * 	  	 	  },
	 * 			]
	 */
	@Override
	public List<TaskInfoVO> getTaskInfo(String taskId, String type) {
		List<TaskInfoVO> finalResult = new ArrayList<>();
		String rType = type.equals("input")?"输入":(type.equals("output")?"输出":(type.equals("resource")?"资源":"约束"));  //关系类型

		List<Map<String, Value>> refer = new ArrayList<>(); //用于封装多个节点信息
		TaskNodeMap taskNodeMap = taskNodeMapService.getById(taskId);   //根据任务Id查找该任务所属节点   比如某个<实例任务>属于<装备巡检任务>
		if(taskNodeMap != null){
			String graphId = taskNodeMap.getNodeId();
			String cql = "match (n)-[:"+rType+"]->(m) where id(n)="+Integer.parseInt(graphId)+" return id(m) as id, m.neoName as neoName, m.neoResType as neoResType, properties(m) as properties";  //查询四类的图节点
			List<Record> records = neo4jUtil.getNeoNodesOrEdges(cql);  //查询相应实体的集合
			for(Record r : records){
				List<Value> values = r.values();
				Map<String, Value> map = new HashMap<>();
				map.put("id",values.get(0));   			//该实体图节点Id
				map.put("neoName",values.get(1));  		//该实体的标题
				map.put("neoResType",values.get(2));	//该实体展示结果的类型
				map.put("properties", values.get(3));	//该实体需要展示的属性
				refer.add(map);
			}

			//查询每个实体对应的表，再去对应的实体表查询对应的字段信息
			for(Map<String, Value>  oneNode : refer){
				TaskInfoVO taskInfoVO = new TaskInfoVO();  //用于封装某一个实体查询结果
				taskInfoVO.setType(oneNode.get("neoResType").asString());
				taskInfoVO.setTitle(oneNode.get("neoName").asString());
				Value properties =  oneNode.get("properties");   //查询这个实体的需要展示哪些列
				Map<String, Object> propertiesMap = properties.asMap();

				List<Map<String, Object>> cols = new ArrayList<>(); //当展现结果为表格时，需要用用该list封装tableColumns属性
				Set<String> tableSet = new HashSet<>();  //待查询的表集合，目前应该只有一个
				propertiesMap.forEach((key,value)->{
					if(!"neoName".equals(key) && !"neoResType".equals(key)){
						String[] strs = key.split("\\$");   // $是正则中特殊字符 需要进行转义
						tableSet.add(strs[0]);

						if("table".equals(oneNode.get("neoResType").asString()) && strs.length>1) {   //当结果展现形式为表格时，顺便需要封装tableColumns属性
							Map<String, Object> col = new HashMap<>();
							col.put("prop", strs[1]);
							col.put("label", value);
							col.put("align", "center");
//							col.put("width", "200");
							cols.add(col);
						}
					}
				});
				if("table".equals(oneNode.get("neoResType").asString())){
					taskInfoVO.setTableColumns(cols);
				}

				//遍历tableSet中存储的表名，查询数据
				tableSet.forEach(tableName -> {
					List<Map<String, Object>> taskInfo = myMapper.getTaskInfo(taskId, tableName);
					if("form".equals(oneNode.get("neoResType").asString())){  //查询结果应该为1条
						if(taskInfo != null && taskInfo.size()>0){
							List<Map<String, Object>> retDataList = new ArrayList<>();
							//接下来封装第一条数据到表单中
							taskInfo.get(0).forEach((key, value)->{
								Map<String, Object> subMap = new HashMap<>();
								if(propertiesMap.get((tableName+"$"+key))!=null){
									subMap.put("label", propertiesMap.get((tableName+"$"+key)));
									subMap.put("content", value);
									retDataList.add(subMap);
								}
							});
							taskInfoVO.setData(retDataList);
						}
					}else if("table".equals(oneNode.get("neoResType").asString())){  //查询结果应该为多条
						List<Map<String, Object>> retDataList = new ArrayList<>();
						taskInfo.forEach(oneRow->{
							retDataList.add(oneRow);
						});
						taskInfoVO.setData(retDataList);
					}else if("file".equals(oneNode.get("neoResType").asString())){  //查询结果应该为多条
						List<Map<String, Object>> retDataList = new ArrayList<>();
						taskInfo.forEach(oneRow->{
							retDataList.add(oneRow);
						});
						taskInfoVO.setData(retDataList);

					}
					finalResult.add(taskInfoVO);
					System.out.println(finalResult);
				});

			}
		}
		return finalResult;
	}

	@Override
	public Map<String, Object> fetchFileData(String tableName, String attachId, Long currentPage) {
		Map<String, Object> res = new HashMap<>();
		List<Map<String, Object>> dataList = myMapper.fetchFileData(tableName, attachId, (currentPage-1)*10);
		res.put("data", dataList);
		Long total = myMapper.getDataTotal(tableName,  attachId);
		res.put("total", total);
		return res;
	}

	@Override
	public List<Map<String, String>> getColumn(String tableName) {
		List<Map<String, String>> cols = new ArrayList<>();
		List<Map<String, String>> res  = myMapper.getTableColumns(tableName);
		res.forEach(item->{
			Map<String, String> columMap = new HashMap<>();
			if(!"attach_id".equals(item.get("column_name"))){
				columMap.put("prop",item.get("column_name"));
				columMap.put("label",item.get("column_comment"));
				columMap.put("align", "center");
				cols.add(columMap);
			}
		});
		return cols;
	}

	/**
	 * 属性转换成get方法
	 * @param property
	 * @return
	 */
	private String property2GetMethod(String property){
		StringBuilder builder = new StringBuilder();
		Arrays.asList(property.split("_")).forEach(temp -> builder.append(StringUtils.capitalize(temp)));
		return "get"+builder.toString();
	}

}
