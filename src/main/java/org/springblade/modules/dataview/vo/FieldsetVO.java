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
package org.springblade.modules.dataview.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.modules.dataview.entity.Fieldset;

/**
 * 视图实体类
 *
 * @author Blade
 * @since 2020-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FieldsetVO对象", description = "FieldsetVO对象")
public class FieldsetVO extends Fieldset {
	private static final long serialVersionUID = 1L;

	/**
	 * 指标类型名称
	 */
	@ApiModelProperty(value = "指标类型名称")
	private String columnTypeName;

	/**
	 * 是否可以为空名称
	 */
	@ApiModelProperty(value = "是否可以为空名称")
	private String columnIsnullName;

}
