package com.mr.sa.data.vo;

import com.bstek.dorado.annotation.PropertyDef;
import lombok.Data;

import java.util.Date;

/**
 * 用户巡逻时长
 */
@Data
public class UserOnlineTimeVO {

    @PropertyDef(label = "id")
    private String id;

    @PropertyDef(label = "人员ID")

    private String userId;

    @PropertyDef(label = "人员名称")
    private String username;

    @PropertyDef(label = "组织ID")
    private String orgId;

    @PropertyDef(label = "在线时长单位秒")
    private Long onlineSecond;

    @PropertyDef(label = "记录时间")
    private Date recordDate;

    @PropertyDef(label = "创建时间")
    private Date createTime;


    @PropertyDef(label = "单位名称")
    private String orgName;


    @PropertyDef(label = "在线时长")
    private String time;


    @PropertyDef(label = "上级单位名称")
    private String parentOrgName;
}
