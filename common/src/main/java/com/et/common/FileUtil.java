package com.et.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * Created by shatao on 13/6/2017.
 */
public class FileUtil {
    static final Log log = LogFactory.getLog(FileUtil.class);

    public FileUtil() {
    }

    public static void createDir(String path) {
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdir();
        }

    }

    public static void createFile(String path, String filename) throws IOException {
        File file = new File(path + "/" + filename);
        if(!file.exists()) {
            file.createNewFile();
        }

    }

    public static void delFile(String path, String filename) {
        File file = new File(path + "/" + filename);
        if(file.exists() && file.isFile()) {
            file.delete();
        }

    }

    public static void delDir(String path) {
        File dir = new File(path);
        if(dir.exists()) {
            File[] tmp = dir.listFiles();

            for(int i = 0; i < tmp.length; ++i) {
                if(tmp[i].isDirectory()) {
                    delDir(path + "/" + tmp[i].getName());
                } else {
                    tmp[i].delete();
                }
            }

            dir.delete();
        }

    }

    public static void copyFile(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        File file = new File(dest);
        if(!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024];

        int c;
        while((c = in.read(buffer)) != -1) {
            for(int i = 0; i < c; ++i) {
                out.write(buffer[i]);
            }
        }

        in.close();
        out.close();
    }

    public static void PrintStreamDemo() {
        try {
            FileOutputStream e = new FileOutputStream("d:/test.txt");
            PrintStream p = new PrintStream(e);

            for(int i = 0; i < 10; ++i) {
                p.println("This is " + i + " line");
            }
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        }

    }

    public static void StringBufferDemo() throws IOException {
        File file = new File("d:/test.txt");
        if(!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(file, true);
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < 100; ++i) {
            sb.append("这是第" + i + "行");
            out.write(sb.toString().getBytes("utf-8"));
        }

        out.close();
    }

    public static void renameFile(String path, String oldname, String newname) {
        if(!oldname.equals(newname)) {
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if(newfile.exists()) {
                System.out.println(newname + "已经存在！");
            } else {
                oldfile.renameTo(newfile);
            }
        }

    }

    public static void changeDirectory(String filename, String oldpath, String newpath, boolean cover) {
        if(!oldpath.equals(newpath)) {
            File oldfile = new File(oldpath + "/" + filename);
            File newfile = new File(newpath + "/" + filename);
            if(newfile.exists()) {
                if(cover) {
                    oldfile.renameTo(newfile);
                } else {
                    System.out.println("在新目录下已经存在：" + filename);
                }
            } else {
                oldfile.renameTo(newfile);
            }
        }

    }

    public static String FileInputStreamDemo(String path) throws IOException {
        File file = new File(path);
        if(file.exists() && !file.isDirectory()) {
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024];

            StringBuffer sb;
            for(sb = new StringBuffer(); fis.read(buf) != -1; buf = new byte[1024]) {
                sb.append(new String(buf));
            }

            return sb.toString();
        } else {
            throw new FileNotFoundException();
        }
    }

    public static String BufferedReaderDemo(String path) throws IOException {
        File file = new File(path);
        if(file.exists() && !file.isDirectory()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp = null;
            StringBuffer sb = new StringBuffer();

            for(temp = br.readLine(); temp != null; temp = br.readLine()) {
                sb.append(temp + " ");
            }

            return sb.toString();
        } else {
            throw new FileNotFoundException();
        }
    }

    public static List<String> readText(String path) throws IOException {
        File file = new File(path);
        if(file.exists() && !file.isDirectory()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp = null;
            temp = br.readLine();

            ArrayList list;
            for(list = new ArrayList(); temp != null; temp = br.readLine()) {
                list.add(temp.split("@")[0]);
            }

            br.close();
            return list;
        } else {
            throw new FileNotFoundException();
        }
    }

    public static Document readXml(String path) throws DocumentException, IOException {
        File file = new File(path);
        BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
        SAXReader saxreader = new SAXReader();
        Document document = saxreader.read(bufferedreader);
        bufferedreader.close();
        return document;
    }

    public static List<String> readTxtFile(String filePath) {
        ArrayList list = new ArrayList();
        String encoding = "UTF-8";
        File file = new File(filePath);
        if(file.isFile() && file.exists()) {
            try {
                InputStreamReader e = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(e);
                String lineTxt = null;

                while((lineTxt = bufferedReader.readLine()) != null) {
                    list.add(lineTxt);
                }

                bufferedReader.close();
            } catch (UnsupportedEncodingException var7) {
                var7.printStackTrace();
            } catch (FileNotFoundException var8) {
                log.info("not found file");
                var8.printStackTrace();
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

        return list;
    }

    public static List<String> replaceStr(String filePath, String oldStr, String newStr, String startIndex) {
        List list = null;
        ArrayList dList = new ArrayList();
        list = readTxtFile(filePath);
        String[] sourceStr = oldStr.split(",");
        String[] destStr = newStr.split(",");
        boolean i = false;
        int j = sourceStr.length;
        int flag = 1;
        Iterator var12 = list.iterator();

        while(true) {
            while(var12.hasNext()) {
                String str = (String)var12.next();
                if(flag++ < Integer.valueOf(startIndex).intValue()) {
                    dList.add(str);
                } else {
                    for(int var13 = 0; var13 < j; ++var13) {
                        str = str.replaceAll(sourceStr[var13], destStr[var13]);
                    }

                    log.info(str);
                    dList.add(str);
                }
            }

            return dList;
        }
    }

    public static void writeFile(String filePath, List<String> list) {
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"), true);
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
                String e = (String)var4.next();
                pw.print(e);
            }

            pw.flush();
            pw.close();
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        }

    }

    public static List<String> readLine(String path) throws IOException {
        File file = new File(path);
        if(file.exists() && !file.isDirectory()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp = null;
            temp = br.readLine();

            ArrayList list;
            for(list = new ArrayList(); temp != null; temp = br.readLine()) {
                list.add(temp);
            }

            br.close();
            return list;
        } else {
            throw new FileNotFoundException();
        }
    }

    public static Properties getProperties(String path, Properties prop) {
        if(path != null && path.length() > 1) {
            InputStream in = FileUtil.class.getResourceAsStream(path);

            try {
                prop.load(in);
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

        return prop;
    }

    public static void result(String filePathAbs, String data) {
        String filePath = filePathAbs;
        if(filePathAbs == null) {
            filePath = System.getProperty("user.dir") + File.separatorChar + "result.log";
        }

        File file = new File(filePath);
        writeData(file, data);
    }

    public static synchronized void writeData(File file, String data) {
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(data);
            fw.flush();
            fw.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new FileUtil();
        List list = replaceStr("D:\\java\\jmeter\\result\\Test.jtl", "</sample>,\">", ",\"/>", "3");
        writeFile("D:\\java\\jmeter\\result\\Test.jtl", list);
    }
}

