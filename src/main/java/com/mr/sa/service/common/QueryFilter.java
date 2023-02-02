package com.mr.sa.service.common;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.mr.sa.utils.common.MyDateUtil;
import com.mr.sa.utils.common.MyLog;
import com.mr.sa.utils.common.MyStringUtil;

@Component
public class QueryFilter {
	private static Logger log = MyLog.get();

	/**
	 * 普通条件过滤
	 */
	public Criteria condition(Criteria criteria, Map<String, ?> map) {
		if (map != null) {
			for (Entry<String, ?> entry : map.entrySet()) {
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				SingleValueFilterCriterion criterion = new SingleValueFilterCriterion();
				// 空值
				if (value == null || StringUtils.isBlank(value.toString())
						|| !(value instanceof String
								|| value instanceof Date)) {
					continue;
				}
				// 日期范围
				// 精确到天
				// 起
				if (key.endsWith("_startTime_day")) {
					criterion.setFilterOperator(FilterOperator.ge);
					key = StringUtils.substringBeforeLast(key, "_startTime");
					if (value instanceof Date) {
						value = MyDateUtil.getCurrStart((Date) value);
					}
					if (value instanceof String) {
						Date date = MyDateUtil.parseToDay2((String) value);
						value = MyDateUtil.getCurrStart(date);
					}
				}
				// 至
				else if (key.endsWith("_endTime_day")) {
					criterion.setFilterOperator(FilterOperator.le);
					key = StringUtils.substringBeforeLast(key, "_endTime");
					if (value instanceof Date) {
						value = MyDateUtil.getCurrEnd((Date) value);
					}
					if (value instanceof String) {
						Date date = MyDateUtil.parseToDay2((String) value);
						value = MyDateUtil.getCurrEnd(date);
					}
				}
				// 精确到时分秒
				// 起
				else if (key.endsWith("_startTime")) {
					criterion.setFilterOperator(FilterOperator.ge);
					key = StringUtils.substringBeforeLast(key, "_startTime");
				}
				// 至
				else if (key.endsWith("_endTime")) {
					criterion.setFilterOperator(FilterOperator.le);
					key = StringUtils.substringBeforeLast(key, "_endTime");
				}
				// 字符串
				// 区分精确/模糊
				else if (key.endsWith("_eq")) {
					criterion.setFilterOperator(FilterOperator.eq);
					key = StringUtils.substringBeforeLast(key, "_eq");
				} else {
					criterion.setFilterOperator(FilterOperator.like);
				}
				criterion.setProperty(key);
				criterion.setValue(value);
				criteria.addCriterion(criterion);
			}
		}
		return criteria;
	}

	/**
	 * 数据查询过滤sql
	 */
	public String conditionSql(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder();
		if (map != null) {
			for (Entry<String, ?> entry : map.entrySet()) {
				// 键
				String key = (String) entry.getKey();
				// 值
				Object value = entry.getValue();
				// 运算符
				String filterOperator = "";
				// 空值
				if (value == null || StringUtils.isBlank(value.toString())
						|| !(value instanceof String
								|| value instanceof Date)) {
					continue;
				}
				// 日期范围
				// 起-精确到天
				if (key.endsWith("_startTime_day")) {
					filterOperator = ">=";
					key = StringUtils.substringBeforeLast(key,
							"_startTime_day");
					if (value instanceof Date) {
						value = MyDateUtil.getCurrStart((Date) value);
					}
					if (value instanceof String) {
						Date date = MyDateUtil.parseToDay1((String) value);
						value = MyDateUtil.format(MyDateUtil.getCurrStart(date),
								MyDateUtil.TIME_24_FORMATTER);
					}
				}
				// 至
				else if (key.endsWith("_endTime_day")) {
					filterOperator = "<=";
					key = StringUtils.substringBeforeLast(key, "_endTime_day");
					if (value instanceof Date) {
						value = MyDateUtil.getCurrEnd((Date) value);
					}
					if (value instanceof String) {
						Date date = MyDateUtil.parseToDay1((String) value);
						value = MyDateUtil.format(MyDateUtil.getCurrEnd(date),
								MyDateUtil.TIME_24_FORMATTER);
					}
				}
				// 起
				else if (key.endsWith("_startTime")) {
					filterOperator = ">=";
					key = StringUtils.substringBeforeLast(key, "_startTime");
					if (value instanceof Date) {
						value = MyDateUtil.getCurrStart((Date) value);
					}
					if (value instanceof String) {
						Date date = MyDateUtil.parseToDay((String) value);
						value = MyDateUtil.format(MyDateUtil.getCurrStart(date),
								MyDateUtil.TIME_24_FORMATTER);
					}
				}
				// 至
				else if (key.endsWith("_endTime")) {
					filterOperator = "<=";
					key = StringUtils.substringBeforeLast(key, "_endTime");
					if (value instanceof Date) {
						value = MyDateUtil.getCurrEnd((Date) value);
					}
					if (value instanceof String) {
						Date date = MyDateUtil.parseToDay((String) value);
						value = MyDateUtil.format(MyDateUtil.getCurrEnd(date),
								MyDateUtil.TIME_24_FORMATTER);
					}
				}
				// 字符串
				// 区分精确/模糊
				else if (key.endsWith("_eq")) {
					filterOperator = "=";
					key = StringUtils.substringBeforeLast(key, "_eq");
				} else {
					filterOperator = "like";
				}
				// 属性名转换为列名
				key = MyStringUtil.fieldToColumn(key);
				filterOperator = " " + filterOperator + " ";
				value = "'" + value + "'";
				sb.append(" and ").append(key).append(filterOperator)
						.append(value);
			}
		}
		return sb.toString();
	}

}
