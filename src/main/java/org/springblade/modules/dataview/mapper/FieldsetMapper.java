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
package org.springblade.modules.dataview.mapper;

import org.springblade.modules.dataview.entity.Fieldset;
import org.springblade.modules.dataview.vo.FieldsetVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 *  Mapper 接口
 *
 * @author Blade
 * @since 2020-07-08
 */
public interface FieldsetMapper extends BaseMapper<Fieldset> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param fieldset
	 * @return
	 */
	List<FieldsetVO> selectFieldsetPage(IPage page, FieldsetVO fieldset);

	/**
	 * 通过父id删除子表数据
	 * @param datanodeId
	 * @return
	 */
	int removeByParentID(Long datanodeId);

	/**
	 * 批量插入数据
	 * @param fieldsetList
	 * @return
	 */
	int insertBatch(List<Fieldset> fieldsetList);

	/**
	 * 批量插入数据
	 * @param datanodeId
	 * @return
	 */
	List<Fieldset> getByDatanodeID(Long datanodeId);
}
