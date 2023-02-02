package com.mr.sa.service.jooq;

import cn.hutool.core.date.DateUtil;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.data.vo.UserOnlineTimeVO;
import com.mr.sa.generator.tables.records.BizOrgRecord;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.mr.sa.generator.Tables.BIZ_ORG;
import static com.mr.sa.generator.Tables.BIZ_USER_ONLINE_TIME;

@Service
public class BizUserOnlineTimeService {

    @Resource
    DSLContext dsl;

    public void queryByPage(String username, Date startTime, Date endTime, Page<UserOnlineTimeVO> page) {
        Pageable pageable = PageRequest.of(page.getPageNo(), page.getPageSize());
        Condition condition = DSL.trueCondition();
        if (StringUtils.isNotBlank(username)) {
            condition = condition.and(BIZ_USER_ONLINE_TIME.USER_NAME.contains(username));
        }
        if (Objects.nonNull(startTime)) {
            condition = condition.and(BIZ_USER_ONLINE_TIME.RECORD_DATE.ge(DateUtil.toLocalDateTime(startTime)));
        }
        if (Objects.nonNull(endTime)) {
            condition = condition.and(BIZ_USER_ONLINE_TIME.RECORD_DATE.le(DateUtil.toLocalDateTime(endTime)));
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
        //SELECT t.org_id AS orgId, o.`name` AS orgName, SUM( t.online_second ) AS onlineSecond,t.user_id as userId,t.user_name as username,t.record_date as recordDate FROM BIZ_USER_ONLINE_TIME
        List<UserOnlineTimeVO> userOnlineTimes = dsl.select(BIZ_USER_ONLINE_TIME.ORG_ID, t1Name.as("orgName"), t2Name.as("parentOrgName"), BIZ_USER_ONLINE_TIME.ONLINE_SECOND,
                        BIZ_USER_ONLINE_TIME.USER_ID, BIZ_USER_ONLINE_TIME.USER_NAME.as("username"), BIZ_USER_ONLINE_TIME.RECORD_DATE)
                .from(BIZ_USER_ONLINE_TIME)
                .leftJoin(t1).on(BIZ_USER_ONLINE_TIME.ORG_ID.eq(t1code))
                .leftJoin(t2).on(t1ParentId.eq(t2Id))
                .where(condition)
                .orderBy(BIZ_USER_ONLINE_TIME.RECORD_DATE.desc())
                .limit((pageable.getPageNumber() - 1) * pageable.getPageSize(), pageable.getPageSize())
                .fetchInto(UserOnlineTimeVO.class);
        Long total = dsl.selectCount()
                .from(BIZ_USER_ONLINE_TIME)
                .where(condition)
                .fetchOneInto(Long.class);
        page.setEntities(userOnlineTimes);
        page.setEntityCount(Math.toIntExact(total));

    }
}
