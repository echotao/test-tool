package com.et.common.reflect;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * Created by shatao on 13/6/2017.
 */
public final class ClassFinder {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String DOT_JAR = ".jar";
    private static final String DOT_CLASS = ".class";
    private static final int DOT_CLASS_LEN = ".class".length();

    private ClassFinder() {
    }

    public static List<String> findClassesThatExtend(String[] paths, Class<?>[] superClasses) throws IOException {
        return findClassesThatExtend(paths, superClasses, false);
    }

    private static String[] addJarsInPath(String[] paths) {
        HashSet fullList = new HashSet();

        for(int i = 0; i < paths.length; ++i) {
            String path = paths[i];
            fullList.add(path);
            if(!path.endsWith(".jar")) {
                File dir = new File(path);
                if(dir.exists() && dir.isDirectory()) {
                    String[] jars = dir.list(new FilenameFilter() {
                        public boolean accept(File f, String name) {
                            return name.endsWith(".jar");
                        }
                    });

                    for(int x = 0; x < jars.length; ++x) {
                        fullList.add(jars[x]);
                    }
                }
            }
        }

        return (String[])fullList.toArray(new String[fullList.size()]);
    }

    public static List<String> findClassesThatExtend(String[] strPathsOrJars, Class<?>[] superClasses, boolean innerClasses) throws IOException {
        return findClassesThatExtend(strPathsOrJars, superClasses, innerClasses, (String)null, (String)null);
    }

    public static List<String> findClassesThatExtend(String[] strPathsOrJars, Class<?>[] superClasses, boolean innerClasses, String contains, String notContains) throws IOException {
        return findClassesThatExtend(strPathsOrJars, superClasses, innerClasses, contains, notContains, false);
    }

    public static List<String> findAnnotatedClasses(String[] strPathsOrJars, Class<? extends Annotation>[] annotations, boolean innerClasses) throws IOException {
        return findClassesThatExtend(strPathsOrJars, annotations, innerClasses, (String)null, (String)null, true);
    }

    public static List<String> findAnnotatedClasses(String[] strPathsOrJars, Class<? extends Annotation>[] annotations) throws IOException {
        return findClassesThatExtend(strPathsOrJars, annotations, false, (String)null, (String)null, true);
    }

    public static List<String> findClassesThatExtend(String[] searchPathsOrJars, Class<?>[] classNames, boolean innerClasses, String contains, String notContains, boolean annotations) throws IOException {
        if(log.isDebugEnabled()) {
            log.debug("searchPathsOrJars : " + Arrays.toString(searchPathsOrJars));
            log.debug("superclass : " + Arrays.toString(classNames));
            log.debug("innerClasses : " + innerClasses + " annotations: " + annotations);
            log.debug("contains: " + contains + " notContains: " + notContains);
        }

        String[] strPathsOrJars = addJarsInPath(searchPathsOrJars);

        for(int listPaths = 0; listPaths < strPathsOrJars.length; ++listPaths) {
            strPathsOrJars[listPaths] = fixPathEntry(strPathsOrJars[listPaths]);
        }

        List var12 = getClasspathMatches(strPathsOrJars);
        if(log.isDebugEnabled()) {
            Iterator listClasses = var12.iterator();

            while(listClasses.hasNext()) {
                String annoclassNames = (String)listClasses.next();
                log.debug("listPaths : " + annoclassNames);
            }
        }

        Object var13;
        if (classNames.getClass().isAnnotation()) {
            (annotations) var13 = new AnnoFilterTreeSet((Annotation)classNames, innerClasses);
            //if (annotations) var13 = new AnnoFilterTreeSet(classNames, innerClasses);
        }
        else
            var13 = new FilterTreeSet(classNames, innerClasses, contains, notContains);
        findClassesInPaths(var12, (Set)var13);
        if(log.isDebugEnabled()) {
            log.debug("listClasses.size()=" + ((Set)var13).size());
            Iterator var11 = ((Set)var13).iterator();

            while(var11.hasNext()) {
                String clazz = (String)var11.next();
                log.debug("listClasses : " + clazz);
            }
        }

        return new ArrayList((Collection)var13);
    }

    private static List<String> getClasspathMatches(String[] strPathsOrJars) {
        String javaClassPath = System.getProperty("java.class.path");
        StringTokenizer stPaths = new StringTokenizer(javaClassPath, File.pathSeparator);
        if(log.isDebugEnabled()) {
            log.debug("Classpath = " + javaClassPath);

            for(int listPaths = 0; listPaths < strPathsOrJars.length; ++listPaths) {
                log.debug("strPathsOrJars[" + listPaths + "] : " + strPathsOrJars[listPaths]);
            }
        }

        ArrayList var7 = new ArrayList();
        String strPath = null;

        while(true) {
            while(stPaths.hasMoreTokens()) {
                strPath = fixPathEntry(stPaths.nextToken());
                if(strPathsOrJars == null) {
                    log.debug("Adding: " + strPath);
                    var7.add(strPath);
                } else {
                    boolean found = false;

                    for(int i = 0; i < strPathsOrJars.length; ++i) {
                        if(strPath.endsWith(strPathsOrJars[i])) {
                            found = true;
                            log.debug("Adding " + strPath + " found at " + i);
                            var7.add(strPath);
                            break;
                        }
                    }

                    if(!found) {
                        log.debug("Did not find: " + strPath);
                    }
                }
            }

            return var7;
        }
    }

    private static String fixPathEntry(String path) {
        if(path == null) {
            return null;
        } else if(path.equals(".")) {
            return System.getProperty("user.dir");
        } else {
            path = path.trim().replace('\\', '/');

            for(path = JOrphanUtils.substitute(path, "//", "/"); path.endsWith("/"); path = path.substring(0, path.length() - 1)) {
                ;
            }

            return path;
        }
    }

    private static boolean isChildOf(Class<?>[] parentClasses, String strClassName, ClassLoader contextClassLoader) {
        try {
            Class ignored = Class.forName(strClassName, false, contextClassLoader);
            if(!ignored.isInterface() && !Modifier.isAbstract(ignored.getModifiers())) {
                for(int i = 0; i < parentClasses.length; ++i) {
                    if(parentClasses[i].isAssignableFrom(ignored)) {
                        return true;
                    }
                }
            }
        } catch (UnsupportedClassVersionError var5) {
            log.debug(var5.getLocalizedMessage());
        } catch (NoClassDefFoundError var6) {
            log.debug(var6.getLocalizedMessage());
        } catch (ClassNotFoundException var7) {
            log.debug(var7.getLocalizedMessage());
        }

        return false;
    }

    private static boolean hasAnnotationOnMethod(Class<? extends Annotation>[] annotations, String classInQuestion, ClassLoader contextClassLoader) {
        try {
            Class ignored = Class.forName(classInQuestion, false, contextClassLoader);
            Method[] var7;
            int var6 = (var7 = ignored.getMethods()).length;

            for(int var5 = 0; var5 < var6; ++var5) {
                Method method = var7[var5];
                Class[] var11 = annotations;
                int var10 = annotations.length;

                for(int var9 = 0; var9 < var10; ++var9) {
                    Class annotation = var11[var9];
                    if(method.isAnnotationPresent(annotation)) {
                        return true;
                    }
                }
            }
        } catch (NoClassDefFoundError var12) {
            log.debug(var12.getLocalizedMessage());
        } catch (ClassNotFoundException var13) {
            log.debug(var13.getLocalizedMessage());
        }

        return false;
    }

    private static String fixClassName(String strClassName) {
        strClassName = strClassName.replace('\\', '.');
        strClassName = strClassName.replace('/', '.');
        strClassName = strClassName.substring(0, strClassName.length() - DOT_CLASS_LEN);
        return strClassName;
    }

    private static void findClassesInOnePath(String strPath, Set<String> listClasses) throws IOException {
        File file = new File(strPath);
        if(file.isDirectory()) {
            findClassesInPathsDir(strPath, file, listClasses);
        } else if(file.exists()) {
            ZipFile zipFile = null;

            try {
                zipFile = new ZipFile(file);
                Enumeration e = zipFile.entries();

                while(e.hasMoreElements()) {
                    String strEntry = ((ZipEntry)e.nextElement()).toString();
                    if(strEntry.endsWith(".class")) {
                        listClasses.add(fixClassName(strEntry));
                    }
                }
            } catch (IOException var14) {
                log.warn("Can not open the jar " + strPath + " " + var14.getLocalizedMessage(), var14);
            } finally {
                if(zipFile != null) {
                    try {
                        zipFile.close();
                    } catch (Exception var13) {
                        ;
                    }
                }

            }
        }

    }

    private static void findClassesInPaths(List<String> listPaths, Set<String> listClasses) throws IOException {
        Iterator var3 = listPaths.iterator();

        while(var3.hasNext()) {
            String path = (String)var3.next();
            findClassesInOnePath(path, listClasses);
        }

    }

    private static void findClassesInPathsDir(String strPathElement, File dir, Set<String> listClasses) throws IOException {
        String[] list = dir.list();

        for(int i = 0; i < list.length; ++i) {
            File file = new File(dir, list[i]);
            if(file.isDirectory()) {
                findClassesInPathsDir(strPathElement, file, listClasses);
            } else if(list[i].endsWith(".class") && file.exists() && file.length() != 0L) {
                String path = file.getPath();
                listClasses.add(path.substring(strPathElement.length() + 1, path.lastIndexOf(46)).replace(File.separator.charAt(0), '.'));
            }
        }

    }

    private static class AnnoFilterTreeSet extends TreeSet<String> {
        private static final long serialVersionUID = 240L;
        private final boolean inner;
        private final Class<? extends Annotation>[] annotations;
        private final transient ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        AnnoFilterTreeSet(Class<? extends Annotation>[] annotations, boolean inner) {
            this.annotations = annotations;
            this.inner = inner;
        }

        public boolean add(String s) {
            return this.contains(s)?false:((s.indexOf(36) == -1 || this.inner) && ClassFinder.hasAnnotationOnMethod(this.annotations, s, this.contextClassLoader)?super.add(s):false);
        }
    }

    private static class FilterTreeSet extends TreeSet<String> {
        private static final long serialVersionUID = 234L;
        private final Class<?>[] parents;
        private final boolean inner;
        private final String contains;
        private final String notContains;
        private final transient ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        FilterTreeSet(Class<?>[] parents, boolean inner, String contains, String notContains) {
            this.parents = parents;
            this.inner = inner;
            this.contains = contains;
            this.notContains = notContains;
        }

        public boolean add(String s) {
            return this.contains(s)?false:(this.contains != null && s.indexOf(this.contains) == -1?false:(this.notContains != null && s.indexOf(this.notContains) != -1?false:((s.indexOf(36) == -1 || this.inner) && ClassFinder.isChildOf(this.parents, s, this.contextClassLoader)?super.add(s):false)));
        }
    }
}

