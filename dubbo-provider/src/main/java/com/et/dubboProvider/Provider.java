package com.et.dubboProvider;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by shatao on 16/6/2017.
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "dubbo-provider.xml" });
        context.start();

        System.in.read(); // 按任意键退出
    }
}
