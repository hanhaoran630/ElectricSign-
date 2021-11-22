package com.wzcc.electricsign;

import com.timvale.sealplatform.sdk.ClientManageService;
import com.timvale.sealplatform.sdk.config.HttpConnectionConfig;
import com.timvale.sealplatform.sdk.config.ProjectConfig;
import com.timvale.sealplatform.sdk.file.SignLocalClient;


public class SignHelper {

    public static SignLocalClient getSignLocalClient(String AppId,String Secret,String Protocol,String DomainName){
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setAppId(AppId);
        projectConfig.setSecret(Secret);
        projectConfig.setDomainName(DomainName);

        HttpConnectionConfig httpConnectionConfig=new HttpConnectionConfig();
        if(Protocol.equalsIgnoreCase("http"))
            httpConnectionConfig.setProtocol(Protocol);
        else
            httpConnectionConfig.setProtocol("https");
        httpConnectionConfig.setConnectionTimeOut(3000);

        return ClientManageService.initClient(projectConfig,httpConnectionConfig);
    }



}
