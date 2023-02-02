package com.mr.sa.controller.task;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mr.sa.entity.Org;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.PatrolPlan;
import com.mr.sa.entity.PatrolRecord;
import com.mr.sa.entity.rel.RelPatrolPlanPoint;
import com.mr.sa.entity.rel.RelPatrolTaskItemPoint;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class PatrolPlanController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "巡防活动", category = "CRUD")
	public void save(List<PatrolPlan> patrolPlans) {
		JpaUtil.save(patrolPlans);
	}

	@DataProvider
	public void query(Page<PatrolPlan> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(PatrolPlan.class)
			.where(criteria)
			.desc("patrolEndTime")
			.paging(page);
		Collection<PatrolPlan> entities = page.getEntities();
		List<String> orgIds = entities.stream().distinct().map(PatrolPlan::getOrgId).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(orgIds)) {
			List<Org> orgList = JpaUtil.linq(Org.class).in("code", orgIds).list();
			List<String> orgParentIds = orgList.stream().map(Org::getParentId).collect(Collectors.toList());
			List<Org> parentOrgList = JpaUtil.linq(Org.class).in("id", orgParentIds).list();

			Map<String, Org> orgCodeMap = orgList.stream().collect(Collectors.toMap(Org::getCode, Function.identity()));
			Map<String, Org> orgParentIdMap = parentOrgList.stream().collect(Collectors.toMap(Org::getId, Function.identity()));

			entities.forEach(patrolPlan -> {
				String orgId = patrolPlan.getOrgId();
				String orgName = "";
				Org org = orgCodeMap.get(orgId);
				if (Objects.nonNull(org)) {
					String parentId = org.getParentId();
					Org parentOrg = orgParentIdMap.get(parentId);
					if (Objects.nonNull(parentOrg)) {
						orgName = String.format("%s - %s", parentOrg.getName(), org.getName());
					} else {
						orgName = org.getName();
					}
				}
				patrolPlan.setOrgName(orgName);
			});
		}
	}
	
	//获取详情-统计巡防点
	@Expose
	public Map<String,Long> getPatrolPointCount(String patrolPlanId) {
		Map<String,Long> map=new HashMap();
		//一共有多少个点
		PatrolPlan patrolPlan=JpaUtil.getOne(PatrolPlan.class, patrolPlanId);
		String patrolTaskItemId=patrolPlan.getPatrolTaskItemId();
		Long countTotal=JpaUtil.linq(RelPatrolTaskItemPoint.class)
	        .equal("patrolTaskItemId", patrolTaskItemId)
	        .count();
		//一共有多少必扫点
		Long countRequired=JpaUtil.linq(RelPatrolTaskItemPoint.class)
		        .equal("patrolTaskItemId", patrolTaskItemId)
		        .isTrue("required")
		        .count();
		//扫了多少个点
		Long countDone=JpaUtil.linq(PatrolRecord.class)
		        .equal("patrolPlanId", patrolPlanId)
		        .count();
		map.put("countTotal", countTotal);
		map.put("countRequired", countRequired);
		map.put("countDone", countDone);
		return map;
	}
}