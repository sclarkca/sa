package com.mr.sa.controller.app;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.app.AppAdvert;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class AppAdvertController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "APP广告", category = "CRUD")
	public void save(List<AppAdvert> appAdverts) {
		JpaUtil.save(appAdverts);
	}

	@DataProvider
	public void query(Page<AppAdvert> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(AppAdvert.class).where(criteria).desc("updateDate").paging(page);
	}
}