package com.mr.sa.controller.task;


import cn.hutool.core.util.IdUtil;
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
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.feilong.core.date.DateUtil;
import com.mr.sa.data.vo.PatrolPointVO;
import com.mr.sa.entity.*;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.common.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 巡逻任务 前端控制器
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */
@Slf4j
@Controller
@Transactional
public class BizPointTaskItemController extends QueryFilter {
    @DataResolver
    @Transactional
    @Log(module = "定点任务项", category = "CRUD")
    public void save(List<BizPointTaskItem> bizPointTaskItems) {
        JpaUtil.save(bizPointTaskItems, new SmartSavePolicyAdapter() {
            @Override
            public void afterUpdate(SaveContext context) {

                BizPointTaskItem pointTaskItem = context.getEntity();
                if (EntityState.MODIFIED.equals(EntityUtils.getState(pointTaskItem))) {
                    // 只影响未开始的taskplan，正在进行和已经结束的不影响
                    List<BizPointPlan> pointPlanList = JpaUtil.linq(BizPointPlan.class)
                            .equal("pointTaskItemId", pointTaskItem.getId())
                            .equal("workStatus", BizConstants.PATROL_PLAN.WORK_STATUS.READY)
                            .list();
                    for (BizPointPlan pointPlan : pointPlanList) {
                        // 时间
                        Date today = pointPlan.getPatrolStartTime();
                        Date startDateTime = MyDateUtil.plusDayTime(today, pointTaskItem.getPatrolStartTime());
                        Date endDateTime = MyDateUtil.plusDayTime(today, pointTaskItem.getPatrolEndTime());
                        pointPlan.setPatrolStartTime(startDateTime);
                        pointPlan.setPatrolEndTime(endDateTime);
                        JpaUtil.merge(pointPlan);
                    }
                }
            }

            @Override
            public boolean beforeDelete(SaveContext context) {
                //BizPointTask
                BizPointTaskItem patrolTaskItem = context.getEntity();
                String pointTaskItemId = patrolTaskItem.getId();
                //BizPointTask-BizPointTaskItem-BizPointPlan
                List<BizPointPlan> patrolPlanList = JpaUtil.linq(BizPointPlan.class)
                        .equal("pointTaskItemId", pointTaskItemId)
                        .list();
                for (BizPointPlan pointPlan : patrolPlanList) {
                    String pointPlanId = pointPlan.getId();
                    //BizPointTask-BizPointTaskItem-BizPointPlan-BizPointPlanPoint
                    JpaUtil.lind(BizPointPlanPoint.class)
                            .equal("pointPlanId", pointPlanId)
                            .delete();
                }
                //BizPointTask-BizPointTaskItem-BizPointPlan-delete
                JpaUtil.lind(BizPointPlan.class)
                        .equal("pointTaskItemId", pointTaskItemId)
                        .delete();

                //BizPointTask-BizPointTaskItem-BizPointTaskItemOrg
                JpaUtil.lind(BizPointTaskItemOrg.class)
                        .equal("pointTaskItemId", pointTaskItemId)
                        .delete();
                //BizPointTask-BizPointTaskItem-BizPointTaskItemPoint
                JpaUtil.lind(BizPointTaskItemPoint.class)
                        .equal("pointTaskItemId", pointTaskItemId)
                        .delete();
                //BizPointTask-BizPointTaskItem-BizPointTaskItemUserAmountRel
                JpaUtil.lind(BizPointTaskItemUserAmountRel.class)
                        .equal("pointTaskItemId", pointTaskItemId)
                        .delete();
                return true;
            }
        });
    }

    @DataProvider
    public void query(Page<BizPointTaskItem> page,
                      Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        criteria = condition(criteria, parameter);
        JpaUtil.linq(BizPointTaskItem.class).where(criteria).desc("updateDate")
                .paging(page);
    }


    // 复制任务
    @Expose
    public void copy(Map<String, Object> parameter) {
        List<BizPointTaskItem> patrolTaskItemListOld = (List<BizPointTaskItem>) parameter.get("selection");
        Date patrolStartTime = (Date) parameter.get("patrolStartTime");
        Date patrolEndTime = (Date) parameter.get("patrolEndTime");
        for (BizPointTaskItem patrolTaskItemOld : patrolTaskItemListOld) {
            // Item-老
            String patrolTaskItemIdOld = patrolTaskItemOld.getId();
            // Task-老
            String patrolTaskIdOld = patrolTaskItemOld.getPointTaskId();
            BizPointTask patrolTask = JpaUtil.linq(BizPointTask.class).idEqual(patrolTaskIdOld).findOne();
            Date patrolStartDate = patrolTask.getPatrolStartDate();
            Date patrolEndDate = patrolTask.getPatrolEndDate();

            // Item-新
            BizPointTaskItem patrolTaskItem = new BizPointTaskItem();

            BeanUtils.copyProperties(patrolTaskItemOld, patrolTaskItem);
            patrolTaskItem.setPatrolStartTime(patrolStartTime);
            patrolTaskItem.setPatrolEndTime(patrolEndTime);
            patrolTaskItem.setId(null);
            JpaUtil.persist(patrolTaskItem);

            // 任务项/组织关系-老
            List<BizPointTaskItemOrg> relPatrolTaskItemOrgListOld = JpaUtil.linq(BizPointTaskItemOrg.class).equal("pointTaskItemId", patrolTaskItemIdOld).list();
            for (BizPointTaskItemOrg relPatrolTaskItemOrgOld : relPatrolTaskItemOrgListOld) {
                // 任务项/组织关系-新
                BizPointTaskItemOrg relPatrolTaskItemOrg = new BizPointTaskItemOrg();

                BeanUtils.copyProperties(relPatrolTaskItemOrgOld, relPatrolTaskItemOrg);
                relPatrolTaskItemOrg.setId(null);
                relPatrolTaskItemOrg.setPointTaskItemId(patrolTaskItem.getId());
                JpaUtil.persist(relPatrolTaskItemOrg);

                String orgId = relPatrolTaskItemOrgOld.getOrgId();
                Org org = JpaUtil.linq(Org.class).equal("code", orgId).findOne();
                // 巡防计划主表-新1
                // 按天切分
                long days = MyDateUtil.getTwoDay(patrolStartDate, patrolEndDate);
                for (int i = 0; i < days; i++) {
                    Date today = MyDateUtil.findDaysAfter(patrolStartDate, i);

                    // @formatter:on
                    String planName = org.getName() + "-" + patrolTask.getName();
                    BizPointPlan patrolPlan = new BizPointPlan();
                    patrolPlan.setName(planName);
                    patrolPlan.setPointTaskItemId(patrolTaskItem.getId());
                    patrolPlan.setOrgId(orgId);
                    // 时间
                    Date startDateTime = MyDateUtil.plusDayTime(today, patrolTaskItem.getPatrolStartTime());
                    Date endDateTime = MyDateUtil.plusDayTime(today, patrolTaskItem.getPatrolEndTime());
                    patrolPlan.setAnnounceTime(new Date());
                    patrolPlan.setPatrolStartTime(startDateTime);
                    patrolPlan.setPatrolEndTime(endDateTime);
                    patrolPlan.setDoneRatio(patrolTask.getDoneRatio());
                    patrolPlan.setWorkStatus(BizConstants.PATROL_PLAN.WORK_STATUS.READY);
                    JpaUtil.persist(patrolPlan);

                    // 巡防计划主表-老2
                    List<BizPointPlan> patrolPlanListOld = JpaUtil.linq(BizPointPlan.class).equal("pointTaskItemId", patrolTaskItemIdOld).list();
                    // 巡防计划/巡防点关系都一样，取一个即可
                    BizPointPlan patrolPlanOld = patrolPlanListOld.get(0);
                    List<BizPointPlanPoint> relPatrolPlanPointListOld = JpaUtil.linq(BizPointPlanPoint.class).equal("pointPlanId", patrolPlanOld.getId()).list();
                    for (BizPointPlanPoint relPatrolPlanPointOld : relPatrolPlanPointListOld) {
                        // 巡防计划/巡防点关系-新
                        BizPointPlanPoint relPatrolPlanPoint = new BizPointPlanPoint();
                        relPatrolPlanPoint.setPointPlanId(patrolPlan.getId());
                        relPatrolPlanPoint.setPointId(relPatrolPlanPointOld.getPointId());
                        relPatrolPlanPoint.setRequired(relPatrolPlanPointOld.getRequired());
                        JpaUtil.persist(relPatrolPlanPoint);
                    }
                }
            }

            // 任务项/巡防点关系-老
            List<BizPointTaskItemPoint> relPatrolTaskItemPointListOld = JpaUtil.linq(BizPointTaskItemPoint.class).equal("pointTaskItemId", patrolTaskItemIdOld).list();
            for (BizPointTaskItemPoint relPatrolTaskItemPointOld : relPatrolTaskItemPointListOld) {
                // 任务项/巡防点关系-新
                BizPointTaskItemPoint relPatrolTaskItemPoint = new BizPointTaskItemPoint();

                BeanUtils.copyProperties(relPatrolTaskItemPointOld, relPatrolTaskItemPoint);
                relPatrolTaskItemPoint.setId(null);
                relPatrolTaskItemPoint.setPointTaskItemId(patrolTaskItem.getId());
                JpaUtil.persist(relPatrolTaskItemPoint);
            }

            // 任务项/用户数量关系-老
            List<BizPointTaskItemUserAmountRel> relPatrolTaskItemUserAmountListOld = JpaUtil.linq(BizPointTaskItemUserAmountRel.class).equal("pointTaskItemId", patrolTaskItemIdOld).list();
            for (BizPointTaskItemUserAmountRel relPatrolTaskItemUserAmountOld : relPatrolTaskItemUserAmountListOld) {
                // 任务项/用户数量-新
                BizPointTaskItemUserAmountRel relPatrolTaskItemUserAmount = new BizPointTaskItemUserAmountRel();

                BeanUtils.copyProperties(relPatrolTaskItemUserAmountOld, relPatrolTaskItemUserAmount);
                relPatrolTaskItemUserAmount.setId(null);
                relPatrolTaskItemUserAmount.setPointTaskItemId(patrolTaskItem.getId());
                JpaUtil.persist(relPatrolTaskItemUserAmount);
            }

            // @formatter:on
        }

    }

    @Expose
    public void deleteOrgModel(Map<String, Object> parameter) {
        List<BizPointTaskItem> patrolTaskItemList = (List<BizPointTaskItem>) parameter.get("selectionPatrolTaskItem");
        String orgId = (String) parameter.get("orgId");
        List<String> patrolTaskItemIds = patrolTaskItemList.stream().map(BizPointTaskItem::getId).distinct().collect(Collectors.toList());
        // 批量删除
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            deleteOrg(patrolTaskItemIds);
        }
    }


    private void deleteOrg(List<String> patrolTaskItemIds) {
        // @formatter:off
        try {
            //删除任务项-组织关系
            JpaUtil.lind(BizPointTaskItemOrg.class).in("pointTaskItemId", patrolTaskItemIds).delete();
            //批量清空前查询-任务项-巡防点关系
            JpaUtil.lind(BizPointTaskItemPoint.class).in("pointTaskItemId", patrolTaskItemIds).delete();
            //清除关系-用户数量
            JpaUtil.lind(BizPointTaskItemUserAmountRel.class).in("pointTaskItemId", patrolTaskItemIds).delete();
            //删除巡防计划主表 未开始&大于now
            List<BizPointPlan> patrolPlanList = JpaUtil.linq(BizPointPlan.class).in("pointTaskItemId", patrolTaskItemIds).equal("workStatus", BizConstants.PATROL_PLAN.WORK_STATUS.READY).list();
            //批量清空前查询-任务项-巡防点关系
            List<String> patrolPlanIds = patrolPlanList.stream().map(BizPointPlan::getId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(patrolPlanIds)) {
                JpaUtil.lind(BizPointPlan.class).in("id", patrolPlanIds).delete();
                JpaUtil.lind(BizPointPlanPoint.class).in("pointPlanId", patrolPlanIds).delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // @formatter:on
    }

    // 关系-组织
    @DataProvider
    public List<Org> loadOrg(Map<String, Object> parameter) {
        List<BizPointTaskItem> patrolTaskItemList = (List<BizPointTaskItem>) parameter.get("selectionPatrolTaskItem");
        List<String> pointTaskItemIds = patrolTaskItemList.stream().map(BizPointTaskItem::getId)
                .collect(Collectors.toList());
        List<Org> orgList = JpaUtil.linq(Org.class).isNull("parentId").list();
        for (Org org : orgList) {
            // @formatter:off
            boolean isExist = JpaUtil.linq(BizPointTaskItemOrg.class)
                    .in("pointTaskItemId", pointTaskItemIds)
                    .equal("orgId", org.getCode())
                    .exists();
            // @formatter:on
            org.setChecked(isExist);
        }
        return orgList;
    }

    @DataProvider
    public List<Org> loadChildrenOrg(Map<String, Object> parameter) {
//        BizPointTaskItem patrolTaskItem = (BizPointTaskItem) parameter.get("selectionPatrolTaskItem");
        List<BizPointTaskItem> patrolTaskItemList = (List<BizPointTaskItem>) parameter.get("selectionPatrolTaskItem");
        List<String> pointTaskItemIds = patrolTaskItemList.stream().map(BizPointTaskItem::getId)
                .collect(Collectors.toList());
//        String patrolTaskItemId = patrolTaskItem.getId();
        String parentId = (String) parameter.get("parentId");
        List<Org> orgList = JpaUtil.linq(Org.class).equal("parentId", parentId)
                .asc("code").list();
        for (Org org : orgList) {
            // @formatter:off
            boolean isExist = JpaUtil.linq(BizPointTaskItemOrg.class)
                    .in("pointTaskItemId", pointTaskItemIds)
                    .equal("orgId", org.getCode())
                    .exists();
            // @formatter:on
            log.info("isExist", isExist);
            org.setChecked(isExist);
        }
        return orgList;
    }

    @DataProvider
    public List<BizPointTaskItemOrg> queryOrg(Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        criteria = condition(criteria, parameter);
        // @formatter:off
        List<BizPointTaskItemOrg> list = JpaUtil
                .linq(BizPointTaskItemOrg.class)
                .where(criteria)
                .desc("updateDate")
                .list();
        // @formatter:on
        return list;
    }

    // 关系-巡防点
    @DataResolver
    public List<BizPointTaskItemPoint> queryPoint(
            Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        criteria = condition(criteria, parameter);
        // @formatter:off
        List<BizPointTaskItemPoint> list = JpaUtil
                .linq(BizPointTaskItemPoint.class)
                .where(criteria)
                .desc("updateDate")
                .list();
        // @formatter:on
        return list;
    }


    // 关系-全
    @Expose
    @Transactional
    public void saveRel(Map<String, Object> parameter) {
        List<BizPointTaskItem> patrolTaskItems = (List<BizPointTaskItem>) parameter.get("selectionPatrolTaskItem");
        List<PatrolPointVO> patrolPointList = (List<PatrolPointVO>) parameter.get("dsPatrolPoint");
        List<BizPointTaskItemUserAmountRel> relPatrolTaskItemUserAmounts = (List<BizPointTaskItemUserAmountRel>) parameter.get("formUserAmount");
        List<String> orgIds = (List<String>) parameter.get("orgIds");

        List<String> patrolTaskItemIds = patrolTaskItems.stream().map(BizPointTaskItem::getId).distinct().collect(Collectors.toList());
        //
//        List<String> patrolTaskItemIds = Arrays.asList(patrolTaskItem.getId());
        if (CollectionUtils.isEmpty(orgIds)) {
            throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请先选择单位!');");
        }
        BizPointTask one = JpaUtil.getOne(BizPointTask.class, patrolTaskItems.get(0).getPointTaskId());
        if (one.getPatrolEndDate().before(new Date())) {
            return;
        }
        // 批量删除
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            deleteOrg(patrolTaskItemIds);
        }
        List<Org> orgList = JpaUtil.linq(Org.class).in("code", orgIds).list();
        batch(patrolTaskItemIds, orgList);
        // 关系-巡防点
        List<PatrolPointVO> checkedList = patrolPointList.stream().filter(patrolPointVO -> patrolPointVO.isChecked()).collect(Collectors.toList());
        savePoint(checkedList, patrolTaskItemIds);
//        for (BizPointTaskItem patrolTaskItem : patrolTaskItemList) {
//        String pointTaskItemId = patrolTaskItem.getId();

        //如果填数量，就不存人
        // 关系-人员数量
        saveUserAmount(relPatrolTaskItemUserAmounts, patrolTaskItemIds);

//        }
        //组织计划
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            JpaUtil.lind(BizPointOrgPlan.class).in("pointTaskItemId", patrolTaskItemIds).delete();
        }
        saveOrgPlan(orgIds, patrolTaskItemIds);

    }

    private void batch(List<String> pointTaskItemIds, List<Org> orgList) {
        List<BizPointTaskItemOrg> relPatrolTaskItemOrgs = new ArrayList<>();

        orgList.forEach(org -> {
            for (String patrolTaskItemId : pointTaskItemIds) {
                BizPointTaskItemOrg relPatrolTaskItemOrg = new BizPointTaskItemOrg();
                relPatrolTaskItemOrg.setPointTaskItemId(patrolTaskItemId);
                relPatrolTaskItemOrg.setOrgId(org.getCode());
                relPatrolTaskItemOrgs.add(relPatrolTaskItemOrg);
            }
        });

        // 创建巡防计划
//        List<PatrolTaskItem> patrolTaskItems = JpaUtil.linq(PatrolTaskItem.class).in("id", pointTaskItemIds).list();
//        List<String> patrolTaskIds = patrolTaskItems.stream().map(PatrolTaskItem::getPatrolTaskId).distinct().collect(Collectors.toList());

        List<BizPointTaskItem> pointTaskItems = JpaUtil.linq(BizPointTaskItem.class).in("id", pointTaskItemIds).list();
        String pointTaskId = pointTaskItems.get(0).getPointTaskId();
        BizPointTask pointTask = JpaUtil.linq(BizPointTask.class).equal("id", pointTaskId).findOne();
        Map<String, List<BizPointTaskItem>> pointTaskMap = pointTaskItems.stream().collect(Collectors.groupingBy(BizPointTaskItem::getPointTaskId));
        List<BizPointPlan> patrolPlanList = new ArrayList<>();
        Date patrolStartDate = pointTask.getPatrolStartDate();
        Date patrolEndDate = pointTask.getPatrolEndDate();
        //判断当前时间是否大于开始时间小于结束时间
        Date now = new Date();
        if (now.after(patrolStartDate) && now.before(patrolEndDate)) {
            patrolStartDate = now;
        } else if (now.after(patrolEndDate)) {
            return;
        }
        //按天切分
        long days = MyDateUtil.getTwoDay(patrolStartDate, patrolEndDate);
        for (int i = 0; i <= days; i++) {
            Date today = MyDateUtil.findDaysAfter(patrolStartDate, i);
            // @formatter:on

            orgList.forEach(org -> {
                String planName = org.getName() + "-" + pointTask.getName();
                pointTaskItems.forEach(pointTaskItem -> {
                    // 时间
                    Date startDateTime = MyDateUtil.plusDayTime(today, pointTaskItem.getPatrolStartTime());
                    Date endDateTime = MyDateUtil.plusDayTime(today, pointTaskItem.getPatrolEndTime());
                    if (endDateTime.before(now) && startDateTime.before(now)) {
                        return;
                    }
                    if (endDateTime.before(startDateTime)) {
                        // 跨天 结束日期+1天
                        endDateTime = DateUtil.addDay(endDateTime, 1);
                    }
                    BizPointPlan patrolPlan = new BizPointPlan();
                    patrolPlan.setName(planName);
                    patrolPlan.setPointTaskItemId(pointTaskItem.getId());
                    patrolPlan.setOrgId(org.getCode());

                    patrolPlan.setAnnounceTime(new Date());
                    patrolPlan.setPatrolStartTime(startDateTime);
                    patrolPlan.setPatrolEndTime(endDateTime);
                    patrolPlan.setDoneRatio(pointTask.getDoneRatio());
                    patrolPlan.setWorkStatus(BizConstants.PATROL_PLAN.WORK_STATUS.READY);
                    patrolPlanList.add(patrolPlan);
                });
            });
        }
        JpaUtil.persist(relPatrolTaskItemOrgs);
        JpaUtil.persist(patrolPlanList);
    }

    private void savePoint(List<PatrolPointVO> patrolPoints, List<String> patrolTaskItemIds) {
        // @formatter:off
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            JpaUtil.lind(BizPointTaskItemPoint.class).in("pointTaskItemId", patrolTaskItemIds).delete();
        }
        // @formatter:on
        List<BizPointTaskItemPoint> relPatrolTaskItemPoints = new ArrayList<>();
        patrolTaskItemIds.forEach(s -> {
            patrolPoints.forEach(patrolPoint -> {
                BizPointTaskItemPoint relPatrolTaskItemPoint = new BizPointTaskItemPoint();
                relPatrolTaskItemPoint.setPointTaskItemId(s);
                relPatrolTaskItemPoint.setPointId(patrolPoint.getId());
                relPatrolTaskItemPoint.setRequired(patrolPoint.isRequired());
                relPatrolTaskItemPoints.add(relPatrolTaskItemPoint);
            });
        });
        JpaUtil.persist(relPatrolTaskItemPoints);
        // 更新巡防计划-更新巡防点关系
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            List<BizPointPlan> patrolPlanList = JpaUtil.linq(BizPointPlan.class).in("pointTaskItemId", patrolTaskItemIds).equal("workStatus", BizConstants.PATROL_PLAN.WORK_STATUS.READY).list();
            List<String> patrolPlanIds = patrolPlanList.stream().map(BizPointPlan::getId).distinct().collect(Collectors.toList());
            JpaUtil.lind(BizPointPlanPoint.class).in("pointPlanId", patrolPlanIds).delete();
            List<BizPointPlanPoint> relPatrolPlanPointList = new ArrayList<>();
            patrolPlanList.stream().forEach(patrolPlan -> {
                patrolPoints.forEach(patrolPoint -> {
                    BizPointPlanPoint relPatrolPlanPoint = new BizPointPlanPoint();
                    relPatrolPlanPoint.setPointPlanId(patrolPlan.getId());
                    relPatrolPlanPoint.setPointId(patrolPoint.getId());
                    relPatrolPlanPoint.setRequired(patrolPoint.isRequired());
                    relPatrolPlanPointList.add(relPatrolPlanPoint);
                });

            });
            JpaUtil.persist(relPatrolPlanPointList);
        }
    }

    // 关系-人员数量
    private void saveUserAmount(List<BizPointTaskItemUserAmountRel> relPatrolTaskItemUserAmounts, List<String> pointTaskItemIds) {
        // @formatter:on
        List<BizPointTaskItemUserAmountRel> temps = new ArrayList<>();
        pointTaskItemIds.forEach(pointTaskItemId -> {
            relPatrolTaskItemUserAmounts.forEach(bizPointTaskItemUserAmountRel -> {
                BizPointTaskItemUserAmountRel rel = new BizPointTaskItemUserAmountRel();
                BeanUtils.copyProperties(bizPointTaskItemUserAmountRel, rel);
                rel.setId(IdUtil.simpleUUID());
                rel.setPointTaskItemId(pointTaskItemId);
                temps.add(rel);
            });
        });
        JpaUtil.persist(temps);
    }

    public void saveOrgPlan(List<String> orgIds, List<String> patrolTaskItemIds) {
        List<BizPointTaskItem> patrolTaskItems = JpaUtil.linq(BizPointTaskItem.class).in("id", patrolTaskItemIds).list();
        List<String> patrolTaskIds = patrolTaskItems.stream().distinct().map(BizPointTaskItem::getPointTaskId).collect(Collectors.toList());
        List<BizPointTask> patrolTasks = JpaUtil.linq(BizPointTask.class).in("id", patrolTaskIds).list();
        Map<String, List<BizPointTaskItem>> patrolTaskIdMap = patrolTaskItems.stream().collect(Collectors.groupingBy(BizPointTaskItem::getPointTaskId));
        List<BizPointOrgPlan> patrolPlanList = new ArrayList<>();
        patrolTasks.forEach(patrolTask -> {
            List<BizPointTaskItem> patrolTaskItemList = patrolTaskIdMap.get(patrolTask.getId());
            patrolTaskItemList.forEach(patrolTaskItem -> {
                // 时间
                Date startDateTime = MyDateUtil.plusDayTime(
                        patrolTask.getPatrolStartDate(),
                        patrolTaskItem.getPatrolStartTime());
                Date endDateTime = MyDateUtil.plusDayTime(patrolTask.getPatrolEndDate(),
                        patrolTaskItem.getPatrolEndTime());
                orgIds.forEach(s -> {
                    BizPointOrgPlan orgPlan = new BizPointOrgPlan();
                    orgPlan.setPointTaskItemId(patrolTaskItem.getId());
                    orgPlan.setOrgId(s);
                    orgPlan.setPatrolStartTime(startDateTime);
                    orgPlan.setPatrolEndTime(endDateTime);
                    patrolPlanList.add(orgPlan);
                });
            });
        });
        JpaUtil.persist(patrolPlanList);
    }
}
