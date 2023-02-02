package com.mr.sa.controller.notice;

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
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.NoticeInfo;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.SpecialTask;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.entity.rel.RelNoticeOrg;
import com.mr.sa.entity.rel.RelNoticeSpecialTask;
import com.mr.sa.entity.rel.RelNoticeUser;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.service.common.RichTextService;

@Controller("saNoticeController")
@Transactional(readOnly = true)
public class NoticeController extends QueryFilter {

	@Autowired
	RichTextService richTextService;

	@DataResolver
	@Transactional
	@Log(module = "公告", category = "CRUD")
	public void save(List<NoticeInfo> notices) {
		JpaUtil.save(notices, new SmartSavePolicyAdapter() {

			@Override
			public boolean beforeInsert(SaveContext context) {
				NoticeInfo notice = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(notice))) {
					String content = notice.getContent();
					if (StringUtils.isNotBlank(content)) {
						content = richTextService.fixContent("image/notice/content", content);
						notice.setContent(content);
					}
				}
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				NoticeInfo notice = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(notice))) {
					String content = notice.getContent();
					if (StringUtils.isNotBlank(content)) {
						content = richTextService.fixContent("image/notice/content", content);
						notice.setContent(content);
					}
				}
				return true;
			}

		});
	}

	@DataProvider
	public void query(Page<NoticeInfo> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(NoticeInfo.class)
			.where(criteria)
			.desc("updateDate")
			.paging(page);
	}

	// 人员-当前组织
	@DataProvider
	public List<AppUser> queryUser(Map<String, Object> parameter) {
		String noticeId = (String) parameter.get("noticeId");
		String orgId = (String) parameter.get("orgId");
		List<AppUser> userList = JpaUtil.linq(AppUser.class)
				.equal("orgId", orgId)
				.list();
		// 加checked
		for (AppUser user : userList) {
			boolean isExist = JpaUtil.linq(RelNoticeUser.class)
					.equal("noticeId", noticeId)
					.equal("userId", user.getUsername()).exists();
			user.setChecked(isExist);
		}
		return userList;
	}

	// 关系-组织
	@DataProvider
	public List<Org> loadOrg(Map<String, Object> parameter) {
		String noticeId = (String) parameter.get("noticeId");

		List<Org> orgList = JpaUtil.linq(Org.class).isNull("parentId").list();
		for (Org org : orgList) {
			// @formatter:off
				boolean isExist = JpaUtil.linq(RelNoticeOrg.class)
						.equal("noticeId", noticeId)
						.equal("orgId", org.getCode())
						.exists();
				// @formatter:on
			org.setChecked(isExist);
		}
		return orgList;
	}

	@DataProvider
	public List<Org> loadChildrenOrg(Map<String, Object> parameter) {
		String noticeId = (String) parameter.get("noticeId");
		String parentId = (String) parameter.get("parentId");
		List<Org> orgList = JpaUtil.linq(Org.class).equal("parentId", parentId).asc("code").list();
		for (Org org : orgList) {
			// @formatter:off
				boolean isExist = JpaUtil.linq(RelNoticeOrg.class)
						.equal("noticeId", noticeId)
						.equal("orgId", org.getCode())
						.exists();
				// @formatter:on
			org.setChecked(isExist);
		}
		return orgList;
	} 
   
	// 专项任务
	@DataProvider
	public List<SpecialTask> querySpecialTask(Map<String, Object> parameter) {
		String noticeId = (String) parameter.get("noticeId");
		List<SpecialTask> specialTaskList = JpaUtil.linq(SpecialTask.class).list();
		// 加checked
		for (SpecialTask specialTask : specialTaskList) {
			// @formatter:off
			boolean isExist = JpaUtil.linq(RelNoticeSpecialTask.class)
					.equal("noticeId", noticeId)
					.equal("specialTaskId", specialTask.getId())
					.exists();
			// @formatter:on
			specialTask.setChecked(isExist);
		}
		return specialTaskList;
	}
}