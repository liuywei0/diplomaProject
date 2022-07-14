package com.liuywei.housekeeping.utils;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private Integer code;//状态码
    private String message;//信息
    private Long timestamp;//时间戳
    private Object data;//数据

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
