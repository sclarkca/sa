package com.mr.sa.config;

import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: lxp
 **/
@Configuration
public class GtApiConfig {
    @Value("${getui.domain}")
    private String domain;

    @Value("${getui.appId}")
    private String appId;

    @Value("${getui.appKey}")
    private String appKey;

    @Value("${getui.appSecret}")
    private String appSecret;

    @Value("${getui.masterSecret}")
    private String masterSecret;

    @Bean(name = "myApiHelper")
    public ApiHelper getApiHelper() {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        //填写应用配置
        apiConfiguration.setAppId(appId);
        apiConfiguration.setAppKey(appKey);
        apiConfiguration.setMasterSecret(masterSecret);
        apiConfiguration.setDomain(domain);
        // 实例化ApiHelper对象，用于创建接口对象
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);

        return apiHelper;
    }

}
