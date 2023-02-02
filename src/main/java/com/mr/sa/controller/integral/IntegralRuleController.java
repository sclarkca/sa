package com.mr.sa.controller.integral;

import com.alibaba.fastjson.JSONObject;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.IntegralRule;
import com.mr.sa.entity.IntegralType;
import com.mr.sa.service.common.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
@Transactional(readOnly = true)
@Slf4j
public class IntegralRuleController extends QueryFilter {

	@Value("${rest.api.integral-refresh}")
	private String integralRefreshUrl;

	@Autowired
	private RestTemplate restTemplate;

	@DataResolver
	@Transactional
	@Log(module = "积分规则", category = "CRUD")
	public void save(List<IntegralRule> integralRules) {
		JpaUtil.save(integralRules);
		try {
			refreshIntegral();
		} catch (Exception e) {
			log.error("积分规则刷新请求失败：{}",e.getMessage());
		}
	}

	@DataProvider
	public void query(Page<IntegralRule> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(IntegralRule.class)
			.collect(IntegralType.class, "integralTypeId")
			.where(criteria)
			.desc("updateDate")
			.paging(page);
	}

	/**
	 * 事件积分通知
	 * @param eventId
	 */
	public void refreshIntegral() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("appToken","QViIYUZ1RZK0x2FTiOcuYSUXh4AhpjDt");
		JSONObject param = new JSONObject();
		HttpEntity<Object> formEntity = new HttpEntity<>(param,headers);
		JSONObject result = restTemplate.postForObject(integralRefreshUrl, formEntity, JSONObject.class);
		log.info("积分规则刷新rest接口返回：{}",result);
	}
}