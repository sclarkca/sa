package com.mr.sa.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.security.ContextUtils;
import com.bstek.dorado.annotation.Expose;
import com.mr.sa.config.RedisTemplateHelper;
import com.mr.sa.entity.NoticeInfo;
import com.mr.sa.entity.NoticeRead;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.SpecialTask;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.entity.rel.RelNoticeOrg;
import com.mr.sa.entity.rel.RelNoticeSpecialTask;
import com.mr.sa.entity.rel.RelNoticeUser;
import com.mr.sa.entity.rel.RelSpecialTaskUser;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.ThreadSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("saNoticeService")
@Transactional
public class NoticeService {
    @Autowired
    private RedisTemplateHelper redisTemplate;


    @Autowired
    private MessageService messageService;

    // 取消多线程调用
    @Autowired
    private ThreadSaveService threadSaveService;

    // 关系-人员
    @Expose
    public void saveUser(Map<String, Object> parameter) {
        String noticeId = (String) parameter.get("noticeId");
        List<AppUser> appUserList = (List<AppUser>) parameter.get("dsUser");

        List<String> userIdsDelete = new ArrayList();
        for (AppUser appUser : appUserList) {
            userIdsDelete.add(appUser.getUsername());
        }
        if (CollectionUtil.isNotEmpty(userIdsDelete)) {
            log.info("删除公告-用户关系-----start");

            //删除公告-用户关系
            JpaUtil.lind(RelNoticeUser.class)
                    .equal("noticeId", noticeId)
                    .in("userId", userIdsDelete)
                    .delete();
            log.info("删除公告-用户关系------end");
//            log.info("删除公告-已读记录-----start");
            //删除公告已读记录
//            JpaUtil.lind(NoticeRead.class)
//                    .equal("noticeId", noticeId)
//                    .in("userId", userIdsDelete)
//                    .isNull("orgId")
//                    .isNull("specialTaskId")
//                    .delete();
//            log.info("删除公告-已读记录-----end");
        }
        batchSave(noticeId, appUserList);

        // 消息发送
        NoticeInfo noticeInfo = JpaUtil.getOne(NoticeInfo.class,
                noticeId);
        List<String> userIds = appUserList.parallelStream().map(AppUser::getUsername).collect(Collectors.toList());
        List<String> cids = new ArrayList<>();
        userIds.parallelStream().forEach(s -> {
            Object o = redisTemplate.hGet(BizConstants.GT_CID_USERID, s);
            if (Objects.nonNull(o)) {
                cids.add(o.toString());
            }
        });
        log.info("用户cids:{}", JSON.toJSONString(cids));
        messageService.sendMessage(noticeInfo, cids);
    }

    // 关系-人员-全员发送
    @Expose
    public void saveUserAll(Map<String, Object> parameter) {
        String noticeId = (String) parameter.get("noticeId");
        List<Org> orgs = JpaUtil.findAll(Org.class);
        List<String> orgCodes = orgs.stream().map(Org::getCode).collect(Collectors.toList());
        List<AppUser> appUserList = JpaUtil.linq(AppUser.class).in("orgId",orgCodes).list();
        List<String> userIdsDelete = new ArrayList();
        for (AppUser appUser : appUserList) {
            userIdsDelete.add(appUser.getUsername());
        }
        if (CollectionUtil.isNotEmpty(userIdsDelete)) {
            log.info("删除公告-用户关系-----start");
            // 删除公告-用户关系
            JpaUtil.lind(RelNoticeUser.class).equal("noticeId", noticeId)
                    .in("userId", userIdsDelete)
                    .delete();
            log.info("删除公告-用户关系------end");

//            log.info("删除公告-已读记录-----start");
            // 删除公告已读记录
//            JpaUtil.lind(NoticeRead.class).equal("noticeId", noticeId)
//                    .in("userId", userIdsDelete)
//                    .isNull("orgId")
//                    .isNull("specialTaskId")
//                    .delete();
//            log.info("删除公告-已读记录-----end");
        }

        batchSave(noticeId, appUserList);
        // 消息发送
        NoticeInfo noticeInfo = JpaUtil.getOne(NoticeInfo.class, noticeId);
        List<String> userIds = appUserList.parallelStream()
                .map(AppUser::getUsername).collect(Collectors.toList());
        List<String> cids = new ArrayList<>();
        userIds.parallelStream().forEach(s -> {
            Object o = redisTemplate.hGet(BizConstants.GT_CID_USERID, s);
            if (Objects.nonNull(o)) {
                cids.add(o.toString());
            }
        });
        log.info("用户cids:{}", JSON.toJSONString(cids));
        messageService.sendMessage(noticeInfo, cids);
    }

    /**
     * 批量保存
     */
    private void batchSave(String noticeId, List<AppUser> appUserList) {
        String operator = ContextUtils.getLoginUsername();
        Date timeStamp = new Date();

        log.info("创建公告-用户关系-入库-----start");
        log.info("创建公告-已读记录-入库-----start");
        List<String> userIds = appUserList.stream().map(AppUser::getUsername).collect(Collectors.toList());
        // 保存-创建公告-用户关系-入库
        threadSaveService.saveNoticeUser(noticeId,
                userIds, operator, timeStamp);
        // 保存-创建公告-已读记录-入库
        List<NoticeRead> noticeReads = JpaUtil.linq(NoticeRead.class).equal("noticeId", noticeId).list();
        if (CollectionUtil.isNotEmpty(noticeReads)) {
            List<String> hasUserIds = noticeReads.stream().map(NoticeRead::getUserId).collect(Collectors.toList());
            //取差集
            userIds = userIds.stream().filter(item -> !hasUserIds.contains(item)).collect(Collectors.toList());
        }
        if (CollectionUtil.isNotEmpty(userIds)) {
            threadSaveService.saveNoticeRead(noticeId,
                    userIds, operator, timeStamp);
        }
        log.info("创建公告-用户关系-入库-----end");
        log.info("创建公告-已读记录-入库-----end");
    }

    // 保存公告-组织关系
    @Expose
    public void saveOrg(Map<String, Object> parameter) {
        String noticeId = (String) parameter.get("noticeId");
        String orgId = (String) parameter.get("orgId");

        //更新公告的全选组织
        NoticeInfo noticeInfo = JpaUtil.getOne(NoticeInfo.class, noticeId);
        JpaUtil.merge(noticeInfo);

        RelNoticeOrg relNoticeOrg = new RelNoticeOrg();
        relNoticeOrg.setNoticeId(noticeId);
        relNoticeOrg.setOrgId(orgId);
        JpaUtil.persist(relNoticeOrg);


        //更新公告已读记录
        List<AppUser> appUserList = JpaUtil.linq(AppUser.class)
                .equal("orgId", orgId)
                .list();
        boolean existsNoticeOrg = JpaUtil.linq(NoticeRead.class)
                .equal("noticeId", noticeId)
                .equal("orgId", orgId)
                .exists();
        List<String> userIds = appUserList.stream().map(AppUser::getUsername).collect(Collectors.toList());
        if (!existsNoticeOrg && CollectionUtil.isNotEmpty(userIds)) {
            List<NoticeRead> list = JpaUtil.linq(NoticeRead.class)
                    .equal("noticeId", noticeId)
                    .in("userId", userIds)
                    .list();
            //取差集
            List<String> readUserList = list.stream().map(NoticeRead::getUserId).collect(Collectors.toList());
            List<String> needSaveUserIds = userIds.stream().filter(userId -> !readUserList.contains(userId)).collect(Collectors.toList());
            List<NoticeRead> needSaveNoticeReads = needSaveUserIds.stream().map(s -> {
                NoticeRead noticeRead = new NoticeRead();
                noticeRead.setNoticeId(noticeId);
                noticeRead.setUserId(s);
                noticeRead.setOrgId(orgId);
                noticeRead.setRead(false);
                return noticeRead;
            }).collect(Collectors.toList());
            JpaUtil.persist(needSaveNoticeReads);
            log.info("已存在的NoticeRead:{}", JSON.toJSONString(readUserList));
            log.info("需要发送消息的用户needSaveUserIds:{}", JSON.toJSONString(needSaveUserIds));
            // 消息发送
            List<String> cids = new ArrayList<>();
            needSaveUserIds.parallelStream().forEach(s -> {
                Object o = redisTemplate.hGet(BizConstants.GT_CID_USERID, s);
                if (Objects.nonNull(o)) {
                    cids.add(o.toString());
                }
            });
            log.info("用户cids:{}", JSON.toJSONString(cids));
            messageService.sendMessage(noticeInfo, cids);
        }

    }

    @Expose
    public void deleteOrg(Map<String, Object> parameter) {
        String noticeId = (String) parameter.get("noticeId");
        String orgId = (String) parameter.get("orgId");
        // @formatter:off
        JpaUtil.lind(RelNoticeOrg.class)
                .equal("noticeId", noticeId)
                .equal("orgId", orgId)
                .delete();

        //删除公告已读记录
        JpaUtil.lind(NoticeRead.class)
                .equal("noticeId", noticeId)
                .equal("orgId", orgId)
                .delete();
        // @formatter:on
    }


    // 保存公告-组织关系-全选
    @Expose
    public void saveOrgAll(Map<String, Object> parameter) {
        String noticeId = (String) parameter.get("noticeId");
        List<Org> orgs = JpaUtil.linq(Org.class).list();
        //更新公告的全选组织
        NoticeInfo noticeInfo = JpaUtil.getOne(NoticeInfo.class, noticeId);
        //更新公告已读记录
        List<AppUser> allUsers = new ArrayList<>();
        List<RelNoticeOrg> relNoticeOrgs = new ArrayList<>();
        List<NoticeRead> noticeReads = new ArrayList<>();
        for (Org org : orgs) {
            String orgId = org.getCode();
            RelNoticeOrg relNoticeOrg = new RelNoticeOrg();
            relNoticeOrg.setNoticeId(noticeId);
            relNoticeOrg.setOrgId(orgId);
            relNoticeOrgs.add(relNoticeOrg);
            //更新公告已读记录
            List<AppUser> appUserList = JpaUtil.linq(AppUser.class)
                    .equal("orgId", orgId)
                    .list();
            allUsers.addAll(appUserList);
            for (AppUser appUser : appUserList) {
                boolean exists = JpaUtil.linq(NoticeRead.class)
                        .equal("noticeId", noticeId)
                        .equal("orgId", orgId)
                        .exists();
                if (!exists) {
                    NoticeRead noticeRead = new NoticeRead();
                    noticeRead.setNoticeId(noticeId);
                    noticeRead.setUserId(appUser.getUsername());
                    noticeRead.setOrgId(orgId);
                    noticeRead.setRead(false);
                    noticeReads.add(noticeRead);
                }
            }

        }
        JpaUtil.persist(relNoticeOrgs);
        JpaUtil.persist(noticeReads);
        // 消息发送
        List<String> userIds = allUsers.parallelStream().map(AppUser::getUsername).collect(Collectors.toList());
        List<String> cids = new ArrayList<>();
        userIds.parallelStream().forEach(s -> {
            Object o = redisTemplate.hGet(BizConstants.GT_CID_USERID, s);
            if (Objects.nonNull(o)) {
                cids.add(o.toString());
            }
        });
        log.info("用户cids:{}", JSON.toJSONString(cids));
        messageService.sendMessage(noticeInfo, cids);

    }


    // 关系-专项任务
    @Expose
    public void saveSpecialTask(Map<String, Object> parameter) {
        String noticeId = (String) parameter.get("noticeId");
        List<SpecialTask> specialTaskList = (List<SpecialTask>) parameter
                .get("dsSpecialTask");
        if (CollectionUtil.isEmpty(specialTaskList)) {
            return;
        }
        for (SpecialTask specialTask : specialTaskList) {
            if (!specialTask.isChecked()) {
                String specialTaskId = specialTask.getId();
                // @formatter:off
                JpaUtil.lind(RelNoticeSpecialTask.class)
                        .equal("noticeId", noticeId)
                        .equal("specialTaskId", specialTaskId)
                        .delete();

                //删除公告已读记录
                JpaUtil.lind(NoticeRead.class)
                        .equal("noticeId", noticeId)
                        .equal("specialTaskId", specialTaskId)
                        .delete();
                // @formatter:on
            }
        }
        Set<String> userIds = new HashSet<>();
        for (SpecialTask specialTask : specialTaskList) {
            String specialTaskId = specialTask.getId();
            boolean isExist = JpaUtil.linq(RelNoticeSpecialTask.class)
                    .equal("noticeId", noticeId)
                    .equal("specialTaskId", specialTaskId).exists();
            // 勾选&&不存在
            if (specialTask.isChecked() && !isExist) {
                RelNoticeSpecialTask relNoticeSpecialTask = new RelNoticeSpecialTask();
                relNoticeSpecialTask.setNoticeId(noticeId);
                relNoticeSpecialTask.setSpecialTaskId(specialTaskId);
                JpaUtil.persist(relNoticeSpecialTask);

                // 更新公告已读记录
                List<RelSpecialTaskUser> relSpecialTaskUserList = JpaUtil
                        .linq(RelSpecialTaskUser.class)
                        .equal("specialTaskId", specialTaskId).list();

                for (RelSpecialTaskUser relSpecialTaskUser : relSpecialTaskUserList) {
                    boolean exists = JpaUtil.linq(NoticeRead.class)
                            .equal("noticeId", noticeId)
                            .equal("userId", relSpecialTaskUser.getUserId())
                            .exists();
                    if (!exists) {
                        NoticeRead noticeRead = new NoticeRead();
                        noticeRead.setNoticeId(noticeId);
                        noticeRead.setUserId(relSpecialTaskUser.getUserId());
                        noticeRead.setSpecialTaskId(specialTaskId);
                        noticeRead.setRead(false);
                        JpaUtil.persist(noticeRead);
                    }
                }
                // APP消息推送
                Set<String> usernameList = relSpecialTaskUserList
                        .parallelStream().map(RelSpecialTaskUser::getUserId)
                        .collect(Collectors.toSet());
                userIds.addAll(usernameList);

            }

        }
        // 消息发送
        NoticeInfo noticeInfo = JpaUtil.getOne(NoticeInfo.class,
                noticeId);
        List<String> cids = new ArrayList<>();
        userIds.parallelStream().forEach(s -> {
            Object o = redisTemplate.hGet(BizConstants.GT_CID_USERID, s);
            if (Objects.nonNull(o)) {
                cids.add(o.toString());
            }
        });
        log.info("用户cids:{}", JSON.toJSONString(cids));
        messageService.sendMessage(noticeInfo, cids);
    }

}