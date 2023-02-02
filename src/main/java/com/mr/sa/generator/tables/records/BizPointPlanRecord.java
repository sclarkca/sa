/*
 * This file is generated by jOOQ.
*/
package com.mr.sa.generator.tables.records;


import com.mr.sa.generator.tables.BizPointPlan;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record20;
import org.jooq.Row20;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 巡防活动
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.8"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BizPointPlanRecord extends UpdatableRecordImpl<BizPointPlanRecord> implements Record20<String, String, String, LocalDate, LocalDateTime, LocalDateTime, Integer, String, String, String, String, Integer, Integer, Integer, Integer, Integer, LocalDateTime, String, LocalDateTime, String> {

    private static final long serialVersionUID = 1899526122;

    /**
     * Setter for <code>sa.biz_point_plan.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>sa.biz_point_plan.name</code>. 计划名称
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.name</code>. 计划名称
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>sa.biz_point_plan.org_id</code>. 组织ID
     */
    public void setOrgId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.org_id</code>. 组织ID
     */
    public String getOrgId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>sa.biz_point_plan.announce_time</code>. 下发时间
     */
    public void setAnnounceTime(LocalDate value) {
        set(3, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.announce_time</code>. 下发时间
     */
    public LocalDate getAnnounceTime() {
        return (LocalDate) get(3);
    }

    /**
     * Setter for <code>sa.biz_point_plan.patrol_start_time</code>. 巡防开始时间
     */
    public void setPatrolStartTime(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.patrol_start_time</code>. 巡防开始时间
     */
    public LocalDateTime getPatrolStartTime() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>sa.biz_point_plan.patrol_end_time</code>. 巡防结束时间
     */
    public void setPatrolEndTime(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.patrol_end_time</code>. 巡防结束时间
     */
    public LocalDateTime getPatrolEndTime() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>sa.biz_point_plan.done_ratio</code>. 规定完成比例
     */
    public void setDoneRatio(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.done_ratio</code>. 规定完成比例
     */
    public Integer getDoneRatio() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>sa.biz_point_plan.work_status</code>. 完成状态
     */
    public void setWorkStatus(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.work_status</code>. 完成状态
     */
    public String getWorkStatus() {
        return (String) get(7);
    }

    /**
     * Setter for <code>sa.biz_point_plan.reason</code>. 原因
     */
    public void setReason(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.reason</code>. 原因
     */
    public String getReason() {
        return (String) get(8);
    }

    /**
     * Setter for <code>sa.biz_point_plan.point_task_item_id</code>. 点子任务ID
     */
    public void setPointTaskItemId(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.point_task_item_id</code>. 点子任务ID
     */
    public String getPointTaskItemId() {
        return (String) get(9);
    }

    /**
     * Setter for <code>sa.biz_point_plan.point_task_id</code>. 点任务ID
     */
    public void setPointTaskId(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.point_task_id</code>. 点任务ID
     */
    public String getPointTaskId() {
        return (String) get(10);
    }

    /**
     * Setter for <code>sa.biz_point_plan.time_interval</code>. 时间间隔(分钟）
     */
    public void setTimeInterval(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.time_interval</code>. 时间间隔(分钟）
     */
    public Integer getTimeInterval() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>sa.biz_point_plan.time_redundancy</code>. 时间误差(分钟)
     */
    public void setTimeRedundancy(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.time_redundancy</code>. 时间误差(分钟)
     */
    public Integer getTimeRedundancy() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>sa.biz_point_plan.member_amount</code>. 巡防人员数量
     */
    public void setMemberAmount(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.member_amount</code>. 巡防人员数量
     */
    public Integer getMemberAmount() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>sa.biz_point_plan.cop_amount</code>. 巡防民警数量
     */
    public void setCopAmount(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.cop_amount</code>. 巡防民警数量
     */
    public Integer getCopAmount() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>sa.biz_point_plan.ap_amount</code>. 巡防辅警人员数量
     */
    public void setApAmount(Integer value) {
        set(15, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.ap_amount</code>. 巡防辅警人员数量
     */
    public Integer getApAmount() {
        return (Integer) get(15);
    }

    /**
     * Setter for <code>sa.biz_point_plan.created_date</code>. 创建日期
     */
    public void setCreatedDate(LocalDateTime value) {
        set(16, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.created_date</code>. 创建日期
     */
    public LocalDateTime getCreatedDate() {
        return (LocalDateTime) get(16);
    }

    /**
     * Setter for <code>sa.biz_point_plan.creator</code>. 创建人
     */
    public void setCreator(String value) {
        set(17, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.creator</code>. 创建人
     */
    public String getCreator() {
        return (String) get(17);
    }

    /**
     * Setter for <code>sa.biz_point_plan.update_date</code>. 修改日期
     */
    public void setUpdateDate(LocalDateTime value) {
        set(18, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.update_date</code>. 修改日期
     */
    public LocalDateTime getUpdateDate() {
        return (LocalDateTime) get(18);
    }

    /**
     * Setter for <code>sa.biz_point_plan.modifier</code>. 修改人
     */
    public void setModifier(String value) {
        set(19, value);
    }

    /**
     * Getter for <code>sa.biz_point_plan.modifier</code>. 修改人
     */
    public String getModifier() {
        return (String) get(19);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record20 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row20<String, String, String, LocalDate, LocalDateTime, LocalDateTime, Integer, String, String, String, String, Integer, Integer, Integer, Integer, Integer, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row20) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row20<String, String, String, LocalDate, LocalDateTime, LocalDateTime, Integer, String, String, String, String, Integer, Integer, Integer, Integer, Integer, LocalDateTime, String, LocalDateTime, String> valuesRow() {
        return (Row20) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return BizPointPlan.BIZ_POINT_PLAN.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return BizPointPlan.BIZ_POINT_PLAN.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return BizPointPlan.BIZ_POINT_PLAN.ORG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDate> field4() {
        return BizPointPlan.BIZ_POINT_PLAN.ANNOUNCE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field5() {
        return BizPointPlan.BIZ_POINT_PLAN.PATROL_START_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field6() {
        return BizPointPlan.BIZ_POINT_PLAN.PATROL_END_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field7() {
        return BizPointPlan.BIZ_POINT_PLAN.DONE_RATIO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return BizPointPlan.BIZ_POINT_PLAN.WORK_STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return BizPointPlan.BIZ_POINT_PLAN.REASON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return BizPointPlan.BIZ_POINT_PLAN.POINT_TASK_ITEM_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return BizPointPlan.BIZ_POINT_PLAN.POINT_TASK_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field12() {
        return BizPointPlan.BIZ_POINT_PLAN.TIME_INTERVAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field13() {
        return BizPointPlan.BIZ_POINT_PLAN.TIME_REDUNDANCY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field14() {
        return BizPointPlan.BIZ_POINT_PLAN.MEMBER_AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field15() {
        return BizPointPlan.BIZ_POINT_PLAN.COP_AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field16() {
        return BizPointPlan.BIZ_POINT_PLAN.AP_AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field17() {
        return BizPointPlan.BIZ_POINT_PLAN.CREATED_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field18() {
        return BizPointPlan.BIZ_POINT_PLAN.CREATOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field19() {
        return BizPointPlan.BIZ_POINT_PLAN.UPDATE_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field20() {
        return BizPointPlan.BIZ_POINT_PLAN.MODIFIER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getOrgId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate component4() {
        return getAnnounceTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component5() {
        return getPatrolStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component6() {
        return getPatrolEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component7() {
        return getDoneRatio();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getWorkStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getReason();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getPointTaskItemId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component11() {
        return getPointTaskId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component12() {
        return getTimeInterval();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component13() {
        return getTimeRedundancy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component14() {
        return getMemberAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component15() {
        return getCopAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component16() {
        return getApAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component17() {
        return getCreatedDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component18() {
        return getCreator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component19() {
        return getUpdateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component20() {
        return getModifier();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getOrgId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate value4() {
        return getAnnounceTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value5() {
        return getPatrolStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value6() {
        return getPatrolEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value7() {
        return getDoneRatio();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getWorkStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getReason();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getPointTaskItemId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getPointTaskId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value12() {
        return getTimeInterval();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value13() {
        return getTimeRedundancy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value14() {
        return getMemberAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value15() {
        return getCopAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value16() {
        return getApAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value17() {
        return getCreatedDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value18() {
        return getCreator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value19() {
        return getUpdateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value20() {
        return getModifier();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value1(String value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value3(String value) {
        setOrgId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value4(LocalDate value) {
        setAnnounceTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value5(LocalDateTime value) {
        setPatrolStartTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value6(LocalDateTime value) {
        setPatrolEndTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value7(Integer value) {
        setDoneRatio(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value8(String value) {
        setWorkStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value9(String value) {
        setReason(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value10(String value) {
        setPointTaskItemId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value11(String value) {
        setPointTaskId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value12(Integer value) {
        setTimeInterval(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value13(Integer value) {
        setTimeRedundancy(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value14(Integer value) {
        setMemberAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value15(Integer value) {
        setCopAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value16(Integer value) {
        setApAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value17(LocalDateTime value) {
        setCreatedDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value18(String value) {
        setCreator(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value19(LocalDateTime value) {
        setUpdateDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord value20(String value) {
        setModifier(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizPointPlanRecord values(String value1, String value2, String value3, LocalDate value4, LocalDateTime value5, LocalDateTime value6, Integer value7, String value8, String value9, String value10, String value11, Integer value12, Integer value13, Integer value14, Integer value15, Integer value16, LocalDateTime value17, String value18, LocalDateTime value19, String value20) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        value19(value19);
        value20(value20);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BizPointPlanRecord
     */
    public BizPointPlanRecord() {
        super(BizPointPlan.BIZ_POINT_PLAN);
    }

    /**
     * Create a detached, initialised BizPointPlanRecord
     */
    public BizPointPlanRecord(String id, String name, String orgId, LocalDate announceTime, LocalDateTime patrolStartTime, LocalDateTime patrolEndTime, Integer doneRatio, String workStatus, String reason, String pointTaskItemId, String pointTaskId, Integer timeInterval, Integer timeRedundancy, Integer memberAmount, Integer copAmount, Integer apAmount, LocalDateTime createdDate, String creator, LocalDateTime updateDate, String modifier) {
        super(BizPointPlan.BIZ_POINT_PLAN);

        set(0, id);
        set(1, name);
        set(2, orgId);
        set(3, announceTime);
        set(4, patrolStartTime);
        set(5, patrolEndTime);
        set(6, doneRatio);
        set(7, workStatus);
        set(8, reason);
        set(9, pointTaskItemId);
        set(10, pointTaskId);
        set(11, timeInterval);
        set(12, timeRedundancy);
        set(13, memberAmount);
        set(14, copAmount);
        set(15, apAmount);
        set(16, createdDate);
        set(17, creator);
        set(18, updateDate);
        set(19, modifier);
    }
}
