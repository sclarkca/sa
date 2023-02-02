package com.mr.sa.dto;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 巡防点
 */
@Data
public class PatrolPointDto {

	private String id;

	@PropertyDef(label = "经度")
	private String longitude;

	@PropertyDef(label = "纬度")
	private String latitude;

}