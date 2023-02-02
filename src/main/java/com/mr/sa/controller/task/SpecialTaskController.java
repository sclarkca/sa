package com.mr.sa.controller.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.PatrolPoint;
import com.mr.sa.entity.SpecialTask;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.entity.rel.RelSpecialTaskPoint;
import com.mr.sa.entity.rel.RelSpecialTaskUser;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class SpecialTaskController extends QueryFilter {

	@DataResolver
	@Transactional
	@Log(module = "专项任务", category = "CRUD")
	public void save(List<SpecialTask> specialTasks) {
		JpaUtil.save(specialTasks);
	}

	@DataProvider
	public void query(Page<SpecialTask> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		// @formatter:off
		criteria = condition(criteria, parameter);
		JpaUtil.linq(SpecialTask.class)
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

	// 专项任务-执行者-当前组织
	@DataProvider
	public List<AppUser> querySpecialTaskUser(Map<String, Object> parameter) {
		String specialTaskId = (String) parameter.get("specialTaskId");
		String orgId = (String) parameter.get("orgId");
		List<AppUser> userList = JpaUtil.linq(AppUser.class)
				.equal("orgId", orgId).list();
		if (CollectionUtils.isNotEmpty(userList)) {
			List<String> userIds = new ArrayList();
			for (AppUser appUser : userList) {
				userIds.add(appUser.getUsername());
			}
			List<RelSpecialTaskUser> relSpecialTaskUserList = JpaUtil
					.linq(RelSpecialTaskUser.class)
					.equal("specialTaskId", specialTaskId)
					.addIf(CollectionUtils.isNotEmpty(userIds))
						.in("userId",userIds)
					.endIf()
					.addIf(CollectionUtils.isEmpty(userIds))
						.equal("userId","xxxxxx")
					.endIf()
					.list();
			// 加checked
			for (AppUser appUser : userList) {
				boolean isExist = relSpecialTaskUserList.stream().anyMatch(
						m -> m.getUserId().equals(appUser.getUsername()));
				appUser.setChecked(isExist);
			}
		}
		return userList;
	}

	// 专项任务-执行者-当前任务所有
	@DataProvider
	public List<AppUser> querySpecialTaskUserAll(Map<String, Object> parameter) {
		String specialTaskId = (String) parameter.get("specialTaskId");
		// @formatter:off
			List<AppUser> userList=JpaUtil.linq(AppUser.class)
					.exists(RelSpecialTaskUser.class)
						.equalProperty("userId", "username")
						.equal("specialTaskId", specialTaskId)
					.end()
					.desc("updateDate")
					.list();
			// @formatter:on
		return userList;
	}

	@DataProvider
	public List<PatrolPoint> querySpecialTaskPoint(Page<PatrolPoint> page,
			Map<String, Object> parameter) {
		String specialTaskId = (String) parameter.get("specialTaskId");
		parameter.remove("specialTaskId");
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(PatrolPoint.class)
			.where(criteria)
			.desc("updateDate")
			.paging(page);
		List<PatrolPoint> patrolPointList = (List<PatrolPoint>) page
				.getEntities();
		
		// String orgId = (String) parameter.get("orgId");
		// @formatter:off 
		List<String> patrolPointIds = new ArrayList();
		for (PatrolPoint patrolPoint : patrolPointList) {
			patrolPointIds.add(patrolPoint.getId());
		}
		// 加checked 
		List<RelSpecialTaskPoint> relPatrolTaskItemPointList = JpaUtil.linq(RelSpecialTaskPoint.class)
					.equal("specialTaskId", specialTaskId)
					.addIf(CollectionUtils.isNotEmpty(patrolPointIds))
						.in("patrolPointId",patrolPointIds)
					.endIf()
					.addIf(CollectionUtils.isEmpty(patrolPointIds))
						.equal("patrolPointId","xxxxxx")
					.endIf()
					.list(); 
		List<RelSpecialTaskPoint> relPatrolTaskItemPointList2 = JpaUtil.linq(RelSpecialTaskPoint.class)
				.equal("specialTaskId", specialTaskId)
				.addIf(CollectionUtils.isNotEmpty(patrolPointIds))
					.in("patrolPointId",patrolPointIds)
				.endIf()
				.addIf(CollectionUtils.isEmpty(patrolPointIds))
					.equal("patrolPointId","xxxxxx")
				.endIf()
				.isTrue("required")
				.list();
		// @formatter:on
		for (PatrolPoint patrolPoint : patrolPointList) {
			boolean isExist = relPatrolTaskItemPointList.stream()
					.anyMatch(m -> m.getPatrolPointId().equals(patrolPoint.getId()));
			patrolPoint.setChecked(isExist);
			boolean isRequired = relPatrolTaskItemPointList2.stream()
					.anyMatch(m -> m.getPatrolPointId().equals(patrolPoint.getId()));
			patrolPoint.setRequired(isRequired);
		}
		return patrolPointList;
	}
	 
}