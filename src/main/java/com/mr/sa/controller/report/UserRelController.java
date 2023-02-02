package com.mr.sa.controller.report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.mr.sa.entity.PatrolTaskItem;
import com.mr.sa.entity.app.AppRole;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.entity.rel.RelPatrolTaskItemUser;
import com.mr.sa.entity.rel.RelPatrolTaskItemUserAmount;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class UserRelController extends QueryFilter {

	//所有
	@DataProvider
	public List<AppUser> queryAll(Map<String, Object> parameter) {
		List<PatrolTaskItem> patrolTaskItemList = (List<PatrolTaskItem>) parameter.get("selectionPatrolTaskItem");
		String orgId = (String) parameter.get("orgId");
		// 全部
		AppRole role1 = JpaUtil.linq(AppRole.class)
				.equal("name", "巡防民警")
				.findOne();
		AppRole role2 = JpaUtil.linq(AppRole.class)
				.equal("name", "巡防辅警")
				.findOne();
		AppRole role3 = JpaUtil.linq(AppRole.class)
				.equal("name", "巡防队员")
				.findOne();
		// @formatter:off
		List<AppUser> userList=JpaUtil.linq(AppUser.class)
			.equal("orgId", orgId)
			.or()
				.equal("roleId", role1.getId())
				.equal("roleId", role2.getId())
				.equal("roleId", role3.getId())
			.end()
			.desc("updateDate")
			.list();
		//加checked
//		for (PatrolTaskItem patrolTaskItem : patrolTaskItemList) {
//			for (AppUser user : userList) {
//				boolean isExist=JpaUtil.linq(RelPatrolTaskItemUser.class)
//						.equal("patrolTaskItemId", patrolTaskItem.getId())
//						.equal("userId", user.getUsername())
//						.exists();
//				user.setChecked(isExist);
//			}
//		}
		List<String> itemIds = patrolTaskItemList.stream().map(PatrolTaskItem::getId).collect(Collectors.toList());
		List<RelPatrolTaskItemUser> relPatrolTaskItemUsers = JpaUtil.linq(RelPatrolTaskItemUser.class).distinct().in("patrolTaskItemId", itemIds).list();
		List<String> checkedUserIds = relPatrolTaskItemUsers.stream().map(RelPatrolTaskItemUser::getUserId).collect(Collectors.toList());
		userList.parallelStream().forEach(appUser -> {
			String username = appUser.getUsername();
			if (checkedUserIds.contains(username)) {
				appUser.setChecked(true);
			}
		});

		// @formatter:on
		return userList;
	}
	
	// 巡防队员
	@DataProvider
	public List<AppUser> queryMember(Map<String, Object> parameter) {
		return query(parameter);
	}

	// 巡防民警
	@DataProvider
	public List<AppUser> queryCop(Map<String, Object> parameter) {
		return query(parameter);
	}

	// 巡防辅警
	@DataProvider
	public List<AppUser> queryAp(Map<String, Object> parameter) {
		return query(parameter);
	}

	private List<AppUser> query(Map<String, Object> parameter) {
		String patrolTaskItemId = (String) parameter.get("patrolTaskItemId");
		String orgId = (String) parameter.get("orgId");
		String userType = (String) parameter.get("userType");
		String roleName = "";
		switch (userType) {
		case BizConstants.USER_TYPE.MEMBER:
			roleName = "巡防队员";
			break;
		case BizConstants.USER_TYPE.COP:
			roleName = "巡防民警";
			break;
		case BizConstants.USER_TYPE.AP:
			roleName = "巡防辅警";
			break;
		default:
			break;
		}
		// 全部
		AppRole role = JpaUtil.linq(AppRole.class).equal("name", roleName).findOne();
		// @formatter:off
		List<AppUser> userList=JpaUtil.linq(AppUser.class)
			.equal("orgId", orgId)
			.equal("roleId", role.getId())
			.desc("updateDate")
			.list();
		//加checked
		for (AppUser user : userList) {
			boolean isExist=JpaUtil.linq(RelPatrolTaskItemUser.class)
					.equal("patrolTaskItemId", patrolTaskItemId)
					.equal("userType", userType)
					.equal("userId", user.getUsername())
					.exists();
			user.setChecked(isExist);
		} 
		// @formatter:on
		return userList;
	}

	// 人员数量
	@DataProvider
	public RelPatrolTaskItemUserAmount queryUserAmount(Map<String, Object> parameter) {
		List<PatrolTaskItem> patrolTaskItemList = (List<PatrolTaskItem>) parameter.get("selectionPatrolTaskItem");
		String orgId = (String) parameter.get("orgId");
		List<RelPatrolTaskItemUserAmount> relPatrolTaskItemUserAmountList=new ArrayList();
		RelPatrolTaskItemUserAmount result=new RelPatrolTaskItemUserAmount();
		try {
			for (PatrolTaskItem patrolTaskItem : patrolTaskItemList) {
				// @formatter:off
				RelPatrolTaskItemUserAmount relPatrolTaskItemUserAmount= JpaUtil
						.linq(RelPatrolTaskItemUserAmount.class)
					.equal("patrolTaskItemId", patrolTaskItem.getId())
					.equal("orgId", orgId)
					.findOne();
				// @formatter:on
				relPatrolTaskItemUserAmountList.add(relPatrolTaskItemUserAmount);
			}
			// 取最大值
			// 巡防民警人数
			relPatrolTaskItemUserAmountList = relPatrolTaskItemUserAmountList.stream()
					.sorted(Comparator.comparing(RelPatrolTaskItemUserAmount::getCopAmount))
					.collect(Collectors.toList());
			int copAmount = relPatrolTaskItemUserAmountList.get(0).getCopAmount();
			// 巡防辅警人数
			relPatrolTaskItemUserAmountList = relPatrolTaskItemUserAmountList.stream()
					.sorted(Comparator.comparing(RelPatrolTaskItemUserAmount::getApAmount))
					.collect(Collectors.toList());
			int apAmount = relPatrolTaskItemUserAmountList.get(0).getApAmount();
			// 巡防队员人数
			relPatrolTaskItemUserAmountList = relPatrolTaskItemUserAmountList.stream()
					.sorted(Comparator.comparing(RelPatrolTaskItemUserAmount::getMemberAmount))
					.collect(Collectors.toList());
			int memberAmount = relPatrolTaskItemUserAmountList.get(0).getMemberAmount();
			
			result.setCopAmount(copAmount);
			result.setApAmount(apAmount);
			result.setMemberAmount(memberAmount);
			
		} catch (NoResultException e) {
			result.setCopAmount(0);
			result.setApAmount(0);
			result.setMemberAmount(0);
		}
		return result;
	}

}
