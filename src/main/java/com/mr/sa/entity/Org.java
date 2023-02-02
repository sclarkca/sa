package com.mr.sa.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_org")
public class Org extends BaseModel implements Serializable {


	@PropertyDef(label = "编码")
	private String code;

	@PropertyDef(label = "名称")
	private String name;

	@PropertyDef(label = "经度")
	private String longitude;

	@PropertyDef(label = "纬度")
	private String latitude;

	@PropertyDef(label = "地址")
	private String address;

	@Transient
	@PropertyDef(label = "是否选中")
	private boolean checked;

	private String parentId;

	@Transient
	private String parentName;

	@Transient
	private List<Org> children;

	@PropertyDef(label = "联系电话")
	private String telephone;

}
