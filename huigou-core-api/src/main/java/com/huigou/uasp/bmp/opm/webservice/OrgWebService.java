package com.huigou.uasp.bmp.opm.webservice;

import javax.jws.WebService;

@WebService
public interface OrgWebService {
    
    String getDeltaOrg(Integer version);

}
