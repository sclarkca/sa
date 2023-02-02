package com.mr.sa.validator.common;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.Expose;
import com.mr.sa.utils.common.MyLog;

@Component
public class PageValidator {
	private static Logger log = MyLog.get();

	/**
	 * 检查唯一性
	 */
	@Expose
	@Transactional
	public String checkUniq(Map<String, Object> parameter) {
		String className = (String) parameter.get("className");
		String fieldName = (String) parameter.get("fieldName");
		String fieldValue = (String) parameter.get("fieldValue");
		if (StringUtils.isBlank(fieldValue)) {
			return null;
		}
		Class clazz;
		try {
			clazz = Class.forName(className);
			List<Class> list = JpaUtil.linq(clazz).equal(fieldName, fieldValue).list();
			if (list.isEmpty()) {
				return null;
			}
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException", e);
		}
		return "数据已存在";
	}
}
