package com.mr.sa.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.Expose;
import com.mr.sa.config.RedisTemplateHelper;
import com.mr.sa.entity.Event;
import com.mr.sa.entity.EventRecord;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.utils.common.GeTuiUtils;

@Service
@Transactional
public class EventRecordService {

	@Autowired
	private RedisTemplateHelper redisTemplate;

	@Autowired
	private GeTuiUtils geTuiUtils;
	// 受理事件
	@Expose
	public void save1(Map<String, Object> parameter) {
		String eventId = (String) parameter.get("eventId");
		Event event = JpaUtil.linq(Event.class).idEqual(eventId).findOne();
		EventRecord eventRecord = new EventRecord();
		eventRecord.setEventId(eventId);
		eventRecord.setCurrentHandler(event.getUserId());
		eventRecord.setCurrentStatus(BizConstants.EVENT.STATUS.UNDO);
		eventRecord.setNextHandler("admin");
		eventRecord.setNextStatus(BizConstants.EVENT.STATUS.READY);
		eventRecord.setNextDesc(event.getDescription());
		JpaUtil.persist(eventRecord);

		event.setStatus(BizConstants.EVENT.STATUS.READY);
		JpaUtil.merge(event);
	}
	
	// 指派人员-检查状态
	@Expose
	public String save2Check(Map<String, Object> parameter) {
		String eventId = (String) parameter.get("eventId");
		String result = "nopass";
		List<EventRecord> eventRecordList = JpaUtil.linq(EventRecord.class)
				.equal("eventId", eventId)
				.desc("createdDate")
				.list();
		try {
			EventRecord eventRecord = eventRecordList.get(0);
			String status = eventRecord.getNextStatus();
			// 待派发,按钮可操作性
			if (BizConstants.EVENT.STATUS.READY.equals(status)) {
				result = "pass";
			}
		} catch (Exception e) {
		}
		return result;
	}

	// 指派人员
	@Expose
	public void save2(Map<String, Object> parameter) {
		String eventId = (String) parameter.get("eventId");
		String userId = (String) parameter.get("userId");
		Event event = JpaUtil.linq(Event.class).idEqual(eventId).findOne();
		EventRecord eventRecord = new EventRecord();
		eventRecord.setEventId(eventId);
		eventRecord.setCurrentHandler(event.getUserId());
		eventRecord.setCurrentStatus(BizConstants.EVENT.STATUS.READY);
		eventRecord.setNextHandler(userId);
		eventRecord.setNextStatus(BizConstants.EVENT.STATUS.ING);
		eventRecord.setNextDesc(event.getDescription());
		JpaUtil.persist(eventRecord);

		AppUser appUser = JpaUtil.linq(AppUser.class)
				.equal("username", userId)
				.findOne();
		event.setUserId(userId);
		event.setUserName(appUser.getNickname());
		event.setStatus(BizConstants.EVENT.STATUS.ING);
		JpaUtil.merge(event);
		Object o = redisTemplate.hGet(BizConstants.GT_CID_USERID, userId);
		if (Objects.nonNull(o)) {
			geTuiUtils.pushToSingleByCid(o.toString(),"事件通知","");
		}
	}

	// 完成事件
	@Expose
	public void save3(Map<String, Object> parameter) {
		String eventId = (String) parameter.get("eventId");
		Event event = JpaUtil.linq(Event.class).idEqual(eventId).findOne();
		EventRecord eventRecord = new EventRecord();
		eventRecord.setEventId(eventId);
		eventRecord.setCurrentHandler(event.getUserId());
		eventRecord.setCurrentStatus(BizConstants.EVENT.STATUS.ING);
		eventRecord.setNextStatus(BizConstants.EVENT.STATUS.DONE);
		eventRecord.setNextDesc(event.getDescription());
		JpaUtil.persist(eventRecord);

		event.setStatus(BizConstants.EVENT.STATUS.DONE);
		JpaUtil.merge(event);
	}
}