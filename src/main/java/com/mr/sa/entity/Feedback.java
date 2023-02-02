package com.mr.sa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.app.AppUser;

import lombok.Data;

/**
 * 意见反馈
 */
@Data
@Entity
@Table(name = "biz_feedback")
public class Feedback extends BaseModel {

	@PropertyDef(label = "用户ID")
	private String userId;

	@PropertyDef(label = "用户名称")
	private String userName;

	@PropertyDef(label = "手机号")
	private String mobile;

	@PropertyDef(label = "反馈类型")
	private String type;

	@PropertyDef(label = "反馈内容")
	private String content;

	@PropertyDef(label = "状态")
	private String status;

	@PropertyDef(label = "联系方式")
	private String contact;
	
	@PropertyDef(label = "图片")
	private String images;
	
	@Transient
	private AppUser user;
	

}