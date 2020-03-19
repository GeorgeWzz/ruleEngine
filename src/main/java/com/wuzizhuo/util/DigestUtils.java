package com.wuzizhuo.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: wuzizhuo
 * @created: 2020/3/12.
 * @updater:
 * @description:
 */
public class DigestUtils {

    public static String stringToMD5(String plainText, String charsetName) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes(charsetName));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("md5编码出错");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
}
