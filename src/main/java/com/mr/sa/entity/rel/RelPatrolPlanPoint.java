package com.mr.sa.entity.rel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_rel_patrol_plan_point")
public class RelPatrolPlanPoint extends BaseModel{

	@PropertyDef(label = "巡防计划ID")
	@Column(name = "patrol_plan_id", length = 32)
	private String patrolPlanId;

	@PropertyDef(label = "巡防点ID")
	@Column(name = "patrol_point_id", length = 64)
	private String patrolPointId;

	@PropertyDef(label = "是否为必扫点")
	@Column(name = "required", length = 8)
	private boolean required;

}