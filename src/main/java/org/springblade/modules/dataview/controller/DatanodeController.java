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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.dataview.entity.Datanode;
import org.springblade.modules.dataview.entity.Fieldset;
import org.springblade.modules.dataview.service.IDatanodeService;
import org.springblade.modules.dataview.service.IDefineTableStructureService;
import org.springblade.modules.dataview.service.IFieldsetService;
import org.springblade.modules.dataview.vo.DatanodeVO;
import org.springblade.modules.dataview.vo.FieldsetVO;
import org.springblade.modules.dataview.wrapper.DatanodeWrapper;
import org.springblade.modules.dataview.wrapper.FieldsetWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 控制器
 *
 * @author Blade
 * @since 2020-07-08
 */
@RestController
@AllArgsConstructor
@RequestMapping("bdc-data/datanode")
@Api(value = "", tags = "接口")
public class DatanodeController extends BladeController {

	private IDatanodeService datanodeService;
	private IDefineTableStructureService defineTableStructureService;
	private IFieldsetService fieldsetService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入datanode")
	public R<DatanodeVO> detail(Datanode datanode) {
		Datanode detail = datanodeService.getOne(Condition.getQueryWrapper(datanode));
		return R.data(DatanodeWrapper.build().entityVO(detail));
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入datanode")
	public R<IPage<DatanodeVO>> list(Datanode datanode, Query query) {
		IPage<Datanode> pages = datanodeService.page(Condition.getPage(query), Condition.getQueryWrapper(datanode));
		return R.data(DatanodeWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入datanode")
	public R<IPage<DatanodeVO>> page(DatanodeVO datanode, Query query) {
		IPage<DatanodeVO> pages = datanodeService.selectDatanodePage(Condition.getPage(query), datanode);
		return R.data(pages);
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Long> list = Func.toLongList(ids);
		for (Long id : list) {
			int removeResult = fieldsetService.removeByParentID(id);
			//首先删除已有的子表数据
			if (removeResult < 0) {
				R.status(false);
				return R.fail("删除id为" + id + "对应的数据表结构失败！");
			}
		}

		return R.status(datanodeService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入datanode")
	public R save(@Valid @RequestBody DatanodeVO datanodeVO) {
		//获取datanode并进行保存
		Datanode datanode = BeanUtil.copy(datanodeVO, Datanode.class);
		R submitResult = R.status(datanodeService.save(datanode));

		if (submitResult.isSuccess()) {
			if (!datanodeService.saveFieldSets(datanodeVO, datanode)) {
				R.status(false);
				R.fail("定义数据结构失败！");
			}
		}

		return submitResult;
	}

	/**
	 * 同步
	 */
	@PostMapping("/share")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "同步", notes = "传入ids")
	public R share(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Long> list = Func.toLongList(ids);
		for (Long id : list) {
			List<Fieldset> removeResult = fieldsetService.getByDatanodeID(id);
			String tableName = datanodeService.getById(id).getDatanodeCode();
			String createTableSQL = "create table ";
			String columnsets = "( id  bigint(64) NOT NULL COMMENT '主键id',\n";
			columnsets = columnsets + "attach_id  bigint(64) NULL COMMENT '原始文件id',\n";
//			columnsets = columnsets + "parent_id  bigint(64) NOT NULL COMMENT '关联节点id',\n";
			columnsets = columnsets + "sort  bigint(64) NOT NULL DEFAULT 0 COMMENT '排序字段',\n";
			for (Fieldset fd : removeResult) {
				FieldsetVO fd_t = FieldsetWrapper.build().entityVO(fd);
				columnsets = columnsets + " " + fd_t.getColumnName() + " " + fd_t.getColumnType() + "("
					+ fd_t.getColumnLength();

				//浮点型数据创建小数点位
				if(fd_t.getColumnType().equals("decimal") || fd_t.getColumnType().equals("double") || fd_t.getColumnType().equals("float")){
					if(fd_t.getColumnPoint() != null){
						columnsets = columnsets + ", " + fd_t.getColumnPoint() + ") ";
					}
				}else{
					columnsets = columnsets + ") ";
				}

				//是否可以为空
				if (fd_t.getColumnIsnullName().equals('否')) {
					columnsets += "NOT NULL ";
				}else{
					columnsets += " NULL ";
				}

				//默认值
				if (fd_t.getColumnDefaultValue() != null) {
					columnsets += "DEFAULT " + fd_t.getColumnDefaultValue();
				} else {
					columnsets += "DEFAULT NULL ";
				}

				//注释
				if(fd_t.getColumnNote() != null) {
					columnsets += "COMMENT '" + fd_t.getColumnNote() + "',\n";
				}else{
					columnsets += "COMMENT '',\n";
				}
			}
			columnsets += "PRIMARY KEY (id)\n";
			columnsets += ") ENGINE=InnoDB\n" +
				" DEFAULT CHARSET=utf8\n" +
				"ROW_FORMAT=COMPACT\n";
			if(datanodeService.getById(id).getDatanodeName() != null) {
				columnsets += "COMMENT='" + datanodeService.getById(id).getDatanodeName() + "'\n";
			}
			columnsets += ";\n";

			createTableSQL = createTableSQL + tableName + columnsets;

			if(!defineTableStructureService.createTable(tableName, createTableSQL)){
				R.status(false);
				return R.fail("定义数据结构"+tableName+"失败！该结构已存在或创建语句存在错误.");
			}
		}
		R.status(true);
		return R.success("动态创建数据结构成功！");
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入datanode")
	public R submit(@Valid @RequestBody DatanodeVO datanodeVO) {
		//获取datanode并进行保存
		Datanode datanode = BeanUtil.copy(datanodeVO, Datanode.class);
		R submitResult = R.status(datanodeService.saveOrUpdate(datanode));

		if (submitResult.isSuccess()) {
			if (!datanodeService.saveFieldSets(datanodeVO, datanode)) {
				R.status(false);
				R.fail("定义数据结构失败！");
			}
		}
		return submitResult;
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入datanode")
	public R update(@Valid @RequestBody DatanodeVO datanodeVO) {
		//获取datanode并进行保存
		Datanode datanode = BeanUtil.copy(datanodeVO, Datanode.class);
		R submitResult = R.status(datanodeService.updateById(datanode));

		if (submitResult.isSuccess()) {
			if (!datanodeService.saveFieldSets(datanodeVO, datanode)) {
				R.status(false);
				R.fail("定义数据结构失败！");
			}
		}

		return submitResult;
	}

}
