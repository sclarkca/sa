package com.mr.sa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 多文件上传实体类
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachmentFile {
	private String id;
	private String name;
	private String percent;
	private String size;
	private String status;
	private String uploadUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

}
