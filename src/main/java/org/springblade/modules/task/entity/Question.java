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
package org.springblade.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体类
 *
 * @author Blade
 * @since 2020-07-20
 */
@Data
@TableName("dev_question_view")
@ApiModel(value = "Question对象", description = "Question对象")
public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String model;
	private LocalDate feedbackDate;
	private String informNo;
	private String systenName;
	private String systemCode;
	private String faultProductName;
	private String faultProductCode;
	private String faultProductNo;
	private String feedbackInformation;
	private String reasonAnalysis;
	private String reasonType;
	private String solution;
	private String isrepair;
	private String dutyOrg;
	private String outsourceOrg;
	private String useTroops;
	private LocalDate completeDate;
	private String isZero;
	private LocalDate planCompleteDate;


}
