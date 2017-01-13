/*
 * Copyright 2015  ChineseAll Inc. All right reserved. This software is the confidential and proprietary information of ChineseAll Inc.
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with ChineseAll Inc.
 */

package org.nqcx.commons.util.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;

/**
 * @author naqichuan 2014年8月14日 上午11:49:38
 */
public class AESUtils {

    private final static String DEFAULT_CHARSET = "UTF-8";
    private final static String DEFAULT_KEY = "HP1ozhw3WrhSIo2X";

    /**
     * 加密
     *
     * @param text
     * @return
     */
    public static String encrypt(String text) {
        return encrypt(text, DEFAULT_KEY);
    }

    /**
     * 加密
     *
     * @param text
     * @param key
     * @return
     */
    public static String encrypt(String text, String key) {
        try {
            return HexUtils.toHexString(encrypt(text.getBytes(DEFAULT_CHARSET), key));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param bytes
     * @return
     */
    public static byte[] encrypt(byte[] bytes) {
        return encrypt(bytes, DEFAULT_KEY);
    }


    /**
     * 加密
     *
     * @param bytes
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] bytes, String key) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            byteFina = cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }


    /**
     * 解密
     *
     * @param text
     * @return
     */
    public static String decrypt(String text) {
        return decrypt(text, DEFAULT_KEY);
    }

    /**
     * 解密
     *
     * @param text
     * @param key
     * @return
     */
    public static String decrypt(String text, String key) {
        return new String(decrypt(HexUtils.toByteArray(text), key));
    }

    /**
     * 解密
     *
     * @param bytes
     * @return
     */
    public static byte[] decrypt(byte[] bytes) {
        return decrypt(bytes, DEFAULT_KEY);
    }


    /**
     * 解密
     *
     * @param bytes
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] bytes, String key) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            byteFina = cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * @param key
     * @return
     */
    private static Key getKey(String key) {
        if (key == null || key.length() == 0)
            key = DEFAULT_KEY;
        try {
            return new SecretKeySpec(key.getBytes(), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("bookId", "1111112");
//        map.put("bs", "basic");
//        map.put("filetype", "TXT");
//        map.put("timestamp", new Date().getTime());
//        String json = JsonUtils.mapToJson(map);
//        String idEncrypt = encrypt(json);
//        System.out.println(idEncrypt);
//        String idDecrypt = decrypt(idEncrypt);
//        System.out.println(idDecrypt);


        String aa = decrypt("341dcf3a5f19eafffa76b41d33bd47d0", "62csRnq0lFiq7uAF");
        System.out.println(aa);
        String bb = encrypt("1", "1IphGIeSwIOPRLlX");
        System.out.println(bb);
    }
}
