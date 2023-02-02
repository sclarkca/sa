package com.mr.sa.entity.rel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_rel_patrol_task_item_org")
public class RelPatrolTaskItemOrg extends BaseModel {

	@PropertyDef(label = "巡防任务ID")
	@Column(name = "patrol_task_item_id", length = 32)
	private String patrolTaskItemId;

	@PropertyDef(label = "组织ID")
	@Column(name = "org_id", length = 64)
	private String orgId;

}