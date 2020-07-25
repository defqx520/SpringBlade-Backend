/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.dataview.wrapper;

import org.springblade.common.constant.CommonConstant;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.dataview.entity.Task;
import org.springblade.modules.dataview.service.ITaskService;
import org.springblade.modules.dataview.vo.TaskVO;
import org.springblade.modules.system.service.IDictService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class TaskWrapper extends BaseEntityWrapper<Task, TaskVO> {

	private static ITaskService taskService;

	private static IDictService dictService;

	static {
		taskService = SpringUtil.getBean(ITaskService.class);
		dictService = SpringUtil.getBean(IDictService.class);
	}

	public static TaskWrapper build() {
		return new TaskWrapper();
	}

	@Override
	public TaskVO entityVO(Task task) {
		TaskVO taskVO = BeanUtil.copy(task, TaskVO.class);
		if (Func.equals(task.getParentId(), CommonConstant.TOP_PARENT_ID)) {
			taskVO.setParentName(CommonConstant.TOP_PARENT_NAME);
		} else {
			Task parent = taskService.getById(task.getParentId());
			taskVO.setParentName(parent.getTaskname());
		}
		taskVO.setModelName(dictService.getValue("model_list", task.getModelid()));
		return taskVO;
	}


	public List<TaskVO> listNodeVO(List<Task> list) {
		List<TaskVO> collect = list.stream().map(task -> BeanUtil.copy(task, TaskVO.class)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}
