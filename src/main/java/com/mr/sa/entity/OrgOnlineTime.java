package com.mr.sa.entity;

import com.bstek.dorado.annotation.PropertyDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 事件
 */
@Data
public class OrgOnlineTime {

    @PropertyDef(label = "用户ID")
    private String userId;

    @PropertyDef(label = "用户名称")
    private String username;

    @PropertyDef(label = "单位ID")
    private String orgId;

    @PropertyDef(label = "单位名称")
    private String orgName;

    @PropertyDef(label = "上级单位名称")
    private String parentOrgName;

    @PropertyDef(label = "在线时长")
    private Long onlineSecond;

    @PropertyDef(label = "在线时长")
    private String time;

    @PropertyDef(label = "记录时间")
    private Date recordDate;
}
