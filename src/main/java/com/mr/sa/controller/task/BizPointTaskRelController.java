package com.mr.sa.controller.task;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.data.vo.PatrolPointVO;
import com.mr.sa.entity.BizPointTaskItem;
import com.mr.sa.service.jooq.BizPatrolPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@Transactional(readOnly = true)
public class BizPointTaskRelController {

//    @Autowired
//    private JdbcTemplate jdbcTemplate;

    @Resource
    private BizPatrolPointService bizPatrolPointService;

    @DataProvider
    public void query(Page<PatrolPointVO> page,
                      Map<String, Object> parameter) {
//        String pointTaskItemId = (String) parameter.get("pointTaskItemId");
//
//        String code = (String) parameter.get("code");
//        String name = (String) parameter.get("name");
//        String orgId = (String) parameter.get("orgId");
        List<BizPointTaskItem> patrolTaskItemList = (List<BizPointTaskItem>) parameter.get("selectionPatrolTaskItem");

        bizPatrolPointService.page(patrolTaskItemList, parameter, page);
//        String sqlString = "SELECT DISTINCT\n" +
//                "IF\n" +
//                "\t( ISNULL( i.point_id ), FALSE, TRUE ) AS checked,\n" +
//                "\ti.required," +
//                "\tp.* \n" +
//                "FROM\n" +
//                "\tbiz_patrol_point p\n" +
//                "\tLEFT JOIN biz_point_task_item_point i ON p.id = i.point_id \n" +
//                "\tAND i.point_task_item_id = '" + pointTaskItemId + "'" +
//                "\tLEFT JOIN biz_point_task_item_org o ON o.point_task_item_id = i.point_task_item_id \n" +
//                "\tAND i.org_id = '" + orgId + "'" +
//                "\nWHERE\n" +
//                "\t1 = 1 \n" +
//                " AND p.`level` = 1" +
//                (StringUtils.isNotBlank(code) ? " AND p.`code` in (" + code + ")" : "") +
//                (StringUtils.isNotBlank(name) ? (StringUtils.isNotBlank(code) ? "OR":"AND")+" REGEXP_LIKE(`name`, '" + StringUtils.replace(name,",","|") + "')" : "") +
//                "\tOR i.point_id IS NOT NULL\n" +
//                "\tORDER BY\n" +
//                "\ti.point_id DESC ,p.`code` DESC  LIMIT " + (page.getPageNo() - 1) * page.getPageSize() + "," + page.getPageSize();
//        String sqlCount = "SELECT\n" +
//                " COUNT(DISTINCT p.id) " +
//                " FROM\n" +
//                "\tbiz_patrol_point p\n" +
//                "\tLEFT JOIN biz_point_task_item_point i ON p.id = i.point_id \n" +
//                "\tAND i.point_task_item_id = '" + pointTaskItemId + "'" +
//                "\tLEFT JOIN biz_point_task_item_org o ON o.point_task_item_id = i.point_task_item_id \n" +
//                "\tAND i.org_id = '" + orgId + "'" +
//                "\nWHERE\n" +
//                "\t1 = 1 \n" +
//                " AND p.`level` = 1" +
//                (StringUtils.isNotBlank(code) ? " AND p.`code` in (" + code + ")" : "") +
//                (StringUtils.isNotBlank(name) ? (StringUtils.isNotBlank(code) ? "OR":"AND")+" REGEXP_LIKE(`name`, '" + StringUtils.replace(name,",","|") + "')" : "") +
//                "\tOR i.point_id IS NOT NULL\n" ;
//        List<PatrolPoint> list = jdbcTemplate.query(sqlString, new PatralPointMapper());
//        Integer integer;
//        try {
//            integer = jdbcTemplate.queryForObject(sqlCount, Integer.class);
//        } catch (DataAccessException e) {
//            integer = 0;
//        }
//        page.setEntities(list);
//        page.setEntityCount(integer);
    }

//    class PatralPointMapper implements RowMapper<PatrolPoint> {
//
//        @Override
//        public PatrolPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
//            PatrolPoint patrolPoint = new PatrolPoint();
//            patrolPoint.setId(rs.getString("id"));
//            patrolPoint.setChecked(rs.getBoolean("checked"));
//            patrolPoint.setRequired(rs.getBoolean("required"));
//            patrolPoint.setCode(rs.getString("code"));
//            patrolPoint.setName(rs.getString("name"));
//            patrolPoint.setLongitude(rs.getDouble("longitude"));
//            patrolPoint.setLatitude(rs.getDouble("latitude"));
//            return patrolPoint;
//        }
//    }
}
