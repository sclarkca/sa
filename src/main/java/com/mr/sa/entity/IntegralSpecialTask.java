package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 专项任务
 */
@Data
@Entity
@Table(name = "biz_integral_special_task")
public class IntegralSpecialTask extends BaseModel {

	@PropertyDef(label = "任务名称")
	private String name;

	@PropertyDef(label = "任务类型")
	private String type;

	@PropertyDef(label = "任务开始日期")
	@Temporal(TemporalType.DATE)
	private Date taskStartDate;

	@PropertyDef(label = "任务结束日期")
	@Temporal(TemporalType.DATE)
	private Date taskEndDate;

	@PropertyDef(label = "任务开始时间")
	@Temporal(TemporalType.TIME)
	private Date taskStartTime;

	@PropertyDef(label = "任务结束时间")
	@Temporal(TemporalType.TIME)
	private Date taskEndTime;

	@PropertyDef(label = "积分值")
	private Integer integralValue;

	@Transient
	@PropertyDef(label = "是否选中")
	private boolean checked;

}