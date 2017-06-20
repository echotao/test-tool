package com.et.common.reflect;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by shatao on 13/6/2017.
 */
public class Reflect2JmeterSample<T> {
    private static Class searchClassObj = null;

    public Reflect2JmeterSample() {
    }

    public static List getInterfaceNameList(Class clazz) {
        ArrayList interfaceList = new ArrayList();
        Method[] me = clazz.getMethods();
        Method[] var6 = me;
        int var5 = me.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            Method md = var6[var4];
            interfaceList.add(md.getName());
        }

        return interfaceList;
    }

    public static Method[] getInterfaceList(Class clazz) {
        Method[] me = clazz.getMethods();
        return me;
    }

    public static <T> Method getMethod(Class<T> service, String methodName) {
        Method method = null;
        Method[] mt = service.getDeclaredMethods();
        Method[] var7 = mt;
        int var6 = mt.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            Method md = var7[var5];
            if(methodName.equals(md.getName())) {
                method = md;
                break;
            }
        }

        return method;
    }

    public static List<Class> getAllClassByInterfaceFromPackage(String className, String jarPath) {
        ArrayList returnClassList = new ArrayList();

        try {
            List e = getPackageClasses(jarPath, className);
            if(searchClassObj != null) {
                Iterator var5 = e.iterator();

                while(var5.hasNext()) {
                    Class c = (Class)var5.next();
                    if(searchClassObj.isAssignableFrom(c) && !searchClassObj.equals(c)) {
                        returnClassList.add(c);
                    }
                }
            }
        } catch (ClassNotFoundException var6) {
            var6.printStackTrace();
        }

        return returnClassList;
    }

    public static List<Class> getAllClassByInterfaceFromJar(String className, String jarPath) {
        ArrayList returnClassList = new ArrayList();

        try {
            List e = getJarClasses(jarPath, className);
            if(searchClassObj != null) {
                for(int i = 0; i < e.size(); ++i) {
                    if(searchClassObj.isAssignableFrom((Class)e.get(i)) && !searchClassObj.equals(e.get(i))) {
                        returnClassList.add((Class)e.get(i));
                    }
                }
            }
        } catch (ClassNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return returnClassList;
    }

    public static List<Class> getPackageClasses(String packageName, String className) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        ArrayList classes = new ArrayList();

        try {
            Enumeration resources = classLoader.getResources(path);
            ArrayList e = new ArrayList();

            while(resources.hasMoreElements()) {
                URL directory = (URL)resources.nextElement();
                e.add(new File(directory.getFile()));
            }

            Iterator var8 = e.iterator();

            while(var8.hasNext()) {
                File directory1 = (File)var8.next();
                classes.addAll(findClasses(directory1, packageName, className));
            }
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return classes;
    }

    public static List<Class> findClasses(File directory, String packageName, String className) throws ClassNotFoundException {
        ArrayList classes = new ArrayList();
        if(!directory.exists()) {
            return classes;
        } else {
            File[] files = directory.listFiles();
            File[] var8 = files;
            int var7 = files.length;

            for(int var6 = 0; var6 < var7; ++var6) {
                File file = var8[var6];
                if(file.isDirectory()) {
                    assert !file.getName().contains(".");

                    classes.addAll(findClasses(file, packageName + "." + file.getName(), className));
                } else if(file.getName().endsWith(".class")) {
                    Class tempObj = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                    if(file.getName().equals(className + ".class")) {
                        searchClassObj = tempObj;
                    }

                    classes.add(tempObj);
                }
            }

            return classes;
        }
    }

    public static List<Class> getJarClasses(String jarPath, String className) throws ClassNotFoundException, IOException {
        Vector result = new Vector();
        JarFile jarFile = new JarFile(jarPath);
        Enumeration files = jarFile.entries();

        while(files.hasMoreElements()) {
            JarEntry entry = (JarEntry)files.nextElement();
            String entryName = entry.getName();
            if(entryName.endsWith(".class") && entryName.indexOf("$") < 1) {
                URL url = new URL("jar:file:" + jarPath + "!/" + entryName);
                new URLClassLoader(new URL[]{url});
                String classNamePackage = entryName.substring(0, entryName.lastIndexOf(".")).replaceAll("/", ".");
                Class c = Class.forName(classNamePackage);
                result.add(c);
                if(entryName.substring(entryName.lastIndexOf("/") + 1).equals(className + ".class")) {
                    searchClassObj = c;
                }
            }
        }

        return result;
    }

    public static List<Class> getInterfaceList(String className, String jarPath) throws ClassNotFoundException, IOException {
        Vector result = new Vector();
        JarFile jarFile = new JarFile(jarPath);
        Enumeration files = jarFile.entries();

        while(files.hasMoreElements()) {
            JarEntry entry = (JarEntry)files.nextElement();
            String entryName = entry.getName();
            if(entryName.endsWith(".class") && entryName.indexOf("$") < 1) {
                URL url = new URL("jar:file:" + jarPath + "!/" + entryName);
                new URLClassLoader(new URL[]{url});
                String classNamePackage = entryName.substring(entryName.lastIndexOf("/") + 1, entryName.lastIndexOf("."));
                if(classNamePackage.equals(className)) {
                    classNamePackage = entryName.substring(0, entryName.lastIndexOf(".")).replaceAll("/", ".");
                    Class c = Class.forName(classNamePackage);
                    result.add(c);
                    break;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            List clazzList = getInterfaceList("CacheService", "D:\\pas-benchmark-0.0.1-SNAPSHOT-package\\lib\\ctg-cache-control-1.4.3-SNAPSHOT.jar");
            Class e1 = (Class)clazzList.get(0);
            Method[] me = e1.getMethods();
            Method[] var7 = me;
            int var6 = me.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                Method mm = var7[var5];
                System.out.println(mm.getName());
            }
        } catch (ClassNotFoundException var8) {
            var8.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }
}
