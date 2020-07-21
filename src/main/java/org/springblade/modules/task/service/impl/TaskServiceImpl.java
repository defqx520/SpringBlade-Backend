package org.springblade.modules.task.service.impl;

import org.apache.commons.lang.StringUtils;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.springblade.modules.metamodel.utils.Neo4jUtil;
import org.springblade.modules.task.entity.*;
import org.springblade.modules.task.mapper.DispatchMapper;
import org.springblade.modules.task.mapper.ProductHardwareMapper;
import org.springblade.modules.task.service.IQuestionService;
import org.springblade.modules.task.service.ITableNodeMapService;
import org.springblade.modules.task.service.ITaskNodeMapService;
import org.springblade.modules.task.service.ITaskService;
import org.springblade.modules.task.vo.TableNodeMapVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service("taskServiceImpl")
@Transactional
public class TaskServiceImpl implements ITaskService{
	@Autowired
	private ITaskNodeMapService taskNodeMapService;
	@Autowired
	private ITableNodeMapService tableNodeMapService;
	@Autowired
	private Neo4jUtil neo4jUtil;

	@Autowired
	private DispatchMapper dispatchMapper;
	@Autowired
	private ProductHardwareMapper productHardwareMapper;
	@Autowired
	private IQuestionService questionService;

	/**
	 * 根据任务ID与选择的四类（输入、输出、资源、约束）返回相应实体数据
	 * @param taskId
	 * @param type
	 * @return  [{
	 * 			  "type":"form/table",
	 * 			  "title":"实物装备",
	 * 			  "data":[{"label":"表单名称1","content":"表单内容1"},{"label":"表单名称2","content":"表单内容2"}]
	 * 			  },
	 *            {
	 *  		  "type":"form/table",
	 *  		  "title":"任务要求",
	 *  		  "data":[{"label":"表单名称1","content":"表单内容1"},{"label":"表单名称2","content":"表单内容2"}]
	 * 	 * 		  },
	 * 			]
	 */
	@Override
	public Map<String, String> getTaskInfo(String taskId, String type) {
		Map<String,String> finalResult = new HashMap<>();
		List<Map<String, Value>> refer = new ArrayList<>();
		TaskNodeMap taskNodeMap = taskNodeMapService.getById(taskId);
		if(taskNodeMap != null){
			String graphId = taskNodeMap.getNodeId();   //任务类型节点
			String rType = type.equals("input")?"输入":(type.equals("output")?"输出":(type.equals("resource")?"资源":"约束")); //关系类型
			String cql = "match (n)-[:"+rType+"]->(m) where id(n)="+Integer.parseInt(graphId)+" return id(m) as id, labels(m), properties(m) as properties";  //查询四类的图节点
			List<Record> records = neo4jUtil.getNeoNodesOrEdges(cql);  //查询相应实体
			for(Record r : records){
				List<Value> values = r.values();
				Map<String, Value> map = new HashMap<>();
				map.put("id",values.get(0));
				map.put("label",values.get(1));
				map.put("properties", values.get(2));
				refer.add(map);
			}
			//查询每个实体对应的表，再去对应的实体表查询对应的字段信息
			for(Map<String, Value>  m : refer){
				List<TableNodeMap> tableNodeMapList = tableNodeMapService.getByNodeId(m.get("id").toString());   //查询表-图节点映射关系
				Value properties =  m.get("properties");   //查询这个实体的哪些列
				if(tableNodeMapList!= null && tableNodeMapList.size()>0){
					String tableName = tableNodeMapList.get(0).getTableName();  //默认一个图节点对应一张表，故只取第一个表

					if("dev_dispatch".equals(tableName)){    //查询调度单表
						List<Dispatch> dispatches = dispatchMapper.getByTaskId(taskId);
						if (dispatches != null && dispatches.size() > 0){
							Dispatch dispatch = dispatches.get(0);
							StringBuilder sb = new StringBuilder();
							for(String key : properties.keys()){
								if(!"label".equals(key)){   //label是为了节点显示的关键字 不作为数据库查询字段
									Class clz = Dispatch.class;
									try {
										Method method = clz.getDeclaredMethod(property2GetMethod(key),null);
										Object invoke = method.invoke(dispatch);
										String ret = "";
										if(invoke instanceof Integer){
											ret = invoke.toString();
										}else{
											ret = invoke.toString();
										}
										if(ret != null && !"".equals(ret)){
											sb.append(properties.get(key).asString()+"：");
											sb.append(ret+"；");
										}
									} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
										e.printStackTrace();
									}
								}
							}
							finalResult.put(m.get("label").get(0).asString(), sb.toString());
						}
					}else if("dev_product_hardware".equals(tableName)){  //查询实体装备
						List<ProductHardware> productHardwares = productHardwareMapper.getByTaskId(taskId);
						if (productHardwares != null && productHardwares.size() > 0){
							ProductHardware productHardware = productHardwares.get(0);
							StringBuilder sb = new StringBuilder();
							for(String key : properties.keys()){
								if(!"label".equals(key)){   //label是为了节点显示的关键字 不作为数据库查询字段
									Class clz = ProductHardware.class;
									try {
										Method method = clz.getDeclaredMethod(property2GetMethod(key),null);
										Object invoke = method.invoke(productHardware);
										String ret = "";
										if(invoke instanceof Integer){
											ret = invoke.toString();
										}else{
											ret = invoke.toString();
										}
										if(ret != null && !"".equals(ret)){
											sb.append(properties.get(key).asString()+"：");
											sb.append(ret+"；");
										}
									} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
										e.printStackTrace();
									}
								}
							}
							finalResult.put(m.get("label").get(0).asString(), sb.toString());
						}
					}else if("dev_question".equals(tableName)){  //查询实体装备
						List<Question> questions = questionService.getByTaskId(taskId);
						if (questions != null && questions.size() > 0){
							Question question = questions.get(0);
							StringBuilder sb = new StringBuilder();
							for(String key : properties.keys()){
								if(!"label".equals(key)){   //label是为了节点显示的关键字 不作为数据库查询字段
									Class clz = Question.class;
									try {
										Method method = clz.getDeclaredMethod(property2GetMethod(key),null);
										Object invoke = method.invoke(question);
										String ret = "";
										if(invoke instanceof Integer){
											ret = invoke.toString();
										}else{
											ret = invoke.toString();
										}
										if(ret != null && !"".equals(ret)){
											sb.append(properties.get(key).asString()+"：");
											sb.append(ret+"；");
										}
									} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
										e.printStackTrace();
									}
								}
							}
							finalResult.put(m.get("label").get(0).asString(), sb.toString());
						}
					}
				}
			}
		}
		return finalResult;
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

//	@Override
//	public Map<String, String> getTaskInfo(String taskId, String type) {
//		Map<String,String> finalResult = new HashMap<>();
//		List<Map<String, Value>> refer = new ArrayList<>();
//		TaskNodeMap taskNodeMap = taskNodeMapService.getById(taskId);
//		if(taskNodeMap != null){
//			String graphId = taskNodeMap.getNodeId();   //任务类型节点
//			String rType = type.equals("input")?"输入":(type.equals("output")?"输出":(type.equals("resource")?"资源":"约束")); //关系类型
//			String cql = "match (n)-[:"+rType+"]->(m) where id(n)="+Integer.parseInt(graphId)+" return id(m) as id, labels(m), properties(m) as properties";  //查询四类的图节点
//			List<Record> records = neo4jUtil.getNeoNodesOrEdges(cql);  //查询相应实体
//			for(Record r : records){
//				List<Value> values = r.values();
//				Map<String, Value> map = new HashMap<>();
//				map.put("id",values.get(0));
//				map.put("label",values.get(1));
//				map.put("properties", values.get(2));
//				refer.add(map);
//			}
//			//查询每个实体对应的表，再去对应的实体表查询对应的字段信息
//			for(Map<String, Value>  m : refer){
//				List<TableNodeMap> tableNodeMapList = tableNodeMapService.getByNodeId(m.get("id").toString());   //查询表-图节点映射关系
//				Value properties =  m.get("properties");   //查询这个实体的哪些列
//				if(tableNodeMapList!= null && tableNodeMapList.size()>0){
//					String tableName = tableNodeMapList.get(0).getTableName();  //默认一个图节点对应一张表，故只取第一个表
//
//					if("dev_dispatch".equals(tableName)){    //查询调度单表
//						List<Dispatch> dispatches = dispatchMapper.getByTaskId(taskId);
//						if (dispatches != null && dispatches.size() > 0){
//							Dispatch dispatch = dispatches.get(0);
//							StringBuilder sb = new StringBuilder();
//							for(String key : properties.keys()){
//								if(!"label".equals(key)){   //label是为了节点显示的关键字 不作为数据库查询字段
//									Class clz = Dispatch.class;
//									try {
//										Method method = clz.getDeclaredMethod(property2GetMethod(key),null);
//										Object invoke = method.invoke(dispatch);
//										String ret = "";
//										if(invoke instanceof Integer){
//											ret = invoke.toString();
//										}else{
//											ret = invoke.toString();
//										}
//										if(ret != null && !"".equals(ret)){
//											sb.append(properties.get(key).asString()+"：");
//											sb.append(ret+"；");
//										}
//									} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//										e.printStackTrace();
//									}
//								}
//							}
//							finalResult.put(m.get("label").get(0).asString(), sb.toString());
//						}
//					}else if("dev_product_hardware".equals(tableName)){  //查询实体装备
//						List<ProductHardware> productHardwares = productHardwareMapper.getByTaskId(taskId);
//						if (productHardwares != null && productHardwares.size() > 0){
//							ProductHardware productHardware = productHardwares.get(0);
//							StringBuilder sb = new StringBuilder();
//							for(String key : properties.keys()){
//								if(!"label".equals(key)){   //label是为了节点显示的关键字 不作为数据库查询字段
//									Class clz = ProductHardware.class;
//									try {
//										Method method = clz.getDeclaredMethod(property2GetMethod(key),null);
//										Object invoke = method.invoke(productHardware);
//										String ret = "";
//										if(invoke instanceof Integer){
//											ret = invoke.toString();
//										}else{
//											ret = invoke.toString();
//										}
//										if(ret != null && !"".equals(ret)){
//											sb.append(properties.get(key).asString()+"：");
//											sb.append(ret+"；");
//										}
//									} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//										e.printStackTrace();
//									}
//								}
//							}
//							finalResult.put(m.get("label").get(0).asString(), sb.toString());
//						}
//					}else if("dev_question".equals(tableName)){  //查询实体装备
//						List<Question> questions = questionService.getByTaskId(taskId);
//						if (questions != null && questions.size() > 0){
//							Question question = questions.get(0);
//							StringBuilder sb = new StringBuilder();
//							for(String key : properties.keys()){
//								if(!"label".equals(key)){   //label是为了节点显示的关键字 不作为数据库查询字段
//									Class clz = Question.class;
//									try {
//										Method method = clz.getDeclaredMethod(property2GetMethod(key),null);
//										Object invoke = method.invoke(question);
//										String ret = "";
//										if(invoke instanceof Integer){
//											ret = invoke.toString();
//										}else{
//											ret = invoke.toString();
//										}
//										if(ret != null && !"".equals(ret)){
//											sb.append(properties.get(key).asString()+"：");
//											sb.append(ret+"；");
//										}
//									} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//										e.printStackTrace();
//									}
//								}
//							}
//							finalResult.put(m.get("label").get(0).asString(), sb.toString());
//						}
//					}
//				}
//			}
//		}
//		return finalResult;
//	}

}
