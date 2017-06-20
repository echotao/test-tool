package com.et.common.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.et.common.reflect.InterfaceExecutor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by shatao on 13/6/2017.
 */
public class DubboUtil implements Serializable {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private DubboUtil() {
    }

    //et added to fix DubboInner
    public DubboUtil(DubboUtil dubboUtil) {
    }

    public static final DubboUtil initDubboFactory() {
        return DubboUtil.DubboInner.INSTANCE;
    }

    public String executeMethod(Method method, JSONArray inputParaArray, Object obj) {
        String result = " error ,not method running !";
        result = InterfaceExecutor.execute(obj, method, inputParaArray);
        return result;
    }

    public Method getMethod2(String serviceName, String methodName) {
        Method method = null;

        try {
            Class clazz = Class.forName(serviceName);
            Method[] e = clazz.getDeclaredMethods();
            Method[] var9 = e;
            int var8 = e.length;

            for(int var7 = 0; var7 < var8; ++var7) {
                Method md = var9[var7];
                if(methodName.equals(md.getName())) {
                    method = md;
                    break;
                }
            }
        } catch (ClassNotFoundException var10) {
            var10.printStackTrace();
        }

        return method;
    }

    public Method getMethod(Object service, String methodName) {
        Method method = null;
        Method[] mt = service.getClass().getDeclaredMethods();
        Method[] var8 = mt;
        int var7 = mt.length;

        for(int var6 = 0; var6 < var7; ++var6) {
            Method md = var8[var6];
            if(methodName.equals(md.getName())) {
                method = md;
                break;
            }
        }

        return method;
    }

    public String getMethodParasJson(Method method, String[] includePackage) {
        String json = "";
        Class[] paramClass = method.getParameterTypes();
        ArrayList ParasJsonList = new ArrayList();

        for(int i = 0; i < paramClass.length; ++i) {
            if(!paramClass[i].getName().equalsIgnoreCase("int") && !paramClass[i].getName().equalsIgnoreCase("long") && !paramClass[i].getName().equalsIgnoreCase("Long") && !paramClass[i].getName().equalsIgnoreCase("double") && !paramClass[i].getName().equalsIgnoreCase("float")) {
                if(paramClass[i].getName().equalsIgnoreCase("String")) {
                    ParasJsonList.add("");
                } else if(!paramClass[i].getName().equalsIgnoreCase("char") && !paramClass[i].getName().equalsIgnoreCase("char[]")) {
                    if(paramClass[i].getName().equalsIgnoreCase("boolean")) {
                        ParasJsonList.add(Boolean.valueOf(false));
                    } else if(paramClass[i].getName().equalsIgnoreCase("Date")) {
                        ParasJsonList.add(new Date());
                    } else if(paramClass[i].getCanonicalName().indexOf("java.") >= 0) {
                        ParasJsonList.add((Object)null);
                    } else if(includePackage != null && includePackage.length > 0 && this.checkClassMatchPackage(paramClass[i].getCanonicalName(), includePackage)) {
                        new HashMap();
                        Map map = this.getFields(paramClass[i], includePackage);
                        ParasJsonList.add(JSON.toJSONString(map, new SerializerFeature[]{SerializerFeature.PrettyFormat, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue}));
                    } else {
                        ParasJsonList.add((Object)null);
                    }
                } else {
                    ParasJsonList.add(Character.valueOf('0'));
                }
            } else {
                ParasJsonList.add(Integer.valueOf(0));
            }
        }

        return ParasJsonList.toString();
    }

    public boolean checkClassMatchPackage(String classCannonicalName, String[] packages) {
        boolean matched = false;
        String[] var7 = packages;
        int var6 = packages.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            String pack = var7[var5];
            if(classCannonicalName.indexOf(pack) >= 0) {
                matched = true;
                break;
            }
        }

        return matched;
    }

    public Map<String, Object> getFields(Class paramClass, String[] includePackage) {
        HashMap map = new HashMap();
        ArrayList fields = new ArrayList();
        fields.addAll(Arrays.asList(paramClass.getDeclaredFields()));

        for(Class superClass = paramClass.getSuperclass(); superClass != null && this.checkClassMatchPackage(superClass.getCanonicalName(), includePackage); superClass = superClass.getSuperclass()) {
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
        }

        for(int j = 0; j < fields.size(); ++j) {
            if(!((Field)fields.get(j)).getName().equalsIgnoreCase("CASE_INSENSITIVE_ORDER") && !((Field)fields.get(j)).getName().equalsIgnoreCase("serialPersistentFields") && !((Field)fields.get(j)).getName().equalsIgnoreCase("serialVersionUID") && !((Field)fields.get(j)).getName().equalsIgnoreCase("genericInfo")) {
                if(((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("List")) {
                    ParameterizedType pt = (ParameterizedType)((Field)fields.get(j)).getGenericType();
                    new HashMap();
                    Class subClass = (Class)pt.getActualTypeArguments()[0];
                    Map submap = this.getFields(subClass, includePackage);
                    ArrayList list = new ArrayList();
                    list.add(submap);
                    map.put(((Field)fields.get(j)).getName(), list);
                } else if(((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("String")) {
                    map.put(((Field)fields.get(j)).getName(), "");
                } else if(!((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("char[]") && !((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("char")) {
                    if(((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("int") || ((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("long") || ((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("double") || ((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("float") || ((Field)fields.get(j)).getType().getSuperclass() != null && ((Field)fields.get(j)).getType().getSuperclass().getSimpleName().equalsIgnoreCase("Number")) {
                        map.put(((Field)fields.get(j)).getName(), Integer.valueOf(0));
                    } else if(((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("boolean")) {
                        map.put(((Field)fields.get(j)).getName(), Boolean.valueOf(false));
                    } else if(((Field)fields.get(j)).getType().getSimpleName().equalsIgnoreCase("Date")) {
                        map.put(((Field)fields.get(j)).getName(), new Date());
                    } else if(((Field)fields.get(j)).getType().getCanonicalName().indexOf("java.") >= 0) {
                        map.put(((Field)fields.get(j)).getName(), (Object)null);
                    } else if(includePackage != null && includePackage.length > 0 && this.checkClassMatchPackage(((Field)fields.get(j)).getType().getCanonicalName(), includePackage)) {
                        map.put(((Field)fields.get(j)).getName(), this.getFields(((Field)fields.get(j)).getType(), includePackage));
                    } else {
                        map.put(((Field)fields.get(j)).getName(), (Object)null);
                    }
                } else {
                    map.put(((Field)fields.get(j)).getName(), Character.valueOf('0'));
                }
            }
        }

        return map;
    }

    public Object getService(String serviceId, ClassPathXmlApplicationContext context) {
        Object obj = context.getBean(serviceId);
        return obj;
    }

    public Object getSystemService(String serviceId, FileSystemXmlApplicationContext context) {
        Object obj = context.getBean(serviceId);
        return obj;
    }

    public ClassPathXmlApplicationContext getContext(String xmlPath) {
        ClassPathXmlApplicationContext context = null;

        try {
            context = new ClassPathXmlApplicationContext(new String[]{xmlPath});
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        context.start();
        return context;
    }

    public FileSystemXmlApplicationContext getSystemContext(String xmlPath) {
        FileSystemXmlApplicationContext context = null;

        try {
            context = new FileSystemXmlApplicationContext(new String[]{xmlPath});
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        context.start();
        return context;
    }

    public List<String> string2List(String[] args) {
        byte i = 0;
        ArrayList list = new ArrayList();
        if(list != null && args.length >= 1) {
            byte var10002 = i;
            int i1 = i + 1;
            list.add(args[var10002]);
        }

        return list;
    }

    public boolean checkBykey(String key, String oldStr) {
        String regEx = "000000";
        if(key != null && !"".equals(key)) {
            regEx = key;
        }

        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(oldStr);
        return m.find();
    }

    public Method[] getMethodList(String serviceName) {
        Method[] mt = null;

        try {
            Class clazz = Class.forName(serviceName);
            mt = clazz.getDeclaredMethods();
        } catch (ClassNotFoundException var5) {
            var5.printStackTrace();
        }

        return mt;
    }

    public static void main(String[] args) {
        DubboUtil du = new DubboUtil();
        ClassPathXmlApplicationContext context = du.getContext("D:\\java\\workspaceJetty\\pas\\pas-common\\dubbo-hello-consumer.xml");
        Object service = du.getService("bidService", context);
        Method method = du.getMethod(service, "getUserInfo");
        HashMap map = new HashMap();
        map.put("cardNo", "10000011111");
        map.put("cardType", "D");
        String json = JSON.toJSONString(map);
        json = "[" + json + "]";
        JSONArray inputParaArray = JSON.parseArray(json);
        du.executeMethod(method, inputParaArray, service);
        String str = "D:\\java\\jmeter\\lib\\ext\\file-test.xml";
        if(str.endsWith(".xml") || str.endsWith(".XML")) {
            log.info("ok");
        }

    }

    private static class DubboInner {
        private static final DubboUtil INSTANCE = new DubboUtil((DubboUtil)null);

        private DubboInner() {
        }
    }
}

