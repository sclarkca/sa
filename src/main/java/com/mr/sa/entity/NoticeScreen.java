package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 公告-大屏
 */
@Data
@Entity
@Table(name = "biz_notice_screen")
public class NoticeScreen extends BaseModel {

	@PropertyDef(label = "名称")
	private String name;

	@PropertyDef(label = "类型")
	private String type;

	@PropertyDef(label = "内容")
	@Type(type = "text")
	private String content;

	@PropertyDef(label = "是否置顶")
	private boolean top;

	@PropertyDef(label = "置顶时间")
	@Temporal(TemporalType.TIMESTAMP)
	private Date topTime;
}