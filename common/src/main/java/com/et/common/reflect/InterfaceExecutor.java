package com.et.common.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.lang.reflect.Method;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Created by shatao on 13/6/2017.
 */

public class InterfaceExecutor {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public InterfaceExecutor() {
    }

    public static <T> String execute(T service, Method method, JSONArray inputParamArray) {
        Object result;
        try {
            Object[] params = convertparams(method.getParameterTypes(), inputParamArray);
            result = method.invoke(service, params);
        } catch (Exception var6) {
            result = var6.getMessage();
            var6.printStackTrace();
        }

        return JSON.toJSONString(result, new SerializerFeature[]{SerializerFeature.QuoteFieldNames, SerializerFeature.WriteMapNullValue, SerializerFeature.SortField});
    }

    public static String execute(Method method, Object service, String[] args) {
        Object result;
        try {
            Object[] params = convertparams(method.getParameterTypes(), args);
            result = method.invoke(service, params);
        } catch (Exception var6) {
            result = var6.getMessage();
            var6.printStackTrace();
        }

        return JSON.toJSONString(result, new SerializerFeature[]{SerializerFeature.QuoteFieldNames, SerializerFeature.WriteMapNullValue, SerializerFeature.SortField});
    }

    public static Object[] convertparams(Class<?>[] paramTypes, JSONArray inputParamArray) throws Exception {
        Object[] paramArr;
        if(inputParamArray != null && inputParamArray.size() >= 1) {
            paramArr = inputParamArray.toArray();
        } else {
            paramArr = new Object[0];
        }

        if(paramTypes == null || paramArr == null || paramArr.length != paramTypes.length) {
            log.error("传入参数个数与接口参数不匹配！paramArr:" + new Object[]{inputParamArray}, new IllegalArgumentException("传入参数个数与接口参数不匹配！"));
        }

        Object[] params = new Object[paramArr.length];

        for(int i = 0; i < paramArr.length; ++i) {
            Object param = paramArr[i];
            Class paramType = paramTypes[i];
            if(isWrapClass(paramType)) {
                params[i] = ConvertUtils.convert(param, paramType);
            } else if(paramType.isEnum()) {
                if(param instanceof String) {
                    String var15 = (String)param;
                    if(var15.length() == 0) {
                        params[i] = null;
                    } else {
                        params[i] = Enum.valueOf(paramType, var15);
                    }
                } else if(param instanceof Number) {
                    int ordinal = ((Number)param).intValue();
                    Method mt = paramType.getMethod("values", new Class[0]);
                    Object[] values = (Object[])mt.invoke((Object)null, new Object[0]);
                    Object[] var13 = values;
                    int var12 = values.length;

                    for(int var11 = 0; var11 < var12; ++var11) {
                        Object value = var13[var11];
                        Enum e = (Enum)value;
                        if(e.ordinal() == ordinal) {
                            params[i] = e;
                        }
                    }
                }
            } else if(param instanceof JSONObject) {
                params[i] = JSON.toJavaObject((JSONObject)param, paramType);
            }
        }

        return params;
    }

    public static Object[] convertparams(Class<?>[] paramTypes, String[] args) throws Exception {
        Object paramArr;
        if(args != null && args.length >= 1) {
            paramArr = args;
        } else {
            paramArr = new Object[0];
        }

        if(paramTypes == null || paramArr == null || ((Object[])paramArr).length != paramTypes.length) {
            log.error("传入参数个数与接口参数不匹配！paramArr:" + new Object[]{args}, new IllegalArgumentException("传入参数个数与接口参数不匹配！"));
        }

        Object[] params = new Object[((Object[])paramArr).length];

        for(int i = 0; i < ((Object[])paramArr).length; ++i) {
            Object param = ((Object[])paramArr)[i];
            Class paramType = paramTypes[i];
            if(isWrapClass(paramType)) {
                params[i] = ConvertUtils.convert(param, paramType);
            } else if(paramType.isEnum()) {
                if(param instanceof String) {
                    String var15 = (String)param;
                    if(var15.length() == 0) {
                        params[i] = null;
                    } else {
                        params[i] = Enum.valueOf(paramType, var15);
                    }
                } else if(param instanceof Number) {
                    int ordinal = ((Number)param).intValue();
                    Method mt = paramType.getMethod("values", new Class[0]);
                    Object[] values = (Object[])mt.invoke((Object)null, new Object[0]);
                    Object[] var13 = values;
                    int var12 = values.length;

                    for(int var11 = 0; var11 < var12; ++var11) {
                        Object value = var13[var11];
                        Enum e = (Enum)value;
                        if(e.ordinal() == ordinal) {
                            params[i] = e;
                        }
                    }
                }
            }
        }

        return params;
    }

    public static boolean isWrapClass(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.getName().startsWith("java.lang") || clazz.getName().startsWith("[");
    }
}
