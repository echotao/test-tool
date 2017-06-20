package com.et.dubboConsumer.service.impl;

import com.et.dubboConsumer.domain.ResponseDTO;
import com.et.dubboConsumer.service.DubboRestService;
import com.et.dubboProvider.DemoService;
import com.et.dubboProvider.domain.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import com.alibaba.dubbo.config.annotation.Reference;

/**
 * Created by shatao on 19/6/2017.
 */
@Service
public class DubboRestServiceImpl implements DubboRestService{
    @Reference(version="3.0.0")
    //DemoService demoService;

    public ResponseDTO dubboRestServiceImpl(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "dubbo-consumer.xml");
        context.start();
        DemoService demoService = (DemoService) context.getBean("demoService");

        ResponseData resp = demoService.syncSayHello("Echo");
        ResponseDTO responseDTO = new ResponseDTO(200, resp.getMsg());
        //responseDTO.getCode();
        return responseDTO;
    }


}
