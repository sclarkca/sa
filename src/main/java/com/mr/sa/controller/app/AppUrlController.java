package com.mr.sa.controller.app;


import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.security.orm.Permission;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.mr.sa.entity.app.AppUrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class AppUrlController {

	@Value("${config.icon-url}")
	private String ossUrl;

	@DataProvider
	@Transactional(readOnly = true)
	public List<AppUrl> load() {
		List<AppUrl> result = new ArrayList<AppUrl>();
		Map<String, List<AppUrl>> childrenMap = new HashMap<String, List<AppUrl>>();
		List<AppUrl> urls = JpaUtil
				.linq(AppUrl.class)
				.asc("order")
				.list();
		for (AppUrl url : urls) {
			String icon = url.getIcon();
			if (StringUtils.isNotBlank(icon)) {
				if (StringUtils.startsWith(icon, "https") || StringUtils.startsWith(icon, "http")) {
					url.setIcon(icon);
				} else {
					url.setIcon(String.format("%s%s", ossUrl, icon));
				}
			}
			if (childrenMap.containsKey(url.getId())) {
				url.setChildren(childrenMap.get(url.getId()));
			} else {
				url.setChildren(new ArrayList<AppUrl>());
				childrenMap.put(url.getId(), url.getChildren());
			}

			if (url.getParentId() == null) {
				result.add(url);
			} else {
				List<AppUrl> children;
				if (childrenMap.containsKey(url.getParentId())) {
					children = childrenMap.get(url.getParentId());
				} else {
					children = new ArrayList<AppUrl>();
					childrenMap.put(url.getParentId(), children);
				}
				children.add(url);
			}
		}
		return result;
	}

	@DataResolver
	@Transactional
	public void save(List<AppUrl> urls) {
		JpaUtil.save(urls, new SmartSavePolicyAdapter() {

			@Override
			public boolean beforeDelete(SaveContext context) {
				AppUrl url = context.getEntity();
				JpaUtil.lind(Permission.class)
					.equal("resourceId", url.getId())
					.delete();
				return true;
			}

			@Override
			public void apply(SaveContext context) {
				AppUrl url = context.getEntity();
				if (url.getParentId() == null) {
					AppUrl parent = context.getParent();
					if (parent != null) {
						url.setParentId(parent.getId());
					}
				}
				super.apply(context);
			}
		});
	}


}
