package com.mr.sa.entity;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.app.AppUser;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 事件
 */
@Data
@Entity
@Table(name = "biz_event_hide")
public class HideEvent extends BaseModel {

	@PropertyDef(label = "人员ID")
	@Column(name = "user_id", length = 64)
	private String userId;

	@PropertyDef(label = "人员名称")
	@Column(name = "user_name", length = 64)
	private String userName;
	
	@PropertyDef(label = "组织ID")
	@Column(name = "org_id", length = 64)
	private String orgId;
	
	@PropertyDef(label = "事件类型")
	@Column(name = "type", length = 8)
	private String type;

	@PropertyDef(label = "事件等级")
	@Column(name = "level", length = 8)
	private String level;

	@PropertyDef(label = "描述")
	@Column(name = "description", length = 16)
	private String description;

	@PropertyDef(label = "地址")
	@Column(name = "address")
	private String address;

	@PropertyDef(label = "经度")
	@Column(name = "longitude", length = 64)
	private Double longitude;

	@PropertyDef(label = "纬度")
	@Column(name = "latitude", length = 64)
	private Double latitude;

	@PropertyDef(label = "状态")
	@Column(name = "status", length = 8)
	private String status;

	@PropertyDef(label = "发生时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "occur_time")
	private Date occurTime;
	
	@PropertyDef(label = "积分")
	@Column(name = "integral")
	private boolean integral;
	
	@PropertyDef(label = "积分设置时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "integral_time")
	private Date integralTime;
	
	@PropertyDef(label = "评价")
	private String evaluate;
	
	@Transient
	private AppUser user;
	
	@Transient
	private Org org;
	
}