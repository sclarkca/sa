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
public class SpecialTaskSubTypeController extends QueryFilter {

	@DataResolver
	@Transactional
	@Log(module = "专项任务类型", category = "CRUD")
	public void save(List<SpecialTaskSubType> specialTaskSubTypes) {
		JpaUtil.save(specialTaskSubTypes);
	}

	@DataProvider
	public void query(Page<SpecialTaskSubType> page,
			Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(SpecialTaskSubType.class).where(criteria).desc("updateDate")
				.paging(page);
	}

	@DataProvider
	public Map<String, String> getValuesName() {
		List<SpecialTaskSubType> list = JpaUtil.linq(SpecialTaskSubType.class)
				.desc("updateDate").list();

		Map<String, String> map = new LinkedHashMap<>();
		for (SpecialTaskSubType specialTaskSubType : list) {
			map.put(specialTaskSubType.getId(), specialTaskSubType.getName());
		}
		return map;
	} 
}