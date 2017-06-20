package com.et.dubboProvider;

import com.et.dubboProvider.domain.ResponseData;

/**
 * Created by shatao on 16/6/2017.
 */
public class DemoServiceImpl implements DemoService {

    public ResponseData syncSayHello(String name) {

        ResponseData resp = new ResponseData();
        resp.setMsg("sync hello " + name);
        return resp;
    }

    public ResponseData asyncSayHello(String name) {

        ResponseData resp = new ResponseData();
        resp.setMsg("sync hello " + name);
        return resp;
    }
}
