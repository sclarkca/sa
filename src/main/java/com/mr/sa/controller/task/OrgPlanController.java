package com.mr.sa.controller.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.OrgPlan;
import com.mr.sa.entity.PatrolTaskItem;
import com.mr.sa.entity.rel.RelPatrolTaskItemOrg;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class OrgPlanController extends QueryFilter {

	@DataProvider
	public void query(Page<OrgPlan> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(OrgPlan.class)
			.collect("code", Org.class,"orgId")
			.where(criteria)
			.desc("updateDate")
			.paging(page);
		
	}

	// 关系-单位时间
	@DataResolver
	public List<OrgPlan> queryOrgPlan(Map<String, Object> parameter) {
		String patrolTaskItemId = (String) parameter.get("patrolTaskItemId");
		List<OrgPlan> orgPlanList = new ArrayList();
		// @formatter:off
		List<PatrolTaskItem> list = JpaUtil.
				linq(PatrolTaskItem.class)
				.desc("updateDate")
				.list();
		// @formatter:on
		for (PatrolTaskItem patrolTaskItem : list) {
			// @formatter:off
			List<Org> orgList = JpaUtil.linq(Org.class)
					.exists(RelPatrolTaskItemOrg.class)
					.equalProperty("orgId", "code")
					.equal("patrolTaskItemId", patrolTaskItem.getId()).end()
					.list();
			// @formatter:on
			for (Org org : orgList) {
				OrgPlan orgPlan = new OrgPlan();
				//orgPlan.setOrgName(org.getName());
				orgPlan.setPatrolStartTime(patrolTaskItem.getPatrolStartTime());
				orgPlan.setPatrolEndTime(patrolTaskItem.getPatrolEndTime());
				orgPlanList.add(orgPlan);
			}
		}

		return orgPlanList;
	}
}