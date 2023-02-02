package com.mr.sa.entity.excel;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.mr.sa.entity.HideEvent;
import com.mr.sa.service.common.BizConstants.EVENT;
import com.mr.sa.utils.Coordinate;
import com.mr.sa.utils.common.LocalUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Data
public class EventReaderListener extends AnalysisEventListener<EventExcelTemplate> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    public List<HideEvent> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    Map<String, String> eventTypeMap = new HashMap<>();
    Map<String, String> eventLevelMap = new HashMap<>();
    Map<String, String> map = new HashMap<>();

    public EventReaderListener(Map<String, String> eventTypeMap, Map<String, String> eventLevelMap) {
        this.eventTypeMap = eventTypeMap;
        this.eventLevelMap = eventLevelMap;
    }

    @Override
    public void invoke(EventExcelTemplate data, AnalysisContext context) {
        //导入数据校验
        String eventType = data.getEventType();
        if (StringUtils.isBlank(eventType)) {
            map.put("code", "400");
            map.put("msg", "事件类型不能为空");
            return;
        }
        String type = eventTypeMap.get(eventType);
        if (StringUtils.isBlank(type)) {
            map.put("code", "400");
            map.put("msg", "事件类型不存在");
            return;
        }
        String eventLevel = data.getEventLevel();
        if (StringUtils.isBlank(eventLevel)) {
            map.put("code", "400");
            map.put("msg", "事件级别不能为空");
            return;
        }
        String level = eventLevelMap.get(eventLevel);
        if (StringUtils.isBlank(level)) {
            map.put("code", "400");
            map.put("msg", "事件级别不存在");
            return;
        }
        Date time = data.getTime();
        if (Objects.isNull(time)) {
            map.put("code", "400");
            map.put("msg", "发生时间不能为空");
            return;
        }
        String desc = data.getDesc();
        String address = data.getAddress();
        if (StringUtils.isBlank(address)) {
            map.put("code", "400");
            map.put("msg", "发生地点不能为空");
            return;
        }
        String user = data.getUser();
        if (StringUtils.isBlank(user)) {
            map.put("code", "400");
            map.put("msg", "上报人不能为空");
            return;
        }
        HideEvent hideEvent = new HideEvent();
        hideEvent.setId(IdUtil.fastSimpleUUID());
        hideEvent.setUserName(user);
        hideEvent.setType(type);
        hideEvent.setLevel(level);
        hideEvent.setDescription(desc);
        hideEvent.setAddress(address);
        hideEvent.setOccurTime(time);
        hideEvent.setStatus(EVENT.STATUS.DONE);
        Coordinate coordinate = LocalUtils.getCoordinateByLocalAddress(address);
        if (Objects.nonNull(coordinate)) {
            hideEvent.setLatitude(coordinate.getLatitude());
            hideEvent.setLongitude(coordinate.getLongitude());
        }
        cachedDataList.add(hideEvent);
        if (cachedDataList.size() >= BATCH_COUNT) {
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
