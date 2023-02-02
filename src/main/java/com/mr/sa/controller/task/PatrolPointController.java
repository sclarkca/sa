package com.mr.sa.controller.task;

import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.google.zxing.WriterException;
import com.mr.sa.dto.PatrolPointDto;
import com.mr.sa.entity.Org;
import com.mr.sa.entity.PatrolPoint;
import com.mr.sa.service.common.QueryFilter;
import com.mr.sa.utils.common.MyStringUtil;
import com.mr.sa.utils.oss.MinioUtil;
import com.mr.sa.utils.qrcode.QRCodeUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@Transactional(readOnly = true)
public class PatrolPointController extends QueryFilter {
    @Autowired
    private MinioUtil minioUtil;

    @DataResolver
    @Transactional
    @Log(module = "巡防点", category = "CRUD")
    public void save(List<PatrolPoint> patrolPoints) {
        JpaUtil.save(patrolPoints);
    }

    @DataProvider
    public void query(Page<PatrolPoint> page, Map<String, Object> parameter) {
        Criteria criteria = new Criteria();
        criteria = condition(criteria, parameter);
        // @formatter:off
        JpaUtil.linq(PatrolPoint.class)
                .where(criteria)
                .desc("updateDate")
                .paging(page);
        // @formatter:on
    }

    @Expose
    @Transactional
    public String getQrcodeImage(String patrolPointId) {
        String imageUrl = "";
        try {
            PatrolPoint patrolPoint = JpaUtil.getOne(PatrolPoint.class,patrolPointId);

            PatrolPointDto patrolPointDto = new PatrolPointDto();
            BeanUtils.copyProperties(patrolPoint, patrolPointDto);
            String json = JSON.toJSONString(patrolPointDto);
            File f = new File(MyStringUtil.getUUID());
            QRCodeUtil.generateQRCodeImage(json, 350, 350, f.getPath());
            imageUrl = minioUtil.uploadFile("image/patrolPoint", f,
                    "patrolPoint.png");
            FileUtils.forceDelete(f);
            patrolPoint.setQrcodeUrl(imageUrl);
            JpaUtil.merge(patrolPoint);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUrl;
    }


}