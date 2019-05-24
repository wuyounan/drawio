package com.huigou.uasp.log.webservice.impl;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import com.huigou.uasp.log.application.LoginLogApplication;
import com.huigou.uasp.log.webservice.LogWebService;

@WebService(endpointInterface = "com.huigou.uasp.log.webservice.LogWebService", serviceName = "logWebService")
public class LogWebServiceImpl implements LogWebService {
    
    @Autowired
    private LoginLogApplication loginLogApplication;
    
    @Override
    public void validateSession() {
    	loginLogApplication.validateSession();
    }
}
