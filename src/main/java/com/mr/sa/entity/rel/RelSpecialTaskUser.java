package com.mr.sa.entity.rel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_rel_special_task_user")
public class RelSpecialTaskUser extends BaseModel {

	@PropertyDef(label = "专项任务ID")
	@Column(name = "special_task_id", length = 32)
	private String specialTaskId;

	@PropertyDef(label = "执行人类型")
	@Column(name = "user_type", length = 64)
	private String userType;

	@PropertyDef(label = "执行人ID")
	@Column(name = "user_id", length = 64)
	private String userId;

}