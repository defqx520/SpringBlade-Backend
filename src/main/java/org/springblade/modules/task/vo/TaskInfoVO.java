package org.springblade.modules.task.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

   /**      [{
	* 			  "type":"table",
	* 			  "title":"实物装备",
	* 			  "columns":[{"prop":"productName","label":"故障产品名称"},{"prop":"reason",”label“:”故障原因“}]
	* 			  "data":[{"productName":"键盘","reason":"人为损坏"},{"productName":"鼠标","reason":"接触不良"}]
	* 			  },
	*            {
	*  		  "type":"form",
	*  		  "title":"任务要求",
	*  		  "data":[{"label":"调度单号","content":"2019-ADK9B-1511"},{"label":"任务描述","content":"前往青岛之行任务"}]
	* 	  		  },
	*            {
	* 	     	  "type":"file",
	* 	  		  "title":"雷达数据",
	* 	  		  "data":[{"url":"D:/xxx"},{"url":"D:/xxx"]
	* 	  	 	  },
	* 			]
    */
@Data
public class TaskInfoVO {
	/**
	 * 返回数据的展示类型 form、table、file
	 */
	private String type;
	/**
	 * 该项数据的展示大标题
	 */
	private String title;
	/**
	 * 当返回结果以表格形式展示时，使用tableColumns定义表格的头部
	 */
	private List<Map<String, Object>> tableColumns;
	/**
	 * 返回的数据列表
	 */
	private List<Map<String, Object>> data;

}
