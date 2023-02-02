package com.mr.sa.service;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.mr.sa.config.RedisTemplateHelper;
import com.mr.sa.entity.*;
import com.mr.sa.entity.app.AppRole;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.entity.rel.*;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.utils.common.GeTuiUtils;
import com.mr.sa.utils.common.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PatrolTaskItemService {

    @Autowired
    private RedisTemplateHelper redisTemplate;
    @Autowired
    private GeTuiUtils geTuiUtils;
    @Autowired
    private OrgPlanService orgPlanService;


    // 关系-全
    @Expose
    @Transactional
    public void save(Map<String, Object> parameter) {
        List<PatrolTaskItem> patrolTaskItemList = (List<PatrolTaskItem>) parameter.get("selectionPatrolTaskItem");
        List<AppUser> appUserList = (List<AppUser>) parameter.get("dsUser");
        List<PatrolPoint> patrolPointList = (List<PatrolPoint>) parameter.get("dsPatrolPoint");
        RelPatrolTaskItemUserAmount relPatrolTaskItemUserAmount = (RelPatrolTaskItemUserAmount) parameter.get("formUserAmount");
        String orgId = (String) parameter.get("orgId");

        List<String> patrolTaskItemIds = patrolTaskItemList.stream().map(PatrolTaskItem::getId).distinct().collect(Collectors.toList());
        //
        if (StringUtils.isBlank(orgId)) {
            // 查询item-org关系表
            List<RelPatrolTaskItemOrg> relPatrolTaskItemOrgs = JpaUtil.linq(RelPatrolTaskItemOrg.class).in("patrolTaskItemId", patrolTaskItemIds).list();
            if (CollectionUtils.isEmpty(relPatrolTaskItemOrgs)) {
                throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请先选择单位!');");
            } else {
                orgId = relPatrolTaskItemOrgs.get(0).getOrgId();
            }
        }
        PatrolTask one = JpaUtil.getOne(PatrolTask.class, patrolTaskItemList.get(0).getPatrolTaskId());
        if (one.getPatrolEndDate().before(new Date())) {
            return;
        }
        // 批量删除
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            deleteOrg(patrolTaskItemIds, orgId);
        }
        Org org = JpaUtil.linq(Org.class).equal("code", orgId).findOne();
        batch(patrolTaskItemIds, org);
        // 关系-巡防点
        List<PatrolPoint> checkedList = patrolPointList.stream().filter(patrolPoint -> patrolPoint.isChecked()).collect(Collectors.toList());
        savePoint(checkedList, orgId, patrolTaskItemIds);
        // 关系-巡防人员
        deleteUser(orgId, patrolTaskItemIds);
        List<AppUser> checkedUserList = appUserList.stream().filter(appUser -> appUser.isChecked()).collect(Collectors.toList());

        List<String> newList = new ArrayList<>();
        for (PatrolTaskItem patrolTaskItem : patrolTaskItemList) {
            String patrolTaskItemId = patrolTaskItem.getId();
            //如果填数量，就不存人
            if (relPatrolTaskItemUserAmount.getCopAmount() > 0 || relPatrolTaskItemUserAmount.getApAmount() > 0 || relPatrolTaskItemUserAmount.getMemberAmount() > 0) {
                // 关系-人员数量
                saveUserAmount(relPatrolTaskItemUserAmount, orgId, patrolTaskItemId);
            } else {
                newList.add(patrolTaskItemId);
            }
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            saveUser(checkedUserList, orgId, newList);
        }
        //组织计划
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            JpaUtil.lind(OrgPlan.class).in("patrolTaskItemId", patrolTaskItemIds).equal("orgId", orgId).delete();
        }
        orgPlanService.saveOrgPlan(orgId, patrolTaskItemIds);
    }

    private void batch(List<String> patrolTaskItemIds, Org org) {
        List<RelPatrolTaskItemOrg> relPatrolTaskItemOrgs = patrolTaskItemIds.stream().map(s -> {
            RelPatrolTaskItemOrg relPatrolTaskItemOrg = new RelPatrolTaskItemOrg();
            relPatrolTaskItemOrg.setPatrolTaskItemId(s);
            relPatrolTaskItemOrg.setOrgId(org.getCode());
            return relPatrolTaskItemOrg;
        }).collect(Collectors.toList());

        // 创建巡防计划
        // @formatter:off
        List<PatrolTaskItem> patrolTaskItems = JpaUtil.linq(PatrolTaskItem.class).in("id", patrolTaskItemIds).list();
        List<String> patrolTaskIds = patrolTaskItems.stream().map(PatrolTaskItem::getPatrolTaskId).distinct().collect(Collectors.toList());
        List<PatrolTask> patrolTasks = JpaUtil.linq(PatrolTask.class).in("id", patrolTaskIds).list();
        Map<String, List<PatrolTaskItem>> patrolTaskIdMap = patrolTaskItems.stream().collect(Collectors.groupingBy(PatrolTaskItem::getPatrolTaskId));
        List<PatrolPlan> patrolPlanList = new ArrayList<>();
        patrolTasks.stream().forEach(patrolTask -> {
            Date patrolStartDate = patrolTask.getPatrolStartDate();
            Date patrolEndDate = patrolTask.getPatrolEndDate();
            List<PatrolTaskItem> patrolTaskItemList = patrolTaskIdMap.get(patrolTask.getId());
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
                String planName = org.getName() + "-" + patrolTask.getName();
                patrolTaskItemList.forEach(patrolTaskItem -> {
                    // 时间
                    Date startDateTime = MyDateUtil.plusDayTime(today, patrolTaskItem.getPatrolStartTime());
                    Date endDateTime = MyDateUtil.plusDayTime(today, patrolTaskItem.getPatrolEndTime());
                    if (endDateTime.before(now)) {
                        return;
                    }
                    PatrolPlan patrolPlan = new PatrolPlan();
                    patrolPlan.setName(planName);
                    patrolPlan.setPatrolTaskItemId(patrolTaskItem.getId());
                    patrolPlan.setOrgId(org.getCode());

                    patrolPlan.setAnnounceTime(new Date());
                    patrolPlan.setPatrolStartTime(startDateTime);
                    patrolPlan.setPatrolEndTime(endDateTime);
                    patrolPlan.setDoneRatio(patrolTask.getDoneRatio());
                    patrolPlan.setWorkStatus(BizConstants.PATROL_PLAN.WORK_STATUS.READY);
                    patrolPlanList.add(patrolPlan);
                });
            }
        });
        JpaUtil.persist(relPatrolTaskItemOrgs);
        JpaUtil.persist(patrolPlanList);
    }

    private void deleteOrg(List<String> patrolTaskItemIds, String orgId) {
        // @formatter:off
        try {
            //删除任务项-组织关系
            JpaUtil.lind(RelPatrolTaskItemOrg.class).in("patrolTaskItemId", patrolTaskItemIds).equal("orgId", orgId).delete();
            //批量清空前查询-任务项-巡防点关系
            JpaUtil.lind(RelPatrolTaskItemPoint.class).in("patrolTaskItemId", patrolTaskItemIds).equal("orgId", orgId).delete();
            //批量清空前查询-任务项-巡防人员关系
            JpaUtil.lind(RelPatrolTaskItemUser.class).in("patrolTaskItemId", patrolTaskItemIds).equal("orgId", orgId).delete();
            //清除关系-用户数量
            JpaUtil.lind(RelPatrolTaskItemUserAmount.class).in("patrolTaskItemId", patrolTaskItemIds).equal("orgId", orgId).delete();
            //删除巡防计划主表 未开始&大于now
            List<PatrolPlan> patrolPlanList = JpaUtil.linq(PatrolPlan.class).in("patrolTaskItemId", patrolTaskItemIds).equal("workStatus", BizConstants.PATROL_PLAN.WORK_STATUS.READY).equal("orgId", orgId).list();
            //批量清空前查询-任务项-巡防点关系
            List<String> patrolPlanIds = patrolPlanList.stream().map(PatrolPlan::getId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(patrolPlanIds)) {
                JpaUtil.lind(PatrolPlan.class).in("id", patrolPlanIds).delete();
                JpaUtil.lind(RelPatrolPlanPoint.class).in("patrolPlanId", patrolPlanIds).delete();
                JpaUtil.lind(RelPatrolPlanUser.class).in("patrolPlanId", patrolPlanIds).delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // @formatter:on
    }

    private void savePoint(List<PatrolPoint> patrolPoints, String orgId, List<String> patrolTaskItemIds) {
        // @formatter:off
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            JpaUtil.lind(RelPatrolTaskItemPoint.class).in("patrolTaskItemId", patrolTaskItemIds).delete();
        }
        // @formatter:on
        List<RelPatrolTaskItemPoint> relPatrolTaskItemPoints = new ArrayList<>();
        patrolTaskItemIds.forEach(s -> {
            patrolPoints.forEach(patrolPoint -> {
                RelPatrolTaskItemPoint relPatrolTaskItemPoint = new RelPatrolTaskItemPoint();
                relPatrolTaskItemPoint.setPatrolTaskItemId(s);
                relPatrolTaskItemPoint.setPatrolPointId(patrolPoint.getId());
                relPatrolTaskItemPoint.setRequired(patrolPoint.isRequired());
                relPatrolTaskItemPoints.add(relPatrolTaskItemPoint);
            });
        });
        JpaUtil.persist(relPatrolTaskItemPoints);
        // 更新巡防计划-更新巡防点关系
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            List<PatrolPlan> patrolPlanList = JpaUtil.linq(PatrolPlan.class).in("patrolTaskItemId", patrolTaskItemIds).equal("workStatus", BizConstants.PATROL_PLAN.WORK_STATUS.READY).list();
            List<String> patrolPlanIds = patrolPlanList.stream().map(PatrolPlan::getId).distinct().collect(Collectors.toList());
            JpaUtil.lind(RelPatrolPlanPoint.class).in("patrolPlanId", patrolPlanIds).delete();
            List<RelPatrolPlanPoint> relPatrolPlanPointList = new ArrayList<>();
            patrolPlanList.stream().forEach(patrolPlan -> {
                patrolPoints.forEach(patrolPoint -> {
                    RelPatrolPlanPoint relPatrolPlanPoint = new RelPatrolPlanPoint();
                    relPatrolPlanPoint.setPatrolPlanId(patrolPlan.getId());
                    relPatrolPlanPoint.setPatrolPointId(patrolPoint.getId());
                    relPatrolPlanPoint.setRequired(patrolPoint.isRequired());
                    relPatrolPlanPointList.add(relPatrolPlanPoint);
                });

            });
            JpaUtil.persist(relPatrolPlanPointList);
        }
    }

    // 关系-巡防人员
    @Transactional
    public void saveUser(List<AppUser> appUserList, String orgId, List<String> patrolTaskItemIds) {
        List<AppRole> roleList = JpaUtil.linq(AppRole.class).list();
        Map<String, AppRole> roleMap = roleList.stream().collect(Collectors.toMap(AppRole::getId, Function.identity()));

        List<PatrolPlan> patrolPlanList = JpaUtil.linq(PatrolPlan.class).in("patrolTaskItemId", patrolTaskItemIds).equal("workStatus", BizConstants.PATROL_PLAN.WORK_STATUS.READY).equal("orgId", orgId).list();
        List<RelPatrolTaskItemUser> relPatrolTaskItemUserList = new ArrayList<>();
        List<RelPatrolPlanUser> relPatrolPlanUserList = new ArrayList<>();
        patrolTaskItemIds.forEach(s -> {
            appUserList.parallelStream().forEach(appUser -> {
                String roleId = appUser.getRoleId();
                AppRole appRole = roleMap.get(roleId);
                if (Objects.nonNull(appRole)) {
                    String userType = this.getRoleType(appRole.getName());
                    RelPatrolTaskItemUser relPatrolTaskItemUser = new RelPatrolTaskItemUser();
                    relPatrolTaskItemUser.setPatrolTaskItemId(s);
                    relPatrolTaskItemUser.setOrgId(orgId);
                    relPatrolTaskItemUser.setUserType(userType);
                    relPatrolTaskItemUser.setUserId(appUser.getUsername());
                    relPatrolTaskItemUserList.add(relPatrolTaskItemUser);
                }
            });

        });
        patrolPlanList.parallelStream().forEach(patrolPlan -> {
            appUserList.parallelStream().forEach(appUser -> {
                String roleId = appUser.getRoleId();
                AppRole appRole = roleMap.get(roleId);
                if (Objects.nonNull(appRole)) {
                    String userType = this.getRoleType(appRole.getName());
                    RelPatrolPlanUser relPatrolPlanUser = new RelPatrolPlanUser();
                    relPatrolPlanUser.setPatrolPlanId(patrolPlan.getId());
                    relPatrolPlanUser.setUserId(appUser.getUsername());
                    relPatrolPlanUser.setUserType(userType);
                    relPatrolPlanUserList.add(relPatrolPlanUser);
                    Object object = redisTemplate.hGet(BizConstants.GT_CID_USERID, appUser.getUsername());
                    //APP消息推送
                    if (Objects.nonNull(object)) {
                        geTuiUtils.pushToSingleByCid(object.toString(), "巡防任务通知", patrolPlan.getName());
                    }
                }
            });

        });
        JpaUtil.persist(relPatrolTaskItemUserList);
        JpaUtil.persist(relPatrolPlanUserList);
    }

    public String getRoleType(String roleName) {
        String userType = "";
        switch (roleName) {
            case "巡防队员":
                userType = BizConstants.USER_TYPE.MEMBER;
                break;
            case "巡防民警":
                userType = BizConstants.USER_TYPE.COP;
                break;
            case "巡防辅警":
                userType = BizConstants.USER_TYPE.AP;
                break;
            default:
                break;
        }
        return userType;
    }

    public void deleteUser(String orgId, List<String> patrolTaskItemIds) {
        // @formatter:off
        JpaUtil.lind(RelPatrolTaskItemUser.class).in("patrolTaskItemId", patrolTaskItemIds).equal("orgId", orgId).delete();
        // 更新巡防计划-删除用户关系
        // @formatter:off
        List<PatrolPlan> patrolPlanList = JpaUtil.linq(PatrolPlan.class).in("patrolTaskItemId", patrolTaskItemIds).equal("workStatus", BizConstants.PATROL_PLAN.WORK_STATUS.READY).equal("orgId", orgId).list();
        List<String> patrolPlanIds = patrolPlanList.stream().map(PatrolPlan::getId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(patrolPlanIds)) {
            JpaUtil.lind(RelPatrolPlanUser.class).in("patrolPlanId", patrolPlanIds).delete();
        }
        //清除关系-用户数量
        JpaUtil.lind(RelPatrolTaskItemUserAmount.class).in("patrolTaskItemId", patrolTaskItemIds).equal("orgId", orgId).delete();
    }

    // 关系-人员数量
    private void saveUserAmount(RelPatrolTaskItemUserAmount relPatrolTaskItemUserAmount, String orgId, String patrolTaskItemId) {

        // @formatter:on
        RelPatrolTaskItemUserAmount relPatrolTaskItemUserAmountNew = new RelPatrolTaskItemUserAmount();
        BeanUtils.copyProperties(relPatrolTaskItemUserAmount, relPatrolTaskItemUserAmountNew);
        relPatrolTaskItemUserAmountNew.setPatrolTaskItemId(patrolTaskItemId);
        relPatrolTaskItemUserAmountNew.setOrgId(orgId);
        JpaUtil.persist(relPatrolTaskItemUserAmountNew);

    }


    // 复制任务
    @Expose
    public void copy(Map<String, Object> parameter) {
        List<PatrolTaskItem> patrolTaskItemListOld = (List<PatrolTaskItem>) parameter.get("selection");
        Date patrolStartTime = (Date) parameter.get("patrolStartTime");
        Date patrolEndTime = (Date) parameter.get("patrolEndTime");
        for (PatrolTaskItem patrolTaskItemOld : patrolTaskItemListOld) {
            // Item-老
            String patrolTaskItemIdOld = patrolTaskItemOld.getId();
            // Task-老
            String patrolTaskIdOld = patrolTaskItemOld.getPatrolTaskId();
            PatrolTask patrolTask = JpaUtil.linq(PatrolTask.class).idEqual(patrolTaskIdOld).findOne();
            Date patrolStartDate = patrolTask.getPatrolStartDate();
            Date patrolEndDate = patrolTask.getPatrolEndDate();

            // Item-新
            PatrolTaskItem patrolTaskItem = new PatrolTaskItem();

            BeanUtils.copyProperties(patrolTaskItemOld, patrolTaskItem);
            patrolTaskItem.setPatrolStartTime(patrolStartTime);
            patrolTaskItem.setPatrolEndTime(patrolEndTime);
            patrolTaskItem.setId(null);
            JpaUtil.persist(patrolTaskItem);

            // 任务项/组织关系-老
            List<RelPatrolTaskItemOrg> relPatrolTaskItemOrgListOld = JpaUtil.linq(RelPatrolTaskItemOrg.class).equal("patrolTaskItemId", patrolTaskItemIdOld).list();
            for (RelPatrolTaskItemOrg relPatrolTaskItemOrgOld : relPatrolTaskItemOrgListOld) {
                // 任务项/组织关系-新
                RelPatrolTaskItemOrg relPatrolTaskItemOrg = new RelPatrolTaskItemOrg();

                BeanUtils.copyProperties(relPatrolTaskItemOrgOld, relPatrolTaskItemOrg);
                relPatrolTaskItemOrg.setId(null);
                relPatrolTaskItemOrg.setPatrolTaskItemId(patrolTaskItem.getId());
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
                    PatrolPlan patrolPlan = new PatrolPlan();
                    patrolPlan.setName(planName);
                    patrolPlan.setPatrolTaskItemId(patrolTaskItem.getId());
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
                    List<PatrolPlan> patrolPlanListOld = JpaUtil.linq(PatrolPlan.class).equal("patrolTaskItemId", patrolTaskItemIdOld).list();
                    // 巡防计划/巡防点关系都一样，取一个即可
                    PatrolPlan patrolPlanOld = patrolPlanListOld.get(0);
                    List<RelPatrolPlanPoint> relPatrolPlanPointListOld = JpaUtil.linq(RelPatrolPlanPoint.class).equal("patrolPlanId", patrolPlanOld.getId()).list();
                    for (RelPatrolPlanPoint relPatrolPlanPointOld : relPatrolPlanPointListOld) {
                        // 巡防计划/巡防点关系-新
                        RelPatrolPlanPoint relPatrolPlanPoint = new RelPatrolPlanPoint();
                        relPatrolPlanPoint.setPatrolPlanId(patrolPlan.getId());
                        relPatrolPlanPoint.setPatrolPointId(relPatrolPlanPointOld.getPatrolPointId());
                        relPatrolPlanPoint.setRequired(relPatrolPlanPointOld.isRequired());
                        JpaUtil.persist(relPatrolPlanPoint);
                    }
                    // 巡防计划/巡防人员关系
                    List<RelPatrolPlanUser> relPatrolPlanUserListOld = JpaUtil.linq(RelPatrolPlanUser.class).equal("patrolPlanId", patrolPlanOld.getId()).list();
                    for (RelPatrolPlanUser relPatrolPlanUserOld : relPatrolPlanUserListOld) {
                        // 巡防计划/巡防点关系-新
                        RelPatrolPlanUser relPatrolPlanUser = new RelPatrolPlanUser();
                        relPatrolPlanUser.setPatrolPlanId(patrolPlan.getId());
                        relPatrolPlanUser.setUserId(relPatrolPlanUserOld.getUserId());
                        JpaUtil.persist(relPatrolPlanUser);
                    }

                }
            }

            // 任务项/巡防点关系-老
            List<RelPatrolTaskItemPoint> relPatrolTaskItemPointListOld = JpaUtil.linq(RelPatrolTaskItemPoint.class).equal("patrolTaskItemId", patrolTaskItemIdOld).list();
            for (RelPatrolTaskItemPoint relPatrolTaskItemPointOld : relPatrolTaskItemPointListOld) {
                // 任务项/巡防点关系-新
                RelPatrolTaskItemPoint relPatrolTaskItemPoint = new RelPatrolTaskItemPoint();

                BeanUtils.copyProperties(relPatrolTaskItemPointOld, relPatrolTaskItemPoint);
                relPatrolTaskItemPoint.setId(null);
                relPatrolTaskItemPoint.setPatrolTaskItemId(patrolTaskItem.getId());
                JpaUtil.persist(relPatrolTaskItemPoint);
            }
            // 任务项/巡防人员关系-老
            List<RelPatrolTaskItemUser> relPatrolTaskItemUserListOld = JpaUtil.linq(RelPatrolTaskItemUser.class).equal("patrolTaskItemId", patrolTaskItemIdOld).list();
            for (RelPatrolTaskItemUser relPatrolTaskItemUserOld : relPatrolTaskItemUserListOld) {
                // 任务项/巡防点关系-新
                RelPatrolTaskItemUser relPatrolTaskItemUser = new RelPatrolTaskItemUser();

                BeanUtils.copyProperties(relPatrolTaskItemUserOld, relPatrolTaskItemUser);
                relPatrolTaskItemUser.setId(null);
                relPatrolTaskItemUser.setPatrolTaskItemId(patrolTaskItem.getId());
                JpaUtil.persist(relPatrolTaskItemUser);
            }
            // 任务项/用户数量关系-老
            List<RelPatrolTaskItemUserAmount> relPatrolTaskItemUserAmountListOld = JpaUtil.linq(RelPatrolTaskItemUserAmount.class).equal("patrolTaskItemId", patrolTaskItemIdOld).list();
            for (RelPatrolTaskItemUserAmount relPatrolTaskItemUserAmountOld : relPatrolTaskItemUserAmountListOld) {
                // 任务项/用户数量-新
                RelPatrolTaskItemUserAmount relPatrolTaskItemUserAmount = new RelPatrolTaskItemUserAmount();

                BeanUtils.copyProperties(relPatrolTaskItemUserAmountOld, relPatrolTaskItemUserAmount);
                relPatrolTaskItemUserAmount.setId(null);
                relPatrolTaskItemUserAmount.setPatrolTaskItemId(patrolTaskItem.getId());
                JpaUtil.persist(relPatrolTaskItemUserAmount);
            }

            // @formatter:on
        }

    }

    @Expose
    public void deleteOrgModel(Map<String, Object> parameter) {
        List<PatrolTaskItem> patrolTaskItemList = (List<PatrolTaskItem>) parameter.get("selectionPatrolTaskItem");
        String orgId = (String) parameter.get("orgId");
        List<String> patrolTaskItemIds = patrolTaskItemList.stream().map(PatrolTaskItem::getId).distinct().collect(Collectors.toList());

        // 批量删除
        if (CollectionUtils.isNotEmpty(patrolTaskItemIds)) {
            deleteOrg(patrolTaskItemIds, orgId);
        }
    }
}