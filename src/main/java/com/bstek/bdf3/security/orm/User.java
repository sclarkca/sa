package com.bstek.bdf3.security.orm;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.CreatedDatePolicy;
import com.bstek.bdf3.dorado.jpa.policy.impl.UpdatedDatePolicy;
import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel;

@Entity
@Table(name = "bdf3_user")
public class User implements UserDetails, OrganizationSupport {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "username_", length = 64)
	private String username;

	@Column(name = "nickname_", length = 64)
	private String nickname;

	@Column(name = "password_", length = 125)
	private String password;

	@Column(name = "administrator_")
	private boolean administrator;

	@Column(name = "account_non_expired_")
	private boolean accountNonExpired = true;

	@Column(name = "account_non_locked_")
	private boolean accountNonLocked = true;

	@Column(name = "credentials_non_expired_")
	private boolean credentialsNonExpired = true;

	@Column(name = "enabled_")
	private boolean enabled = true;

	@Transient
	private Object organization;

	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@PropertyDef(label = "组织")
	@Column(name = "org_id_", length = 64)
	private String orgId;

	@PropertyDef(label = "性别")
	@Column(name = "gender_", length = 8)
	private String gender;

	@PropertyDef(label = "电话")
	@Column(name = "phone_", length = 16)
	private String phone;

	@PropertyDef(label = "邮箱")
	@Column(name = "mail_", length = 64)
	private String mail;

	@PropertyDef(label = "住址")
	@Column(name = "address_", length = 255)
	private String address;

	@PropertyDef(label = "备注")
	@Column(name = "remark_", length = 255)
	private String remark;

	@Transient
	@PropertyDef(label = "是否选中")
	private boolean checked;
	
	@PropertyDef(label = "创建日期")
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	@Generator(policy = CreatedDatePolicy.class)
	private Date createdDate;

	@Column(name = "creator")
	@PropertyDef(label = "创建人")
	private String creator;

	@Column(name = "update_date")
	@PropertyDef(label = "修改日期")
	@Temporal(TemporalType.TIMESTAMP)
	@Generator(policy = UpdatedDatePolicy.class)
	private Date updateDate;

	@Column(name = "modifier")
	@PropertyDef(label = "修改人")
	private String modifier;
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * 昵称
	 * 
	 * @return nickname
	 */
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOrganization() {
		return (T) organization;
	}

	@Override
	public <T> void setOrganization(T organization) {
		this.organization = organization;

	}

	public String getOrgId() {
		return orgId;
	}

	public String getGender() {
		return gender;
	}

	public String getPhone() {
		return phone;
	}

	public String getMail() {
		return mail;
	}

	public String getAddress() {
		return address;
	}

	public String getRemark() {
		return remark;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getCreator() {
		return creator;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public String getModifier() {
		return modifier;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
