package com.mr.sa.controller.app;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.app.AppRole;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.page.PageUtil;

@Controller
@Transactional(readOnly = true)
public class AppRoleController extends QueryFilter {

	@Autowired
	PageUtil pageUtil;
	
	@DataProvider
	public void load(Page<AppRole> page, Criteria criteria) {
		JpaUtil.linq(AppRole.class).where(criteria).paging(page);
	}

	@DataResolver
	@Transactional
	public void save(List<AppRole> appRoles) {
		JpaUtil.save(appRoles, new SmartSavePolicyAdapter() {

			@Override
			public boolean beforeDelete(SaveContext context) {
				AppRole appRole = context.getEntity();
				if (EntityState.DELETED.equals(EntityUtils.getState(appRole))) {
					String roleId = appRole.getId();
					List result;
					result = JpaUtil.linq(AppUser.class).equal("roleId", roleId).list();
					if (!result.isEmpty()) {
						pageUtil.notifyView("存在已关联用户]");
						return false;
					}
				}
				return true;
			}
		});
	}

	@DataProvider
	public Map<String, String> getValuesName() {
		List<AppRole> list = JpaUtil.linq(AppRole.class).desc("updateDate").list();

		Map<String, String> map = new LinkedHashMap<>();
		for (AppRole appRole : list) {
			map.put(appRole.getId(), appRole.getName());
		}
		return map;
	}
}
