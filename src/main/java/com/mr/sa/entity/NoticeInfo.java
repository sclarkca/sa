package com.mr.sa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 公告
 */
@Data
@Entity
@Table(name = "biz_notice")
public class NoticeInfo extends BaseModel {

	@PropertyDef(label = "名称")
	private String name;

	@PropertyDef(label = "类型")
	private String type;
	
	@PropertyDef(label = "内容")
	@Type(type = "text")
	private String content; 
}