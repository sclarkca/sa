package com.mr.sa.data.vo;

import com.bstek.dorado.annotation.PropertyDef;
import lombok.Data;

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
public class BizPointPlanVO {


    private String idStr;
    /**
     * 计划名称
     */
    private String name;
    /**
     * 巡防开始时间
     */
    private Date patrolStartTime;

    /**
     * 巡防结束时间
     */
    @PropertyDef(label = "结束时间")
    private Date patrolEndTime;

    /**
     * 完成状态
     */
    private String workStatus;

    /**
     * 原因
     */
    private String reason;


}
