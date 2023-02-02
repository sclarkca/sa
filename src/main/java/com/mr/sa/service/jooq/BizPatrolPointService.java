package com.mr.sa.service.jooq;

import com.bstek.dorado.data.provider.Page;
import com.mr.sa.data.vo.PatrolPointVO;
import com.mr.sa.entity.BizPointTaskItem;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mr.sa.generator.Tables.*;

@Service
public class BizPatrolPointService {

    @Resource
    DSLContext dsl;

    public void page(List<BizPointTaskItem> patrolTaskItemList, Map<String, Object> parameter, Page<PatrolPointVO> page) {
        Pageable pageable = PageRequest.of(page.getPageNo(), page.getPageSize());
        Condition condition = DSL.trueCondition();
        condition = condition.and(BIZ_PATROL_POINT.LEVEL.eq("1"));
        if (MapUtils.isNotEmpty(parameter)) {

            if (Objects.nonNull(parameter.get("code"))) {
                condition = condition.and(BIZ_PATROL_POINT.CODE.in((String) parameter.get("code")));
                if (!StringUtils.isEmpty((String) parameter.get("name"))) {
                    // 放入要查询的条件信息
                    String name = (String) parameter.get("name");
                    condition = condition.or(BIZ_PATROL_POINT.NAME.likeRegex(StringUtils.replace(name, ",", "|")));
                }
            } else {
                if (!StringUtils.isEmpty((String) parameter.get("name"))) {
                    String name = (String) parameter.get("name");
                    condition = condition.and(BIZ_PATROL_POINT.NAME.likeRegex(StringUtils.replace(name, ",", "|")));
                }
            }

            if (Objects.nonNull(parameter.get("orgId"))) {
                condition = condition.and(BIZ_POINT_TASK_ITEM_ORG.ORG_ID.eq((String) parameter.get("orgId")));
            }

        }
        List<String> pointTaskItemIds = patrolTaskItemList.stream().map(BizPointTaskItem::getId).collect(Collectors.toList());

        Field<Object> isChecked = DSL.field("IF (ISNULL({0}),false,true)", BIZ_POINT_TASK_ITEM_POINT.POINT_ID).as("checked");
        List<PatrolPointVO> pointList = dsl.selectDistinct(isChecked, BIZ_PATROL_POINT.ID, BIZ_PATROL_POINT.CODE, BIZ_PATROL_POINT.NAME)
                .from(BIZ_PATROL_POINT)
                .leftJoin(BIZ_POINT_TASK_ITEM_POINT).on(BIZ_POINT_TASK_ITEM_POINT.POINT_ID.eq(BIZ_PATROL_POINT.ID).and(BIZ_POINT_TASK_ITEM_POINT.POINT_TASK_ITEM_ID.in(pointTaskItemIds)))
                .leftJoin(BIZ_POINT_TASK_ITEM_ORG).on(BIZ_POINT_TASK_ITEM_ORG.POINT_TASK_ITEM_ID.eq(BIZ_POINT_TASK_ITEM_POINT.POINT_TASK_ITEM_ID))
                .where(condition)
                .or(BIZ_POINT_TASK_ITEM_POINT.POINT_ID.isNotNull())
                .orderBy(BIZ_POINT_TASK_ITEM_POINT.POINT_ID.desc(), BIZ_PATROL_POINT.CODE.desc())
                .limit((pageable.getPageNumber() - 1) * pageable.getPageSize(), pageable.getPageSize())
                .fetchInto(PatrolPointVO.class);
        Table<Record4<Object, String, String, String>> table = dsl.selectDistinct(isChecked, BIZ_PATROL_POINT.ID, BIZ_PATROL_POINT.CODE, BIZ_PATROL_POINT.NAME)
                .from(BIZ_PATROL_POINT)
                .leftJoin(BIZ_POINT_TASK_ITEM_POINT).on(BIZ_POINT_TASK_ITEM_POINT.POINT_ID.eq(BIZ_PATROL_POINT.ID))
                .leftJoin(BIZ_POINT_TASK_ITEM_ORG).on(BIZ_POINT_TASK_ITEM_ORG.POINT_TASK_ITEM_ID.eq(BIZ_POINT_TASK_ITEM_POINT.POINT_TASK_ITEM_ID))
                .where(condition)
                .or(BIZ_POINT_TASK_ITEM_POINT.POINT_ID.isNotNull()).asTable();
        Long total = dsl.selectCount()
                .from(table)
                .fetchOneInto(Long.class);
        page.setEntities(pointList);
        page.setEntityCount(Math.toIntExact(total));
    }
}
