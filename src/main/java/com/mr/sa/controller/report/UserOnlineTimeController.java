package com.mr.sa.controller.report;

import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mr.sa.config.RedisTemplateHelper;
import com.mr.sa.data.vo.UserOnlineTimeVO;
import com.mr.sa.dto.OrgOnlineTimeDTO;
import com.mr.sa.dto.UserOnlineTimeDTO;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.OrgOnlineTime;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.service.jooq.BizUserOnlineTimeService;
import com.mr.sa.utils.common.MyDateUtil;
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


@Controller
@Transactional(readOnly = true)
public class UserOnlineTimeController extends QueryFilter {

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource
    private BizUserOnlineTimeService bizUserOnlineTimeService;

    @DataProvider
    public void query(Page<UserOnlineTimeVO> page, Map<String, Object> parameter) {
//        Criteria criteria = new Criteria();
//        criteria = condition(criteria, parameter);
        String username = (String) parameter.get("username");
        Object recordDate_startTime = parameter.get("recordDate_startTime");
        Object recordDate_endTime = parameter.get("recordDate_endTime");
        Date startTime = null;
        Date endTime = null;
        if (Objects.nonNull(recordDate_startTime)) {
            startTime = (Date) recordDate_startTime;
        }
        if (Objects.nonNull(recordDate_endTime)) {
            endTime = (Date) recordDate_endTime;
        }
        bizUserOnlineTimeService.queryByPage(username, startTime, endTime, page);
        Collection<UserOnlineTimeVO> entities = page.getEntities();
        entities.forEach(userOnlineTime -> {
            Long onlineSecond = userOnlineTime.getOnlineSecond();
            String time = MyDateUtil.transfer2(onlineSecond);
            userOnlineTime.setTime(time);
        });
    }

    @DataProvider
    public void queryOrg(Page<OrgOnlineTime> page, Map<String, Object> parameter) {
        Object orgId = parameter.get("orgId");
        Object startTime = parameter.get("recordDate_startTime");
        Object endTime = parameter.get("recordDate_endTime");
        String sql = "SELECT t.org_id AS orgId, o.`name` AS orgName, SUM( t.online_second ) AS onlineSecond FROM biz_user_online_time t LEFT JOIN biz_org o ON t.org_id = o.`code` WHERE 1=1 " +
                (Objects.nonNull(orgId) ? " AND t.org_id = " + orgId : "")
                +
                (Objects.nonNull(startTime) ? " AND t.record_date >= '" + MyDateUtil.formatToSecond((Date) startTime) + "'" : "")
                +
                (Objects.nonNull(endTime) ? " AND t.record_date <= '" + MyDateUtil.formatToSecond((Date) endTime) + "'" : "")
                +
                " GROUP BY t.org_id ORDER BY onlineSecond DESC LIMIT " + (page.getPageNo() - 1) * page.getPageSize() + "," + page.getPageSize();

        String sqlcount = "SELECT COUNT(DISTINCT t.org_id) FROM biz_user_online_time t LEFT JOIN biz_org o ON t.org_id = o.`code` WHERE 1=1 " +
                (Objects.nonNull(orgId) ? " AND t.org_id = " + orgId : "")
                +
                (Objects.nonNull(startTime) ? " AND t.record_date >= '" + MyDateUtil.formatToSecond((Date) startTime) + "'" : "")
                +
                (Objects.nonNull(endTime) ? " AND t.record_date <= '" + MyDateUtil.formatToSecond((Date) endTime) + "'" : "");
        List<OrgOnlineTime> list = jdbcTemplate.query(sql, new OrgOnlineTimeMapper());
        Integer integer;
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
            return orgOnlineTime;
        }
    }

    @DataProvider
    public List<UserOnlineTimeDTO> queryDayPersonOnlineTimeList(Map<String, Object> parameter) throws JsonProcessingException {
        String s = redisTemplateHelper.get(BizConstants.REDIS_ONLINE_DURATION_DAILY);
        Map<String, Integer> onlineTime = new HashMap<>();
        if (StringUtils.isNotBlank(s)) {
            onlineTime = JSON.parseObject(s, Map.class);
        }
        Object username = parameter.get("username");
        List<String> userIds = new ArrayList<>();
        if (Objects.nonNull(username)) {
            List<AppUser> appUsers = JpaUtil.linq(AppUser.class)
                    .like("nickname", String.format("%%%s%%", username)).list();
            userIds = appUsers.stream().map(AppUser::getUsername).collect(Collectors.toList());
        }
        List<UserOnlineTimeDTO> userOnlineTimeDTOS = new ArrayList<>();
        onlineTime.entrySet().forEach(stringIntegerEntry -> {
            UserOnlineTimeDTO userOnlineTimeDTO = new UserOnlineTimeDTO();
            userOnlineTimeDTO.setUserId(stringIntegerEntry.getKey());
            userOnlineTimeDTO.setSeconds(Long.valueOf(stringIntegerEntry.getValue()));
            userOnlineTimeDTOS.add(userOnlineTimeDTO);
        });
        List<UserOnlineTimeDTO> userOnlineTimeDTOList;
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<String> finalUserIds = userIds;
            userOnlineTimeDTOList = userOnlineTimeDTOS.stream().filter(userOnlineTimeDTO -> finalUserIds.contains(userOnlineTimeDTO.getUserId())).collect(Collectors.toList());
        } else {
            userOnlineTimeDTOList = userOnlineTimeDTOS;
        }
        List<String> userIdList = userOnlineTimeDTOList.stream().map(UserOnlineTimeDTO::getUserId).collect(Collectors.toList());
        // 查询用户信息
        if (CollectionUtils.isNotEmpty(userIdList)) {
            List<AppUser> appUsers = JpaUtil.linq(AppUser.class).in("username", userIdList).list();
            Map<String, AppUser> userMap = appUsers.stream().collect(Collectors.toMap(AppUser::getUsername, Function.identity()));
            // 查询组织信息
            List<String> orgIds = appUsers.stream().distinct().map(AppUser::getOrgId).collect(Collectors.toList());
            List<Org> orgList = JpaUtil.linq(Org.class).in("code", orgIds).list();
            Map<String, Org> orgMap = orgList.stream().collect(Collectors.toMap(Org::getCode, Function.identity()));
            userOnlineTimeDTOList.stream().forEach(userOnlineTimeDTO -> {
                AppUser appUser = userMap.get(userOnlineTimeDTO.getUserId());
                if (Objects.nonNull(appUser)) {
                    userOnlineTimeDTO.setUsername(appUser.getNickname());
                    userOnlineTimeDTO.setOrgId(appUser.getOrgId());
                    if (Objects.nonNull(appUser.getOrgId())) {
                        Org org = orgMap.get(appUser.getOrgId());
                        if (Objects.nonNull(org)) {
                            userOnlineTimeDTO.setOrgName(org.getName());
                        }
                    }
                }
                Long onlineSecond = userOnlineTimeDTO.getSeconds();
                String time = MyDateUtil.transfer2(onlineSecond);
                userOnlineTimeDTO.setTime(time);
            });
        }
        return userOnlineTimeDTOList.stream().sorted(Comparator.comparing(UserOnlineTimeDTO::getSeconds, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @DataProvider
    public List<OrgOnlineTimeDTO> queryDayOrgOnlineTimeList(Map<String, Object> parameter) throws JsonProcessingException {
        String s = redisTemplateHelper.get(BizConstants.REDIS_ONLINE_DURATION_DAILY);
        Map<String, Integer> onlineTime = new HashMap<>();
        if (StringUtils.isNotBlank(s)) {
            onlineTime = JSON.parseObject(s, Map.class);
        }
        Object orgName = parameter.get("orgName");
        List<String> userIds = new ArrayList<>();
        if (Objects.nonNull(orgName)) {
            List<Org> orgs = JpaUtil.linq(Org.class)
                    .like("name", String.format("%%%s%%", orgName)).list();
            List<String> orgCodes = orgs.stream().distinct().map(Org::getCode).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(orgCodes)) {
                // 查询用户
                List<AppUser> appUsers = JpaUtil.linq(AppUser.class).in("orgId", orgCodes).list();
                userIds = appUsers.stream().map(AppUser::getUsername).collect(Collectors.toList());
            }
        }
        List<UserOnlineTimeDTO> userOnlineTimeDTOS = new ArrayList<>();
        onlineTime.entrySet().forEach(stringIntegerEntry -> {
            UserOnlineTimeDTO userOnlineTimeDTO = new UserOnlineTimeDTO();
            userOnlineTimeDTO.setUserId(stringIntegerEntry.getKey());
            userOnlineTimeDTO.setSeconds(Long.valueOf(stringIntegerEntry.getValue()));
            userOnlineTimeDTOS.add(userOnlineTimeDTO);
        });
        List<UserOnlineTimeDTO> userOnlineTimeDTOList;
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<String> finalUserIds = userIds;
            userOnlineTimeDTOList = userOnlineTimeDTOS.stream().filter(userOnlineTimeDTO -> finalUserIds.contains(userOnlineTimeDTO.getUserId())).collect(Collectors.toList());
        } else {
            userOnlineTimeDTOList = userOnlineTimeDTOS;
        }
        List<String> userIdList = userOnlineTimeDTOList.stream().map(UserOnlineTimeDTO::getUserId).collect(Collectors.toList());
        // 查询用户信息
        if (CollectionUtils.isNotEmpty(userIdList)) {
            List<AppUser> appUsers = JpaUtil.linq(AppUser.class).in("username", userIdList).list();
            Map<String, AppUser> userMap = appUsers.stream().collect(Collectors.toMap(AppUser::getUsername, Function.identity()));
            // 查询组织信息
            List<String> orgIds = appUsers.stream().distinct().map(AppUser::getOrgId).collect(Collectors.toList());
            List<Org> orgList = JpaUtil.linq(Org.class).in("code", orgIds).list();
            Map<String, Org> orgMap = orgList.stream().collect(Collectors.toMap(Org::getCode, Function.identity()));
            userOnlineTimeDTOList.stream().forEach(userOnlineTimeDTO -> {
                AppUser appUser = userMap.get(userOnlineTimeDTO.getUserId());
                if (Objects.nonNull(appUser)) {
                    userOnlineTimeDTO.setUsername(appUser.getNickname());
                    userOnlineTimeDTO.setOrgId(appUser.getOrgId());
                    if (Objects.nonNull(appUser.getOrgId())) {
                        Org org = orgMap.get(appUser.getOrgId());
                        if (Objects.nonNull(org)) {
                            userOnlineTimeDTO.setOrgName(org.getName());
                        }
                    }
                }
            });
        }
        List<OrgOnlineTimeDTO> orgOnlineTimeDTOS = new ArrayList<>();
        Map<String, LongSummaryStatistics> orgOnlineGroupMap = userOnlineTimeDTOList
                .stream()
                .filter(userOnlineTimeDTO -> Objects.nonNull(userOnlineTimeDTO) && StringUtils.isNotBlank(userOnlineTimeDTO.getOrgName()))
                .collect(Collectors.groupingBy(UserOnlineTimeDTO::getOrgName, Collectors.summarizingLong(UserOnlineTimeDTO::getSeconds)));
        orgOnlineGroupMap.entrySet().forEach(entry -> {
            OrgOnlineTimeDTO orgOnlineTimeDTO = new OrgOnlineTimeDTO();
            orgOnlineTimeDTO.setOrgName(entry.getKey());
            orgOnlineTimeDTO.setSeconds(entry.getValue().getSum());
            orgOnlineTimeDTOS.add(orgOnlineTimeDTO);
        });
        orgOnlineTimeDTOS.stream().forEach(orgOnlineTimeDTO -> {
            Long onlineSecond = orgOnlineTimeDTO.getSeconds();
            String time = MyDateUtil.transfer2(onlineSecond);
            orgOnlineTimeDTO.setTime(time);
        });

        return orgOnlineTimeDTOS.stream().sorted(Comparator.comparing(OrgOnlineTimeDTO::getSeconds, Comparator.reverseOrder())).collect(Collectors.toList());
    }
}
