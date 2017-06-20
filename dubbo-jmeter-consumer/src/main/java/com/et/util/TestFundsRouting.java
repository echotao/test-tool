package com.et.util;

import java.io.Serializable;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import org.apache.jmeter.config.Arguments;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pas.common.dubbo.DubboUtil;


/**
 * Created by shatao on 13/6/2017.
 */
@SuppressWarnings("serial")
public class TestFundsRouting extends AbstractJavaSamplerClient implements Serializable{


    public Arguments getDefaultParameters(){
        Arguments args=new Arguments();
        args.addArgument("ServiceName","FundsRoutingService");//服务接口名
        args.addArgument("MethodName","fundsRouting");//方法名
        args.addArgument("JsonString", "[{\"periods\":\"12\",\"subProductType\":\"0\",\"storeName\":\"\",\"notSignType\":\"2\",\"areaCode\":\"450300\",\"notSignSum\":\"200\",\"retailNo\":\"2015041700000001\",\"businessType\":\"PGSB016\",\"productType\":\"030\",\"retailName\":\"\",\"operatormode\":\"\",\"customerId\":\"\",\"contractNo\":\"100200300\",\"businessAmount\":\"50\",\"stores\":\"10000\",\"sureType\":\"BQ\"}]");
        return args;
    }


    @SuppressWarnings("deprecation")
    public SampleResult runTest(JavaSamplerContext arg0) {
        SampleResult sr=new SampleResult();
        String result="";
        DubboUtil du = DubboUtil.initDubboFactory();
        ClassPathXmlApplicationContext context = du.getContext("dubbo-hello-consumer.xml");
        Object service = du.getService(arg0.getParameter("ServiceName"), context);
        Method method = du.getMethod(service, arg0.getParameter("MethodName"));
        System.out.println("JsonString="+arg0.getParameter("JsonString"));
        JSONArray inputParaArray = JSON.parseArray(arg0.getParameter("JsonString"));
        sr.sampleStart();
        try {
            result= du.executeMethod(method, inputParaArray, service);
        } catch (Exception e) {
            result=e.getMessage().toString()+"\r\n"+e.getStackTrace();
        }
        sr.sampleEnd();
        sr.setSuccessful(true);
        sr.setSampleLabel("Dubbo_FundsRouting");
        try {
            sr.setResponseData(new String(new String(result.getBytes("GBK"),"UTF-8")));
        } catch (UnsupportedEncodingException e) {
            result=e.getMessage().toString()+"\r\n"+e.getStackTrace();
        }

        return sr;

    }

}
