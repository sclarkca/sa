package com.mr.sa.controller.report;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dictionary.domain.Dictionary;
import com.bstek.bdf3.dictionary.domain.DictionaryItem;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.mr.sa.entity.HideEvent;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.PatrolPoint;
import com.mr.sa.entity.app.AppConf;
import com.mr.sa.entity.app.AppUser;
import com.mr.sa.entity.excel.EventExcelTemplate;
import com.mr.sa.entity.excel.EventReaderListener;
import com.mr.sa.entity.excel.SelectSheetWriteHandler;
import com.mr.sa.entity.rel.RelEventPoint;
import com.mr.sa.service.common.BizConstants;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.common.MyDateUtil;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@Transactional(readOnly = true)
public class HideEventController extends QueryFilter {

    @DataResolver
    @Transactional
    @Log(module = "事件", category = "CRUD")
    public void save(List<HideEvent> events) {
        JpaUtil.save(events, new SmartSavePolicyAdapter() {
            @Override
            public boolean beforeUpdate(SaveContext context) {
                HideEvent event = context.getEntity();
                if (EntityState.MODIFIED.equals(EntityUtils.getState(event))) {
                    event.setIntegralTime(new Date());
                }
                return true;
            }

            @Override
            public boolean beforeDelete(SaveContext context) {
                HideEvent event = context.getEntity();
                if (EntityState.DELETED.equals(EntityUtils.getState(event))) {
                    List<RelEventPoint> relEventPoints = JpaUtil.linq(RelEventPoint.class).equal("eventId", event.getId()).list();
                    if (CollectionUtils.isNotEmpty(relEventPoints)) {
                        JpaUtil.remove(relEventPoints);
                    }
                }
                return true;
            }
        });

    }

    @DataProvider
    public void query(Page<HideEvent> page, Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        criteria = condition(criteria, parameter);
        JpaUtil.linq(HideEvent.class)
                .collect("username", AppUser.class, "userId")
                .collect("code", Org.class, "orgId")
                .where(criteria)
                .desc("occurTime")
                .paging(page);
    }

    @FileResolver
    @Transactional
    public String importExcel(UploadFile file, Map<String, Object> parameter) throws IOException {
        log.info("file import");
        Dictionary eventTypeDict = JpaUtil.linq(Dictionary.class).equal("code", "EVENT_TYPE").findOne();
        Dictionary eventLevelDict = JpaUtil.linq(Dictionary.class).equal("code", "EVENT_LEVEL").findOne();
        String eventTypeDictId = eventTypeDict.getId();
        String eventLevelDictId = eventLevelDict.getId();
        List<DictionaryItem> eventTypeDictList = JpaUtil.linq(DictionaryItem.class).equal("dictionaryId", eventTypeDictId).asc("order").list();
        List<DictionaryItem> eventLevelDictList = JpaUtil.linq(DictionaryItem.class).equal("dictionaryId", eventLevelDictId).asc("order").list();
        MultipartFile multipartFile = file.getMultipartFile();
        InputStream inputStream = multipartFile.getInputStream();
        Map<String, String> eventTypeMap = new HashMap<>();
        eventTypeDictList.forEach(dictionaryItem -> {
            eventTypeMap.put(dictionaryItem.getValue(), dictionaryItem.getKey());
        });
        Map<String, String> eventLevelMap = new HashMap<>();
        eventLevelDictList.forEach(dictionaryItem -> {
            eventLevelMap.put(dictionaryItem.getValue(), dictionaryItem.getKey());
        });
        EventReaderListener eventReaderListener = new EventReaderListener(eventTypeMap, eventLevelMap);
        EasyExcel.read(inputStream, EventExcelTemplate.class, eventReaderListener).doReadAll();
        List<HideEvent> cachedDataList = eventReaderListener.getCachedDataList();
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        AppConf appConf = JpaUtil.linq(AppConf.class).findOne();
        double pointRadius = appConf.getPointRadius() * 0.001 / 120.0;
        for (HideEvent e : cachedDataList) {
            Double latitude = e.getLatitude();
            log.info("latitude={}", latitude);
            Double longitude = e.getLongitude();
            if (Objects.isNull(latitude) || Objects.isNull(longitude)) {
                continue;
            }
            log.info("longitude={}", longitude);
            Double minLat = latitude - pointRadius;
            log.info("minLat={}", minLat);
            Double minLng = longitude - pointRadius;
            log.info("minLng={}", minLng);
            Double maxLat = latitude + pointRadius;
            log.info("maxLat={}", maxLat);
            Double maxLng = longitude + pointRadius;
            log.info("maxLng={}", maxLng);
            // 查询point点位表
            List<PatrolPoint> points = JpaUtil.linq(PatrolPoint.class)
                    .ge("latitude", minLat)
                    .ge("longitude", minLng)
                    .le("latitude", maxLat)
                    .le("longitude", maxLng).list();
            log.info("points={}", JSON.toJSON(points));
            if (points.size() == 0) {
                RelEventPoint eventPoint = new RelEventPoint();
                eventPoint.setEventId(e.getId());
                eventPoint.setType(BizConstants.EVENT_TYPE_DARK);
                eventPoint.setCreateTime(new Date());
                JpaUtil.persist(eventPoint);
            } else {
                List<RelEventPoint> relEventPoints = new ArrayList<>();
                for (PatrolPoint point : points) {
                    RelEventPoint eventPoint = new RelEventPoint();
                    eventPoint.setEventId(e.getId());
                    eventPoint.setType(BizConstants.EVENT_TYPE_DARK);
                    eventPoint.setCreateTime(new Date());
                    eventPoint.setPointId(point.getId());
                    relEventPoints.add(eventPoint);
                }
                JpaUtil.persist(relEventPoints);
            }
        }

        JpaUtil.persist(cachedDataList);
        log.info("存储数据库成功！");
        return "success";
    }

    @GetMapping("/event/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("事件导入模板" + MyDateUtil.formatToDay(new Date()), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        Dictionary eventTypeDict = JpaUtil.linq(Dictionary.class).equal("code", "EVENT_TYPE").findOne();
        Dictionary eventLevelDict = JpaUtil.linq(Dictionary.class).equal("code", "EVENT_LEVEL").findOne();
        String eventTypeDictId = eventTypeDict.getId();
        String eventLevelDictId = eventLevelDict.getId();
        List<DictionaryItem> eventTypeDictList = JpaUtil.linq(DictionaryItem.class).equal("dictionaryId", eventTypeDictId).asc("order").list();
        List<DictionaryItem> eventLevelDictList = JpaUtil.linq(DictionaryItem.class).equal("dictionaryId", eventLevelDictId).asc("order").list();
        //k是列,v是下拉的选项
        Map<Integer, List<String>> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eventTypeDictList)) {
            map.put(0, eventTypeDictList.stream().map(DictionaryItem::getValue).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(eventLevelDictList)) {
            map.put(1, eventLevelDictList.stream().map(DictionaryItem::getValue).collect(Collectors.toList()));
        }
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0).head(EventExcelTemplate.class).registerWriteHandler(new SelectSheetWriteHandler(map)).build();
        excelWriter.write(Collections.emptyList(), writeSheet);
        excelWriter.finish();
    }

}