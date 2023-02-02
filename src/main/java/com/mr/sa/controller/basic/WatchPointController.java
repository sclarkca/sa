package com.mr.sa.controller.basic;

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
import com.mr.sa.entity.basic.WatchPoint;
import com.mr.sa.service.common.QueryFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Transactional(readOnly = true)
public class WatchPointController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "守望点", category = "CRUD")
	public void save(List<WatchPoint> watchPoints) {
		JpaUtil.save(watchPoints);
	}

	@DataProvider
	public void query(Page<WatchPoint> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(WatchPoint.class).where(criteria).paging(page);
	}

}
