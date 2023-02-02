package com.mr.sa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
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
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "biz_point_task")
public class BizPointTask extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 任务名称
     */
    private String name;

    /**
     * 巡访开始时间
     */
    private Date patrolStartDate;

    /**
     * 巡访结束时间
     */
    private Date patrolEndDate;

    /**
     * 完成比例
     */
    private Integer doneRatio;

    /**
     * 激活标记
     */
    private String activeFlag;

    /**
     * 激活时间
     */
    private Date activeTime;

    private Boolean integralTask;

    private Integer integralValue;


    /**
     * 激活状态
     */
    private String activeStatus;


}
