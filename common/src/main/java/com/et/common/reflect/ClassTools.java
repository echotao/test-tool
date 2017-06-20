package com.et.common.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.lang3.ClassUtils;
import org.apache.jorphan.util.JMeterException;

/**
 * Created by shatao on 13/6/2017.
 */
public class ClassTools {
    public ClassTools() {
    }

    public static Object construct(String className) throws JMeterException {
        Object instance = null;

        try {
            instance = ClassUtils.getClass(className).newInstance();
            return instance;
        } catch (ClassNotFoundException var3) {
            throw new JMeterException(var3);
        } catch (InstantiationException var4) {
            throw new JMeterException(var4);
        } catch (IllegalAccessException var5) {
            throw new JMeterException(var5);
        }
    }

    public static Object construct(String className, int parameter) throws JMeterException {
        Object instance = null;

        try {
            Class e = ClassUtils.getClass(className);
            Constructor constructor = e.getConstructor(new Class[]{Integer.TYPE});
            instance = constructor.newInstance(new Object[]{Integer.valueOf(parameter)});
            return instance;
        } catch (ClassNotFoundException var5) {
            throw new JMeterException(var5);
        } catch (InstantiationException var6) {
            throw new JMeterException(var6);
        } catch (IllegalAccessException var7) {
            throw new JMeterException(var7);
        } catch (SecurityException var8) {
            throw new JMeterException(var8);
        } catch (NoSuchMethodException var9) {
            throw new JMeterException(var9);
        } catch (IllegalArgumentException var10) {
            throw new JMeterException(var10);
        } catch (InvocationTargetException var11) {
            throw new JMeterException(var11);
        }
    }

    public static Object construct(String className, String parameter) throws JMeterException {
        Object instance = null;

        try {
            Class e = Class.forName(className);
            Constructor constructor = e.getConstructor(new Class[]{String.class});
            instance = constructor.newInstance(new Object[]{parameter});
            return instance;
        } catch (ClassNotFoundException var5) {
            throw new JMeterException(var5);
        } catch (InstantiationException var6) {
            throw new JMeterException(var6);
        } catch (IllegalAccessException var7) {
            throw new JMeterException(var7);
        } catch (NoSuchMethodException var8) {
            throw new JMeterException(var8);
        } catch (IllegalArgumentException var9) {
            throw new JMeterException(var9);
        } catch (InvocationTargetException var10) {
            throw new JMeterException(var10);
        }
    }

    public static void invoke(Object instance, String methodName) throws SecurityException, IllegalArgumentException, JMeterException {
        try {
            Method m = ClassUtils.getPublicMethod(instance.getClass(), methodName, new Class[0]);
            m.invoke(instance, (Object[])null);
        } catch (NoSuchMethodException var4) {
            throw new JMeterException(var4);
        } catch (IllegalAccessException var5) {
            throw new JMeterException(var5);
        } catch (InvocationTargetException var6) {
            throw new JMeterException(var6);
        }
    }
}

