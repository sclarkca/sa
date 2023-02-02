/*
 * This file is generated by jOOQ.
*/
package com.mr.sa.generator.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Generated;


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
public class BizOrg implements Serializable {

    private static final long serialVersionUID = -1446284464;

    private String        id;
    private String        code;
    private String        name;
    private String        parentId;
    private String        longitude;
    private String        latitude;
    private String        address;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private String        creator;
    private String        modifier;
    private String        telephone;

    public BizOrg() {}

    public BizOrg(BizOrg value) {
        this.id = value.id;
        this.code = value.code;
        this.name = value.name;
        this.parentId = value.parentId;
        this.longitude = value.longitude;
        this.latitude = value.latitude;
        this.address = value.address;
        this.createdDate = value.createdDate;
        this.updateDate = value.updateDate;
        this.creator = value.creator;
        this.modifier = value.modifier;
        this.telephone = value.telephone;
    }

    public BizOrg(
        String        id,
        String        code,
        String        name,
        String        parentId,
        String        longitude,
        String        latitude,
        String        address,
        LocalDateTime createdDate,
        LocalDateTime updateDate,
        String        creator,
        String        modifier,
        String        telephone
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.parentId = parentId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.creator = creator;
        this.modifier = modifier;
        this.telephone = telephone;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return this.modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BizOrg (");

        sb.append(id);
        sb.append(", ").append(code);
        sb.append(", ").append(name);
        sb.append(", ").append(parentId);
        sb.append(", ").append(longitude);
        sb.append(", ").append(latitude);
        sb.append(", ").append(address);
        sb.append(", ").append(createdDate);
        sb.append(", ").append(updateDate);
        sb.append(", ").append(creator);
        sb.append(", ").append(modifier);
        sb.append(", ").append(telephone);

        sb.append(")");
        return sb.toString();
    }
}