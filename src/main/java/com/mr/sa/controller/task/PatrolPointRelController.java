package com.mr.sa.controller.task;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.entity.PatrolPoint;
import com.mr.sa.entity.PatrolTaskItem;
import com.mr.sa.entity.rel.RelPatrolTaskItemPoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@Transactional(readOnly = true)
public class PatrolPointRelController {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @DataProvider
    public List<PatrolPoint> queryAll(Page<PatrolPoint> page,
                                      Map<String, Object> parameter) {

        List<PatrolTaskItem> patrolTaskItemList = (List<PatrolTaskItem>) parameter.get("selectionPatrolTaskItem");
        parameter.remove("selectionPatrolTaskItem");
        Criteria criteria = new Criteria();
//        criteria = condition(criteria, parameter);
        JpaUtil.linq(PatrolPoint.class)
                .where(criteria)
                .desc("updateDate")
                .paging(page);
        List<PatrolPoint> patrolPointList = (List<PatrolPoint>) page
                .getEntities();

        // String orgId = (String) parameter.get("orgId");
        // @formatter:off
        List<String> patrolPointIds = new ArrayList();
        for (PatrolPoint patrolPoint : patrolPointList) {
            patrolPointIds.add(patrolPoint.getId());
        }
        for (PatrolTaskItem record : patrolTaskItemList) {
            // 加checked
            List<RelPatrolTaskItemPoint> relPatrolTaskItemPointList = JpaUtil.linq(RelPatrolTaskItemPoint.class)
                    .equal("patrolTaskItemId", record.getId())
                    .addIf(CollectionUtils.isNotEmpty(patrolPointIds))
                    .in("patrolPointId", patrolPointIds)
                    .endIf()
                    .addIf(CollectionUtils.isEmpty(patrolPointIds))
                    .equal("patrolPointId", "xxxxxx")
                    .endIf()
                    .list();
            List<RelPatrolTaskItemPoint> relPatrolTaskItemPointList2 = JpaUtil.linq(RelPatrolTaskItemPoint.class)
                    .equal("patrolTaskItemId", record.getId())
                    .addIf(CollectionUtils.isNotEmpty(patrolPointIds))
                    .in("patrolPointId", patrolPointIds)
                    .endIf()
                    .addIf(CollectionUtils.isEmpty(patrolPointIds))
                    .equal("patrolPointId", "xxxxxx")
                    .endIf()
                    .isTrue("required")
                    .list();
            // @formatter:on
            for (PatrolPoint patrolPoint : patrolPointList) {
                boolean isExist = relPatrolTaskItemPointList.stream()
                        .anyMatch(m -> m.getPatrolPointId().equals(patrolPoint.getId()));
                patrolPoint.setChecked(isExist);
                boolean isRequired = relPatrolTaskItemPointList2.stream()
                        .anyMatch(m -> m.getPatrolPointId().equals(patrolPoint.getId()));
                patrolPoint.setRequired(isRequired);
            }
        }


        return patrolPointList;
    }

    @DataProvider
    public void query(Page<PatrolPoint> page,
                      Map<String, Object> parameter) {
        String patrolTaskItemId = (String) parameter.get("patrolTaskItemId");
        String code = (String) parameter.get("code");
        String name = (String) parameter.get("name");
        String orgId = (String) parameter.get("orgId");

        String sqlString = "SELECT DISTINCT\n" +
                "IF\n" +
                "\t( ISNULL( i.patrol_point_id ), FALSE, TRUE ) AS checked,\n" +
                "\ti.required," +
                "\tp.* \n" +
                "FROM\n" +
                "\tbiz_patrol_point p\n" +
                "\tLEFT JOIN biz_rel_patrol_task_item_point i ON p.id = i.patrol_point_id \n" +
                "\tAND i.patrol_task_item_id = '" + patrolTaskItemId + "'" +
                "\tLEFT JOIN biz_rel_patrol_task_item_org o ON o.patrol_task_item_id = i.patrol_task_item_id \n" +
                "\tAND i.org_id = '" + orgId + "'" +
                "\nWHERE\n" +
                "\t1 = 1 \n" +
                (StringUtils.isNotBlank(code) ? " AND p.`code` in (" + code + ")" : "") +
                (StringUtils.isNotBlank(name) ? (StringUtils.isNotBlank(code) ? "OR":"AND")+" REGEXP_LIKE(`name`, '" + StringUtils.replace(name,",","|") + "')" : "") +
                "\tOR i.patrol_point_id IS NOT NULL\n" +
                "\tORDER BY\n" +
                "\ti.patrol_point_id DESC ,p.`code` DESC  LIMIT " + (page.getPageNo() - 1) * page.getPageSize() + "," + page.getPageSize();
        String sqlCount = "SELECT\n" +
                " COUNT(DISTINCT p.id) " +
                " FROM\n" +
                "\tbiz_patrol_point p\n" +
                "\tLEFT JOIN biz_rel_patrol_task_item_point i ON p.id = i.patrol_point_id \n" +
                "\tAND i.patrol_task_item_id = '" + patrolTaskItemId + "'" +
                "\tLEFT JOIN biz_rel_patrol_task_item_org o ON o.patrol_task_item_id = i.patrol_task_item_id \n" +
                "\tAND i.org_id = '" + orgId + "'" +
                "\nWHERE\n" +
                "\t1 = 1 \n" +
                (StringUtils.isNotBlank(code) ? " AND p.`code` in (" + code + ")" : "") +
                (StringUtils.isNotBlank(name) ? (StringUtils.isNotBlank(code) ? "OR":"AND")+" REGEXP_LIKE(`name`, '" + StringUtils.replace(name,",","|") + "')" : "") +
                "\tOR i.patrol_point_id IS NOT NULL\n" ;
        List<PatrolPoint> list = jdbcTemplate.query(sqlString, new PatralPointMapper());
        Integer integer;
        try {
            integer = jdbcTemplate.queryForObject(sqlCount, Integer.class);
        } catch (DataAccessException e) {
            integer = 0;
        }
        page.setEntities(list);
        page.setEntityCount(integer);
    }

    class PatralPointMapper implements RowMapper<PatrolPoint> {

        @Override
        public PatrolPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
            PatrolPoint patrolPoint = new PatrolPoint();
            patrolPoint.setId(rs.getString("id"));
            patrolPoint.setChecked(rs.getBoolean("checked"));
            patrolPoint.setRequired(rs.getBoolean("required"));
            patrolPoint.setCode(rs.getString("code"));
            patrolPoint.setName(rs.getString("name"));
            patrolPoint.setLongitude(rs.getDouble("longitude"));
            patrolPoint.setLatitude(rs.getDouble("latitude"));
            return patrolPoint;
        }
    }
}
