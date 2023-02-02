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
 * 重点区域
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "biz_focus_area")
public class FocusArea {

	@Id
	@Column(name = "id", length = 64)
	@PropertyDef(label = "id")
	@Generator(policy = UUIDPolicy.class)
	private String id;

	@PropertyDef(label = "重点区域名称")
	private String areaName;

	@PropertyDef(label = "重点区域坐标框")
	private String coordinate;

	@PropertyDef(label = "中心点经度")
	private double centerLng;

	@PropertyDef(label = "中心点纬度")
	private double centerLat;

}