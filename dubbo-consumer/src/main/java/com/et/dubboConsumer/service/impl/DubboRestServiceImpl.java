package com.et.dubboConsumer.service.impl;

import com.et.dubboConsumer.domain.ResponseDTO;
import com.et.dubboConsumer.service.DubboRestService;
import com.et.dubboConsumer.service.SpringContextHelper;
import com.et.dubboProvider.DemoService;
import com.et.dubboProvider.DemoServiceImpl;
import com.et.dubboProvider.domain.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.alibaba.dubbo.config.annotation.Reference;

/**
 * Created by shatao on 19/6/2017.
 */
@Component
public class DubboRestServiceImpl implements DubboRestService{

    @Reference(version="1.0.0")
    DemoService demoService;

    public ResponseDTO dubboRestServiceImpl(){
        ApplicationContext context = SpringContextHelper.getApplicationContext();
        //ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
        //        "dubbo-consumer.xml");
        //DubboRestService dubboRestConsumer = (DubboRestService) context.getBean("dubboRestConsumer");
        //System.out.println("+++++++Thread name+++++++" + context.getBean("dubboRestConsumer").getClass());
        //context.start();
        //DemoService demoService = (DemoService) context.getBean("demoService");

        System.out.println("+++++++++++" + context.getApplicationName());
        ResponseData resp = demoService.syncSayHello("Echo");
        //ResponseDTO responseDTO = new ResponseDTO(200, resp.getMsg());
        ResponseDTO responseDTO = new ResponseDTO(200, "Echo");
        responseDTO.getCode();
        return responseDTO;
    }


}
