package com.mr.sa.controller.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.PatrolPlan;
import com.mr.sa.entity.PatrolTaskItem;
import com.mr.sa.entity.rel.RelPatrolPlanPoint;
import com.mr.sa.entity.rel.RelPatrolPlanUser;
import com.mr.sa.entity.rel.RelPatrolTaskItemOrg;
import com.mr.sa.entity.rel.RelPatrolTaskItemPoint;
import com.mr.sa.entity.rel.RelPatrolTaskItemUser;
import com.mr.sa.entity.rel.RelPatrolTaskItemUserAmount;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.common.MyDateUtil;

@Controller
@Transactional(readOnly = true)
public class PatrolTaskItemController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "巡逻任务项", category = "CRUD")
	public void save(List<PatrolTaskItem> patrolTaskItems) {
		JpaUtil.save(patrolTaskItems, new SmartSavePolicyAdapter() {
			@Override
			public void afterUpdate(SaveContext context) {

				PatrolTaskItem patrolTaskItem = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(patrolTaskItem))) {
					// 只影响未开始的taskplan，正在进行和已经结束的不影响
					List<PatrolPlan> patrolPlanList = JpaUtil.linq(PatrolPlan.class)
							.equal("patrolTaskItemId", patrolTaskItem.getId())
							.equal("workStatus", BizConstants.PATROL_PLAN.WORK_STATUS.READY)
							.list();
					for (PatrolPlan patrolPlan : patrolPlanList) {
						// 时间
						Date today = patrolPlan.getPatrolStartTime();
						Date startDateTime = MyDateUtil.plusDayTime(today, patrolTaskItem.getPatrolStartTime());
						Date endDateTime = MyDateUtil.plusDayTime(today, patrolTaskItem.getPatrolEndTime());
						patrolPlan.setPatrolStartTime(startDateTime);
						patrolPlan.setPatrolEndTime(endDateTime);
						JpaUtil.merge(patrolPlan);
					}
				}
			}
			
			@Override
			public boolean beforeDelete(SaveContext context) {
				//PatrolTask
				PatrolTaskItem patrolTaskItem = context.getEntity(); 
				String patrolTaskItemId=patrolTaskItem.getId(); 
				//PatrolTask-PatrolTaskItem-PatrolPlan
				List<PatrolPlan> patrolPlanList=JpaUtil.linq(PatrolPlan.class)
						.equal("patrolTaskItemId", patrolTaskItemId)
						.list();
				for (PatrolPlan patrolPlan : patrolPlanList) {
					String patrolPlanId=patrolPlan.getId(); 
					//PatrolTask-PatrolTaskItem-PatrolPlan-RelPatrolPlanPoint
					JpaUtil.lind(RelPatrolPlanPoint.class)
	                    .equal("patrolPlanId", patrolPlanId)
	                    .delete();
					//PatrolTask-PatrolTaskItem-PatrolPlan-RelPatrolPlanUser
					JpaUtil.lind(RelPatrolPlanUser.class)
	                    .equal("patrolPlanId", patrolPlanId)
	                    .delete();
				}
				//PatrolTask-PatrolTaskItem-PatrolPlan-delete
				JpaUtil.lind(PatrolPlan.class)
	                .equal("patrolTaskItemId", patrolTaskItemId)
	                .delete();
				
				//PatrolTask-PatrolTaskItem-RelPatrolTaskItemOrg
				JpaUtil.lind(RelPatrolTaskItemOrg.class)
                    .equal("patrolTaskItemId", patrolTaskItemId)
                    .delete();
				//PatrolTask-PatrolTaskItem-RelPatrolTaskItemPoint
				JpaUtil.lind(RelPatrolTaskItemPoint.class)
                    .equal("patrolTaskItemId", patrolTaskItemId)
                    .delete();
				//PatrolTask-PatrolTaskItem-RelPatrolTaskItemUser
				JpaUtil.lind(RelPatrolTaskItemUser.class)
                    .equal("patrolTaskItemId", patrolTaskItemId)
                    .delete();
				//PatrolTask-PatrolTaskItem-RelPatrolTaskItemUserAmount 
				JpaUtil.lind(RelPatrolTaskItemUserAmount.class)
                    .equal("patrolTaskItemId", patrolTaskItemId)
                    .delete();
			return true;
			}
		});
	}

	@DataProvider
	public void query(Page<PatrolTaskItem> page,
			Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(PatrolTaskItem.class).where(criteria).desc("updateDate")
				.paging(page);
	}
}