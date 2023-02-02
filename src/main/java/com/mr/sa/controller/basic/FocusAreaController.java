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
import com.mr.sa.entity.basic.FocusArea;
import com.mr.sa.service.common.QueryFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Transactional(readOnly = true)
public class FocusAreaController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "重点区域", category = "CRUD")
	public void save(List<FocusArea> focusAreas) {
		JpaUtil.save(focusAreas);
	}

	@DataProvider
	public void query(Page<FocusArea> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(FocusArea.class).where(criteria).paging(page);
	}

}
