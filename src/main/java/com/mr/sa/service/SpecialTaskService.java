package com.mr.sa.service;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.Expose;
import com.mr.sa.config.RedisTemplateHelper;
import com.mr.sa.entity.PatrolPoint;
import com.mr.sa.entity.SpecialTask;
import com.mr.sa.entity.rel.RelPatrolTaskItemUser;
import com.mr.sa.entity.rel.RelSpecialTaskPoint;
import com.mr.sa.entity.rel.RelSpecialTaskUser;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.utils.common.GeTuiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class SpecialTaskService {

    @Autowired
    private RedisTemplateHelper redisTemplate;
    @Autowired
    private GeTuiUtils geTuiUtils;

    // 关系-巡防点
    @Expose
    public void savePoint(Map<String, Object> parameter) {
        String specialTaskId = (String) parameter.get("specialTaskId");
        PatrolPoint patrolPoint = (PatrolPoint) parameter.get("patrolPoint");
        // @formatter:off
        JpaUtil.lind(RelSpecialTaskPoint.class)
                .equal("specialTaskId", specialTaskId)
                .equal("patrolPointId", patrolPoint.getId())
                .delete();
        // @formatter:on
        if (patrolPoint.isChecked()) {
            RelSpecialTaskPoint relSpecialTaskPoint = new RelSpecialTaskPoint();
            relSpecialTaskPoint.setSpecialTaskId(specialTaskId);
            relSpecialTaskPoint.setPatrolPointId(patrolPoint.getId());
            relSpecialTaskPoint.setRequired(patrolPoint.isRequired());
            JpaUtil.persist(relSpecialTaskPoint);
        }
        // @formatter:on

    }

    // 关系-巡防人员
    @Expose
    public void saveUser(Map<String, Object> parameter) {
        String specialTaskId = (String) parameter.get("specialTaskId");
        String username = (String) parameter.get("username");
        boolean isExist=JpaUtil.linq(RelSpecialTaskUser.class)
        		.equal("specialTaskId", specialTaskId)
        		.equal("userId", username)
        		.exists();
        if (!isExist) {
        	 RelSpecialTaskUser relSpecialTaskUser = new RelSpecialTaskUser();
             relSpecialTaskUser.setSpecialTaskId(specialTaskId);
             relSpecialTaskUser.setUserId(username);
             JpaUtil.persist(relSpecialTaskUser);
             //APP消息推送
             SpecialTask specialTask = JpaUtil.getOne(SpecialTask.class, specialTaskId);
             Object object = redisTemplate.hGet(BizConstants.GT_CID_USERID, username);
             if (Objects.nonNull(object)) {
                 geTuiUtils.pushToSingleByCid(object.toString(), "专项任务通知", specialTask.getName());
             }
        }
       
    }

    @Expose
    public void deleteUser(Map<String, Object> parameter) {
        String username = (String) parameter.get("username");
        String specialTaskId = (String) parameter.get("specialTaskId");
        // @formatter:off
        JpaUtil.lind(RelSpecialTaskUser.class)
                .equal("specialTaskId", specialTaskId)
                .equal("userId", username)
                .delete();
        // @formatter:on
    }

}