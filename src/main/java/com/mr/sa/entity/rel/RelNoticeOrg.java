package com.mr.sa.entity.rel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_notice_org_rel")
public class RelNoticeOrg extends BaseModel {

	@PropertyDef(label = "公告ID")
	@Column(name = "notice_id", length = 32)
	private String noticeId;

	@PropertyDef(label = "组织ID")
	@Column(name = "org_id", length = 64)
	private String orgId;

}