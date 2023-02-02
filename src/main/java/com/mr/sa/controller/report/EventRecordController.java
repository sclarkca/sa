package com.mr.sa.controller.report;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.mr.sa.entity.EventRecord;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class EventRecordController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "事件处理记录", category = "CRUD")
	public void save(List<EventRecord> eventRecords) {
		JpaUtil.save(eventRecords);
	}

	@DataProvider
	public List<EventRecord> query(Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		List<EventRecord> eventRecordList = JpaUtil.linq(EventRecord.class)
				.where(criteria)
				.desc("updateDate")
				.list();
		if (!eventRecordList.isEmpty()) {
			// 收集 eventRecordList 中的 bId 属性值
			Set<String> bIds = JpaUtil.collect(eventRecordList,
					"currentHandler");
			List<AppUser> appUserList = JpaUtil.linq(AppUser.class)
					.collect("code", Org.class, "orgId").in("username", bIds)
					.list();
			Map<String, AppUser> bMap = JpaUtil.index(appUserList, "username");
			// 给 A 分配 B
			for (EventRecord a : eventRecordList) {
				a.setUserLast(bMap.get(a.getCurrentHandler()));
			}
		}
		if (!eventRecordList.isEmpty()) {
			Set<String> bIds2 = JpaUtil.collect(eventRecordList, "nextHandler");
			List<AppUser> appUserList2 = JpaUtil.linq(AppUser.class)
					.collect("code", Org.class, "orgId").in("username", bIds2)
					.list();
			Map<String, AppUser> bMap2 = JpaUtil.index(appUserList2,
					"username");
			for (EventRecord a : eventRecordList) {
				a.setUser(bMap2.get(a.getNextHandler()));
			}
		}
		return eventRecordList;
	}
}