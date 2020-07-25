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

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.dataview.entity.Fieldset;
import org.springblade.modules.dataview.vo.FieldsetVO;
import org.springblade.modules.system.service.IDictService;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-07-08
 */
public class FieldsetWrapper extends BaseEntityWrapper<Fieldset, FieldsetVO>  {

	private static IDictService dictService;

	static {
		dictService = SpringUtil.getBean(IDictService.class);
	}

    public static FieldsetWrapper build() {
        return new FieldsetWrapper();
    }

	@Override
	public FieldsetVO entityVO(Fieldset fieldset) {
		FieldsetVO fieldsetVO = BeanUtil.copy(fieldset, FieldsetVO.class);
		fieldsetVO.setColumnTypeName(dictService.getValue("data_field_type", fieldset.getColumnType()));
		fieldsetVO.setColumnIsnullName(dictService.getValue("yes_no", fieldset.getColumnIsnull()));
		return fieldsetVO;
	}

}
