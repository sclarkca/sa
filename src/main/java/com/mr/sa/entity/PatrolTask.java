package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 巡防任务
 */
@Data
@Entity
@Table(name = "biz_patrol_task")
public class PatrolTask extends BaseModel {

	@PropertyDef(label = "任务名称")
	private String name;

	@PropertyDef(label = "巡防开始日期")
	@Temporal(TemporalType.TIMESTAMP)
	private Date patrolStartDate;

	@PropertyDef(label = "巡防结束日期")
	@Temporal(TemporalType.TIMESTAMP)
	private Date patrolEndDate;

	@PropertyDef(label = "完成比例")
	private Integer doneRatio;

	@PropertyDef(label = "状态")
	private String activeStatus;

}