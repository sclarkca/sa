package com.mr.sa.dto;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 积分排行
 */
@Data
public class IntegralRankVo {

	@PropertyDef(label = "积分")
	private Integer integral;

	@PropertyDef(label = "用户ID")
	private String userId;

	@PropertyDef(label = "用户名称")
	private String username;

	@PropertyDef(label = "头像")
	private String avatar;

	@PropertyDef(label = "角色Id")
	private String roleId;

	@PropertyDef(label = "角色名称")
	private String roleName;

	@PropertyDef(label = "组织Id")
	private String orgId;

	@PropertyDef(label = "组织名称")
	private String orgName;

}