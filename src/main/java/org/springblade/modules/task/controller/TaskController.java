package org.springblade.modules.task.controller;

import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.modules.task.service.ITaskService;
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
	@Qualifier("taskServiceImpl")
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
	public R<Map<String, String>> getTaskInfo(String taskId, String type){
		Map<String,String> map = new HashMap<>();
		map = iTaskService.getTaskInfo(taskId, type);
		return R.data(map);
	}

}
