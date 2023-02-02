package com.mr.sa.controller.task;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dictionary.domain.DictionaryItem;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.*;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class SpecialTaskTypeController extends QueryFilter {

	@DataResolver
	@Transactional
	@Log(module = "专项任务类型", category = "CRUD")
	public void save(List<SpecialTaskType> specialTaskTypes) {
		JpaUtil.save(specialTaskTypes);
	}

	@DataProvider
	public void query(Page<SpecialTaskType> page,
			Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(SpecialTaskType.class).where(criteria).desc("updateDate")
				.paging(page);
	}

	@DataProvider
	public Map<String, String> getValuesName() {
		List<SpecialTaskType> list = JpaUtil.linq(SpecialTaskType.class)
				.desc("updateDate").list();

		Map<String, String> map = new LinkedHashMap<>();
		for (SpecialTaskType specialTaskType : list) {
			map.put(specialTaskType.getId(), specialTaskType.getName());
		}
		return map;
	}
}