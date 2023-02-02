package com.mr.sa.entity.rel;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_rel_integral_special_task_point")
public class RelIntegralSpecialTaskPoint extends BaseModel {

	@PropertyDef(label = "专项任务ID")
	private String integralSpecialTaskId;

	@PropertyDef(label = "组织ID")
	private String orgId;
	
	@PropertyDef(label = "打卡点ID")
	private String patrolPointId;

	@PropertyDef(label = "是否为必扫点")
	private boolean required;

}