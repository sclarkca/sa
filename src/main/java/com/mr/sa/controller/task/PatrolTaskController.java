package com.mr.sa.controller.task;

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
import com.mr.sa.entity.PatrolTask;
import com.mr.sa.entity.PatrolTaskItem;
import com.mr.sa.entity.rel.RelPatrolPlanPoint;
import com.mr.sa.entity.rel.RelPatrolPlanUser;
import com.mr.sa.entity.rel.RelPatrolTaskItemOrg;
import com.mr.sa.entity.rel.RelPatrolTaskItemPoint;
import com.mr.sa.entity.rel.RelPatrolTaskItemUser;
import com.mr.sa.entity.rel.RelPatrolTaskItemUserAmount;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class PatrolTaskController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "巡逻任务", category = "CRUD")
	public void save(List<PatrolTask> patrolTasks) {
		JpaUtil.save(patrolTasks, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeDelete(SaveContext context) {
				// PatrolTask
				PatrolTask patrolTask = context.getEntity();
				if (EntityState.DELETED
						.equals(EntityUtils.getState(patrolTask))) {
					String patrolTaskId = patrolTask.getId();
					// PatrolTask-PatrolTaskItem-list
					List<PatrolTaskItem> patrolTaskItemList = JpaUtil
							.linq(PatrolTaskItem.class)
							.equal("patrolTaskId", patrolTaskId).list();
					for (PatrolTaskItem patrolTaskItem : patrolTaskItemList) {
						String patrolTaskItemId = patrolTaskItem.getId();
						// PatrolTask-PatrolTaskItem-PatrolPlan-list
						List<PatrolPlan> patrolPlanList = JpaUtil
								.linq(PatrolPlan.class)
								.equal("patrolTaskItemId", patrolTaskItemId)
								.list();
						for (PatrolPlan patrolPlan : patrolPlanList) {
							String patrolPlanId = patrolPlan.getId();
							// PatrolTask-PatrolTaskItem-PatrolPlan-RelPatrolPlanPoint
							JpaUtil.lind(RelPatrolPlanPoint.class)
									.equal("patrolPlanId", patrolPlanId)
									.delete();
							// PatrolTask-PatrolTaskItem-PatrolPlan-RelPatrolPlanUser
							JpaUtil.lind(RelPatrolPlanUser.class)
									.equal("patrolPlanId", patrolPlanId)
									.delete();
						}
						// PatrolTask-PatrolTaskItem-PatrolPlan-delete
						JpaUtil.lind(PatrolPlan.class)
								.equal("patrolTaskItemId", patrolTaskItemId)
								.delete();

						// PatrolTask-PatrolTaskItem-RelPatrolTaskItemOrg
						JpaUtil.lind(RelPatrolTaskItemOrg.class)
								.equal("patrolTaskItemId", patrolTaskItemId)
								.delete();
						// PatrolTask-PatrolTaskItem-RelPatrolTaskItemPoint
						JpaUtil.lind(RelPatrolTaskItemPoint.class)
								.equal("patrolTaskItemId", patrolTaskItemId)
								.delete();
						// PatrolTask-PatrolTaskItem-RelPatrolTaskItemUser
						JpaUtil.lind(RelPatrolTaskItemUser.class)
								.equal("patrolTaskItemId", patrolTaskItemId)
								.delete();
						// PatrolTask-PatrolTaskItem-RelPatrolTaskItemUserAmount
						JpaUtil.lind(RelPatrolTaskItemUserAmount.class)
								.equal("patrolTaskItemId", patrolTaskItemId)
								.delete();
					}
					// PatrolTask-PatrolTaskItem-delete
					JpaUtil.lind(PatrolTaskItem.class)
							.equal("patrolTaskId", patrolTaskId).delete();
				}
				return true;
			}

		});
	}

	@DataProvider
	public void query(Page<PatrolTask> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(PatrolTask.class).where(criteria).desc("updateDate")
				.paging(page);
	}
}