package com.huigou.uasp.bmp.opm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.context.PersonMember;
import com.huigou.domain.ValidStatus;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.opm.LicenseChecker;
import com.huigou.uasp.bmp.opm.LoginStatus;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bmp.opm.application.AccessApplication;
import com.huigou.uasp.bmp.opm.application.AuthenticationApplication;
import com.huigou.uasp.bmp.opm.application.OrgApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.util.Md5Builder;
import com.lowagie.text.pdf.codec.Base64;

public class AuthenticationApplicationImpl extends BaseApplication implements AuthenticationApplication {

    private String NOT_CHECK_PASSWORD = "_._not_check_password_.";

    private OrgApplication orgApplication;

    private AccessApplication accessApplication;

    private static long LICESENE_INDEX = 0;

    public void setOrgApplication(OrgApplication orgApplication) {
        this.orgApplication = orgApplication;
    }

    public void setAccessApplication(AccessApplication accessApplication) {
        this.accessApplication = accessApplication;
    }

    private LicenseChecker getLicenseChecker() {
        return StandardLicenseChecker.getInstance();
    }

    @Override
    public void setOperatorContext(Operator operator, String personMemberId) {
        Org personMemberEntity = this.orgApplication.loadEabledOrg(personMemberId);

        PersonMember personMember = OpmUtil.toPersonMember(personMemberEntity);

        operator.setOrgContext(personMember);

        List<Org> personMemberEntities = this.orgApplication.queryPersonMembersByPersonId(personMemberEntity.getPerson().getId());

        List<String> fullIds = new ArrayList<String>(personMemberEntities.size());
        for (Org item : personMemberEntities) {
            fullIds.add(item.getFullId());
        }
        operator.fillPersonMemberFullIds(fullIds);

        List<String> roles = accessApplication.queryPersonRoleIds(operator.getUserId());
        operator.fillRoleIds(roles);
        operator.setRoleKind(accessApplication.getPersonRoleKind(operator.getUserId()));

    }

    @Override
    public Operator createOperatorByPersonMemberId(String personMemberId) {
        Assert.hasText(personMemberId, "参数personMemberId不能为空。");

        Org personMemberEntity = this.orgApplication.loadOrg(personMemberId);
        PersonMember personMember = OpmUtil.toPersonMember(personMemberEntity);
        Operator operator = Operator.newInstance(personMember);

        return operator;
    }

    @Override
    public Operator createOperatorByLoginName(String loginName) {
        Assert.hasText(loginName, "参数loginName不能为空。");

        Org personMemberEntity = this.orgApplication.loadMainOrgByLoginName(loginName);
        PersonMember personMember = OpmUtil.toPersonMember(personMemberEntity);
        Operator operator = Operator.newInstance(personMember);

        return operator;
    }

    @Override
    public Map<String, Object> loginFromErp(String loginName) {
        return loginFromErp(loginName, NOT_CHECK_PASSWORD);
    }

    @Override
    public Map<String, Object> loginFromErp(String loginName, String password) {
        Assert.hasText(loginName, "参数loginName不能为空。");
        Assert.hasText(password, "参数password不能为空。");

        Org mainPersonMember = this.orgApplication.loadMainOrgByLoginName(loginName);

        LoginStatus loginStatus = LoginStatus.UNKNOWN_ERROR;

        if (mainPersonMember == null) {
            loginStatus = LoginStatus.USER_NOT_EXIST_OR_PASSWORD_ERROR;
        } else {
            ValidStatus personStatus = mainPersonMember.getPerson().getValidStatus();
            if (!NOT_CHECK_PASSWORD.equals(password) && !password.equalsIgnoreCase(mainPersonMember.getPerson().getPassword())) {
                loginStatus = LoginStatus.USER_NOT_EXIST_OR_PASSWORD_ERROR;
            } else {
                switch (personStatus) {
                case ENABLED:
                    loginStatus = LoginStatus.SUCCESS;
                    break;
                case DISABLED:
                    loginStatus = LoginStatus.USER_DISABLED;
                    break;
                case LOGIC_DELETE:
                    loginStatus = LoginStatus.USER_LOGIC_DELETE;
                    break;
                }
            }
        }

        Map<String, Object> result = loginStatus.getLoginStatus();
        if (loginStatus == LoginStatus.SUCCESS) {
            result.put("data", OpmUtil.buildLoginResultData(mainPersonMember));
        }

        return result;
    }

    private void checkLicense() {
        if (LICESENE_INDEX >= 500) {
            String sql = "select count(*) from SA_OnlineSession";
            long onlineUser = this.generalRepository.coungByNativeSql(sql, null);
            if (!this.getLicenseChecker().checkOnlineUser((int) onlineUser)) {
                throw new ApplicationException("在线用户数已超过注册数。");
            }
            sql = "select count(*) from act_ru_task";

            long taskCount = this.generalRepository.coungByNativeSql(sql, null);
            if (!this.getLicenseChecker().checkTask((int) taskCount)) {
                throw new ApplicationException("任务数已超过注册数。");
            }

            if (!this.getLicenseChecker().checkValidTime()) {
                throw new ApplicationException("授权已过期。");
            }
            LICESENE_INDEX = 0;
        }
        LICESENE_INDEX++;
    }

    @Override
    public Map<String, Object> login(String loginName, String password) {
        String decodedPassword = new String(Base64.decode(password));

        try {
            /*
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2018);        
            calendar.set(Calendar.MONTH, Calendar.JUNE);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            
            Date currentDate = new Date();
            
            if (currentDate.after(calendar.getTime())){
                throw new ApplicationException("登录出错，错误编码：0001。");
            }
            */
            checkLicense();
            Person person = this.orgApplication.loadPersonByLoginName(loginName);

            if (person == null) {
                return LoginStatus.USER_NOT_EXIST_OR_PASSWORD_ERROR.getLoginStatus();
            }

            String md5Password = Md5Builder.getMd5(decodedPassword);
            return loginFromErp(loginName, md5Password);

        } catch (Exception ex) {
            return LoginStatus.USER_NOT_EXIST_OR_PASSWORD_ERROR.getLoginStatus();
        }
    }

}
