package com.et.jmeterext;

import com.et.util.Common;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shatao on 5/7/2017.
 */
public class EncryptUtil {
    //private static String CRYPT_KEY = "somekey"; //Static, change me
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("cryptKey", "somekey");
        params.addArgument("userId", "123456");
        return params;
    }

    public SampleResult runTest(JavaSamplerContext arg0) {
        String userId = arg0.getParameter("userId");
        String key = arg0.getParameter("cryptKey");
        long currentTimeMills = System.currentTimeMillis();
        String content = userId + "&" + currentTimeMills;
        SampleResult sr = new SampleResult();
        try {
            String result = encrypt(key,content);
            sr.setResponseData(result.getBytes());
            sr.setResponseMessage(result);
            sr.setResponseCode(userId);
            sr.setSuccessful(true);
        } catch (Exception e) {
            sr.setSuccessful(false);
            sr.setResponseCode("500");
            sr.setResponseData(e.getStackTrace().toString().getBytes());
            e.printStackTrace();
        }
        return sr;
    }

    public static String encrypt(String key, String content) throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append(key).append(sdf.format(new Date()));
        String KEY = DigestUtils.md5DigestAsHex(buf.toString().getBytes()).toUpperCase();
        KEY = KEY.substring(0, 8);
        byte[] bytes = KEY.getBytes();
        DESKeySpec ks = new DESKeySpec(bytes);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(ks);
        IvParameterSpec iv2 = new IvParameterSpec(bytes);

        Cipher ecip = Cipher.getInstance("DES/CBC/PKCS5Padding");
        ecip.init(Cipher.ENCRYPT_MODE, sk, iv2);
        byte[] bytes1 = ecip.doFinal(content.getBytes("ascii"));
        return Common.byte2hex(bytes1);
    }

    public static String decrypt(String key, String content) throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append(key).append(sdf.format(new Date()));
        String KEY = DigestUtils.md5DigestAsHex(buf.toString().getBytes()).toUpperCase();
        KEY = KEY.substring(0, 8);
        byte[] bytes = KEY.getBytes();
        DESKeySpec ks = new DESKeySpec(bytes);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(ks);
        IvParameterSpec iv2 = new IvParameterSpec(bytes);
        Cipher dcip = Cipher.getInstance("DES/CBC/PKCS5Padding");
        dcip.init(Cipher.DECRYPT_MODE, sk, iv2);
        byte[] bytes1 = Common.hex2byte(content);
        bytes = dcip.doFinal(bytes1);
        return new String(bytes, "ascii");
    }

    // test
    public static void main(String[] args) throws Exception {
        String password = "202991";
        String CRYPT_KEY = "someotherkey"; //Static, change me
        String en = encrypt(CRYPT_KEY,password);
        System.out.println(en);
        System.out.println(decrypt(CRYPT_KEY,"309D3066F1A4F672"));
    }

}
