package com.mr.sa.entity;

import com.bstek.dorado.annotation.PropertyDef;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 专项任务类型
 */
@Data
@Entity
@Table(name = "biz_integral_special_task_type")
public class IntegralSpecialTaskType extends BaseModel {

	@PropertyDef(label = "名称")
	@Column(name = "name")
	private String name;

	@PropertyDef(label = "积分规则说明")
	private String description;

}