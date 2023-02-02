package com.mr.sa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 积分类型
 */
@Data
@Entity
@Table(name = "biz_integral_type")
public class IntegralType extends BaseModel {

	@PropertyDef(label = "积分")
	private Integer integral;

	@PropertyDef(label = "名称")
	private String name;

}