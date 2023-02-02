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
import com.mr.sa.entity.app.AppConf;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class AppConfController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "App配置", category = "CRUD")
	public void save(List<AppConf> appConfs) {
		JpaUtil.save(appConfs);
	}

	@DataProvider
	public void query(Page<AppConf> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(AppConf.class).where(criteria).desc("updateDate")
				.paging(page);
	}
}