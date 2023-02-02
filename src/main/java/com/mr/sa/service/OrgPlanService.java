package com.mr.sa.service;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.mr.sa.entity.OrgPlan;
import com.mr.sa.entity.PatrolTask;
import com.mr.sa.entity.PatrolTaskItem;
import com.mr.sa.utils.common.MyDateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrgPlanService {

    // 组织-时间段
    public void saveOrgPlan(String orgId, String patrolTaskItemId) {
        PatrolTaskItem patrolTaskItem = JpaUtil.linq(PatrolTaskItem.class)
                .idEqual(patrolTaskItemId)
                .findOne();
        PatrolTask patrolTask = JpaUtil.linq(PatrolTask.class)
                .idEqual(patrolTaskItem.getPatrolTaskId())
                .findOne();
        // 时间
        Date startDateTime = MyDateUtil.plusDayTime(
                patrolTask.getPatrolStartDate(),
                patrolTaskItem.getPatrolStartTime());
        Date endDateTime = MyDateUtil.plusDayTime(patrolTask.getPatrolEndDate(),
                patrolTaskItem.getPatrolEndTime());
        OrgPlan orgPlan = new OrgPlan();
        orgPlan.setPatrolTaskItemId(patrolTaskItemId);
        orgPlan.setOrgId(orgId);
        orgPlan.setPatrolStartTime(startDateTime);
        orgPlan.setPatrolEndTime(endDateTime);
        JpaUtil.persist(orgPlan);
    }

    public void saveOrgPlan(String orgId, List<String> patrolTaskItemIds) {
        List<PatrolTaskItem> patrolTaskItems = JpaUtil.linq(PatrolTaskItem.class).in("id", patrolTaskItemIds).list();
        List<String> patrolTaskIds = patrolTaskItems.stream().distinct().map(PatrolTaskItem::getPatrolTaskId).collect(Collectors.toList());
        List<PatrolTask> patrolTasks = JpaUtil.linq(PatrolTask.class).in("id", patrolTaskIds).list();
        Map<String, List<PatrolTaskItem>> patrolTaskIdMap = patrolTaskItems.stream().collect(Collectors.groupingBy(PatrolTaskItem::getPatrolTaskId));
        List<OrgPlan> patrolPlanList = new ArrayList<>();
        patrolTasks.forEach(patrolTask -> {
            List<PatrolTaskItem> patrolTaskItemList = patrolTaskIdMap.get(patrolTask.getId());
            patrolTaskItemList.forEach(patrolTaskItem -> {
                // 时间
                Date startDateTime = MyDateUtil.plusDayTime(
                        patrolTask.getPatrolStartDate(),
                        patrolTaskItem.getPatrolStartTime());
                Date endDateTime = MyDateUtil.plusDayTime(patrolTask.getPatrolEndDate(),
                        patrolTaskItem.getPatrolEndTime());
                OrgPlan orgPlan = new OrgPlan();
                orgPlan.setPatrolTaskItemId(patrolTaskItem.getId());
                orgPlan.setOrgId(orgId);
                orgPlan.setPatrolStartTime(startDateTime);
                orgPlan.setPatrolEndTime(endDateTime);
                patrolPlanList.add(orgPlan);
            });
        });
        JpaUtil.persist(patrolPlanList);
    }

    public void deleteOrgPlan(String orgId, String patrolTaskItemId) {
        // @formatter:off
        JpaUtil.lind(OrgPlan.class)
                .equal("patrolTaskItemId", patrolTaskItemId)
                .equal("orgId", orgId)
                .delete();
        // @formatter:on
    }
}