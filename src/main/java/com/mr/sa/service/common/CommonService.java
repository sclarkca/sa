package com.mr.sa.service.common;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;
import com.mr.sa.utils.pwd.MakeRandomPasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

	@Expose
	public void changeSkin(String skin) {
		WebConfigure.set(DoradoContext.SESSION, "view.skin", skin);
	}

	@Expose
	public String resetPwd() {
		String password = MakeRandomPasswordUtil.makeRandomPassword();
		return password;
	}
}
