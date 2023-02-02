/*
 * This file is generated by jOOQ.
*/
package com.mr.sa.generator.tables.records;


import com.mr.sa.generator.tables.BizOrg;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 组织
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.8"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BizOrgRecord extends UpdatableRecordImpl<BizOrgRecord> implements Record12<String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, String, String, String> {

    private static final long serialVersionUID = -1515742889;

    /**
     * Setter for <code>sa.biz_org.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>sa.biz_org.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>sa.biz_org.code</code>. 编码
     */
    public void setCode(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>sa.biz_org.code</code>. 编码
     */
    public String getCode() {
        return (String) get(1);
    }

    /**
     * Setter for <code>sa.biz_org.name</code>. 名称
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>sa.biz_org.name</code>. 名称
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>sa.biz_org.parent_id</code>. 父节点ID
     */
    public void setParentId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>sa.biz_org.parent_id</code>. 父节点ID
     */
    public String getParentId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>sa.biz_org.longitude</code>. 经度
     */
    public void setLongitude(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>sa.biz_org.longitude</code>. 经度
     */
    public String getLongitude() {
        return (String) get(4);
    }

    /**
     * Setter for <code>sa.biz_org.latitude</code>. 纬度
     */
    public void setLatitude(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>sa.biz_org.latitude</code>. 纬度
     */
    public String getLatitude() {
        return (String) get(5);
    }

    /**
     * Setter for <code>sa.biz_org.address</code>. 地址
     */
    public void setAddress(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>sa.biz_org.address</code>. 地址
     */
    public String getAddress() {
        return (String) get(6);
    }

    /**
     * Setter for <code>sa.biz_org.created_date</code>. 创建时间
     */
    public void setCreatedDate(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>sa.biz_org.created_date</code>. 创建时间
     */
    public LocalDateTime getCreatedDate() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>sa.biz_org.update_date</code>. 更新时间
     */
    public void setUpdateDate(LocalDateTime value) {
        set(8, value);
    }

    /**
     * Getter for <code>sa.biz_org.update_date</code>. 更新时间
     */
    public LocalDateTime getUpdateDate() {
        return (LocalDateTime) get(8);
    }

    /**
     * Setter for <code>sa.biz_org.creator</code>. 创建人
     */
    public void setCreator(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>sa.biz_org.creator</code>. 创建人
     */
    public String getCreator() {
        return (String) get(9);
    }

    /**
     * Setter for <code>sa.biz_org.modifier</code>. 修改人
     */
    public void setModifier(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>sa.biz_org.modifier</code>. 修改人
     */
    public String getModifier() {
        return (String) get(10);
    }

    /**
     * Setter for <code>sa.biz_org.telephone</code>. 联系电话
     */
    public void setTelephone(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>sa.biz_org.telephone</code>. 联系电话
     */
    public String getTelephone() {
        return (String) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record12 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, String, String, String> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<String, String, String, String, String, String, String, LocalDateTime, LocalDateTime, String, String, String> valuesRow() {
        return (Row12) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return BizOrg.BIZ_ORG.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return BizOrg.BIZ_ORG.CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return BizOrg.BIZ_ORG.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return BizOrg.BIZ_ORG.PARENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return BizOrg.BIZ_ORG.LONGITUDE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return BizOrg.BIZ_ORG.LATITUDE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return BizOrg.BIZ_ORG.ADDRESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field8() {
        return BizOrg.BIZ_ORG.CREATED_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field9() {
        return BizOrg.BIZ_ORG.UPDATE_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return BizOrg.BIZ_ORG.CREATOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return BizOrg.BIZ_ORG.MODIFIER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return BizOrg.BIZ_ORG.TELEPHONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getParentId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getLongitude();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getLatitude();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getAddress();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component8() {
        return getCreatedDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component9() {
        return getUpdateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getCreator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component11() {
        return getModifier();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component12() {
        return getTelephone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getParentId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getLongitude();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getLatitude();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getAddress();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value8() {
        return getCreatedDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value9() {
        return getUpdateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getCreator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getModifier();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getTelephone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value1(String value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value2(String value) {
        setCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value3(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value4(String value) {
        setParentId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value5(String value) {
        setLongitude(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value6(String value) {
        setLatitude(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value7(String value) {
        setAddress(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value8(LocalDateTime value) {
        setCreatedDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value9(LocalDateTime value) {
        setUpdateDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value10(String value) {
        setCreator(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value11(String value) {
        setModifier(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord value12(String value) {
        setTelephone(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizOrgRecord values(String value1, String value2, String value3, String value4, String value5, String value6, String value7, LocalDateTime value8, LocalDateTime value9, String value10, String value11, String value12) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BizOrgRecord
     */
    public BizOrgRecord() {
        super(BizOrg.BIZ_ORG);
    }

    /**
     * Create a detached, initialised BizOrgRecord
     */
    public BizOrgRecord(String id, String code, String name, String parentId, String longitude, String latitude, String address, LocalDateTime createdDate, LocalDateTime updateDate, String creator, String modifier, String telephone) {
        super(BizOrg.BIZ_ORG);

        set(0, id);
        set(1, code);
        set(2, name);
        set(3, parentId);
        set(4, longitude);
        set(5, latitude);
        set(6, address);
        set(7, createdDate);
        set(8, updateDate);
        set(9, creator);
        set(10, modifier);
        set(11, telephone);
    }
}
