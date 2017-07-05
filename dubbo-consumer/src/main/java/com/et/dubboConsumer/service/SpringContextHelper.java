package com.et.dubboConsumer.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by shatao on 21/6/2017.
 */
@Component
public class SpringContextHelper implements ApplicationContextAware{

    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext context) throws BeansException{
        this.context = context;
    }

    public static ApplicationContext getApplicationContext(){
        return context;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) context.getBean(name);
    }

}
