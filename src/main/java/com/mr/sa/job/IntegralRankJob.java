package com.mr.sa.job;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.mr.sa.entity.Integral;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.app.AppRole;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.utils.common.MyLog;

@Service
public class IntegralRankJob {

	private static Logger log = MyLog.get();

	/**
	 * 
	 * 每5秒执行一次
	 */
	// @Scheduled(cron = "*/5 * * * * ?") // 每5秒执行一次
	// @Scheduled(cron = "0 0 1 * * ?") // 每日1点执行一次
	@Transactional
	public void updateStatus() {
		try {
			// @formatter:off
			List<Integral> integralList = JpaUtil.linq(Integral.class)
				.or()
					.isNull("roleId")
				.end()
				.list();
			// @formatter:on
			for (Integral integral : integralList) {
				String userId=integral.getUserId();
				AppUser appUser=JpaUtil.linq(AppUser.class)
						.equal("username", userId)
						.findOne();
				String roleId=appUser.getRoleId();
				AppRole role=JpaUtil.linq(AppRole.class)
						.idEqual(roleId)
						.findOne();
				String orgId=appUser.getOrgId();
				Org org=JpaUtil.linq(Org.class)
						.equal("code", orgId)
						.findOne();
				integral.setRoleId(roleId);
				integral.setRoleName(role.getName());
				integral.setOrgId(orgId);
				integral.setOrgName(org.getName());
				JpaUtil.merge(integral);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}