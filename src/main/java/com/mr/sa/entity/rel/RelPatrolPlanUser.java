package com.mr.sa.entity.rel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_rel_patrol_plan_user")
public class RelPatrolPlanUser extends BaseModel {

	@PropertyDef(label = "巡防计划ID")
	@Column(name = "patrol_plan_id", length = 32)
	private String patrolPlanId;

	@PropertyDef(label = "巡防人员类型")
	@Column(name = "user_type", length = 64)
	private String userType;

	@PropertyDef(label = "巡防人员ID")
	@Column(name = "user_id", length = 64)
	private String userId;

}