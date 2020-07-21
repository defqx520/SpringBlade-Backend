package org.springblade.modules.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("dev_dispatch")
@ApiModel(value = "调度单对象", description = "调度单对象")
public class Dispatch {

	@TableId(value = "id")
	private String id;
	@ApiModelProperty(value = "任务要求")
	private String detailContent;
	@ApiModelProperty(value = "调度单号")
	private String taskNo;
	@ApiModelProperty(value = "任务ID")
	private String taskId;

}
