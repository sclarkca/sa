package com.mr.sa.data.vo;

import com.bstek.dorado.annotation.PropertyDef;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * 巡防点
 */
@Data
public class PatrolPointVO {

    @PropertyDef(label = "id")
    private String id;

    @JsonIgnore
    private Date createdDate;

    @JsonIgnore
    @PropertyDef(label = "创建人")
    private String creator;

    @JsonIgnore
    @PropertyDef(label = "修改日期")
    private Date updateDate;

    @JsonIgnore
    @PropertyDef(label = "修改人")
    private String modifier;

    @PropertyDef(label = "编码")
    private String code;

    @PropertyDef(label = "名称")
    private String name;

    @PropertyDef(label = "经度")
    private Double longitude;

    @PropertyDef(label = "纬度")
    private Double latitude;

    @PropertyDef(label = "二维码地址")
    private String qrcodeUrl;

    @PropertyDef(label = "二维码")
    private String qrCode;

    @PropertyDef(label = "组织ID")
    private String orgId;

    @PropertyDef(label = "级别")
    private String level;

    @PropertyDef(label = "类型")
    private String type;


    @PropertyDef(label = "是否选中")
    private boolean checked;

    @PropertyDef(label = "是否为必扫点")
    private boolean required;
}
