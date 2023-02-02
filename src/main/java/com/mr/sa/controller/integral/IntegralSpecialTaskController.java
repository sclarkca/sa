package com.mr.sa.controller.integral;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

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
import com.mr.sa.entity.IntegralSpecialTask;
import com.mr.sa.entity.NoticeInfo;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.PatrolPoint;
import com.mr.sa.entity.rel.RelIntegralSpecialTaskPoint;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.validator.IntegralSpecialTaskValidator;

@Controller
@Transactional(readOnly = true)
public class IntegralSpecialTaskController extends QueryFilter {

	@Autowired
	IntegralSpecialTaskValidator integralSpecialTaskValidator;

	@DataResolver
	@Transactional
	@Log(module = "专项任务", category = "CRUD")
	public void save(List<IntegralSpecialTask> integralIntegralSpecialTasks) {
		JpaUtil.save(integralIntegralSpecialTasks,
				new SmartSavePolicyAdapter() {

					@Override
					public boolean beforeInsert(SaveContext context) {
						IntegralSpecialTask integralSpecialTask = context
								.getEntity();
						if (EntityState.NEW.equals(
								EntityUtils.getState(integralSpecialTask))) {
							integralSpecialTaskValidator
									.checkDate(integralSpecialTask);
							integralSpecialTaskValidator
									.checkTime(integralSpecialTask);
						}
						return true;
					}

					@Override
					public boolean beforeUpdate(SaveContext context) {
						IntegralSpecialTask integralSpecialTask = context
								.getEntity();
						if (EntityState.MODIFIED.equals(
								EntityUtils.getState(integralSpecialTask))) {
							integralSpecialTaskValidator
									.checkDate(integralSpecialTask);
							integralSpecialTaskValidator
									.checkTime(integralSpecialTask);
						}
						return true;
					}

				});
	}

	@DataProvider
	public void query(Page<IntegralSpecialTask> page,
			Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		// @formatter:off
		criteria = condition(criteria, parameter);
		JpaUtil.linq(IntegralSpecialTask.class)
			.where(criteria)
			.desc("updateDate")
			.paging(page);
		// @formatter:on
	}

	// 关系-组织
	@DataProvider
	public List<Org> loadOrg(Map<String, Object> parameter) {
		// @formatter:off
			List<Org> orgList = JpaUtil.linq(Org.class)
					.isNull("parentId")
					.list();
			// @formatter:on
		return orgList;
	}

	@DataProvider
	public List<Org> loadChildrenOrg(Map<String, Object> parameter) {
		String parentId = (String) parameter.get("parentId");
		// @formatter:off
			List<Org> orgList = JpaUtil.linq(Org.class)
					.equal("parentId", parentId)
					.asc("code")
					.list();
			// @formatter:on
		return orgList;
	}

	@DataProvider
	public List<PatrolPoint> queryIntegralSpecialTaskPoint(
			Map<String, Object> parameter) {
		String integralIntegralSpecialTaskId = (String) parameter
				.get("integralIntegralSpecialTaskId");
		String orgId = (String) parameter.get("orgId");
		// @formatter:off
		List<PatrolPoint> patrolPointList=JpaUtil.linq(PatrolPoint.class)
				.equal("orgId", orgId)
				.desc("updateDate")
				.list();
			//加checked
			for (PatrolPoint patrolPoint : patrolPointList) {
				boolean isExist=JpaUtil.linq(RelIntegralSpecialTaskPoint.class)
						.equal("integralIntegralSpecialTaskId", integralIntegralSpecialTaskId)
						.equal("patrolPointId", patrolPoint.getId())
						.exists();
				patrolPoint.setChecked(isExist);
				
				boolean isRequired=JpaUtil.linq(RelIntegralSpecialTaskPoint.class)
						.equal("integralIntegralSpecialTaskId", integralIntegralSpecialTaskId)
						.equal("patrolPointId", patrolPoint.getId())
						.isTrue("required")
						.exists();
				// @formatter:on
			patrolPoint.setRequired(isRequired);
		}
		return patrolPointList;
	}

}