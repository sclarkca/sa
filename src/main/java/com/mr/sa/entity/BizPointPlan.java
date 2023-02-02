package com.mr.sa.entity;

import com.bstek.dorado.annotation.PropertyDef;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * 巡防活动
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "biz_point_plan")
public class BizPointPlan extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 计划名称
     */
    private String name;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 下发时间
     */
    @PropertyDef(label = "下发时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "announce_time")
    private Date announceTime;

    /**
     * 巡防开始时间
     */
    @PropertyDef(label = "开始时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "patrol_start_time")
    private Date patrolStartTime;

    /**
     * 巡防结束时间
     */
    @PropertyDef(label = "结束时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "patrol_end_time")
    private Date patrolEndTime;

    /**
     * 规定完成比例
     */
    private Integer doneRatio;

    /**
     * 完成状态
     */
    private String workStatus;

    /**
     * 原因
     */
    private String reason;

    /**
     * 点子任务ID
     */
    private String pointTaskItemId;

    /**
     * 点任务ID
     */
    private String pointTaskId;

    /**
     * 时间间隔(分钟）
     */
    private Integer timeInterval;

    /**
     * 时间误差(分钟)
     */
    private Integer timeRedundancy;

    /**
     * 巡防人员数量
     */
    private Integer memberAmount;

    /**
     * 巡防民警数量
     */
    private Integer copAmount;

    /**
     * 巡防辅警人员数量
     */
    private Integer apAmount;


    @Transient
    @PropertyDef(label = "组织")
    private String orgName;

}
