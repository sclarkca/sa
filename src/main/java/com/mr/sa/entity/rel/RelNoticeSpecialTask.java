package com.mr.sa.entity.rel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_notice_special_task_rel")
public class RelNoticeSpecialTask extends BaseModel {

	@PropertyDef(label = "公告ID")
	@Column(name = "notice_id", length = 32)
	private String noticeId;

	@PropertyDef(label = "专项任务ID")
	@Column(name = "special_task_id", length = 32)
	private String specialTaskId;

}