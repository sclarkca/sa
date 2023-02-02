package com.mr.sa.utils.page;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.mr.sa.utils.common.MyLog;

/**
 * 处理页面交互 提示 参数传递等工具类 因为考虑方便现有单元测试框架的原因，此处没有采用静态方法
 *
 */
@Component
public class PageUtil {
	private static Logger log = MyLog.get();

	/**
	 * 通知前端-notify
	 */
	public void notifyView(String tip) {
		throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('" + tip + "');");
	}

	/**
	 * 通知前端-warn
	 */
	public void warnView(String tip) {
		throw new ClientRunnableException("dorado.MessageBox.alert('" + tip + "', { icon: \"WARNING\" });");
	}
	
	/**
	 * 获取页面参数,去除99999，去除all
	 *
	 * @param key
	 * @return
	 */
	public String getParamAsString(Map<String, Object> parameter, String key) {
		try {
			if (null == parameter || !parameter.containsKey(key)
					|| StringUtils.isBlank((String) parameter.get(key))
					|| "all".equals((String) parameter.get(key))) {
				return null;
			}
		} catch (Exception e) {
			log.error("getParamAsString error occurs while key:" + key, e);
			this.notifyView("页面参数获取系统错误!参数:" + key);
		}
		String result = StringUtils.trimToNull((String) parameter.get(key));
		if ("-1".equals(result) || "all".equals(result)) {
			return null;
		}
		return result;
	}

	/**
	 * 获取页面参数,,保留99999，all
	 *
	 * @param key
	 * @return
	 */
	public String getParamAsStringAll(Map<String, Object> parameter,
			String key) {
		try {
			if (null == parameter || !parameter.containsKey(key)
					|| StringUtils.isBlank((String) parameter.get(key))) {
				return null;
			}
		} catch (Exception e) {
			log.error("getParamAsString error occurs while key:" + key, e);
			this.notifyView("页面参数获取系统错误!参数:" + key);
		}
		String result = StringUtils.trimToNull((String) parameter.get(key));

		return result;
	}

	/**
	 * 获取页面参数,,保留-1，去除99999
	 *
	 * @param key
	 * @return
	 */
	public String getParamAsStringN1(Map<String, Object> parameter,
			String key) {
		try {
			if (null == parameter || !parameter.containsKey(key)
					|| StringUtils.isBlank((String) parameter.get(key))
					|| "all".equals((String) parameter.get(key))
					|| "全部".equals((String) parameter.get(key))) {
				return null;
			}
		} catch (Exception e) {
			log.error("getParamAsString error occurs while key:" + key, e);
			this.notifyView("页面参数获取系统错误!参数:" + key);
		}
		String result = StringUtils.trimToNull((String) parameter.get(key));
		if ("99999".equals(result) || "all".equals(result)) {
			return null;
		}
		return result;
	}

	/**
	 * 获取页面参数,保留99999，去除all
	 *
	 * @param key
	 * @return
	 */
	public String getParamAsString99999(Map<String, Object> parameter,
			String key) {
		try {
			if (null == parameter || !parameter.containsKey(key)
					|| StringUtils.isBlank((String) parameter.get(key))) {
				return null;
			}
		} catch (Exception e) {
			log.error("getParamAsString error occurs while key:" + key, e);
			this.notifyView("页面参数获取系统错误!参数:" + key);
		}
		String result = StringUtils.trimToNull((String) parameter.get(key));
		if ("-1".equals(result) || "all".equals(result)) {
			return null;
		}
		return result;
	}

	/**
	 * 获取页面参数
	 *
	 * @param key
	 * @return
	 */
	public String getParamAsStringAllEmpty(Map<String, Object> parameter,
			String key) {
		try {
			if (null == parameter || !parameter.containsKey(key)
					|| StringUtils.isBlank((String) parameter.get(key))
					|| "all".equals((String) parameter.get(key))) {
				return null;
			}
		} catch (Exception e) {
			log.error("getParamAsString error occurs while key:" + key, e);
			this.notifyView("页面参数获取系统错误!参数:" + key);
		}
		String result = StringUtils.trimToNull((String) parameter.get(key));
		if ("-1".equals(result) || StringUtils.equalsIgnoreCase(result, "all")
				|| StringUtils.equalsIgnoreCase(result, "全部")) {
			return null;
		}
		return result;
	}

	/**
	 * 获取页面参数
	 *
	 * @param key
	 * @return
	 */
	public Integer getParamAsInteger(Map<String, Object> parameter,
			String key) {
		if (null == parameter) {
			return null;
		}
		if (parameter.containsKey(key) && (null != parameter.get(key))) {
			Integer result = null;
			try {
				result = (Integer) parameter.get(key);
			} catch (Exception e) {
				log.error("getParamAsInteger error occurs while key:" + key, e);
				this.notifyView("页面参数获取系统错误!参数:" + key);
			}
			if (-1 == result) {
				return null;
			}
			return result;
		}
		return null;
	}

	/**
	 * 获取页面参数
	 *
	 * @param key
	 * @return
	 */
	public Long getParamAsLong(Map<String, Object> parameter, String key) {
		if (null == parameter) {
			return null;
		}
		if (parameter.containsKey(key) && (null != parameter.get(key))) {
			Long result = null;
			try {
				result = (Long) parameter.get(key);
			} catch (Exception e) {
				log.error("getParamAsInteger error occurs while key:" + key, e);
				this.notifyView("页面参数获取系统错误!参数:" + key);
			}
			if (-1 == result) {
				return null;
			}
			return result;
		}
		return null;
	}

	/**
	 * 获取页面参数
	 *
	 * @param key
	 * @return
	 */
	public Date getParamAsDate(Map<String, Object> parameter, String key) {
		if (null == parameter) {
			return null;
		}
		if (parameter.containsKey(key) && (null != parameter.get(key))) {
			return (Date) parameter.get(key);
		}
		return null;
	}

}
