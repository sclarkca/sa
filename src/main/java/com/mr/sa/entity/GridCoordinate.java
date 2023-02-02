package com.mr.sa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_grid_coordinates")
public class GridCoordinate {

	@Id
	@Generator(policy = UUIDPolicy.class)
	private String id;

	@PropertyDef(label = "网格id")
	private String gridId;

	@PropertyDef(label = "纬度坐标")
	private Double latitude;

	@PropertyDef(label = "经度坐标")
	private Double longitude;

}
