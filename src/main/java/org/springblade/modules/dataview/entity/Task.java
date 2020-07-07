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
 * @since 2020-07-07
 */
@Data
@TableName("bdc_task")
@ApiModel(value = "Task对象", description = "Task对象")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 菜单父主键
	 */
	@ApiModelProperty(value = "父主键")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

	/**
     * 任务类型
     */
    @ApiModelProperty(value = "任务类型")
    private String tasktype;
    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String taskname;
    /**
     * 任务描述
     */
    @ApiModelProperty(value = "任务描述")
    private String taskdesc;
    /**
     * 任务编码
     */
    @ApiModelProperty(value = "任务编码")
    private String taskcode;
    /**
     * 所属型号
     */
    @ApiModelProperty(value = "所属型号")
    private Long modelid;
    /**
     * 大数据统一编码
     */
    @ApiModelProperty(value = "大数据统一编码")
    private String unicode;


}
