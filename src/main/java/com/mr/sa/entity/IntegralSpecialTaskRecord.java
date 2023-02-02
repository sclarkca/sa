package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 专项任务执行记录
 */
@Data
@Entity
@Table(name = "biz_integral_special_task_record")
public class IntegralSpecialTaskRecord extends BaseModel {

	@PropertyDef(label = "专项任务ID")
	@Column(name = "integral_special_task_id", length = 32)
	private String integralSpecialTaskId;

	@PropertyDef(label = "任务名称")
	@Column(name = "name", length = 100)
	private String name;

	@PropertyDef(label = "执行人ID")
	@Column(name = "user_id", length = 32)
	private String userId;

	@PropertyDef(label = "执行人姓名")
	@Column(name = "user_name", length = 32)
	private String userName;

	@PropertyDef(label = "任务人数")
	@Column(name = "user_ammount", length = 16)
	private Integer userAmmount;

	@PropertyDef(label = "任务描述")
	@Column(name = "description", length = 16)
	private String description;

	@PropertyDef(label = "到岗时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "duty_time")
	private Date dutyTime;

}