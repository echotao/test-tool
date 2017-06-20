package com.et.dubboProvider.domain;

import java.io.Serializable;

/**
 * Created by shatao on 20/6/2017.
 */
public class ResponseData implements Serializable{
    private static final long serialVersionUID = 1L;
    //private Integer code;
    private String msg;

    public void responseData(){

    }

    public String getMsg()
    {
        return this.msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
