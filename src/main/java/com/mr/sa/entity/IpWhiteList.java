package com.mr.sa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * IP白名单
 */
@Data
@Entity
@Table(name = "biz_ip_white_list")
public class IpWhiteList extends BaseModel {

	@PropertyDef(label = "IP")
	private String ip;

}