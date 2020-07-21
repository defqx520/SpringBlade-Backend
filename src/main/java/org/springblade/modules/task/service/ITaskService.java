package org.springblade.modules.task.service;

import java.util.List;
import java.util.Map;

public interface ITaskService {

	Map<String, String> getTaskInfo(String taskId, String type);
}
