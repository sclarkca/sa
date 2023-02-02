package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 操作记录
 */
@Data
@Entity
@Table(name = "biz_op_record")
public class OpRecord {

	@Id
	private int id;

	@PropertyDef(label = "操作类型")
	private String opType;

	@PropertyDef(label = "操作key")
	private String opId;

	@PropertyDef(label = "详情")
	private String details;

	@PropertyDef(label = "用户id")
	private String userId;

	@PropertyDef(label = "用户昵称")
	private String userName;

	@Temporal(TemporalType.TIMESTAMP)
	@PropertyDef(label = "创建时间")
	private Date createdData;
}
