package com.mr.sa.utils.jpa;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bstek.bdf3.dorado.jpa.CriteriaUtils;
import com.mr.sa.utils.common.MyStringUtil;

public class MyCriteriaUtils extends CriteriaUtils {
	private static final Logger logger = LoggerFactory.getLogger(MyCriteriaUtils.class);

	public static void buildCriteriaMap(Map<String, Object> map) {
		Map<String, Object> valueMap = new HashMap<>();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = map.get(key);
			if (value.getClass().getName().startsWith("com.mr.qdp")) {
				iterator.remove();
				valueMap.putAll(getNotNullValue(key, value));
			}
		}
		map.putAll(valueMap);
	}

	public static Map<String, Object> getNotNullValue(String prefix, Object obj) {
		Map<String, Object> map = new HashMap<>();
		Field[] fs = obj.getClass().getSuperclass().getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true);
			Object value = new Object();
			try {
				value = f.get(obj);
				if (value != null && MyStringUtil.isNotEmpty(value.toString()) && f.getModifiers() == 2) {
					map.put(prefix + "." + f.getName(), value);
				}
			} catch (Exception e) {
				logger.info("Exception", e);
			}
		}
		return map;
	}

	public static void clearEmptyValue(Map<String, Object> map) {
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			Object value = map.get(iterator.next());
			if (value == null || MyStringUtil.isEmpty(value.toString())) {
				iterator.remove();
			}
		}
	}
}
