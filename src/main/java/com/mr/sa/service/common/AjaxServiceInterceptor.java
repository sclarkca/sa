package com.mr.sa.service.common;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.proxy.PatternMethodInterceptor;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.bstek.dorado.web.DoradoContext;

/**
 * 操作员检查-拦截
 */
public class AjaxServiceInterceptor extends PatternMethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		String loginUsername = (String) DoradoContext.getCurrent()
				.getAttribute(DoradoContext.SESSION, "loginUsername");
		if (StringUtils.isNotBlank(loginUsername)) {
			return methodInvocation.proceed();

		}
		// 会话超时，需重新登录
		else {
			StringBuffer script = new StringBuffer();
			script.append("open($url('>/com.mr.sa.view.sys.Login.d'), '_self');");
			script.append(
					"dorado.widget.NotifyTipManager.notify('会话超时，请重新登录');");
			throw new ClientRunnableException(script.toString());
		}

	}
}