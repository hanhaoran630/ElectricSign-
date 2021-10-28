package com.wzcc.electricsign;

import com.timvale.sealplatform.sdk.ClientManageService;
import com.timvale.sealplatform.sdk.config.HttpConnectionConfig;
import com.timvale.sealplatform.sdk.config.ProjectConfig;
import com.timvale.sealplatform.sdk.file.SignLocalClient;


public class SignHelper {

    public static SignLocalClient getSignLocalClient(String AppId,String Secret,Boolean IsTest,Boolean IsHTTP){
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setAppId(AppId);
        projectConfig.setSecret(Secret);

        if(IsTest)
            //测试环境的ip端口
            projectConfig.setDomainName("124.70.33.109:9999");
        else
            //正式的域名端口
            //projectConfig.setDomainName("zjyz.zjzwfw.gov.cn:9999");
            //内网IP端口
            projectConfig.setDomainName("10.249.136.160:9999");

        HttpConnectionConfig httpConnectionConfig=new HttpConnectionConfig();

        if(IsHTTP)
            httpConnectionConfig.setProtocol("http");
        else
            httpConnectionConfig.setProtocol("https");

        httpConnectionConfig.setConnectionTimeOut(3000);
        return ClientManageService.initClient(projectConfig,httpConnectionConfig);
    }



}
