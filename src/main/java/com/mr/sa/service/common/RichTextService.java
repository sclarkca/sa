package com.mr.sa.service.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.mr.sa.utils.oss.MinioUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mr.sa.utils.common.MyLog;

@Service
public class RichTextService {
 

	private static Logger log = MyLog.get();

	@Autowired
	private MinioUtil minioUtil;

	/**
	 * 富文本修正内容
	 * 
	 * @param content
	 */
	public String fixContent(String dir,String content) {
		// 获取内容的src url路径
		Map<String, String> urlMap = new HashMap<>();
		urlMap.putAll(getContentUploadUrls(content, "<img src", "imagedownload?file=", "upload/ueditorupload/image/"));
		urlMap.putAll(getContentUploadUrls(content, "<source src", "filedownload?file=", "upload/ueditorupload/file/"));
		// 定义上传成功后 oss 返回的 url 和上传的url对应的map
		Map<String, String> replaceMap = new HashMap<>();

		for (Map.Entry<String, String> item : urlMap.entrySet()) {
			String fileName = StringUtils.substringAfterLast(item.getValue(), "/");
			File file = new File(item.getValue());
//			String ossUrl = TencentOssUtil.uploadImage(dir,file, fileName);
			String ossUrl = minioUtil.uploadFile(dir,file, fileName);
			replaceMap.put(item.getKey(), ossUrl);
		}
		// 替换URL为OSS URL
		content = replaceContentUrl(content, replaceMap);
		//content = content.concat("<p><br/></p>");
		return content;
	}

	/**
	 * 替换上传文件URL为oss 返回的url
	 * 
	 * @param content
	 * @param separator
	 * @param open
	 * @param uploadPath
	 * @return
	 */
	private Map<String, String> getContentUploadUrls(String content, String separator, String open, String uploadPath) {
		if (StringUtils.isBlank(content)) {
			return new HashMap<String, String>();
		}
		String[] splitArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(content, separator);
		Map<String, String> convertMap = new HashMap<>();
		for (String splitStr : splitArray) {
			String url = StringUtils.substringBetween(splitStr, open, "\"");
			if (!splitStr.startsWith("=") || StringUtils.isBlank(url)) {
				continue;
			}
			String prefix = splitStr.substring(0, StringUtils.indexOf(splitStr, open)) + open;

			convertMap.put(prefix + url, uploadPath + url);
		}
		return convertMap;
	}

	/**
	 * 替换内容中的url
	 * 
	 * @param content
	 * @param replaceMap
	 * @return
	 */
	private String replaceContentUrl(String content, Map<String, String> replaceMap) {
		String contentStr = content;
		if (null == replaceMap || replaceMap.size() == 0) {
			return contentStr;
		}
		for (Map.Entry<String, String> item : replaceMap.entrySet()) {
			contentStr = StringUtils.replace(contentStr, item.getKey(), "=\"" + item.getValue());
		}
		return contentStr;
	}
}