package com.mr.sa.job;

import java.util.Date;
import java.util.List;

import com.mr.sa.entity.BizPointTask;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.mr.sa.entity.PatrolTask;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.BizConstants.PATROL_TASK;
import com.mr.sa.utils.common.MyDateUtil;
import com.mr.sa.utils.common.MyLog;

@Service
public class PatrolTaskJob {

	private static Logger log = MyLog.get();

	/**
	 *
	 * 每5秒执行一次
	 */
	@Scheduled(cron = "*/5 * * * * ?") // 每5秒执行一次
	// @Scheduled(cron = "0 0 1 * * ?") // 每日1点执行一次
	@Transactional
	public void updateStatus() {
		try {
			// @formatter:off
			List<PatrolTask> patrolTaskList = JpaUtil.linq(PatrolTask.class)
				.or()
					.notEqual("activeStatus", PATROL_TASK.ACTIVE_STATUS.END)
					.isNull("activeStatus")
				.end()
				.list();
			// @formatter:on
			for (PatrolTask patrolTask : patrolTaskList) {
				Date startDate = patrolTask.getPatrolStartDate();
				Date endDate = patrolTask.getPatrolEndDate();
				Date startTime = MyDateUtil.getCurrStart(startDate);
				Date endTime = MyDateUtil.getCurrEnd(endDate);
				Date now = new Date();
				String status = patrolTask.getActiveStatus();
				// 小于开始时间叫做未开始
				if ((!PATROL_TASK.ACTIVE_STATUS.INACTIVE.equals(status))
						&& now.before(MyDateUtil.getCurrStart(startTime))) {
					patrolTask.setActiveStatus(PATROL_TASK.ACTIVE_STATUS.INACTIVE);
					// log.info("[" + patrolTask.getName() + "]状态更新为[未开始]");
				}
				// 开始时间和结束时间中间叫进行中
				if ((!PATROL_TASK.ACTIVE_STATUS.ING.equals(status))
						&& now.after(MyDateUtil.getCurrStart(startTime))
						&& now.before(MyDateUtil.getCurrEnd(endTime))) {
					patrolTask.setActiveStatus(PATROL_TASK.ACTIVE_STATUS.ING);
					// log.info("[" + patrolTask.getName() + "]状态更新为[进行中]");
				}
				// 大于结束时间叫已结束
				if ((!PATROL_TASK.ACTIVE_STATUS.END.equals(status))
						&& now.after(MyDateUtil.getCurrEnd(endTime))) {
					patrolTask.setActiveStatus(PATROL_TASK.ACTIVE_STATUS.END);
					// log.info("[" + patrolTask.getName() + "]状态更新为[已结束]");
				}
				//JpaUtil.merge(patrolTask);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 *
	 * 每5秒执行一次
	 */
	@Scheduled(cron = "*/5 * * * * ?") // 每5秒执行一次
	// @Scheduled(cron = "0 0 1 * * ?") // 每日1点执行一次
	@Transactional
	public void updatePointTaskStatus() {
		try {
			log.info("updatePointTaskStatus---task");
			// @formatter:off
			List<BizPointTask> patrolTaskList = JpaUtil.linq(BizPointTask.class)
					.or()
					.notEqual("activeStatus", PATROL_TASK.ACTIVE_STATUS.END)
					.isNull("activeStatus")
					.end()
					.list();
			// @formatter:on
			for (BizPointTask patrolTask : patrolTaskList) {
				Date startDate = patrolTask.getPatrolStartDate();
				Date endDate = patrolTask.getPatrolEndDate();
				Date startTime = MyDateUtil.getCurrStart(startDate);
				Date endTime = MyDateUtil.getCurrEnd(endDate);
				Date now = new Date();
				String status = patrolTask.getActiveStatus();
				// 小于开始时间叫做未开始
				if ((!PATROL_TASK.ACTIVE_STATUS.INACTIVE.equals(status))
						&& now.before(MyDateUtil.getCurrStart(startTime))) {
					patrolTask.setActiveStatus(PATROL_TASK.ACTIVE_STATUS.INACTIVE);
					 log.info("[" + patrolTask.getName() + "]状态更新为[未开始]");
				}
				// 开始时间和结束时间中间叫进行中
				if ((!PATROL_TASK.ACTIVE_STATUS.ING.equals(status))
						&& now.after(MyDateUtil.getCurrStart(startTime))
						&& now.before(MyDateUtil.getCurrEnd(endTime))) {
					patrolTask.setActiveStatus(PATROL_TASK.ACTIVE_STATUS.ING);
					 log.info("[" + patrolTask.getName() + "]状态更新为[进行中]");
				}
				// 大于结束时间叫已结束
				if ((!PATROL_TASK.ACTIVE_STATUS.END.equals(status))
						&& now.after(MyDateUtil.getCurrEnd(endTime))) {
					patrolTask.setActiveStatus(PATROL_TASK.ACTIVE_STATUS.END);
					 log.info("[" + patrolTask.getName() + "]状态更新为[已结束]");
				}
				JpaUtil.merge(patrolTask);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
