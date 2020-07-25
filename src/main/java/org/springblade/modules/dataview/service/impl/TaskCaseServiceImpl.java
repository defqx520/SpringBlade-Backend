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
package org.springblade.modules.dataview.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.modules.dataview.entity.Task;
import org.springblade.modules.dataview.mapper.TaskMapper;
import org.springblade.modules.dataview.service.ITaskCaseService;
import org.springblade.modules.dataview.vo.TaskVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  服务实现类
 *
 * @author Blade
 * @since 2020-07-07
 */
@Service
public class TaskCaseServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskCaseService {

	@Override
	public IPage<TaskVO> selectTaskPage(IPage<TaskVO> page, TaskVO task) {
		return page.setRecords(baseMapper.selectTaskPage(page, task));
	}

	@Override
	public List<TaskVO> tree() {
		return ForestNodeMerger.merge(baseMapper.tree());
	}

}
