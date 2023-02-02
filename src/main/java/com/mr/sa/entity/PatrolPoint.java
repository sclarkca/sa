package com.mr.sa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 巡防点
 */
@Data
@Entity
@Table(name = "biz_patrol_point")
public class PatrolPoint extends BaseModel {

	@PropertyDef(label = "编码")
	@Column(name = "code", length = 32)
	private String code;

	@PropertyDef(label = "名称")
	@Column(name = "name", length = 64)
	private String name;

	@PropertyDef(label = "经度")
	@Column(name = "longitude")
	private Double longitude;

	@PropertyDef(label = "纬度")
	@Column(name = "latitude")
	private Double latitude;

	@PropertyDef(label = "二维码地址")
	@Column(name = "qrcode_url")
	private String qrcodeUrl;

	@PropertyDef(label = "二维码")
	private String qrCode;

	@PropertyDef(label = "组织ID")
	@Column(name = "org_id", length = 32)
	private String orgId;

	@PropertyDef(label = "级别")
	@Column(name = "level", length = 32)
	private String level;

	@PropertyDef(label = "类型")
	@Column(name = "type", length = 32)
	private String type;


	@Transient
	@PropertyDef(label = "是否选中")
	private boolean checked;

	@Transient
	@PropertyDef(label = "是否为必扫点")
	private boolean required;
}
