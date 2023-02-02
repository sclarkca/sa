package com.mr.sa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <p>
 * 关系:巡防任务/巡防人员
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "biz_point_task_item_user_amount_rel")
public class BizPointTaskItemUserAmountRel extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 巡防任务ID
     */
    private String pointTaskItemId;

    private String orgId;

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
    private String orgName;
}
