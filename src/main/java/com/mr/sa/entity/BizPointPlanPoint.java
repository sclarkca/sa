package com.mr.sa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 * 关系:巡防计划/巡防点
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "biz_point_plan_point")
public class BizPointPlanPoint extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 巡防任务ID
     */
    private String pointPlanId;

    /**
     * 巡防点组织ID
     */
    private String pointId;

    /**
     * 是否为必扫点
     */
    private Boolean required;


}
