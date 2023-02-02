package com.mr.sa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.bstek.dorado.annotation.PropertyDef;

import lombok.Data;

/**
 * 摄像头
 */
@Data
@Entity
@Table(name = "biz_camera_info")
public class Camera extends BaseModel {

	@PropertyDef(label = "摄像头服务器ip")
	private String serverIpAddr;

	@PropertyDef(label = "摄像头ip")
	private String ipAddr;

	@PropertyDef(label = "摄像头端口号")
	private Integer port;

	@PropertyDef(label = "摄像头名称（通道名）")
	private String cameraName;

	@PropertyDef(label = "维度")
	private Double latitude;

	@PropertyDef(label = "经度")
	private Double longitude;

	@PropertyDef(label = "通道码")
	private String tunnelCode;

	@PropertyDef(label = "摄像头用户名")
	private String userName;

	@PropertyDef(label = "摄像头密码")
	private String password;

	@PropertyDef(label = "摄像头标识码")
	private String cameraSn;

}
