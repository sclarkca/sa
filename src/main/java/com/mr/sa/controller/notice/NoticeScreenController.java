package com.mr.sa.controller.notice;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.NoticeScreen;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.service.common.RichTextService;

@Controller
@Transactional(readOnly = true)
public class NoticeScreenController extends QueryFilter {

	@Autowired
	RichTextService richTextService;

	@DataResolver
	@Transactional
	@Log(module = "公告", category = "CRUD")
	public void save(List<NoticeScreen> noticeScreens) {
		JpaUtil.save(noticeScreens);
	}

	@DataProvider
	public void query(Page<NoticeScreen> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(NoticeScreen.class).where(criteria).desc("topTime", "updateDate").paging(page);
	}

}