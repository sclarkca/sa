package com.mr.sa.entity.rel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_notice_user_rel")
public class RelNoticeUser extends BaseModel {

	@PropertyDef(label = "公告ID")
	@Column(name = "notice_id", length = 32)
	private String noticeId;

	@PropertyDef(label = "执行人类型")
	@Column(name = "user_type", length = 64)
	private String userType;

	@PropertyDef(label = "执行人ID")
	@Column(name = "user_id", length = 64)
	private String userId;

}