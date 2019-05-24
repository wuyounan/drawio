package com.huigou.uasp.bmp.portal.mainpage.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户桌面分屏类
 * 
 * @author xx
 */
public class UserScreen {
    private Long id;

    private String personMemberId;

    private String personId;

    private List<UserScreenFunction> functions = new ArrayList<UserScreenFunction>();

    public UserScreen() {

    }

    public UserScreen(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonMemberId() {
        return personMemberId;
    }

    public void setPersonMemberId(String personMemberId) {
        this.personMemberId = personMemberId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public List<UserScreenFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<UserScreenFunction> functions) {
        this.functions = functions;
    }

    public void addFunction(UserScreenFunction fun) {
        this.functions.add(fun);
    }
}
