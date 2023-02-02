package com.mr.sa.controller.report;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.OpRecord;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class OpRecordController extends QueryFilter {
	 

	@DataProvider
	public void query(Page<OpRecord> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(OpRecord.class).where(criteria).desc("createdData").paging(page);
	}
}