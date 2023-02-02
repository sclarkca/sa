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
@Table(name = "biz_grid_info")
public class Grid {

	@Id
	@Generator(policy = UUIDPolicy.class)
	private String id;

	@PropertyDef(label = "网格名称")
	private String gridName;

	@PropertyDef(label = "街道名称")
	private String streetName;

	@PropertyDef(label = "社区名称	")
	private String communityName;

	@PropertyDef(label = "坐标信息	")
	private String coordinate;

	@PropertyDef(label = "网格员")
	private String gridman;

	@PropertyDef(label = "网格员电话")
	private String gridmanPhone;

	@PropertyDef(label = "网格员头像地址")
	private String gridmanPic;

	@PropertyDef(label = "社区民警")
	private String policeName;

	@PropertyDef(label = "社区民警电话")
	private String policePhone;

	@PropertyDef(label = "巡逻民警")
	private String patrolName;

	@PropertyDef(label = "巡逻民警电话")
	private String patrolPhone;

	@PropertyDef(label = "警格底色")
	private String patrolColor;

	@PropertyDef(label = "网格底色")
	private String gridColor;

	@PropertyDef(label = "所属派出所")
	private String policeOffice;

}
