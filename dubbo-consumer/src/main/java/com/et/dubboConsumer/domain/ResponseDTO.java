package com.et.dubboConsumer.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.et.dubboProvider.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.alibaba.dubbo.rpc.RpcContext;
import com.et.dubboConsumer.domain.City;
import java.util.List;

/**
 * Created by shatao on 19/6/2017.
 */

public class ResponseDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    private Integer code;
    private String msg;
    private Map<String, Object> dataMap;

    public ResponseDTO(Integer code) {
        setCode(code);
    }

    public ResponseDTO(Integer code, String resp) {
        setCode(code);
        setMsg(resp);
    }

    public Integer getCode()
    {
        System.out.println("+++++++++++++in ResponseDTO++++++++++++++" + this.code + "+++++++++++++++++");
        return this.code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public void setMsg(String str)
    {
        this.msg = str;
    }
}
