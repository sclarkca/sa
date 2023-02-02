/*
 * This file is generated by jOOQ.
*/
package com.mr.sa.generator.tables;


import com.mr.sa.generator.Indexes;
import com.mr.sa.generator.Keys;
import com.mr.sa.generator.Sa;
import com.mr.sa.generator.tables.records.BizAppUserRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * 用户
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.8"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BizAppUser extends TableImpl<BizAppUserRecord> {

    private static final long serialVersionUID = 351737474;

    /**
     * The reference instance of <code>sa.biz_app_user</code>
     */
    public static final BizAppUser BIZ_APP_USER = new BizAppUser();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<BizAppUserRecord> getRecordType() {
        return BizAppUserRecord.class;
    }

    /**
     * The column <code>sa.biz_app_user.id_</code>.
     */
    public final TableField<BizAppUserRecord, String> ID_ = createField("id_", org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>sa.biz_app_user.role_Id</code>.
     */
    public final TableField<BizAppUserRecord, String> ROLE_ID = createField("role_Id", org.jooq.impl.SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>sa.biz_app_user.username_</code>. 用户名
     */
    public final TableField<BizAppUserRecord, String> USERNAME_ = createField("username_", org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "用户名");

    /**
     * The column <code>sa.biz_app_user.enabled_</code>. 是否可用
     */
    public final TableField<BizAppUserRecord, Boolean> ENABLED_ = createField("enabled_", org.jooq.impl.SQLDataType.BIT, this, "是否可用");

    /**
     * The column <code>sa.biz_app_user.nickname_</code>. 昵称
     */
    public final TableField<BizAppUserRecord, String> NICKNAME_ = createField("nickname_", org.jooq.impl.SQLDataType.VARCHAR(64), this, "昵称");

    /**
     * The column <code>sa.biz_app_user.password_</code>. 密码
     */
    public final TableField<BizAppUserRecord, String> PASSWORD_ = createField("password_", org.jooq.impl.SQLDataType.VARCHAR(125), this, "密码");

    /**
     * The column <code>sa.biz_app_user.org_id_</code>. 组织ID
     */
    public final TableField<BizAppUserRecord, String> ORG_ID_ = createField("org_id_", org.jooq.impl.SQLDataType.VARCHAR(64), this, "组织ID");

    /**
     * The column <code>sa.biz_app_user.gender_</code>. 性别
     */
    public final TableField<BizAppUserRecord, String> GENDER_ = createField("gender_", org.jooq.impl.SQLDataType.VARCHAR(8), this, "性别");

    /**
     * The column <code>sa.biz_app_user.phone_</code>. 手机号
     */
    public final TableField<BizAppUserRecord, String> PHONE_ = createField("phone_", org.jooq.impl.SQLDataType.VARCHAR(100), this, "手机号");

    /**
     * The column <code>sa.biz_app_user.mail_</code>. 邮箱
     */
    public final TableField<BizAppUserRecord, String> MAIL_ = createField("mail_", org.jooq.impl.SQLDataType.VARCHAR(100), this, "邮箱");

    /**
     * The column <code>sa.biz_app_user.address_</code>. 地址
     */
    public final TableField<BizAppUserRecord, String> ADDRESS_ = createField("address_", org.jooq.impl.SQLDataType.VARCHAR(255), this, "地址");

    /**
     * The column <code>sa.biz_app_user.remark_</code>. 备注
     */
    public final TableField<BizAppUserRecord, String> REMARK_ = createField("remark_", org.jooq.impl.SQLDataType.VARCHAR(255), this, "备注");

    /**
     * The column <code>sa.biz_app_user.created_date</code>. 创建日期
     */
    public final TableField<BizAppUserRecord, LocalDateTime> CREATED_DATE = createField("created_date", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "创建日期");

    /**
     * The column <code>sa.biz_app_user.creator</code>. 创建人
     */
    public final TableField<BizAppUserRecord, String> CREATOR = createField("creator", org.jooq.impl.SQLDataType.VARCHAR(32), this, "创建人");

    /**
     * The column <code>sa.biz_app_user.update_date</code>. 修改日期
     */
    public final TableField<BizAppUserRecord, LocalDateTime> UPDATE_DATE = createField("update_date", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "修改日期");

    /**
     * The column <code>sa.biz_app_user.modifier</code>. 修改人
     */
    public final TableField<BizAppUserRecord, String> MODIFIER = createField("modifier", org.jooq.impl.SQLDataType.VARCHAR(32), this, "修改人");

    /**
     * The column <code>sa.biz_app_user.avatar_</code>. 用户头像
     */
    public final TableField<BizAppUserRecord, String> AVATAR_ = createField("avatar_", org.jooq.impl.SQLDataType.VARCHAR(255), this, "用户头像");

    /**
     * Create a <code>sa.biz_app_user</code> table reference
     */
    public BizAppUser() {
        this(DSL.name("biz_app_user"), null);
    }

    /**
     * Create an aliased <code>sa.biz_app_user</code> table reference
     */
    public BizAppUser(String alias) {
        this(DSL.name(alias), BIZ_APP_USER);
    }

    /**
     * Create an aliased <code>sa.biz_app_user</code> table reference
     */
    public BizAppUser(Name alias) {
        this(alias, BIZ_APP_USER);
    }

    private BizAppUser(Name alias, Table<BizAppUserRecord> aliased) {
        this(alias, aliased, null);
    }

    private BizAppUser(Name alias, Table<BizAppUserRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "用户");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Sa.SA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.BIZ_APP_USER_PRIMARY, Indexes.BIZ_APP_USER_USERNAME_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<BizAppUserRecord> getPrimaryKey() {
        return Keys.KEY_BIZ_APP_USER_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<BizAppUserRecord>> getKeys() {
        return Arrays.<UniqueKey<BizAppUserRecord>>asList(Keys.KEY_BIZ_APP_USER_PRIMARY, Keys.KEY_BIZ_APP_USER_USERNAME_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizAppUser as(String alias) {
        return new BizAppUser(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizAppUser as(Name alias) {
        return new BizAppUser(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public BizAppUser rename(String name) {
        return new BizAppUser(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public BizAppUser rename(Name name) {
        return new BizAppUser(name, null);
    }
}