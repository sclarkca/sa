package com.mr.sa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.app.AppUser;

import lombok.Data;

/**
 * 事件处理记录
 */
@Data
@Entity
@Table(name = "biz_event_record")
public class EventRecord extends BaseModel {

	@PropertyDef(label = "事件ID")
	private String eventId;

	@PropertyDef(label = "当前处理人")
	private String currentHandler;

	@PropertyDef(label = "当前状态")
	private String currentStatus;

	@PropertyDef(label = "变化处理人")
	private String nextHandler;

	@PropertyDef(label = "变化状态")
	private String nextStatus;

	@PropertyDef(label = "变化描述")
	private String nextDesc;
	
	@Transient
	private AppUser userLast;
	@Transient
	private AppUser user;
	

}