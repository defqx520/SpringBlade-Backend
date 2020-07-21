package org.springblade.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("dev_product_hardware")
@ApiModel(value = "装备实体对象", description = "装备实体对象")
public class ProductHardware implements Serializable {

	private static final long serialVersionUID = 1269685623367123384L;
	@TableId(value = "id")
	private String id;
	@ApiModelProperty(value = "武器系统编号")
	private String armsNo;
	@ApiModelProperty(value = "军兵种")
	private String army;
	@ApiModelProperty(value = "批次号")
	private String batch;
	@ApiModelProperty(value = "数量")
	private Integer count;
	@ApiModelProperty(value = "火力（战术）单元名称")
	private String fireUnit;
	@ApiModelProperty(value = "型号")
	private String model;
	@ApiModelProperty(value = "部队名称")
	private String troopsName;
	@ApiModelProperty(value = "部队位置")
	private String troopsStation;
	@ApiModelProperty(value = "战区")
	private String warZone;
	@ApiModelProperty(value = "任务ID")
	private String taskId;

}
