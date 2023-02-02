package com.mr.sa.entity.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.bdf3.security.orm.Permission;
import com.mr.sa.entity.BaseModel2;

import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "biz_app_role")
public class AppRole extends BaseModel2 {

	@Column(name = "name_")
	private String name;

	@Column(name = "group_id")
	private String group;

	@Column(name = "description_", length = 255)
	private String description;

	@Transient
	private List<AppPermission> permissions;
}
