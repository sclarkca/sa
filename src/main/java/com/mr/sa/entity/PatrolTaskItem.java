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
@Table(name = "biz_patrol_task_item")
public class PatrolTaskItem extends BaseModel {

	@PropertyDef(label = "巡防任务ID")
	@Column(name = "patrol_task_id", length = 16)
	private String patrolTaskId;

	@PropertyDef(label = "巡防开始时间")
	@Temporal(TemporalType.TIME)
	@Column(name = "patrol_start_time")
	private Date patrolStartTime;

	@PropertyDef(label = "巡防结束时间")
	@Temporal(TemporalType.TIME)
	@Column(name = "patrol_end_time")
	private Date patrolEndTime;

}