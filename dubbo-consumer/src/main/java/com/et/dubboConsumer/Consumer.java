package com.et.dubboConsumer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.et.dubboProvider.DemoService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.rpc.RpcContext;

import com.et.dubboProvider.domain.ResponseData;

/**
 * Created by shatao on 16/6/2017.
 */
public class Consumer {
    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "dubbo-consumer.xml");
        context.start();
        DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理

        ResponseData respSync = demoService.syncSayHello("world");
        ResponseData respAsync = demoService.asyncSayHello("world");
        System.out.println(respSync.getMsg());
        System.out.println(respAsync.getMsg());
        Future<String> future = RpcContext.getContext().getFuture();
        //System.out.println(future.get());
    }
}