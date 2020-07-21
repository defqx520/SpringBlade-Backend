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
package org.springblade.modules.task.controller;

import io.swagger.annotations.Api;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.task.entity.TaskNodeMap;
import org.springblade.modules.task.vo.TaskNodeMapVO;
import org.springblade.modules.task.service.ITaskNodeMapService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 *  控制器
 *
 * @author Blade
 * @since 2020-07-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("task/tasknodemap")
@Api(value = "", tags = "接口")
public class TaskNodeMapController extends BladeController {

	private ITaskNodeMapService taskNodeMapService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入taskNodeMap")
	public R<TaskNodeMap> detail(TaskNodeMap taskNodeMap) {
		TaskNodeMap detail = taskNodeMapService.getOne(Condition.getQueryWrapper(taskNodeMap));
		return R.data(detail);
	}

	/**
	 * 分页 
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入taskNodeMap")
	public R<IPage<TaskNodeMap>> list(TaskNodeMap taskNodeMap, Query query) {
		IPage<TaskNodeMap> pages = taskNodeMapService.page(Condition.getPage(query), Condition.getQueryWrapper(taskNodeMap));
		return R.data(pages);
	}

	/**
	 * 自定义分页 
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入taskNodeMap")
	public R<IPage<TaskNodeMapVO>> page(TaskNodeMapVO taskNodeMap, Query query) {
		IPage<TaskNodeMapVO> pages = taskNodeMapService.selectTaskNodeMapPage(Condition.getPage(query), taskNodeMap);
		return R.data(pages);
	}

	/**
	 * 新增 
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入taskNodeMap")
	public R save(@Valid @RequestBody TaskNodeMap taskNodeMap) {
		return R.status(taskNodeMapService.save(taskNodeMap));
	}

	/**
	 * 修改 
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入taskNodeMap")
	public R update(@Valid @RequestBody TaskNodeMap taskNodeMap) {
		return R.status(taskNodeMapService.updateById(taskNodeMap));
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入taskNodeMap")
	public R submit(@Valid @RequestBody TaskNodeMap taskNodeMap) {
		return R.status(taskNodeMapService.saveOrUpdate(taskNodeMap));
	}

	
	/**
	 * 删除 
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(taskNodeMapService.removeByIds(Func.toLongList(ids)));
	}

	
}
