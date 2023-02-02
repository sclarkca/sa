package com.mr.sa.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.Expose;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.PatrolPlan;
import com.mr.sa.entity.PatrolTask;
import com.mr.sa.entity.PatrolTaskItem;
import com.mr.sa.entity.rel.RelPatrolPlanPoint;
import com.mr.sa.entity.rel.RelPatrolPlanUser;
import com.mr.sa.entity.rel.RelPatrolTaskItemOrg;
import com.mr.sa.entity.rel.RelPatrolTaskItemPoint;
import com.mr.sa.entity.rel.RelPatrolTaskItemUser;
import com.mr.sa.entity.rel.RelPatrolTaskItemUserAmount;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.utils.common.MyDateUtil;

@Service
@Transactional
public class PatrolTaskService {

	// 复制任务
	@Expose
	public void copy(Map<String, Object> parameter) {
		List<PatrolTask> patrolTaskList=(List<PatrolTask>)parameter.get("selection");
		Date patrolStartDate = (Date) parameter.get("patrolStartDate");
		Date patrolEndDate = (Date) parameter.get("patrolEndDate");
		for (PatrolTask patrolTaskOld : patrolTaskList) {
			// Task-老
			String patrolTaskId=patrolTaskOld.getId();
			// @formatter:off
			// Task-新
			PatrolTask patrolTask=new PatrolTask();
			
			BeanUtils.copyProperties(patrolTaskOld, patrolTask);
			patrolTask.setPatrolStartDate(patrolStartDate);
			patrolTask.setPatrolEndDate(patrolEndDate);
			patrolTask.setId(null);
			patrolTask.setActiveStatus(null);
			JpaUtil.persist(patrolTask);
			
			// Item-老
			List<PatrolTaskItem> patrolTaskItemListOld = 
					JpaUtil.linq(PatrolTaskItem.class)
					.equal("patrolTaskId", patrolTaskId)
					.list();
			for (PatrolTaskItem patrolTaskItemOld : patrolTaskItemListOld) {
				String patrolTaskItemIdOld = patrolTaskItemOld.getId();
				// Item-新
				PatrolTaskItem patrolTaskItem=new PatrolTaskItem();
				
				BeanUtils.copyProperties(patrolTaskItemOld, patrolTaskItem);
				patrolTaskItem.setId(null);
				patrolTaskItem.setPatrolTaskId(patrolTask.getId());
				JpaUtil.persist(patrolTaskItem);
				
				
		        //任务项/组织关系-老
		        List<RelPatrolTaskItemOrg> relPatrolTaskItemOrgListOld=
		        		JpaUtil.linq(RelPatrolTaskItemOrg.class)
		                .equal("patrolTaskItemId", patrolTaskItemIdOld)
		                .list();
		        for (RelPatrolTaskItemOrg relPatrolTaskItemOrgOld : relPatrolTaskItemOrgListOld) {
		        	// 任务项/组织关系-新
		        	RelPatrolTaskItemOrg relPatrolTaskItemOrg=new RelPatrolTaskItemOrg();
		        	
					BeanUtils.copyProperties(relPatrolTaskItemOrgOld, relPatrolTaskItemOrg);
					relPatrolTaskItemOrg.setId(null);
					relPatrolTaskItemOrg.setPatrolTaskItemId(patrolTaskItem.getId());
					JpaUtil.persist(relPatrolTaskItemOrg);
					
					String orgId=relPatrolTaskItemOrgOld.getOrgId();
					Org org=JpaUtil.linq(Org.class)
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
						PatrolPlan patrolPlan = new PatrolPlan();
						patrolPlan.setName(planName);
						patrolPlan.setPatrolTaskItemId(patrolTaskItem.getId());
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
						List<PatrolPlan> patrolPlanListOld = JpaUtil
								.linq(PatrolPlan.class)
								.equal("patrolTaskItemId", patrolTaskItemIdOld).list();
						// 巡防计划/巡防点关系都一样，取一个即可
						PatrolPlan patrolPlanOld = patrolPlanListOld.get(0);
						List<RelPatrolPlanPoint> relPatrolPlanPointListOld = JpaUtil
								.linq(RelPatrolPlanPoint.class)
								.equal("patrolPlanId", patrolPlanOld.getId())
								.list();
						for (RelPatrolPlanPoint relPatrolPlanPointOld : relPatrolPlanPointListOld) {
							// 巡防计划/巡防点关系-新
							RelPatrolPlanPoint relPatrolPlanPoint = new RelPatrolPlanPoint();
							relPatrolPlanPoint.setPatrolPlanId(patrolPlan.getId());
							relPatrolPlanPoint.setPatrolPointId(
									relPatrolPlanPointOld.getPatrolPointId());
							relPatrolPlanPoint.setRequired(
									relPatrolPlanPointOld.isRequired());
							JpaUtil.persist(relPatrolPlanPoint);
						}
						// 巡防计划/巡防人员关系
						List<RelPatrolPlanUser> relPatrolPlanUserListOld = JpaUtil
								.linq(RelPatrolPlanUser.class)
								.equal("patrolPlanId", patrolPlanOld.getId())
								.list();
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
				List<RelPatrolTaskItemPoint> relPatrolTaskItemPointListOld = JpaUtil
						.linq(RelPatrolTaskItemPoint.class)
						.equal("patrolTaskItemId", patrolTaskItemIdOld).list();
				for (RelPatrolTaskItemPoint relPatrolTaskItemPointOld : relPatrolTaskItemPointListOld) {
					// 任务项/巡防点关系-新
					RelPatrolTaskItemPoint relPatrolTaskItemPoint = new RelPatrolTaskItemPoint();

					BeanUtils.copyProperties(relPatrolTaskItemPointOld,
							relPatrolTaskItemPoint);
					relPatrolTaskItemPoint.setId(null);
					relPatrolTaskItemPoint
							.setPatrolTaskItemId(patrolTaskItem.getId());
					JpaUtil.persist(relPatrolTaskItemPoint);
				}
				// 任务项/巡防人员关系-老
				List<RelPatrolTaskItemUser> relPatrolTaskItemUserListOld = JpaUtil
						.linq(RelPatrolTaskItemUser.class)
						.equal("patrolTaskItemId", patrolTaskItemIdOld).list();
				for (RelPatrolTaskItemUser relPatrolTaskItemUserOld : relPatrolTaskItemUserListOld) {
					// 任务项/巡防点关系-新
					RelPatrolTaskItemUser relPatrolTaskItemUser = new RelPatrolTaskItemUser();

					BeanUtils.copyProperties(relPatrolTaskItemUserOld,
							relPatrolTaskItemUser);
					relPatrolTaskItemUser.setId(null);
					relPatrolTaskItemUser
							.setPatrolTaskItemId(patrolTaskItem.getId());
					JpaUtil.persist(relPatrolTaskItemUser);
				}
				// 任务项/用户数量关系-老
				List<RelPatrolTaskItemUserAmount> relPatrolTaskItemUserAmountListOld = JpaUtil
						.linq(RelPatrolTaskItemUserAmount.class)
						.equal("patrolTaskItemId", patrolTaskItemIdOld).list();
				for (RelPatrolTaskItemUserAmount relPatrolTaskItemUserAmountOld : relPatrolTaskItemUserAmountListOld) {
					// 任务项/用户数量-新
					RelPatrolTaskItemUserAmount relPatrolTaskItemUserAmount = new RelPatrolTaskItemUserAmount();

					BeanUtils.copyProperties(relPatrolTaskItemUserAmountOld,
							relPatrolTaskItemUserAmount);
					relPatrolTaskItemUserAmount.setId(null);
					relPatrolTaskItemUserAmount
							.setPatrolTaskItemId(patrolTaskItem.getId());
					JpaUtil.persist(relPatrolTaskItemUserAmount);
				}

				// @formatter:on
			}
		}
		

	}
}