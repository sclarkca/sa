package com.mr.sa.service;

import java.util.Date;
import java.util.List;

import com.mr.sa.service.common.ThreadSaveService;
import com.mr.sa.utils.page.SpringContextUtils;

/**
 * 创建公告-已读记录-入库
 */
public class NoticeSaveUserReadThread extends Thread {

	private String noticeId;
	private List<String> userIds;
	private String operator;
	private Date timeStamp;

	public NoticeSaveUserReadThread(String noticeId, List<String> userIds, String operator, Date timeStamp) {
		super();
		this.noticeId = noticeId;
		this.userIds = userIds;
		this.operator = operator;
		this.timeStamp = timeStamp;
	}

	public void run() {
		ThreadSaveService treadSaveService = SpringContextUtils.getBean("threadSaveServiceInitiator");
		treadSaveService.saveNoticeRead(noticeId, userIds, operator, timeStamp);
	}

}