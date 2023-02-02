package com.mr.sa.service.common;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.mr.sa.utils.jpa.JpaRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.mr.sa.entity.NoticeRead;
import com.mr.sa.entity.rel.RelNoticeUser;

/**
 * 多线程-入库
 */
@Slf4j
@Service
public class ThreadSaveService {

    @Transactional
    public void saveNoticeUser(String noticeId, List<String> userIds, String operator, Date timeStamp) {
        List<RelNoticeUser> relNoticeUsers = userIds.stream().map(s -> {
            RelNoticeUser relNoticeUser = new RelNoticeUser();
            relNoticeUser.setNoticeId(noticeId);
            relNoticeUser.setUserId(s);
            relNoticeUser.setCreator(operator);
            relNoticeUser.setCreatedDate(new Date());
            return relNoticeUser;
        }).collect(Collectors.toList());
        JpaUtil.persist(relNoticeUsers);
    }

    @Transactional
    public void saveNoticeRead(String noticeId, List<String> userIds, String operator, Date timeStamp) {
        List<NoticeRead> noticeReads = userIds.stream().map(s -> {
            NoticeRead noticeRead = new NoticeRead();
            noticeRead.setNoticeId(noticeId);
            noticeRead.setUserId(s);
            noticeRead.setCreator(operator);
            noticeRead.setCreatedDate(timeStamp);
            return noticeRead;
        }).collect(Collectors.toList());
        JpaUtil.persist(noticeReads);

    }
}