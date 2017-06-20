package com.et.common.reflect;

import com.et.common.FileUtil;
import com.et.common.reflect.InterfaceExecutor;
import com.et.common.reflect.Reflect2JmeterSample;

import com.alibaba.fastjson.JSONArray;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
/**
 * Created by shatao on 13/6/2017.
 */
public class ReflectRunTool {
    public ReflectRunTool() {
    }

    public <T> String execTestcase(Class<T> clazz, T service, String[] testcase) {
        Method method = Reflect2JmeterSample.getMethod(clazz, testcase[0]);
        String[] paras = new String[testcase.length];
        System.arraycopy(testcase, 1, paras, 1, testcase.length - 1);
        return this.executeMethod2String(method, service, paras);
    }

    public <T> String execTestcase(Class<T> clazz, T service, JSONArray testcase) {
        Method method = Reflect2JmeterSample.getMethod(clazz, testcase.get(0).toString());
        testcase.remove(0);
        String tmp = testcase.get(0).toString();
        testcase.remove(0);
        return this.executeMethod(method, service, testcase).equals(tmp) ? "Okey" : "Fail";
    }

    public List<String> getTestcase(String testcasepath) {
        String filePath = "testcase.dat";
        if(testcasepath != null) {
            filePath = testcasepath;
        }

        return FileUtil.readTxtFile(filePath);
    }

    public Class<?> getService(String serviceName, String jarPath) {
        Class clazz = null;

        try {
            List e = Reflect2JmeterSample.getInterfaceList(serviceName, jarPath);
            clazz = (Class)e.get(0);
        } catch (ClassNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var5){
            var5.printStackTrace();
        }

        return clazz;
    }

    public String invokeMethod(Class<?> clazz, String methodName, String... args) {
        String result = null;

        try {
            Object e = clazz.newInstance();
            Method mm = Reflect2JmeterSample.getMethod(clazz, methodName);
            result = (String)mm.invoke(e, args);
        } catch (IllegalAccessException | InstantiationException var7) {
            var7.printStackTrace();
        } catch (IllegalArgumentException var8) {
            var8.printStackTrace();
        } catch (InvocationTargetException var9) {
            var9.printStackTrace();
        }

        return result;
    }

    public String executeMethod(Method method, Object service, JSONArray paras) {
        String result = " error ,not method running !";
        result = InterfaceExecutor.execute(service, method, paras);
        return result;
    }

    public String executeMethod2String(Method method, Object service, String[] args) {
        String result = " error ,not method running !";
        result = InterfaceExecutor.execute(method, service, args);
        return result;
    }
}

