package com.mr.sa.entity;

import com.bstek.bdf3.dorado.jpa.annotation.Generator;
import com.bstek.bdf3.dorado.jpa.policy.impl.UUIDPolicy;
import com.bstek.dorado.annotation.PropertyDef;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 事件
 */
@Data
@Entity
@Table(name = "biz_user_online_time")
public class UserOnlineTime {
    @Id
    @Column(name = "id", length = 64)
    @PropertyDef(label = "id")
    @Generator(policy = UUIDPolicy.class)
    private String id;

    @PropertyDef(label = "人员ID")
    @Column(name = "user_id", length = 64)
    private String userId;

    @PropertyDef(label = "人员名称")
    @Column(name = "user_name", length = 64)
    private String username;

    @PropertyDef(label = "组织ID")
    @Column(name = "org_id", length = 64)
    private String orgId;

    @PropertyDef(label = "在线时长单位秒")
    @Column(name = "online_second")
    private Long onlineSecond;

    @PropertyDef(label = "记录时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "record_date")
    private Date recordDate;

    @PropertyDef(label = "创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Transient
    @PropertyDef(label = "单位名称")
    private String orgName;

    @Transient
    @PropertyDef(label = "在线时长")
    private String time;

    @Transient
    @PropertyDef(label = "上级单位名称")
    private String parentOrgName;
}
