package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 专项任务
 */
@Data
@Entity
@Table(name = "biz_special_task")
public class SpecialTask extends BaseModel {

	@PropertyDef(label = "任务名称")
	private String name;

	@PropertyDef(label = "任务类型ID")
	private String type;

	@PropertyDef(label = "任务开始时间")
	@Temporal(TemporalType.TIMESTAMP)
	private Date taskStartTime;

	@PropertyDef(label = "任务结束时间")
	@Temporal(TemporalType.TIMESTAMP)
	private Date taskEndTime;

	@PropertyDef(label = "完成比例")
	private Integer doneRatio;

	@Transient
	@PropertyDef(label = "是否选中")
	private boolean checked;

}