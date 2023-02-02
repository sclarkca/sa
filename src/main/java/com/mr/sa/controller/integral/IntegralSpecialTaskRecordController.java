package com.mr.sa.controller.integral;

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
import com.mr.sa.entity.IntegralSpecialTaskRecord;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class IntegralSpecialTaskRecordController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "积分专项任务执行记录", category = "CRUD")
	public void save(List<IntegralSpecialTaskRecord> integralSpecialTaskRecords) {
		JpaUtil.save(integralSpecialTaskRecords);
	}

	@DataProvider
	public void query(Page<IntegralSpecialTaskRecord> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		// @formatter:off
		JpaUtil.linq(IntegralSpecialTaskRecord.class)
			.where(criteria)
			.desc("updateDate")
			.paging(page);
		// @formatter:on
	}
}