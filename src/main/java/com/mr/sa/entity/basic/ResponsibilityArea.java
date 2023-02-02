package com.mr.sa.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 责任区
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "biz_responsibility_area")
public class ResponsibilityArea{

	@Id
	@Column(name = "id", length = 64)
	@PropertyDef(label = "id")
	@Generator(policy = UUIDPolicy.class)
	private String id;
	
	@PropertyDef(label = "责任区名称")
	private String areaName;

	@PropertyDef(label = "责任区坐标范围")
	private String coordinate;

	@PropertyDef(label = "中心点经度")
	private double centerLng;

	@PropertyDef(label = "中心点纬度")
	private double centerLat;

	@PropertyDef(label = "责任区类型")
	private String type;

	@PropertyDef(label = "区域颜色")
	private String color;

}