package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.app.AppUser;

import lombok.Data;

/**
 * 巡逻记录
 */
@Data
@Entity
@Table(name = "biz_patrol_record")
public class PatrolRecord extends BaseModel {
	
	@PropertyDef(label = "巡防计划ID")
	@Column(name = "patrol_plan_id", length = 32)
	private String patrolPlanId;
	
	@PropertyDef(label = "组织ID")
	@Column(name = "org_id", length = 32)
	private String orgId;

	@PropertyDef(label = "巡防人员ID")
	@Column(name = "user_id", length = 32)
	private String userId;

	@PropertyDef(label = "巡防人名称")
	@Column(name = "nickname", length = 64)
	private String nickname;

	@PropertyDef(label = "巡防点ID")
	@Column(name = "patrol_point_id", length = 32)
	private String patrolPointId;

	@PropertyDef(label = "巡防点名称")
	@Column(name = "patrol_point_name", length = 64)
	private String patrolPointName;

	@PropertyDef(label = "扫码时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "scan_time")
	private Date scanTime;
 
	@Transient
	private AppUser user;
}