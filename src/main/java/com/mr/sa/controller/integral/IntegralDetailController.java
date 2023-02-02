package com.mr.sa.controller.integral;

import cn.hutool.core.map.MapUtil;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.IntegralDetail;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.page.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Controller
@Transactional(readOnly = true)
public class IntegralDetailController extends QueryFilter {

	@Autowired
	PageUtil pageUtil;
	
	@DataResolver
	@Transactional
	@Log(module = "积分详情", category = "CRUD")
	public void save(List<IntegralDetail> integralDetails) {
		JpaUtil.save(integralDetails);
	}

	@DataProvider
	public void query(Page<IntegralDetail> page,
			Map<String, Object> parameter) {
		String userName=pageUtil.getParamAsString(parameter, "userName");
		if (MapUtil.isNotEmpty(parameter)) {
			parameter.remove("userName");
		}
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		// @formatter:off
		JpaUtil.linq(IntegralDetail.class)
			.collect("username", AppUser.class, "userId")
			.where(criteria)
			.addIf(StringUtils.isNotBlank(userName))
			.exists(AppUser.class)
				.equalProperty("username", "userId") 
				.like("nickname", "%"+userName+"%")
			.end() 
			.endIf()
			.desc("createdDate")
			.paging(page);
		// @formatter:on
		
	}

}