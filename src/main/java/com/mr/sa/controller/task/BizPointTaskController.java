package com.mr.sa.controller.task;


import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.*;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.common.MyDateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 巡逻任务 前端控制器
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */
@Controller
@Transactional
public class BizPointTaskController extends QueryFilter {

    @DataResolver
    @Transactional
    @Log(module = "巡逻任务", category = "CRUD")
    public void save(List<BizPointTask> bizPointTasks) {
        JpaUtil.save(bizPointTasks, new SmartSavePolicyAdapter() {
            @Override
            public boolean beforeDelete(SaveContext context) {
                // BizPointTask
                BizPointTask patrolTask = context.getEntity();
                if (EntityState.DELETED
                        .equals(EntityUtils.getState(patrolTask))) {
                    String pointTaskId = patrolTask.getId();
                    // PatrolTask-PatrolTaskItem-list
                    List<BizPointTaskItem> patrolTaskItemList = JpaUtil
                            .linq(BizPointTaskItem.class)
                            .equal("pointTaskId", pointTaskId).list();
                    for (BizPointTaskItem patrolTaskItem : patrolTaskItemList) {
                        String pointTaskItemId = patrolTaskItem.getId();
                        // PatrolTask-PatrolTaskItem-BizPointPlan-list
                        List<BizPointPlan> patrolPlanList = JpaUtil
                                .linq(BizPointPlan.class)
                                .equal("pointTaskItemId", pointTaskItemId)
                                .list();
                        for (BizPointPlan patrolPlan : patrolPlanList) {
                            String pointPlanId = patrolPlan.getId();
                            // PatrolTask-PatrolTaskItem-BizPointPlan-RelPatrolPlanPoint
                            JpaUtil.lind(BizPointPlanPoint.class)
                                    .equal("pointPlanId", pointPlanId)
                                    .delete();

                        }
                        // PatrolTask-PatrolTaskItem-BizPointPlan-delete
                        JpaUtil.lind(BizPointPlan.class)
                                .equal("pointTaskItemId", pointTaskItemId)
                                .delete();

                        // PatrolTask-PatrolTaskItem-RelPatrolTaskItemOrg
                        JpaUtil.lind(BizPointTaskItemOrg.class)
                                .equal("pointTaskItemId", pointTaskItemId)
                                .delete();
                        // PatrolTask-PatrolTaskItem-RelPatrolTaskItemPoint
                        JpaUtil.lind(BizPointTaskItemPoint.class)
                                .equal("pointTaskItemId", pointTaskItemId)
                                .delete();

                        // PatrolTask-PatrolTaskItem-RelPatrolTaskItemUserAmount
                        JpaUtil.lind(BizPointTaskItemUserAmountRel.class)
                                .equal("pointTaskItemId", pointTaskItemId)
                                .delete();
                    }
                    // PatrolTask-PatrolTaskItem-delete
                    JpaUtil.lind(BizPointTaskItem.class)
                            .equal("pointTaskId", pointTaskId).delete();
                }
                return true;
            }

        });
    }

    @DataProvider
    public void query(Page<BizPointTask> page, Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        criteria = condition(criteria, parameter);
        JpaUtil.linq(BizPointTask.class).where(criteria).desc("updateDate")
                .paging(page);
    }

    // 复制任务
    @Expose
    public void copy(Map<String, Object> parameter) {
        List<BizPointTask> patrolTaskList = (List<BizPointTask>) parameter.get("selection");
        Date patrolStartDate = (Date) parameter.get("patrolStartDate");
        Date patrolEndDate = (Date) parameter.get("patrolEndDate");
        for (BizPointTask patrolTaskOld : patrolTaskList) {
            // Task-老
            String pointTaskId = patrolTaskOld.getId();
            // @formatter:off
            // Task-新
            BizPointTask patrolTask = new BizPointTask();

            BeanUtils.copyProperties(patrolTaskOld, patrolTask);
            patrolTask.setPatrolStartDate(patrolStartDate);
            patrolTask.setPatrolEndDate(patrolEndDate);
            patrolTask.setId(null);
            patrolTask.setActiveStatus(null);
            JpaUtil.persist(patrolTask);

            // Item-老
            List<BizPointTaskItem> patrolTaskItemListOld =
                    JpaUtil.linq(BizPointTaskItem.class)
                            .equal("pointTaskId", pointTaskId)
                            .list();
            for (BizPointTaskItem patrolTaskItemOld : patrolTaskItemListOld) {
                String patrolTaskItemIdOld = patrolTaskItemOld.getId();
                // Item-新
                BizPointTaskItem patrolTaskItem = new BizPointTaskItem();

                BeanUtils.copyProperties(patrolTaskItemOld, patrolTaskItem);
                patrolTaskItem.setId(null);
                patrolTaskItem.setPointTaskId(patrolTask.getId());
                JpaUtil.persist(patrolTaskItem);


                //任务项/组织关系-老
                List<BizPointTaskItemOrg> relPatrolTaskItemOrgListOld =
                        JpaUtil.linq(BizPointTaskItemOrg.class)
                                .equal("pointTaskItemId", patrolTaskItemIdOld)
                                .list();
                for (BizPointTaskItemOrg relPatrolTaskItemOrgOld : relPatrolTaskItemOrgListOld) {
                    // 任务项/组织关系-新
                    BizPointTaskItemOrg relPatrolTaskItemOrg = new BizPointTaskItemOrg();

                    BeanUtils.copyProperties(relPatrolTaskItemOrgOld, relPatrolTaskItemOrg);
                    relPatrolTaskItemOrg.setId(null);
                    relPatrolTaskItemOrg.setPointTaskItemId(patrolTaskItem.getId());
                    JpaUtil.persist(relPatrolTaskItemOrg);

                    String orgId = relPatrolTaskItemOrgOld.getOrgId();
                    Org org = JpaUtil.linq(Org.class)
                            .equal("code", orgId)
                            .findOne();
                    //巡防计划主表-新1
                    //按天切分
                    long days = MyDateUtil.getTwoDay(patrolStartDate, patrolEndDate);
                    for (int i = 0; i < days; i++) {
                        Date today = MyDateUtil.findDaysAfter(patrolStartDate, i);

                        // @formatter:on
                        String planName = org.getName() + "-"
                                + patrolTask.getName();
                        BizPointPlan patrolPlan = new BizPointPlan();
                        patrolPlan.setName(planName);
                        patrolPlan.setPointTaskItemId(patrolTaskItem.getId());
                        patrolPlan.setOrgId(orgId);
                        // 时间
                        Date startDateTime = MyDateUtil.plusDayTime(today,
                                patrolTaskItem.getPatrolStartTime());
                        Date endDateTime = MyDateUtil.plusDayTime(today,
                                patrolTaskItem.getPatrolEndTime());
                        patrolPlan.setAnnounceTime(new Date());
                        patrolPlan.setPatrolStartTime(startDateTime);
                        patrolPlan.setPatrolEndTime(endDateTime);
                        patrolPlan.setDoneRatio(patrolTask.getDoneRatio());
                        patrolPlan.setWorkStatus(
                                BizConstants.PATROL_PLAN.WORK_STATUS.READY);
                        JpaUtil.persist(patrolPlan);

                        // 巡防计划主表-老2
                        List<BizPointPlan> patrolPlanListOld = JpaUtil
                                .linq(BizPointPlan.class)
                                .equal("pointTaskItemId", patrolTaskItemIdOld).list();
                        // 巡防计划/巡防点关系都一样，取一个即可
                        BizPointPlan patrolPlanOld = patrolPlanListOld.get(0);
                        List<BizPointPlanPoint> relPatrolPlanPointListOld = JpaUtil
                                .linq(BizPointPlanPoint.class)
                                .equal("pointPlanId", patrolPlanOld.getId())
                                .list();
                        for (BizPointPlanPoint relPatrolPlanPointOld : relPatrolPlanPointListOld) {
                            // 巡防计划/巡防点关系-新
                            BizPointPlanPoint relPatrolPlanPoint = new BizPointPlanPoint();
                            relPatrolPlanPoint.setPointPlanId(patrolPlan.getId());
                            relPatrolPlanPoint.setPointId(
                                    relPatrolPlanPointOld.getPointId());
                            relPatrolPlanPoint.setRequired(
                                    relPatrolPlanPointOld.getRequired());
                            JpaUtil.persist(relPatrolPlanPoint);
                        }

                    }
                }

                // 任务项/巡防点关系-老
                List<BizPointTaskItemPoint> relPatrolTaskItemPointListOld = JpaUtil
                        .linq(BizPointTaskItemPoint.class)
                        .equal("pointTaskItemId", patrolTaskItemIdOld).list();
                for (BizPointTaskItemPoint relPatrolTaskItemPointOld : relPatrolTaskItemPointListOld) {
                    // 任务项/巡防点关系-新
                    BizPointTaskItemPoint relPatrolTaskItemPoint = new BizPointTaskItemPoint();

                    BeanUtils.copyProperties(relPatrolTaskItemPointOld,
                            relPatrolTaskItemPoint);
                    relPatrolTaskItemPoint.setId(null);
                    relPatrolTaskItemPoint
                            .setPointTaskItemId(patrolTaskItem.getId());
                    JpaUtil.persist(relPatrolTaskItemPoint);
                }

                // 任务项/用户数量关系-老
                List<BizPointTaskItemUserAmountRel> relPatrolTaskItemUserAmountListOld = JpaUtil
                        .linq(BizPointTaskItemUserAmountRel.class)
                        .equal("pointTaskItemId", patrolTaskItemIdOld).list();
                for (BizPointTaskItemUserAmountRel relPatrolTaskItemUserAmountOld : relPatrolTaskItemUserAmountListOld) {
                    // 任务项/用户数量-新
                    BizPointTaskItemUserAmountRel relPatrolTaskItemUserAmount = new BizPointTaskItemUserAmountRel();

                    BeanUtils.copyProperties(relPatrolTaskItemUserAmountOld,
                            relPatrolTaskItemUserAmount);
                    relPatrolTaskItemUserAmount.setId(null);
                    relPatrolTaskItemUserAmount
                            .setPointTaskItemId(patrolTaskItem.getId());
                    JpaUtil.persist(relPatrolTaskItemUserAmount);
                }

                // @formatter:on
            }
        }
    }
}
