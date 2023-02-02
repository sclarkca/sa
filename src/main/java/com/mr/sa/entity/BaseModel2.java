package com.mr.sa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.CreatedDatePolicy;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.bdf3.dorado.jpa.policy.impl.UpdatedDatePolicy;
import com.bstek.bdf3.security.ui.policy.CreatorPolicy;
import com.bstek.bdf3.security.ui.policy.ModifierPolicy;
import com.bstek.dorado.annotation.PropertyDef;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@MappedSuperclass
public class BaseModel2 {

	@Id
	@Column(name = "id_", length = 64)
	@PropertyDef(label = "id")
	@Generator(policy = UUIDPolicy.class)
	private String id;

	@JsonIgnore
	@PropertyDef(label = "创建日期")
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	@Generator(policy = CreatedDatePolicy.class)
	private Date createdDate;

	@JsonIgnore
	@Column(name = "creator")
	@PropertyDef(label = "创建人")
	@Generator(policy = CreatorPolicy.class)
	private String creator;

	@JsonIgnore
	@Column(name = "update_date")
	@PropertyDef(label = "修改日期")
	@Temporal(TemporalType.TIMESTAMP)
	@Generator(policy = UpdatedDatePolicy.class)
	private Date updateDate;

	@JsonIgnore
	@Column(name = "modifier")
	@PropertyDef(label = "修改人")
	@Generator(policy = ModifierPolicy.class)
	private String modifier;
}
