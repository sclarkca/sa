package com.mr.sa.entity.app;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

/**
 * App配置
 */
@Data
@Entity
@Table(name = "biz_app_conf")
public class AppConf extends BaseModel {

	@PropertyDef(label = "热力图计算开始时间")
	@Temporal(TemporalType.TIMESTAMP)
	private Date hotMapCountStartTime;

	@PropertyDef(label = "热力图计算结束时间")
	@Temporal(TemporalType.TIMESTAMP)
	private Date hotMapCountEndTime;

	@PropertyDef(label = "扫码点半径")
	private Integer pointRadius;

}