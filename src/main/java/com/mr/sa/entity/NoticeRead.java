package com.mr.sa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.app.AppUser;

import lombok.Data;

/**
 * 用户公告已读表
 */
@Data
@Entity
@Table(name = "biz_notice_read")
public class NoticeRead extends BaseModel {

	@PropertyDef(label = "公告ID")
	private String noticeId;

	@PropertyDef(label = "用户ID")
	private String userId;

	@PropertyDef(label = "组织ID")
	private String orgId;

	@PropertyDef(label = "专项任务ID")
	private String specialTaskId;

	@PropertyDef(label = "是否已阅", description = "0-否，1-是")
	@Column(name = "is_read")
	private boolean read;
	
	@Transient
	private AppUser user;

}
