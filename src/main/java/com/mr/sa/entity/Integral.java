package com.mr.sa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.app.AppUser;

import lombok.Data;

/**
 * 积分详情
 */
@Data
@Entity
@Table(name = "biz_integral")
public class Integral extends BaseModel {

	@PropertyDef(label = "用户ID")
	@Column(name = "user_id", length = 32)
	private String userId;

	@PropertyDef(label = "用户名称")
	@Column(name = "user_name", length = 32)
	private String username;

	@PropertyDef(label = "积分")
	@Column(name = "integral", length = 32)
	private Integer integral;

	@PropertyDef(label = "角色Id")
	private String roleId;

	@PropertyDef(label = "角色名称")
	private String roleName;

	@PropertyDef(label = "组织Id")
	private String orgId;

	@PropertyDef(label = "组织名称")
	private String orgName;
	
	@Transient
	private AppUser user;
	
	@Transient
	private Org org;

}