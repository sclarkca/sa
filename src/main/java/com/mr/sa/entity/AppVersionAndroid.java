package com.mr.sa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * App版本管理-安卓版本
 */
@Data
@Entity
@Table(name = "biz_app_version_android")
public class AppVersionAndroid extends BaseModel {

	@PropertyDef(label = "APPID")
	private String appId;

	@PropertyDef(label = "版本号")
	private String appVersion;

	@PropertyDef(label = "更新类型")
	private String updateType;

	@PropertyDef(label = "更新描述")
	private String versionDescription;

	@PropertyDef(label = "允许最低版本")
	private String allowLowestVersion;

	@PropertyDef(label = "APK文件的MD5值")
	private String md5;

	@PropertyDef(label = "apk文件oss存储地址")
	private String ossUrl;

	@PropertyDef(label = "发布状态")
	private String versionStatus;

	@PropertyDef(label = "灰度发布")
	private String grayReleased;

	@PropertyDef(label = "逻辑删除")
	private boolean deleted;

	@PropertyDef(label = "简介")
	@Type(type = "text")
	private String introduction;

}