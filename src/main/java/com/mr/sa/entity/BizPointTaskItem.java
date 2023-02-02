package com.mr.sa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * 巡逻任务
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */
@Data
@Entity
@Table(name = "biz_point_task_item")
public class BizPointTaskItem extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 任务名称
     */
    private String pointTaskId;

    /**
     * 巡访开始时间
     */
    @Temporal(TemporalType.TIME)
    @Column(name = "patrol_start_time")
    private Date patrolStartTime;

    /**
     * 巡访结束时间
     */
    @Temporal(TemporalType.TIME)
    @Column(name = "patrol_end_time")
    private Date patrolEndTime;

    @Transient
    private Boolean acrossDay = false;

    /**
     * 时间间隔(分钟）
     */
    private Integer timeInterval = 60;

    /**
     * 时间误差(分钟）
     */
    private Integer timeRedundancy = 5;


}
