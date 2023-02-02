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

@Data
@Entity
@Table(name = "biz_org_plan")
public class OrgPlan extends BaseModel {

	@PropertyDef(label = "巡防任务ID")
	@Column(name = "patrol_task_item_id", length = 32)
	private String patrolTaskItemId;

	@PropertyDef(label = "单位ID")
	@Column(name = "org_id", length = 64)
	private String orgId;

	@PropertyDef(label = "巡防开始时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "patrol_start_time")
	private Date patrolStartTime;

	@PropertyDef(label = "巡防结束时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "patrol_end_time")
	private Date patrolEndTime;

	@Transient
	private Org org;
}
