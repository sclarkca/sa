package com.mr.sa.entity.app;

import com.bstek.dorado.annotation.PropertyDef;
import com.mr.sa.entity.BaseModel2;
import com.mr.sa.entity.Org;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Entity
@Table(name = "biz_app_user")
public class AppUser extends BaseModel2 {

    @Column(name = "username_", length = 64)
    private String username;

    @Column(name = "nickname_", length = 64)
    private String nickname;

    @Column(name = "password_", length = 125)
    private String password;

    @Column(name = "enabled_")
    private boolean enabled = true;

    @PropertyDef(label = "角色")
    @Column(name = "role_id", length = 64)
    private String roleId;

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

    @PropertyDef(label = "头像")
    @Column(name = "avatar_")
    private String avatar;

    @PropertyDef(label = "备注")
    @Column(name = "remark_", length = 255)
    private String remark;

    @Transient
    @PropertyDef(label = "是否选中")
    private boolean checked;

    @Transient
    private Org org;

}
