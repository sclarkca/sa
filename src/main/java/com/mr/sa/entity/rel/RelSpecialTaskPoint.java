package com.mr.sa.entity.rel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_rel_special_task_point")
public class RelSpecialTaskPoint extends BaseModel {

	@PropertyDef(label = "专项任务ID")
	@Column(name = "special_task_id", length = 32)
	private String specialTaskId;

	@PropertyDef(label = "组织ID")
	@Column(name = "org_id", length = 16)
	private String orgId;
	
	@PropertyDef(label = "打卡点ID")
	@Column(name = "patrol_point_id", length = 64)
	private String patrolPointId;

	@PropertyDef(label = "是否为必扫点")
	@Column(name = "required", length = 8)
	private boolean required;

}