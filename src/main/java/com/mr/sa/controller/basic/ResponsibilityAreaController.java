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
import com.mr.sa.entity.basic.ResponsibilityArea;
import com.mr.sa.service.common.QueryFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * RESPONSIBILITYAREA
 */
@Slf4j
@Controller
@Transactional(readOnly = true)
public class ResponsibilityAreaController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "责任区", category = "CRUD")
	public void save(List<ResponsibilityArea> responsibilityAreas) {
		JpaUtil.save(responsibilityAreas);
	}

	@DataProvider
	public void query(Page<ResponsibilityArea> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(ResponsibilityArea.class).where(criteria).paging(page);
	}

}
