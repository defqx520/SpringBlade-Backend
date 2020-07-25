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
package org.springblade.modules.dataview.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author Blade
 * @since 2020-07-08
 */
@Data
@TableName("bdc_fieldset")
@ApiModel(value = "Fieldset对象", description = "Fieldset对象")
public class Fieldset implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
    /**
     * 指标名称
     */
    @ApiModelProperty(value = "指标名称")
    private String columnName;
    /**
     * 指标类型
     */
    @ApiModelProperty(value = "指标类型")
    private String columnType;
    /**
     * 指标长度
     */
    @ApiModelProperty(value = "指标长度")
    private Integer columnLength;
    /**
     * 小数点位数
     */
    @ApiModelProperty(value = "小数点位数")
    private Integer columnPoint;
  private String columnDefaultValue;
    /**
     * 指标注释
     */
    @ApiModelProperty(value = "指标注释")
    private String columnNote;
    /**
     * 是否允许为空
     */
    @ApiModelProperty(value = "是否允许为空")
    private String columnIsnull;
    /**
     * 所属专业
     */
    @ApiModelProperty(value = "关联节点")
    private Long datanodeId;


}
