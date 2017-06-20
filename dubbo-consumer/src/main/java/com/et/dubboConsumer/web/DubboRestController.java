package com.et.dubboConsumer.web;

import com.et.dubboConsumer.domain.ResponseDTO;
import com.et.dubboConsumer.service.impl.DubboRestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shatao on 20/6/2017.
 */
@RestController
@RequestMapping(value = "/api/dubborest")
public class DubboRestController {
    @Autowired
    private DubboRestServiceImpl service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseDTO hello(){
        System.out.println("+++++++++++In DubboRestController+++++++++++++++");
        return service.dubboRestServiceImpl();
    }

}
