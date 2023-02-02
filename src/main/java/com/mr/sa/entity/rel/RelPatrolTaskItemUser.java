package com.mr.sa.entity.rel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_rel_patrol_task_item_user")
public class RelPatrolTaskItemUser extends BaseModel {

	@PropertyDef(label = "巡防任务ID")
	@Column(name = "patrol_task_item_id", length = 32)
	private String patrolTaskItemId;

	@PropertyDef(label = "组织ID")
	@Column(name = "org_id", length = 16)
	private String orgId;
	
	@PropertyDef(label = "巡防人员类型")
	@Column(name = "user_type", length = 64)
	private String userType;

	@PropertyDef(label = "巡防人员ID")
	@Column(name = "user_id", length = 64)
	private String userId;

}