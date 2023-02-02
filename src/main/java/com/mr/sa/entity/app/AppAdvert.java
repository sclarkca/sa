package com.mr.sa.entity.app;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

import lombok.Data;

/**
 * APP广告
 */
@Data
@Entity
@Table(name = "biz_app_advert")
public class AppAdvert extends BaseModel {

	@PropertyDef(label = "标题")
	private String title;

	@PropertyDef(label = "图片")
	private String imageUrl;

	@PropertyDef(label = "链接")
	private String link;

	@PropertyDef(label = "排序")
	private Integer sort;

}