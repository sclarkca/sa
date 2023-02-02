package com.mr.sa.service.common;

import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.mr.sa.dto.AttachmentFile;
import com.mr.sa.utils.common.MyLog;
import com.mr.sa.utils.common.MyStringUtil;
import com.mr.sa.utils.oss.MinioUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttachmentService {

    private static Logger log = MyLog.get();

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 校验相关-附件上传
     */
    public void checkedAttachmentFile(List<AttachmentFile> attachmentFileList) {
        if (CollectionUtils.isNotEmpty(attachmentFileList)) {
            for (AttachmentFile attachmentFile : attachmentFileList) {
                if (null == attachmentFile) {
                    continue;
                }
                if (StringUtils.isBlank(attachmentFile.getUploadUrl())) {
                    throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('附件需要先上传!');");
                }
            }
        }
    }

    /**
     * 上传附件
     */
    @FileResolver
    @Transactional
    public Map<String, String> uploadAttachment(UploadFile file, Map<String, Object> parameter) throws Exception {
        String dir = (String) parameter.get("dir");
        File f = new File(MyStringUtil.getUUID());
        file.transferTo(f);
//		String url = TencentOssUtil.uploadAttachment(dir, f, file.getFileName());
        String url = minioUtil.uploadFile(dir, f, file.getFileName());
        FileUtils.forceDelete(f);
        Map<String, String> data = new HashMap<>();
        String success = null;
        data.put("url", url);
        if (StringUtils.isNotBlank(url)) {
            success = "true";
        }
        data.put("success", success);
        return data;

    }

    /**
     * 上传图片
     */
    @FileResolver
    @Transactional
    public Map<String, String> uploadImage(UploadFile file, Map<String, Object> parameter) throws Exception {
        String dir = (String) parameter.get("dir");
        File f = new File(MyStringUtil.getUUID());
        file.transferTo(f);
//		String url = TencentOssUtil.uploadImage(dir, f, file.getFileName());
        String url = minioUtil.uploadFile(dir, f, file.getFileName());
        try {
            FileUtils.forceDelete(f);
        } catch (IOException e) {
        }
        Map<String, String> data = new HashMap<>();
        String success = null;
        data.put("url", url);
        if (StringUtils.isNotBlank(url)) {
            success = "true";
        }
        data.put("success", success);
        return data;
    }
    /**
     * 上传图片
     */
    @FileResolver
    @Transactional
    public Map<String, String> uploadIcon(UploadFile file, Map<String, Object> parameter) throws Exception {
        String url = minioUtil.uploadFile("icon", file.getMultipartFile(), file.getFileName());
        Map<String, String> data = new HashMap<>();
        String success = null;
        data.put("url", url);
        if (StringUtils.isNotBlank(url)) {
            success = "true";
        }
        data.put("success", success);
        return data;
    }

    /**
     * 上传APK
     */
    @FileResolver
    @Transactional
    public Map<String, String> uploadApk(UploadFile file, Map<String, Object> parameter) throws Exception {
        String url = minioUtil.uploadApp(file.getMultipartFile(), file.getFileName());
        Map<String, String> data = new HashMap<>();
        String success = null;
        data.put("url", url);
        if (StringUtils.isNotBlank(url)) {
            success = "true";
        }
        data.put("success", success);
        return data;
    }

}
