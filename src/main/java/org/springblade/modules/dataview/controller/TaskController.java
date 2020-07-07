/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.dataview.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.dataview.entity.Task;
import org.springblade.modules.dataview.service.ITaskService;
import org.springblade.modules.dataview.vo.TaskVO;
import org.springblade.modules.dataview.wrapper.TaskWrapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 *  控制器
 *
 * @author Blade
 * @since 2020-07-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("bdc-data/task")
@Api(value = "", tags = "接口")
public class TaskController extends BladeController {

	private ITaskService taskService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入task")
	public R<Task> detail(Task task) {
		Task detail = taskService.getOne(Condition.getQueryWrapper(task));
		return R.data(detail);
	}

	/**
	 * 分页 
	 */
//	@GetMapping("/list")
//	@ApiOperationSupport(order = 2)
//	@ApiOperation(value = "分页", notes = "传入task")
//	public R<IPage<Task>> list(Task task, Query query) {
//		IPage<Task> pages = taskService.page(Condition.getPage(query), Condition.getQueryWrapper(task));
//		return R.data(pages);
//	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taskcode", value = "任务编号", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "taskname", value = "任务名称", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入task")
	public R<IPage<TaskVO>> list(@ApiIgnore @RequestParam Map<String, Object> task, Query query) {
		IPage<Task> pages = taskService.page(Condition.getPage(query), Condition.getQueryWrapper(task, Task.class));
		return R.data(TaskWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入task")
	public R<IPage<TaskVO>> page(TaskVO task, Query query) {
		IPage<TaskVO> pages = taskService.selectTaskPage(Condition.getPage(query), task);
		return R.data(pages);
	}

	/**
	 * 新增 
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入task")
	public R save(@Valid @RequestBody Task task) {
		return R.status(taskService.save(task));
	}

	/**
	 * 修改 
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入task")
	public R update(@Valid @RequestBody Task task) {
		return R.status(taskService.updateById(task));
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入task")
	public R submit(@Valid @RequestBody Task task) {
		return R.status(taskService.saveOrUpdate(task));
	}

	/**
	 * 获取菜单树形结构
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value= "树形结构", notes = "树形结构")
	public R<List<TaskVO>> tree() {
		List<TaskVO> tree = taskService.tree();
		return R.data(tree);
	}

	/**
	 * 删除 
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(taskService.removeByIds(Func.toLongList(ids)));
	}


	
}
