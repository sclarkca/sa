package com.mr.sa.controller.report;


import com.alibaba.fastjson.JSONObject;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.dto.AttachmentFile;
import com.mr.sa.entity.Event;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.jpa.JpaRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@Transactional(readOnly = true)
@Slf4j
public class EventController extends QueryFilter {

	@Value("${rest.api.integral-event}")
	private String integralEventUrl;
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JpaRepo jpaRepo;
	@DataResolver
	@Transactional
	@Log(module = "事件", category = "CRUD")
	public void save(List<Event> events) {
		JpaUtil.save(events, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeUpdate(SaveContext context) {
				Event event = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(event))) {
					event.setIntegralTime(new Date());
				}
				try {
					Event oldEvent = JpaUtil.getOne(Event.class, event.getId());
					if (event.isIntegral() != oldEvent.isIntegral()) {
						restNotice(event.getId());
					}
				} catch (Exception e) {
					log.error("事件积分通知失败：{}",e.getMessage());
				}
				return true;
			}
		});

	}

	/**
	 * 事件积分通知
	 * @param eventId
	 */
	public void restNotice(String eventId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("appToken","QViIYUZ1RZK0x2FTiOcuYSUXh4AhpjDt");
		JSONObject param = new JSONObject();
		param.put("eventId", eventId);
		HttpEntity<Object> formEntity = new HttpEntity<>(param,headers);
		JSONObject result = restTemplate.postForObject(integralEventUrl, formEntity, JSONObject.class);
		log.info("事件通知rest接口返回：{}",result);
	}

	@DataProvider
	public void query(Page<Event> page, Map<String, Object> parameter) {
		Object orgId_eq = parameter.get("orgId_eq");
		String sql = "WITH RECURSIVE td AS (\n" +
				"    SELECT biz_org.`code`,biz_org.id,biz_org.parent_id FROM biz_org\tWHERE `code` = '"+orgId_eq+"'\n" +
				"    UNION ALL\n" +
				"    SELECT c.`code`,c.id,c.parent_id FROM biz_org c ,td WHERE c.parent_id = td.id\n" +
				") SELECT td.`code` FROM td";
		parameter.remove("orgId_eq");
		List<Object[]> orgCodes = jpaRepo.getListBySql(sql);
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(Event.class)
			.collect("username", AppUser.class, "userId")
			.collect("code", Org.class, "orgId")
			.where(criteria)
			.addIf(CollectionUtils.isNotEmpty(orgCodes))
				.in("orgId",orgCodes)
			.endIf()
			.addIf(CollectionUtils.isEmpty(orgCodes))
				.equal("orgId","xxxxxx")
			.endIf()
			.desc("occurTime")
			.paging(page);
	}

	@DataProvider
	public List<AttachmentFile> getImages(Map<String, Object> parameter) {
		String eventId = (String) parameter.get("eventId");
		Event event = JpaUtil.linq(Event.class).idEqual(eventId).findOne();
		String attachmentFileStr = event.getImages();
		String[] attachmentFileArray = StringUtils.split(attachmentFileStr, ",");
		List<AttachmentFile> attachmentFileList = new ArrayList();
		try {
			for (String image : attachmentFileArray) {
				AttachmentFile attachmentFile = new AttachmentFile();
				attachmentFile.setUploadUrl(image);
				attachmentFileList.add(attachmentFile);
			}
		} catch (Exception e) {
		}
		return attachmentFileList;
	}
	 
	@DataProvider
	public List<AttachmentFile> getVideos(Map<String, Object> parameter) {
		String eventId = (String) parameter.get("eventId");
		Event event = JpaUtil.linq(Event.class).idEqual(eventId).findOne();
		String attachmentFileStr = event.getVideos();
		String[] attachmentFileArray = StringUtils.split(attachmentFileStr, ",");
		List<AttachmentFile> attachmentFileList = new ArrayList();
		try {
			for (String image : attachmentFileArray) {
				AttachmentFile attachmentFile = new AttachmentFile();
				attachmentFile.setUploadUrl(image);
				attachmentFileList.add(attachmentFile);
			}
		} catch (Exception e) {
		}
		return attachmentFileList;
	}
	
	/**
	 * 查询未处理消息数量接口
	 */
	@Expose
	public String queryUnhandleMsgCount(Map<String, Object> parameter) {
		// @formatter:off
		Long count=	JpaUtil.linq(Event.class)
				.or()
					.equal("status", BizConstants.EVENT.STATUS.UNDO)
					.equal("status", BizConstants.EVENT.STATUS.READY)
				.end()
				.count();
		// @formatter:on
		return String.valueOf(count);
	}
}