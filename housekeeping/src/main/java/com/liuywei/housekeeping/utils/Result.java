package com.liuywei.housekeeping.utils;

import java.util.Date;
import java.util.Map;

public class Result {

    /**
     * 返回成功消息 和 数据
     * @param msg
     * @param data
     * @return
     */
    public static Message success(String msg, Object data){
        Message message = new Message();
        message.setMessage(msg);
        message.setCode(200);
        message.setTimestamp(new Date().getTime());
        message.setData(data);
        return message;
    }

    /**
     * 返回成功消息
     * @param msg
     * @return
     */
    public static Message success(String msg){
        Message message = new Message();
        message.setMessage(msg);
        message.setCode(200);
        message.setTimestamp(new Date().getTime());
        return message;
    }

    /**
     * 返回错误消息
     * @param code
     * @param msg
     * @return
     */
    public static Message error(Integer code, String msg){
        Message message = new Message();
        message.setMessage(msg);
        message.setCode(code);
        message.setTimestamp(new Date().getTime());
        return message;
    }

    /**
     * 返回错误消息
     * @param msg
     * @return
     */
    public static Message error(String msg){
        Message message = new Message();
        message.setMessage(msg);
        message.setCode(500);
        message.setTimestamp(new Date().getTime());
        return message;
    }

    /**
     * 返回错误消息
     * @return
     */
    public static Message error(){
        Message message = new Message();
        message.setMessage("数据库异常！");
        message.setCode(500);
        message.setTimestamp(new Date().getTime());
        return message;
    }

    /**
     * 404错误
     * @return
     */
    public static Message notFound(){
        Message message = new Message();
        message.setMessage("NOT FOUND");
        message.setCode(404);
        message.setTimestamp(new Date().getTime());
        return message;
    }

    public static Message forbidden(String msg){
        Message message = new Message();
        message.setMessage(msg);
        message.setCode(403);
        message.setTimestamp(new Date().getTime());
        return message;
    }


}
