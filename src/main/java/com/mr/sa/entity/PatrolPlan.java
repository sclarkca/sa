package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 巡防活动
 */
@Data
@Entity
@Table(name = "biz_patrol_plan")
public class PatrolPlan extends BaseModel {

	@PropertyDef(label = "计划名称")
	@Column(name = "name", length = 16)
	private String name;

	@PropertyDef(label = "巡防任务项ID")
	@Column(name = "patrol_task_item_id", length = 16)
	private String patrolTaskItemId;

	@PropertyDef(label = "组织ID")
	@Column(name = "org_id", length = 16)
	private String orgId;

	@PropertyDef(label = "下发时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "announce_time")
	private Date announceTime;

	@PropertyDef(label = "巡防开始时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "patrol_start_time")
	private Date patrolStartTime;

	@PropertyDef(label = "巡防结束时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "patrol_end_time")
	private Date patrolEndTime;

	@PropertyDef(label = "规定完成比例")
	@Column(name = "done_ratio", length = 11)
	private Integer doneRatio;

	@PropertyDef(label = "完成状态")
	@Column(name = "work_status", length = 16)
	private String workStatus;

	@PropertyDef(label = "原因")
	@Column(name = "reason", length = 16)
	private String reason;

	@Transient
	@PropertyDef(label = "扫码总数")
	private Long countTotal;
	
	@Transient
	@PropertyDef(label = "必扫总数")
	private Long countRequired;
	
	@Transient
	@PropertyDef(label = "已扫总数")
	private Long countDone;

	@Transient
	@PropertyDef(label = "组织")
	private String orgName;
}