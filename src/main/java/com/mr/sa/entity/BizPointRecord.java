package com.mr.sa.entity;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.app.AppUser;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 巡逻记录
 */
@Data
@Entity
@Table(name = "biz_point_record")
public class BizPointRecord  {
	@Id
	@Column(name = "id", length = 64)
	@PropertyDef(label = "id")
	@Generator(policy = UUIDPolicy.class)
	private String id;

	@PropertyDef(label = "巡防计划ID")
	@Column(name = "point_plan_id", length = 32)
	private String pointPlanId;

	@PropertyDef(label = "组织ID")
	@Column(name = "org_id", length = 32)
	private String orgId;

	@Transient
	private String orgName;

	@PropertyDef(label = "巡防人员ID")
	@Column(name = "user_id", length = 32)
	private String userId;

	@PropertyDef(label = "巡防人名称")
	@Column(name = "nickname", length = 64)
	private String nickname;

	@PropertyDef(label = "巡防点ID")
	@Column(name = "point_id", length = 32)
	private String pointId;

	@PropertyDef(label = "巡防点名称")
	@Column(name = "point_name", length = 64)
	private String pointName;

	@PropertyDef(label = "扫码时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "scan_time")
	private Date scanTime;

	@Transient
	private AppUser user;
}
