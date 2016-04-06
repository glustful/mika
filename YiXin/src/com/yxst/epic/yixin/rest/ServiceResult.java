package com.yxst.epic.yixin.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yxst.epic.yixin.utils.Utils;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class ServiceResult<T> implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 6977558218691386450L;

    /**
     * 成功与否
     */
    private boolean succeed = true;

    /**
     * 代码
     */
    private int code = -1;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据对象
     */
    private T data;

    public ServiceResult() {
    }

    public ServiceResult(T data) {
        this.data = data;
    }

    public ServiceResult(boolean succeed, int code, String msg) {
        this.succeed = succeed;
        this.code = code;
        this.msg = msg;
    }

    public ServiceResult(boolean succeed, T data, String msg) {
        this.succeed = succeed;
        this.data = data;
        this.msg = msg;
    }

    public ServiceResult(boolean succeed, T data, int code, String msg) {
        this.succeed = succeed;
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public ServiceResult(boolean succeed, String msg) {
        this.succeed = succeed;
        this.msg = msg;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return Utils.writeValueAsString(this);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 所有额外的列属性
     */
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonAnyGetter
    public Object getAdditionalProperties(String name) {
        return this.additionalProperties.get(name);
    }
}

