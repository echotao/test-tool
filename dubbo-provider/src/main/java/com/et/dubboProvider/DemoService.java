package com.et.dubboProvider;

import com.et.dubboProvider.domain.ResponseData;

/**
 * Created by shatao on 16/6/2017.
 */
public interface DemoService {
    ResponseData syncSayHello(String name);
    ResponseData asyncSayHello(String name);
}
