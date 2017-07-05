package com.et.util;

/**
 * Created by shatao on 5/7/2017.
 */
public class Common {
    public static String byte2hex(byte[] bytes) {
        StringBuilder hs = new StringBuilder();
        for (byte b : bytes)
            hs.append(String.format("%1$02X", b));
        return hs.toString();
    }

    public static byte[] hex2byte(String content) {
        int l = content.length() >> 1;
        byte[] result = new byte[l];
        for (int i = 0; i < l; i++) {
            int j = i << 1;
            String s = content.substring(j, j + 2);
            result[i] = Integer.valueOf(s, 16).byteValue();
        }
        return result;
    }
}
