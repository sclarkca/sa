package com.mr.sa.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.excel.annotation.format.NumberFormat;
import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 守望点
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "biz_watch_point")
public class WatchPoint {

	@Id
	@Column(name = "id", length = 64)
	@PropertyDef(label = "id")
	@Generator(policy = UUIDPolicy.class)
	private String id;

	@PropertyDef(label = "守望点名称")
	private String pointName;

	@PropertyDef(label = "守望点地址")
	private String pointAddr;

	@PropertyDef(label = "责任人")
	private String owner;

	@PropertyDef(label = "责任人电话")
	private String ownerPhone;

	@PropertyDef(label = "当班人数")
	@NumberFormat("##")
	private Integer workerNum;

	@PropertyDef(label = "手套数量")
	@NumberFormat("##")
	private Integer gloversNum;

	@PropertyDef(label = "钢叉数量")
	@NumberFormat("##")
	private Integer forkNum;

	@PropertyDef(label = "警棍数量")
	@NumberFormat("##")
	private Integer bationNum;

	@PropertyDef(label = "盾牌数量")
	@NumberFormat("##")
	private Integer shieldNum;

	@PropertyDef(label = "经度")
	private double lng;

	@PropertyDef(label = "纬度")
	private double lat;

}
