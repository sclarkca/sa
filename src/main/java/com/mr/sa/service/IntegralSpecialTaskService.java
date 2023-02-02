package com.mr.sa.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.Expose;
import com.mr.sa.entity.PatrolPoint;
import com.mr.sa.entity.rel.RelIntegralSpecialTaskPoint;

@Service
@Transactional
public class IntegralSpecialTaskService {

	// 关系-巡防点
	@Expose
	public void savePoint(Map<String, Object> parameter) {
		String integralSpecialTaskId = (String) parameter.get("integralSpecialTaskId");
		PatrolPoint patrolPoint = (PatrolPoint) parameter.get("patrolPoint");
		String orgId = (String) parameter.get("orgId");
		// @formatter:off
			JpaUtil.lind(RelIntegralSpecialTaskPoint.class)
				.equal("integralSpecialTaskId", integralSpecialTaskId)
				.equal("patrolPointId", patrolPoint.getId())
				.delete();
			// @formatter:on
		if (patrolPoint.isChecked()) {
			RelIntegralSpecialTaskPoint relIntegralSpecialTaskPoint = new RelIntegralSpecialTaskPoint();
			relIntegralSpecialTaskPoint.setIntegralSpecialTaskId(integralSpecialTaskId);
			relIntegralSpecialTaskPoint.setPatrolPointId(patrolPoint.getId());
			relIntegralSpecialTaskPoint.setOrgId(orgId);
			relIntegralSpecialTaskPoint.setRequired(patrolPoint.isRequired());
			JpaUtil.persist(relIntegralSpecialTaskPoint);
		}
		// @formatter:on

	}
 

}