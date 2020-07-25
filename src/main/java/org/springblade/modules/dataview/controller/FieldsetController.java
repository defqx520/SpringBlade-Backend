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
import org.springblade.modules.dataview.entity.Fieldset;
import org.springblade.modules.dataview.vo.FieldsetVO;
import org.springblade.modules.dataview.wrapper.FieldsetWrapper;
import org.springblade.modules.dataview.service.IFieldsetService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 *  控制器
 *
 * @author Blade
 * @since 2020-07-08
 */
@RestController
@AllArgsConstructor
@RequestMapping("bdc-data/fieldset")
@Api(value = "", tags = "接口")
public class FieldsetController extends BladeController {

	private IFieldsetService fieldsetService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入fieldset")
	public R<FieldsetVO> detail(Fieldset fieldset) {
		Fieldset detail = fieldsetService.getOne(Condition.getQueryWrapper(fieldset));
		return R.data(FieldsetWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入fieldset")
	public R<IPage<FieldsetVO>> list(Fieldset fieldset, Query query) {
		IPage<Fieldset> pages = fieldsetService.page(Condition.getPage(query), Condition.getQueryWrapper(fieldset));
		return R.data(FieldsetWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入fieldset")
	public R<IPage<FieldsetVO>> page(FieldsetVO fieldset, Query query) {
		IPage<FieldsetVO> pages = fieldsetService.selectFieldsetPage(Condition.getPage(query), fieldset);
		return R.data(pages);
	}

	/**
	 * 新增 
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入fieldset")
	public R save(@Valid @RequestBody Fieldset fieldset) {
		return R.status(fieldsetService.save(fieldset));
	}

	/**
	 * 修改 
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入fieldset")
	public R update(@Valid @RequestBody Fieldset fieldset) {
		return R.status(fieldsetService.updateById(fieldset));
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入fieldset")
	public R submit(@Valid @RequestBody Fieldset fieldset) {
		return R.status(fieldsetService.saveOrUpdate(fieldset));
	}

	
	/**
	 * 删除 
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(fieldsetService.removeByIds(Func.toLongList(ids)));
	}

	
}
