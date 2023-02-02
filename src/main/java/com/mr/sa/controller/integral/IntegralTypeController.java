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
import com.mr.sa.entity.IntegralType;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class IntegralTypeController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "积分类型", category = "CRUD")
	public void save(List<IntegralType> integralTypes) {
		JpaUtil.save(integralTypes);
	}

	@DataProvider
	public void query(Page<IntegralType> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(IntegralType.class).where(criteria).desc("updateDate").paging(page);
	}

	@DataProvider
	public Map<String, String> getValuesName() {
		List<IntegralType> list = JpaUtil.linq(IntegralType.class).desc("updateDate").list();

		Map<String, String> map = new LinkedHashMap<>();
		for (IntegralType integralType : list) {
			map.put(integralType.getId(), integralType.getName());
		}
		return map;
	}
	
	@DataProvider
	public Map<String, String> getNamesName() {
		List<IntegralType> list = JpaUtil.linq(IntegralType.class).desc("updateDate").list();

		Map<String, String> map = new LinkedHashMap<>();
		for (IntegralType integralType : list) {
			map.put(integralType.getName(), integralType.getName());
		}
		return map;
	}
}