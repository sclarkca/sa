package com.mr.sa.controller.baisc;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.Org;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Transactional(readOnly = true)
public class OrgController {

    @DataResolver
    @Transactional
    public void save(List<Org> orgs) {
        JpaUtil.save(orgs);
    }

    @DataProvider
    public void load(Page<Org> page) {
        JpaUtil
                .linq(Org.class)
                .isNull("parentId")
                .paging(page);
    }

    @DataProvider
    public List<Org> loadChildren(String parentId) {
        return JpaUtil
                .linq(Org.class)
                .equal("parentId", parentId)
                .asc("code")
                .list();
    }

    //全量组装
    @DataProvider
    public List<Org> load() {
        List<Org> result = new ArrayList<Org>();
        Map<String, List<Org>> childrenMap = new HashMap<String, List<Org>>();
        List<Org> orgs = JpaUtil.linq(Org.class).list();
        for (Org org : orgs) {
            if (childrenMap.containsKey(org.getId())) {
                org.setChildren(childrenMap.get(org.getId()));
            } else {
                org.setChildren(new ArrayList<Org>());
                childrenMap.put(org.getId(), org.getChildren());
            }
            if (org.getParentId() == null) {
                result.add(org);
            } else {
                List<Org> children;
                if (childrenMap.containsKey(org.getParentId())) {
                    children = childrenMap.get(org.getParentId());
                } else {
                    children = new ArrayList<Org>();
                    childrenMap.put(org.getParentId(), children);
                }
                children.add(org);
            }
        }
        return result;
    }

    @Expose
    public String isExists(String code) {
        boolean isExists = JpaUtil
                .linq(Org.class)
                .equal("code", code)
                .exists();
        if (isExists) {
            return "编码已存在。";
        }
        return null;
    }

	@Expose
	public String getParentName(String parentId) {
		String name = "";
		Org org;
		try {
			if (StringUtils.isNotBlank(parentId)) {
				org = JpaUtil.getOne(Org.class, parentId);
				name = org.getName();
			}

		} catch (Exception e) {

		}
		return name;
	}

    @DataProvider
    public List<Object> getAll() {
        return JpaUtil.linq(Org.class).asc("code").list();
    }
}
