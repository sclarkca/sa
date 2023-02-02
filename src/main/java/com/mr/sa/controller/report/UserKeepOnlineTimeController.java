package com.mr.sa.controller.report;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.config.RedisTemplateHelper;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.OrgOnlineTime;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.OrgService;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.service.jooq.BizUserKeepOnlineTimeService;
import com.mr.sa.utils.common.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Controller
@Transactional(readOnly = true)
public class UserKeepOnlineTimeController extends QueryFilter {
    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource
    private BizUserKeepOnlineTimeService bizUserKeepOnlineTimeService;

    @Resource
    private OrgService orgService;


    @DataProvider
    public void query(Page<OrgOnlineTime> page, Map<String, Object> parameter) {
        String username = (String) parameter.get("username");
        DateTime startTime = DateTime.of((Date) parameter.get("recordDate_startTime"));
        DateTime endTime = DateTime.of((Date) parameter.get("recordDate_endTime"));
        String roleId = (String) parameter.get("roleId");
        String orgId = (String) parameter.get("orgId");

        startTime = Objects.isNull(startTime) ? DateUtil.beginOfDay(new Date()) : DateUtil.beginOfDay(startTime);
        endTime = Objects.isNull(endTime) ? DateUtil.endOfDay(new Date()) : DateUtil.endOfDay(endTime);

        bizUserKeepOnlineTimeService.queryByPage(username, startTime, endTime, roleId, orgId, page);
        // 判断查询条件结束时间是否大于当天开始时间
        DateTime beginOfToday = DateUtil.beginOfDay(new Date());
        Map<String, Integer> onlineTime = new HashMap<>();
        //
        if (startTime.isAfterOrEquals(beginOfToday)) {
            // 组装查询redis当天数据分页展示
            // 查询redis中当天在线时间
            String s = redisTemplateHelper.get(BizConstants.REDIS_ONLINE_DURATION_DAILY);
            if (StringUtils.isNotBlank(s)) {
                onlineTime = JSON.parseObject(s, Map.class);
            }
            List<String> userIds = new ArrayList<>();
            if (Objects.nonNull(username) || Objects.nonNull(roleId) || Objects.nonNull(orgId)) {
                List<AppUser> appUsers = JpaUtil.linq(AppUser.class)
                        .addIf(Objects.nonNull(username))
                        .like("nickname", String.format("%%%s%%", username))
                        .endIf()
                        .addIf(Objects.nonNull(roleId))
                        .equal("roleId", roleId)
                        .endIf()
                        .addIf(Objects.nonNull(orgId))
                        .equal("orgId", roleId)
                        .endIf()
                        .list();
                userIds = appUsers.stream().map(AppUser::getUsername).collect(Collectors.toList());
            }
            List<OrgOnlineTime> userOnlineTimeDTOS = new ArrayList<>();
            onlineTime.entrySet().forEach(stringIntegerEntry -> {
                OrgOnlineTime userOnlineTimeDTO = new OrgOnlineTime();
                userOnlineTimeDTO.setUserId(stringIntegerEntry.getKey());
                userOnlineTimeDTO.setOnlineSecond(Long.valueOf(stringIntegerEntry.getValue()));
                userOnlineTimeDTOS.add(userOnlineTimeDTO);
            });
            List<OrgOnlineTime> userOnlineTimeDTOList;
            if (CollectionUtils.isNotEmpty(userIds)) {
                List<String> finalUserIds = userIds;
                userOnlineTimeDTOList = userOnlineTimeDTOS.stream().filter(userOnlineTimeDTO -> finalUserIds.contains(userOnlineTimeDTO.getUserId())).collect(Collectors.toList());
            } else {
                userOnlineTimeDTOList = userOnlineTimeDTOS;
            }
            List<String> userIdList = userOnlineTimeDTOList.stream().map(OrgOnlineTime::getUserId).collect(Collectors.toList());
            // 查询用户信息
            if (CollectionUtils.isNotEmpty(userIdList)) {
                List<AppUser> appUsers = JpaUtil.linq(AppUser.class).in("username", userIdList).list();
                Map<String, AppUser> userMap = appUsers.stream().collect(Collectors.toMap(AppUser::getUsername, Function.identity()));
                // 查询组织信息
                List<String> orgIds = appUsers.stream().distinct().map(AppUser::getOrgId).collect(Collectors.toList());
//                List<Org> orgList = JpaUtil.linq(Org.class).in("code", orgIds).list();
                List<Org> orgList = orgService.getAllOrgs();
                Map<String, Org> orgMap = orgList.stream().collect(Collectors.toMap(Org::getCode, Function.identity()));
                Map<String, Org> orgIdMap = orgList.stream().collect(Collectors.toMap(Org::getId, Function.identity()));
                userOnlineTimeDTOList.stream().forEach(userOnlineTimeDTO -> {
                    AppUser appUser = userMap.get(userOnlineTimeDTO.getUserId());
                    if (Objects.nonNull(appUser)) {
                        userOnlineTimeDTO.setUsername(appUser.getNickname());
                        userOnlineTimeDTO.setOrgId(appUser.getOrgId());
                        if (Objects.nonNull(appUser.getOrgId())) {
                            Org org = orgMap.get(appUser.getOrgId());
                            if (Objects.nonNull(org)) {
                                userOnlineTimeDTO.setOrgName(org.getName());
                                Org parentOrg = orgIdMap.get(org.getParentId());
                                if (Objects.nonNull(parentOrg)) {
                                    userOnlineTimeDTO.setParentOrgName(parentOrg.getName());
                                }
                            }
                        }
                    }
                    Long onlineSecond = userOnlineTimeDTO.getOnlineSecond();
                    String time = MyDateUtil.transfer2(onlineSecond);
                    userOnlineTimeDTO.setTime(time);
                });
            }
            List<OrgOnlineTime> collect = userOnlineTimeDTOList.stream().sorted(Comparator.comparing(OrgOnlineTime::getOnlineSecond, Comparator.reverseOrder())).collect(Collectors.toList());
            //当前页码
            int pageNo = page.getPageNo();
            //每页条数
            int displayLength = page.getPageSize();
            //忽略上一页，如果是第一页肯定不用忽略
            int skipnum = displayLength * (pageNo - 1);
            List resultList = collect.stream().skip(skipnum).limit(displayLength).collect(Collectors.toList());
            page.setEntities(resultList);
            page.setEntityCount(collect.size());
            return;
        } else if (startTime.before(beginOfToday) && endTime.isAfter(beginOfToday)) {
            // 查询redis中当天在线时间
            String s = redisTemplateHelper.get(BizConstants.REDIS_ONLINE_DURATION_DAILY);
            if (StringUtils.isNotBlank(s)) {
                onlineTime = JSON.parseObject(s, Map.class);
            }
        }
        Map<String, Integer> finalOnlineTime = onlineTime;
        Collection<OrgOnlineTime> entities = page.getEntities();
        entities.forEach(userKeepOnlineTime -> {
            Long onlineSecond = userKeepOnlineTime.getOnlineSecond();
            Integer todayOnlineTime = finalOnlineTime.get(userKeepOnlineTime.getUserId());
            if (Objects.nonNull(todayOnlineTime)) {
                onlineSecond = onlineSecond + todayOnlineTime;
            }
            String time = MyDateUtil.transfer2(onlineSecond);
            userKeepOnlineTime.setTime(time);
        });

    }

    @DataProvider
    public void queryOrg(Page<OrgOnlineTime> page, Map<String, Object> parameter) {
        Object orgId = parameter.get("orgId");
        Object startTime = parameter.get("recordDate_startTime");
        Object endTime = parameter.get("recordDate_endTime");
        String sql = "SELECT t.org_id AS orgId, o.`name` AS orgName, SUM( t.online_second ) AS onlineSecond,t.user_id as userId,t.user_name as username,t.record_date as recordDate FROM biz_user_keep_online_time t LEFT JOIN biz_org o ON t.org_id = o.`code` WHERE 1=1 " +
                (Objects.nonNull(orgId) ? " AND t.org_id = " + orgId : "")
                +
                (Objects.nonNull(startTime) ? " AND t.record_date >= '" + MyDateUtil.formatToSecond((Date) startTime) + "'" : "")
                +
                (Objects.nonNull(endTime) ? " AND t.record_date <= '" + MyDateUtil.formatToSecond((Date) endTime) + "'" : "")
                +
                " GROUP BY t.org_id ORDER BY onlineSecond DESC LIMIT " + (page.getPageNo() - 1) * page.getPageSize() + "," + page.getPageSize();

        String sqlcount = "SELECT COUNT(DISTINCT t.org_id) FROM biz_user_keep_online_time t WHERE 1=1 " +
                (Objects.nonNull(orgId) ? " AND t.org_id = " + orgId : "")
                +
                (Objects.nonNull(startTime) ? " AND t.record_date >= '" + MyDateUtil.formatToSecond((Date) startTime) + "'" : "")
                +
                (Objects.nonNull(endTime) ? " AND t.record_date <= '" + MyDateUtil.formatToSecond((Date) endTime) + "'" : "");
        List<OrgOnlineTime> list = jdbcTemplate.query(sql, new OrgOnlineTimeMapper());
        Integer integer = null;
        try {
            integer = jdbcTemplate.queryForObject(sqlcount, Integer.class);
        } catch (DataAccessException e) {
            integer = 0;
        }
        list.stream().forEach(orgOnlineTime -> {
            Long onlineSecond = orgOnlineTime.getOnlineSecond();
            String time = MyDateUtil.transfer2(onlineSecond);
            orgOnlineTime.setTime(time);
        });
        page.setEntities(list);
        page.setEntityCount(integer);
    }

    class OrgOnlineTimeMapper implements RowMapper<OrgOnlineTime> {

        @Override
        public OrgOnlineTime mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrgOnlineTime orgOnlineTime = new OrgOnlineTime();
            orgOnlineTime.setOrgId(rs.getString("orgId"));
            orgOnlineTime.setOrgName(rs.getString("orgName"));
            orgOnlineTime.setOnlineSecond(rs.getLong("onlineSecond"));
            orgOnlineTime.setUserId(rs.getString("userId"));
            orgOnlineTime.setUsername(rs.getString("username"));
//            orgOnlineTime.setRecordDate(rs.getDate("recordDate"));
            return orgOnlineTime;
        }
    }
}
