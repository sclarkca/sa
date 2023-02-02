package com.mr.sa.entity.app;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * App日志
 */
@Data
@Entity
@Table(name = "biz_app_logs")
public class AppLog {

	@Id
	@PropertyDef(label = "id")
	@Generator(policy = UUIDPolicy.class)
	private String id;

	@Type(type = "text")
	@PropertyDef(label = "日志详情")
	private String details;

	@PropertyDef(label = "日志类型")
	private String type;

	@PropertyDef(label = "用户名")
	private String userId;

	@PropertyDef(label = "创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	@Transient
	private AppUser user;

}