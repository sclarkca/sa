package com.mr.sa.controller.integral;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.feilong.core.text.MessageFormatUtil;
import com.mr.sa.dto.IntegralRankVo;
import com.mr.sa.entity.Integral;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.jpa.JpaRepo;
import com.mr.sa.utils.page.PageUtil;

@Controller
@Transactional(readOnly = true)
public class IntegralRankController extends QueryFilter {

	@Autowired
	JpaRepo jpaRepo;

	@Autowired
	PageUtil pageUtil;
	
	@DataProvider
	public void query(Page<Integral> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(Integral.class)
				.collect("username", AppUser.class, "userId").where(criteria)
				.desc("integral").paging(page);
	}

	@DataProvider
	public void queryDetail(Page<IntegralRankVo> page,
			Map<String, Object> parameter) {
		String username =pageUtil.getParamAsString(parameter, "username");
		if ( StringUtils.isNotBlank(username)) {
			username = "'%" + username + "%'";
		}
		String orgId = pageUtil.getParamAsString(parameter, "orgId");
		if ( StringUtils.isNotBlank(orgId)) {
			orgId = "'" + orgId + "'";
		}
		String roleId = pageUtil.getParamAsString(parameter, "roleId");
		if ( StringUtils.isNotBlank(roleId)) {
			roleId = "'" + roleId + "'";
		}
		String year = pageUtil.getParamAsString(parameter, "year");
		String quarter = pageUtil.getParamAsString(parameter, "quarter");
		String pattern = "SELECT "
				+ "id.user_id,"
				+ "u.nickname_,"
				+ "u.avatar_,"
				+ "r.name_,"
				+ "o.name,"
				+ "SUM(id.integral)"
				+ " FROM biz_integral_detail id" 
				+ " LEFT JOIN biz_app_user u ON id.user_id = u.username_"
				+ " LEFT JOIN biz_org o ON u.org_id_ = o.code"
				+ " LEFT JOIN biz_app_role r ON u.role_id = r.id_"
				+ " WHERE YEAR(id.created_date)={0}"
				+ (StringUtils.isNotBlank(quarter) ? " AND QUARTER(id.created_date)={1}" : "")
				+ (StringUtils.isNotBlank(username) ? " AND u.nickname_ LIKE {2}" : "")
				+ (StringUtils.isNotBlank(orgId) ? " AND o.code ={3}" : "")
				+ (StringUtils.isNotBlank(roleId) ? " AND r.id_ ={4}" : "")
				+ " GROUP BY "
				+ (StringUtils.isNotBlank(quarter) ? " QUARTER(id.created_date)," : "")
				+ "id.user_id"
				+ " ORDER BY SUM(id.integral) desc ";
		 
		String sql = MessageFormatUtil.format(pattern, year, quarter, username, orgId, roleId);
		//String countSql="SELECT COUNT(*) FROM ("+sql+"	) COUNT";
		List<Object[]> objectList = jpaRepo.getListBySql(sql);
		//Long total=jpaRepo.getLongBySql(countSql);
		List<IntegralRankVo> integralRankVoList = new ArrayList();
		for (Object[] objects : objectList) {
			IntegralRankVo integralRankVo = new IntegralRankVo();
			integralRankVo.setUserId((String) objects[0]);
			integralRankVo.setUsername((String) objects[1]);
			integralRankVo.setAvatar((String) objects[2]);
			integralRankVo.setRoleName((String) objects[3]);
			integralRankVo.setOrgName((String) objects[4]);
			integralRankVo.setIntegral(
					Integer.valueOf(String.valueOf((BigDecimal) objects[5])));
			integralRankVoList.add(integralRankVo);
		}
		
	
		page.setEntities(integralRankVoList);
		//page.setEntityCount(total.intValue());
	}

}