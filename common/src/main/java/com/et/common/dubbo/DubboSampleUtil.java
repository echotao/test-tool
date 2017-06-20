package com.et.common.dubbo;

import com.alibaba.fastjson.JSON;
import com.et.common.dubbo.ApplicationServices;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by shatao on 13/6/2017.
 */
public class DubboSampleUtil {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public DubboSampleUtil() {
    }

    public static Document readXml(String path) {
        File file = new File(path);
        BufferedReader bufferedReader = null;
        Document document = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            SAXReader e = new SAXReader();
            document = e.read(bufferedReader);
            bufferedReader.close();
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        } catch (DocumentException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return document;
    }

    public List<String> getApplicationList(String xmlPath, String[] confParams) {
        ArrayList list = new ArrayList();
        String[] args = new String[]{"application name"};
        if(confParams != null && confParams.length >= 1) {
            args[0] = confParams[0];
        }

        if(!"".equals(xmlPath) && xmlPath != null && args != null && args.length >= 1) {
            Document doc = readXml(xmlPath);
            Element root = doc.getRootElement();
            if(args[0] != null) {
                List regElement = root.elements(args[0]);
                Iterator var9 = regElement.iterator();

                while(var9.hasNext()) {
                    Element ab = (Element)var9.next();
                    ab.content();
                    StringBuffer sb = new StringBuffer();
                    sb.append(ab.attribute(args[0]).getValue() + "@");
                    sb.append(xmlPath);
                    list.add(sb.toString());
                }
            }
        }

        return list;
    }

    public static ApplicationServices getServiceList4Application(String xmlPath, String[] confParams) throws DocumentException {
        ApplicationServices appServices = new ApplicationServices();
        String[] args = new String[]{"reference", "interface", "id"};
        if(confParams != null && confParams.length >= 3) {
            args[1] = confParams[1];
            args[2] = confParams[2];
            args[3] = confParams[3];
        }

        if(!"".equals(xmlPath) && xmlPath != null && args != null && args.length >= 1) {
            Document doc = readXml(xmlPath);
            Element root = doc.getRootElement();
            if(root.elements("application").size() == 0) {
                return null;
            }

            Element appElement = (Element)root.elements("application").toArray()[0];
            appElement.content();
            appServices.setApplicationName(appElement.attribute("name").getValue());
            appServices.setXmlPath(xmlPath);
            if(args[0] != null) {
                List regElement = root.elements(args[0]);
                ArrayList list = new ArrayList();
                Iterator var10 = regElement.iterator();

                while(var10.hasNext()) {
                    Element ab = (Element)var10.next();
                    ab.content();
                    StringBuffer sb = new StringBuffer();
                    sb.append(ab.attribute(args[1]).getValue() + "@");
                    sb.append(ab.attribute(args[2]).getValue());
                    list.add(sb.toString());
                }

                appServices.setServicesName(list);
            }
        }

        return appServices;
    }

    public List<String> getServerList(String xmlPath, String[] confParams) {
        ArrayList list = new ArrayList();
        String[] args = new String[]{"reference", "interface", "id"};
        if(confParams != null && confParams.length >= 3) {
            args[0] = confParams[0];
            args[1] = confParams[1];
            args[2] = confParams[2];
        }

        if(!"".equals(xmlPath) && xmlPath != null && args != null && args.length >= 1) {
            Document doc = readXml(xmlPath);
            Element root = doc.getRootElement();
            if(args[0] != null) {
                List regElement = root.elements(args[0]);
                Iterator var9 = regElement.iterator();

                while(var9.hasNext()) {
                    Element ab = (Element)var9.next();
                    ab.content();
                    StringBuffer sb = new StringBuffer();
                    sb.append(ab.attribute(args[1]).getValue() + "@");
                    sb.append(ab.attribute(args[2]).getValue() + "@");
                    list.add(sb.toString());
                }
            }
        }

        return list;
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

    public static List<String> getConfNameList(String folderPath, String type) {
        File dir = new File(folderPath);
        ArrayList list = new ArrayList();
        String ty = "XML";
        if(!"".equals(type) || type != null) {
            ty = type;
        }

        if(dir.exists()) {
            File[] tmp = dir.listFiles();

            for(int i = 0; i < tmp.length; ++i) {
                if(tmp[i].isDirectory()) {
                    getConfNameList(tmp[i].getAbsolutePath(), ty);
                } else if(tmp[i].getName().toUpperCase().endsWith(ty.toUpperCase())) {
                    list.add(tmp[i].getAbsolutePath().toString());
                }
            }
        }

        return list;
    }

    public String initRequestData(Method method) {
        String str = null;
        if(method != null) {
            Class[] clazzs = method.getParameterTypes();
            if(clazzs != null && clazzs.length >= 1) {
                Class[] var7 = clazzs;
                int var6 = clazzs.length;

                for(int var5 = 0; var5 < var6; ++var5) {
                    Class clazz = var7[var5];

                    try {
                        Object obj = clazz.newInstance();
                        str = JSON.toJSONString(obj);
                    } catch (InstantiationException var10) {
                        var10.printStackTrace();
                    } catch (IllegalAccessException var11) {
                        var11.printStackTrace();
                    }
                }
            }
        }

        return str;
    }

    public static List<ApplicationServices> getAppServicesList(String fileFolder, String[] confParams) {
        new ArrayList();
        List list = getConfNameList(fileFolder, "XML");
        ArrayList appServices = new ArrayList();
        Iterator var5 = list.iterator();

        while(var5.hasNext()) {
            String path = (String)var5.next();

            try {
                new ApplicationServices();
                ApplicationServices e = getServiceList4Application(path, confParams);
                if(e != null) {
                    appServices.add(e);
                }
            } catch (DocumentException var7) {
                var7.printStackTrace();
            }
        }

        return appServices;
    }

    public static void main(String[] args) {
        new DubboSampleUtil();

        try {
            ApplicationServices e = getServiceList4Application("dubbo-hello-consumer.xml", (String[])null);
        } catch (DocumentException var3) {
            var3.printStackTrace();
        }

    }
}

