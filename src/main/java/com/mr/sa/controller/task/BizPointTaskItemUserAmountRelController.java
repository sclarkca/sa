package com.mr.sa.controller.task;


import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.mr.sa.entity.BizPointTaskItem;
import com.mr.sa.entity.BizPointTaskItemUserAmountRel;
import com.mr.sa.entity.Org;
import com.mr.sa.service.common.OrgService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 关系:巡防任务/巡防人员 前端控制器
 * </p>
 *
 * @author suaike
 * @since 2022-10-01
 */
@Controller
@Transactional(readOnly = true)
public class BizPointTaskItemUserAmountRelController {

    @Resource
    private OrgService orgService;
    // 人员数量
    @DataProvider
    public List<BizPointTaskItemUserAmountRel> queryUserAmount(Map<String, Object> parameter) {
//        BizPointTaskItem patrolTaskItem = (BizPointTaskItem) parameter.get("selectionPatrolTaskItem");
        List<BizPointTaskItem> patrolTaskItemList = (List<BizPointTaskItem>) parameter.get("selectionPatrolTaskItem");
        List<String> pointTaskItemIds = patrolTaskItemList.stream().map(BizPointTaskItem::getId)
                .collect(Collectors.toList());
        List<String> orgIds = (List<String>) parameter.get("orgIds");
        List<Org> orgs = orgService.getAllOrgs();
        Map<String, Org> orgMap = orgs.stream().collect(Collectors.toMap(Org::getCode, Function.identity()));
        // @formatter:off
        List<BizPointTaskItemUserAmountRel> relPatrolTaskItemUserAmounts = JpaUtil
                .linq(BizPointTaskItemUserAmountRel.class)
                .in("pointTaskItemId", pointTaskItemIds)
                .addIf(CollectionUtils.isNotEmpty(orgIds))
                    .in("orgId", orgIds)
                .endIf()
                .list();
        // @formatter:on
        relPatrolTaskItemUserAmounts.stream().forEach(bizPointTaskItemUserAmountRel -> {
            Org org = orgMap.get(bizPointTaskItemUserAmountRel.getOrgId());
            if (Objects.nonNull(org)) {
                bizPointTaskItemUserAmountRel.setOrgName(org.getName());
            }
        });
        return relPatrolTaskItemUserAmounts;
    }
}
