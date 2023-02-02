package com.mr.sa.entity.rel;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_patrol_task_item_user_amount_rel")
public class RelPatrolTaskItemUserAmount extends BaseModel {

	@PropertyDef(label = "巡防任务ID")
	private String patrolTaskItemId;

	@PropertyDef(label = "组织ID")
	private String orgId;

	@PropertyDef(label = "巡防队员人数")
	private int memberAmount;

	@PropertyDef(label = "巡防民警人数")
	private int copAmount;

	@PropertyDef(label = "巡防辅警人数")
	private int apAmount;

}