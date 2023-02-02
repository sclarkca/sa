package com.mr.sa.validator;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mr.sa.entity.IntegralSpecialTask;
import com.mr.sa.utils.page.PageUtil;

@Component
public class IntegralSpecialTaskValidator {

	@Autowired
	PageUtil pageUtil;

	public void checkDate(IntegralSpecialTask integralSpecialTask) {
		Date taskStartDate = integralSpecialTask.getTaskStartDate();
		Date taskEndDate = integralSpecialTask.getTaskEndDate();
		if (null != taskStartDate && null != taskEndDate) {
			if (taskStartDate.after(taskEndDate)) {
				pageUtil.notifyView("结束日期不能早于开始日期");
			}
		}
	}

	public void checkTime(IntegralSpecialTask integralSpecialTask) {
		Date taskStartTime = integralSpecialTask.getTaskStartTime();
		Date taskEndTime = integralSpecialTask.getTaskEndTime();
		if (null != taskStartTime && null != taskEndTime) {
			if (taskStartTime.after(taskEndTime)) {
				pageUtil.notifyView("结束时间不能早于开始时间");
			}
		}
	}
}
