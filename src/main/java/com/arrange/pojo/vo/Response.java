package com.arrange.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class Response<T> implements Serializable {
    /** 返回信息码*/
    private int code;
    /** 返回信息内容*/
    private String msg;

    private T data = null;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp = new Date();

    public Response(){}
    //只能返回定义的返回信息
    //根据异常类构造
    public Response<T> success(T data){
        this.code = ResponseMsg.SUCCESS.code;
        this.msg = ResponseMsg.SUCCESS.msg;
        this.data = data;
        return this;
    }
    public static Response success(){
        return new Response(ResponseMsg.SUCCESS);
    }
    public Response(ResponseMsg msg){
        this.code = msg.code;
        this.msg = msg.msg;
    }
}
