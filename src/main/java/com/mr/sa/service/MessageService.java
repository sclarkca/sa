package com.mr.sa.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mr.sa.entity.NoticeInfo;
import com.mr.sa.utils.common.GeTuiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: lxp
 **/
@Service
@Slf4j
public class MessageService {

    @Autowired
    private GeTuiUtils geTuiUtils;

    @Async
    public void sendMessage(NoticeInfo noticeInfo, List<String> cids) {
        List<String> collect = cids.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
        log.info("异步消息推送-开始:notice={},cidList={}", noticeInfo.getName(), JSON.toJSONString(collect));
        if (CollectionUtil.isNotEmpty(collect)) {
            geTuiUtils.pushListByCid(collect, "消息通知",
                    noticeInfo.getName());
        }
        log.info("异步消息推送-结束");
    }

}
