package com.mr.sa.controller.task;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.feilong.core.lang.StringUtil;
import com.mr.sa.entity.BizPointRecord;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.OrgService;
import com.mr.sa.service.common.QueryFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@Transactional(readOnly = true)
public class BizPointRecordController extends QueryFilter {


    @Resource
    private OrgService orgService;

    @DataProvider
    public void query(Page<BizPointRecord> page, Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        Object orgName = parameter.get("orgName");
        if (Objects.isNull(orgName)) {
            parameter.remove("orgId");
        }
        parameter.remove("orgName");
        criteria = condition(criteria, parameter);
        JpaUtil.linq(BizPointRecord.class).where(criteria).desc("scanTime").paging(page);
        Collection<BizPointRecord> entities = page.getEntities();
        Map<String, Org> orgMap = orgService.getAllOrgs().stream().collect(Collectors.toMap(Org::getCode, Function.identity()));
        entities.forEach(bizPointRecord -> {
            String orgId = bizPointRecord.getOrgId();
            Org org = orgMap.get(orgId);
            if (Objects.nonNull(org)) {
                bizPointRecord.setOrgName(org.getName());
            }
        });
    }

    @DataProvider
    public void queryByIds(Page<BizPointRecord> page, Map<String, Object> parameter) {
        String idStr = (String) parameter.get("planId");
        String[] ids = StringUtils.split(idStr,",");
        JpaUtil.linq(BizPointRecord.class).in("pointPlanId",ids).desc("scanTime").paging(page);
        Collection<BizPointRecord> entities = page.getEntities();
        Map<String, Org> orgMap = orgService.getAllOrgs().stream().collect(Collectors.toMap(Org::getCode, Function.identity()));
        entities.forEach(bizPointRecord -> {
            String orgId = bizPointRecord.getOrgId();
            Org org = orgMap.get(orgId);
            if (Objects.nonNull(org)) {
                bizPointRecord.setOrgName(org.getName());
            }
        });
    }
}
