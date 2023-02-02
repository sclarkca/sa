package com.bstek.bdf3.dorado.jpa.policy.impl;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import com.mr.sa.utils.common.MyStringUtil;

public class UUIDPolicy extends AbstractNewGeneratorPolicy {
	
	@Override
	protected Object getValue(Object entity, Field field) {
		Object value = ReflectionUtils.getField(field, entity);
		return value == null ? MyStringUtil.getUUID() : value;
	}
}