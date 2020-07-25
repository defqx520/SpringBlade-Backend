package org.springblade.modules.task.controller;

import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.modules.task.dto.Page;
import org.springblade.modules.task.mapper.MyMapper;
import org.springblade.modules.task.service.ITaskService;
import org.springblade.modules.task.vo.TaskInfoVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@RestController
@RequestMapping("task/show")
@AllArgsConstructor
public class TaskController {

	@Autowired
	public ITaskService iTaskService;


	@GetMapping("/allTasks")
	public List<Map<String, String>> getAllTasks(String nodeID, String type){
		List<Map<String,String>> list = new ArrayList<>();
		return list;
	}

	/**
	 *
	 * @param taskId
	 * @param type
	 * @return
	 */
	@GetMapping("/detail")
	public R<List<TaskInfoVO>> getTaskInfo(String taskId, String type){
		List<TaskInfoVO> res = new ArrayList<>();
		res = iTaskService.getTaskInfo(taskId, type);
		return R.data(res);
	}

	@GetMapping("fileData")
	public R<Map<String, Object>> fetchFileData(String tableName, String attachId, Long currentPage){
		Map<String, Object> res = new HashMap<>();
		res = iTaskService.fetchFileData(tableName, attachId, currentPage);
		return R.data(res);
	}

	@GetMapping("getColumn")
	public R<List<Map<String, String>>> getColumn(String tableName){
		List<Map<String, String>> res = new ArrayList<>();
		res = iTaskService.getColumn(tableName);
		return R.data(res);
	}


//	@GetMapping("detail")
//	public R test(){
//		String sql = "create table bdc_test( id  bigint(64) NOT NULL ,\n" +
//			" name varchar(255)  NULL DEFAULT NULL COMMENT '',\n" +
//			" code decimal(64, 2)  NULL DEFAULT NULL COMMENT '',\n" +
//			"PRIMARY KEY (id)\n" +
//			") ENGINE=InnoDB\n" +
//			" DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci\n" +
//			"ROW_FORMAT=COMPACT\n" +
//			"COMMENT='测试数据表'\n" +
//			";\n";
//		myMapper.createTable(sql);
//		return R.data("chengg");
//	}

}
