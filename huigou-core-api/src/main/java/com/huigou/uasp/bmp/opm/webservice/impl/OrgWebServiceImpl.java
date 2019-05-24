package com.huigou.uasp.bmp.opm.webservice.impl;

import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.repository.org.OrgRepository;
import com.huigou.uasp.bmp.opm.webservice.OrgWebService;
import com.huigou.util.JSONUtil;

@WebService(endpointInterface = "com.huigou.uasp.bmp.opm.webservice.impl.OrgWebServiceImpl", serviceName = "orgWebService")
public class OrgWebServiceImpl implements OrgWebService {

    @Autowired
    private OrgRepository orgRepository;

    @Override
    public String getDeltaOrg(Integer version) {
        List<Org> orgs = orgRepository.findDeltaOrg(version);
        return JSONUtil.toString(orgs);
    }

}
