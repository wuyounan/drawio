package com.huigou.uasp.bmp.bizconfig.function.domain.model;

import java.util.ArrayList;
import java.util.List;

public class BpmFunctionsGroupDto {

    private String id;

    private String name;

    private String color;

    private String code;

    private List<BpmFunctionsDetailsDto> funs;

    public BpmFunctionsGroupDto() {
        funs = new ArrayList<BpmFunctionsDetailsDto>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<BpmFunctionsDetailsDto> getFuns() {
        return funs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setFuns(List<BpmFunctionsDetailsDto> funs) {
        this.funs = funs;
    }

    public void addFuns(BpmFunctionsDetailsDto dto) {
        this.funs.add(dto);
    }

    public int getFunsLength() {
        return this.funs.size();
    }
}
