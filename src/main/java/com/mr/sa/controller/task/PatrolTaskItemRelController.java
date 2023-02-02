package com.mr.sa.controller.task;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.PatrolTaskItem;
import com.mr.sa.entity.rel.RelPatrolTaskItemOrg;
import com.mr.sa.entity.rel.RelPatrolTaskItemPoint;
import com.mr.sa.entity.rel.RelPatrolTaskItemUser;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class PatrolTaskItemRelController extends QueryFilter {

	// 关系-组织
	@DataProvider
	public List<Org> loadOrg(Map<String, Object> parameter) {
		List<PatrolTaskItem> patrolTaskItemList = (List<PatrolTaskItem>) parameter.get("selectionPatrolTaskItem");
		List<String> patrolTaskItemIds = patrolTaskItemList.stream().distinct().map(PatrolTaskItem::getId)
				.collect(Collectors.toList());
		List<Org> orgList = JpaUtil.linq(Org.class).isNull("parentId").list();
		for (Org org : orgList) {
			// @formatter:off
			boolean isExist = JpaUtil.linq(RelPatrolTaskItemOrg.class)
					.in("patrolTaskItemId", patrolTaskItemIds)
					.equal("orgId", org.getCode())
					.exists();
			// @formatter:on
			org.setChecked(isExist);
		}
		return orgList;
	}

	@DataProvider
	public List<Org> loadChildrenOrg(Map<String, Object> parameter) {
		List<PatrolTaskItem> patrolTaskItemList = (List<PatrolTaskItem>) parameter.get("selectionPatrolTaskItem");
		List<String> patrolTaskItemIds = patrolTaskItemList.stream().distinct().map(PatrolTaskItem::getId)
				.collect(Collectors.toList());
		String parentId = (String) parameter.get("parentId");
		List<Org> orgList = JpaUtil.linq(Org.class).equal("parentId", parentId)
				.asc("code").list();
		for (Org org : orgList) {
			// @formatter:off
			boolean isExist = JpaUtil.linq(RelPatrolTaskItemOrg.class)
					.in("patrolTaskItemId", patrolTaskItemIds)
					.equal("orgId", org.getCode())
					.exists();
			// @formatter:on
			org.setChecked(isExist);
		}
		return orgList;
	}

	@DataProvider
	public List<RelPatrolTaskItemOrg> queryOrg(Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		// @formatter:off
		List<RelPatrolTaskItemOrg> list = JpaUtil
				.linq(RelPatrolTaskItemOrg.class)
				.where(criteria)
				.desc("updateDate")
				.list();
		// @formatter:on
		return list;
	}

	// 关系-巡防点
	@DataResolver
	public List<RelPatrolTaskItemPoint> queryPoint(
			Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		// @formatter:off
		List<RelPatrolTaskItemPoint> list = JpaUtil
				.linq(RelPatrolTaskItemPoint.class)
				.where(criteria)
				.desc("updateDate")
				.list();
		// @formatter:on
		return list;
	}

	// 关系-巡防人员
	@DataResolver
	public List<RelPatrolTaskItemUser> queryUser(
			Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		// @formatter:off
		List<RelPatrolTaskItemUser> list = JpaUtil
				.linq(RelPatrolTaskItemUser.class)
				.where(criteria)
				.desc("updateDate").list();
		// @formatter:on
		return list;
	}
}