package com.mr.sa.service.jooq;

import cn.hutool.core.date.DateUtil;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.data.vo.BizPointPlanVO;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mr.sa.generator.tables.BizPointPlan.BIZ_POINT_PLAN;

@Service
public class BizPointPlanService {

    @Resource
    DSLContext dsl;

    public void page(Map<String, Object> parameter, Page<BizPointPlanVO> page) {
        Pageable pageable = PageRequest.of(page.getPageNo(), page.getPageSize());
        Condition condition = DSL.trueCondition();

        if (MapUtils.isNotEmpty(parameter)) {
            if (!StringUtils.isEmpty((String) parameter.get("name"))) {
                // 放入要查询的条件信息
                condition = condition.and(BIZ_POINT_PLAN.NAME.contains((String) parameter.get("name")));
            }
            if (Objects.nonNull(parameter.get("workStatus"))) {
                condition = condition.and(BIZ_POINT_PLAN.WORK_STATUS.eq((String) parameter.get("workStatus")));
            }
            if (Objects.nonNull(parameter.get("patrolStartTime_startTime"))) {
                Date patrolStartTime_startTime = (Date) parameter.get("patrolStartTime_startTime");
                condition = condition.and(BIZ_POINT_PLAN.PATROL_START_TIME.ge(DateUtil.toLocalDateTime(patrolStartTime_startTime.toInstant())));
            }
            if (Objects.nonNull(parameter.get("patrolStartTime_endTime"))) {
                Date patrolStartTime_endTime = (Date) parameter.get("patrolStartTime_endTime");
                condition = condition.and(BIZ_POINT_PLAN.PATROL_START_TIME.le(DateUtil.toLocalDateTime(patrolStartTime_endTime)));
            }
            if (!StringUtils.isEmpty((String) parameter.get("orgName"))) {
                // 放入要查询的条件信息
                condition = condition.and(BIZ_POINT_PLAN.ORG_ID.eq((String) parameter.get("orgId")));
            }
        }
        Field<String> name = DSL.field("SUBSTRING_INDEX( {0}, '-',- 1 )", SQLDataType.VARCHAR, BIZ_POINT_PLAN.NAME);
        Field<String> reason = DSL.field("GROUP_CONCAT( {0} )", SQLDataType.VARCHAR, BIZ_POINT_PLAN.REASON);
        Field<String> id = DSL.field("GROUP_CONCAT( {0} )", SQLDataType.VARCHAR, BIZ_POINT_PLAN.ID);
        List<BizPointPlanVO> pointPlanList = dsl.select(id.as("idStr"),name.as("name"), BIZ_POINT_PLAN.WORK_STATUS.min().as("workStatus"), reason.as("reason")
                        , BIZ_POINT_PLAN.PATROL_START_TIME, BIZ_POINT_PLAN.PATROL_END_TIME)
                .from(BIZ_POINT_PLAN)
                .where(condition)
                .groupBy(BIZ_POINT_PLAN.POINT_TASK_ITEM_ID, BIZ_POINT_PLAN.PATROL_START_TIME, BIZ_POINT_PLAN.PATROL_END_TIME)
                .orderBy(BIZ_POINT_PLAN.PATROL_START_TIME.desc())
                .limit((pageable.getPageNumber() - 1) * pageable.getPageSize(), pageable.getPageSize())
                .fetchInto(BizPointPlanVO.class);
        Table<Record5<String, String, String, LocalDateTime, LocalDateTime>> table = dsl.select(name.as("name"), BIZ_POINT_PLAN.WORK_STATUS.min().as("workStatus"), reason.as("reason")
                        , BIZ_POINT_PLAN.PATROL_START_TIME, BIZ_POINT_PLAN.PATROL_END_TIME)
                .from(BIZ_POINT_PLAN)
                .where(condition)
                .groupBy(BIZ_POINT_PLAN.POINT_TASK_ITEM_ID, BIZ_POINT_PLAN.PATROL_START_TIME, BIZ_POINT_PLAN.PATROL_END_TIME).asTable();
        Long total = dsl.selectCount()
                .from(table)
                .fetchOneInto(Long.class);
        page.setEntities(pointPlanList);
        page.setEntityCount(Math.toIntExact(total));
    }
}
