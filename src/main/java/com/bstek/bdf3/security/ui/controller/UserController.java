package com.bstek.bdf3.security.ui.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.security.orm.RoleGrantedAuthority;
import com.bstek.bdf3.security.orm.User;
import com.bstek.bdf3.security.ui.service.UserService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.NoticeInfo;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class UserController extends QueryFilter {
	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected UserService userService;

	@DataProvider
	public List<User> query(Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		List<User> userList = JpaUtil.linq(User.class).where(criteria).desc("updateDate").list();
		return userList;
	}

	@Expose
	public String validateOldPassword(String oldPassword) {
		return userService.validateOldPassword(oldPassword);
	}

	@DataResolver
	@Transactional
	public void save(List<User> users) {
		JpaUtil.save(users, new SmartSavePolicyAdapter() {

			@Override
			public boolean beforeInsert(SaveContext context) {
				User user = context.getEntity();
				if (!user.getPassword().startsWith("{bcrypt}$")) {
					user.setPassword(passwordEncoder.encode(user.getPassword()));
				}
				return true;
			}
			@Override
			public boolean beforeUpdate(SaveContext context) {
				User user = context.getEntity();
				if (!user.getPassword().startsWith("{bcrypt}$")) {
					user.setPassword(passwordEncoder.encode(user.getPassword()));
				}
				return true;
			}
			@Override
			public boolean beforeDelete(SaveContext context) {
				User user = context.getEntity();
				JpaUtil.lind(RoleGrantedAuthority.class).equal("actorId", user.getUsername()).delete();
				return true;
			}

		});
	}

	@Expose
	@Transactional
	public void changePassword(String username, String newPassword) {
		userService.changePassword(username, newPassword);
	}

	@Expose
	public String isExists(Map<String, Object> parameter) {
		User user = (User) parameter.get("entity");
		String username = user.getUsername();
		// @formatter:off
		boolean isExists = JpaUtil.linq(User.class)
				.addIf(StringUtils.isBlank(username))
					.equal("username", username)
				.endIf()
				.addIf(StringUtils.isNotBlank(username))
					.notEqual("username", username)
					.equal("username", username)
				.endIf()
				.exists();
		// @formatter:on
		if (isExists) {
			return "用户名已存在。";
		}
		return null;
	}
}
