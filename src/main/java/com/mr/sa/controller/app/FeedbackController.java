package com.mr.sa.controller.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.mr.sa.dto.AttachmentFile;
import com.mr.sa.entity.Feedback;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.service.common.QueryFilter;

@Controller
@Transactional(readOnly = true)
public class FeedbackController extends QueryFilter {
	@DataResolver
	@Transactional
	@Log(module = "意见反馈", category = "CRUD")
	public void save(List<Feedback> feedbacks) {
		JpaUtil.save(feedbacks);
	}

	@DataProvider
	public void query(Page<Feedback> page, Map<String, Object> parameter) {
		Criteria criteria = new Criteria();
		criteria = condition(criteria, parameter);
		JpaUtil.linq(Feedback.class)
			.collect("username", AppUser.class, "userId")
			.where(criteria)
			.desc("updateDate")
			.paging(page);
	}

	@DataProvider
	public List<AttachmentFile> getImages(Map<String, Object> parameter) {
		String feedbackId = (String) parameter.get("feedbackId");
		Feedback feedback = JpaUtil.linq(Feedback.class).idEqual(feedbackId).findOne();
		String attachmentFileStr = feedback.getImages();
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
}