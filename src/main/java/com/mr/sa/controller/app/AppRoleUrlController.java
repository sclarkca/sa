package com.mr.sa.controller.app;


import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.mr.sa.entity.app.AppPermission;
import com.mr.sa.entity.app.AppUrl;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Controller
@Transactional
public class AppRoleUrlController {

    @DataProvider
    public List<AppPermission> load(String roleId) {
        return JpaUtil.linq(AppPermission.class)
                .toEntity()
                .equal("roleId", roleId)
                .collect(AppUrl.class, "resourceId")
                .list();
    }

    @DataResolver
    public void save(List<AppPermission> permissions) {
        JpaUtil.save(permissions);
    }

}
