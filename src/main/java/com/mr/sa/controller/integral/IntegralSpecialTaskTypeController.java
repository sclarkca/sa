package com.mr.sa.controller.integral;

import java.util.LinkedHashMap;
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
import com.mr.sa.entity.IntegralSpecialTaskType;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class IntegralSpecialTaskTypeController extends QueryFilter {

	@DataResolver
	@Transactional
	@Log(module = "专项任务类型", category = "CRUD")
	public void save(List<IntegralSpecialTaskType> integralSpecialTaskTypes) {
		JpaUtil.save(integralSpecialTaskTypes);
	}

	@DataProvider
	public void query(Page<IntegralSpecialTaskType> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(IntegralSpecialTaskType.class).where(criteria).desc("updateDate").paging(page);
	}

	@DataProvider
	public Map<String, String> getValuesName() {
		List<IntegralSpecialTaskType> list = JpaUtil.linq(IntegralSpecialTaskType.class).desc("updateDate").list();

		Map<String, String> map = new LinkedHashMap<>();
		for (IntegralSpecialTaskType integralSpecialTaskType : list) {
			map.put(integralSpecialTaskType.getId(), integralSpecialTaskType.getName());
		}
		return map;
	}
}