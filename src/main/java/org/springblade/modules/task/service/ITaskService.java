package org.springblade.modules.task.service;

import org.springblade.modules.task.dto.Page;
import org.springblade.modules.task.vo.TaskInfoVO;

import java.util.List;
import java.util.Map;

public interface ITaskService {

	List<TaskInfoVO> getTaskInfo(String taskId, String type);

	Map<String, Object> fetchFileData(String tableName, String attachId, Long currentPage);

	List<Map<String, String>> getColumn(String tableName);
}
