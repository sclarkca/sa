package com.mr.sa.entity;

import com.bstek.dorado.annotation.PropertyDef;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "biz_point_org_plan")
public class BizPointOrgPlan extends BaseModel {

	@PropertyDef(label = "巡防任务ID")
	@Column(name = "point_task_item_id", length = 32)
	private String pointTaskItemId;

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
