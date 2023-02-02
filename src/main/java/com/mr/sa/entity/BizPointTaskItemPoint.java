package com.mr.sa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 * 关系:巡防任务/巡防组织
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "biz_point_task_item_point")
public class BizPointTaskItemPoint extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 巡防任务ID
     */
    private String pointTaskItemId;

    private String orgId;

    /**
     * 巡防点ID
     */
    private String pointId;

    /**
     * 是否为必扫点
     */
    private Boolean required;


}
