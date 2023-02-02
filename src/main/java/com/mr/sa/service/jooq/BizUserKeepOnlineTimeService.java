package com.mr.sa.service.jooq;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.OrgOnlineTime;
import com.mr.sa.generator.tables.records.BizOrgRecord;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.mr.sa.generator.Tables.*;

@Service
public class BizUserKeepOnlineTimeService {


    @Resource
    DSLContext dsl;

    public void queryByPage(String username, DateTime startTime, DateTime endTime, String roleId, String orgId, Page<OrgOnlineTime> page) {
        Pageable pageable = PageRequest.of(page.getPageNo(), page.getPageSize());
        Condition condition = DSL.trueCondition();
        if (StringUtils.isNotBlank(username)) {
            condition = condition.and(BIZ_USER_KEEP_ONLINE_TIME.USER_NAME.contains(username));
        }
        if (StringUtils.isNotBlank(roleId)) {
            condition = condition.and(BIZ_APP_USER.ROLE_ID.eq(roleId));
        }
        if (StringUtils.isNotBlank(orgId)) {
            condition = condition.and(BIZ_USER_KEEP_ONLINE_TIME.ORG_ID.eq(orgId));
        }
        if (Objects.nonNull(startTime)) {
            condition = condition.and(BIZ_USER_KEEP_ONLINE_TIME.RECORD_DATE.ge(DateUtil.toLocalDateTime(startTime)));
        }
        if (Objects.nonNull(endTime)) {
            condition = condition.and(BIZ_USER_KEEP_ONLINE_TIME.RECORD_DATE.le(DateUtil.toLocalDateTime(endTime)));
        }
        Table<BizOrgRecord> t1 = BIZ_ORG.as("t1");
        Table<BizOrgRecord> t2 = BIZ_ORG.as("t2");
        Field<String> t1code = t1.field(BIZ_ORG.CODE);
        Field<String> t1Name = t1.field(BIZ_ORG.NAME);
        Field<String> t1Id = t1.field(BIZ_ORG.ID);
        Field<String> t1ParentId = t1.field(BIZ_ORG.PARENT_ID);
        Field<String> t2Name = t2.field(BIZ_ORG.NAME);
        Field<String> t2Id = t2.field(BIZ_ORG.ID);
        Field<String> t2ParentId = t2.field(BIZ_ORG.PARENT_ID);
        Field<BigDecimal> onlineSecond = DSL.sum(BIZ_USER_KEEP_ONLINE_TIME.ONLINE_SECOND).as("onlineSecond");
        List<OrgOnlineTime> orgOnlineTimes;
        Table<Record7<String, String, String, BigDecimal, String, String, LocalDateTime>> table;
        orgOnlineTimes = dsl.select(BIZ_USER_KEEP_ONLINE_TIME.ORG_ID, t1Name.as("orgName"), t2Name.as("parentOrgName"), onlineSecond,
                        BIZ_USER_KEEP_ONLINE_TIME.USER_ID, BIZ_USER_KEEP_ONLINE_TIME.USER_NAME.as("username"), BIZ_USER_KEEP_ONLINE_TIME.RECORD_DATE)
                .from(BIZ_USER_KEEP_ONLINE_TIME)
                .leftJoin(t1).on(BIZ_USER_KEEP_ONLINE_TIME.ORG_ID.eq(t1code))
                .leftJoin(t2).on(t1ParentId.eq(t2Id))
                .innerJoin(BIZ_APP_USER).on(BIZ_USER_KEEP_ONLINE_TIME.USER_ID.eq(BIZ_APP_USER.USERNAME_))
                .where(condition)
                .groupBy(BIZ_USER_KEEP_ONLINE_TIME.USER_ID)
                .orderBy(onlineSecond.desc())
                .limit((pageable.getPageNumber() - 1) * pageable.getPageSize(), pageable.getPageSize())
                .fetchInto(OrgOnlineTime.class);
        table = dsl.select(BIZ_USER_KEEP_ONLINE_TIME.ORG_ID, t1Name.as("orgName"), t2Name.as("parentOrgName"), DSL.sum(BIZ_USER_KEEP_ONLINE_TIME.ONLINE_SECOND).as("onlineSecond"),
                        BIZ_USER_KEEP_ONLINE_TIME.USER_ID, BIZ_USER_KEEP_ONLINE_TIME.USER_NAME.as("username"), BIZ_USER_KEEP_ONLINE_TIME.RECORD_DATE)
                .from(BIZ_USER_KEEP_ONLINE_TIME)
                .leftJoin(t1).on(BIZ_USER_KEEP_ONLINE_TIME.ORG_ID.eq(t1code))
                .leftJoin(t2).on(t1ParentId.eq(t2Id))
                .innerJoin(BIZ_APP_USER).on(BIZ_USER_KEEP_ONLINE_TIME.USER_ID.eq(BIZ_APP_USER.USERNAME_))
                .where(condition)
                .groupBy(BIZ_USER_KEEP_ONLINE_TIME.USER_ID).asTable();

        Long total = dsl.selectCount()
                .from(table)
                .fetchOneInto(Long.class);
        page.setEntities(orgOnlineTimes);
        page.setEntityCount(Math.toIntExact(total));
    }
}
