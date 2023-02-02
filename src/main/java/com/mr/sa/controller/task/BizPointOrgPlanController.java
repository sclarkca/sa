package com.mr.sa.controller.task;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.BizPointOrgPlan;
import com.mr.sa.entity.BizPointTaskItem;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.rel.RelPatrolTaskItemOrg;
import com.mr.sa.service.common.QueryFilter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Transactional(readOnly = true)
public class BizPointOrgPlanController extends QueryFilter {

    @DataProvider
    public void query(Page<BizPointOrgPlan> page, Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        criteria = condition(criteria, parameter);
        JpaUtil.linq(BizPointOrgPlan.class)
                .collect("code", Org.class, "orgId")
                .where(criteria)
                .desc("updateDate")
                .paging(page);

    }

    // 关系-单位时间
    @DataResolver
    public List<BizPointOrgPlan> queryOrgPlan(Map<String, Object> parameter) {
        String patrolTaskItemId = (String) parameter.get("pointTaskItemId");
        List<BizPointOrgPlan> orgPlanList = new ArrayList();
        // @formatter:off
        List<BizPointTaskItem> list = JpaUtil.
                linq(BizPointTaskItem.class)
                .desc("updateDate")
                .list();
        // @formatter:on
        for (BizPointTaskItem pointTaskItem : list) {
            // @formatter:off
            List<Org> orgList = JpaUtil.linq(Org.class)
                    .exists(RelPatrolTaskItemOrg.class)
                    .equalProperty("orgId", "code")
                    .equal("pointTaskItemId", pointTaskItem.getId()).end()
                    .list();
            // @formatter:on
            for (Org org : orgList) {
                BizPointOrgPlan bizPointOrgPlan = new BizPointOrgPlan();
                //orgPlan.setOrgName(org.getName());
                bizPointOrgPlan.setPatrolStartTime(pointTaskItem.getPatrolStartTime());
                bizPointOrgPlan.setPatrolEndTime(pointTaskItem.getPatrolEndTime());
                orgPlanList.add(bizPointOrgPlan);
            }
        }

        return orgPlanList;
    }
}
