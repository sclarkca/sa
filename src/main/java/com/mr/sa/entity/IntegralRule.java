package com.mr.sa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 积分规则
 */
@Data
@Entity
@Table(name = "biz_integral_rule")
public class IntegralRule extends BaseModel {

	@PropertyDef(label = "积分名称")
	private String name;

	@PropertyDef(label = "积分类型ID")
	private String integralTypeId;

	@PropertyDef(label = "单次累加(分)")
	private Integer increment;

	@PropertyDef(label = "当天上限(分)")
	private Integer limitDay;
	
	@PropertyDef(label = "积分规则说明")
	private String description;
	
	@Transient
	private IntegralType integralType;

	@PropertyDef(label = "排序")
	private Integer sort;

}