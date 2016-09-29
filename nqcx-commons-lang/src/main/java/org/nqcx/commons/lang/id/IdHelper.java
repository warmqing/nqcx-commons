/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.lang.id;

import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * @author naqichuan 16/5/30 14:50
 */
public class IdHelper {

    //private static String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String ALPHABET = "1fGyLYQ3qHrc2M6j8mdREba90tKZBTi4vWsgpShouJzwlCeAFO7nU5IkPDNXxV";

    /**
     * 数字转字符串
     *
     * @param input
     * @return
     */
    public static String toAlphabet(long input) {
        int alphabetLen = ALPHABET.length();

        input = mov(input);

        String output = new String();
        for (int i = (int) log(input, alphabetLen); i >= 0; i--) {
            long pow = (long) Math.pow((double) alphabetLen, (double) i);
            int position = (int) (input / pow) % alphabetLen;
            input -= (position * pow);

            output += ALPHABET.substring(position, position + 1);
        }
        return StringUtils.reverse(output);
    }

    /**
     * 字符串转成数字
     *
     * @param input
     * @return
     */
    public static long toDigital(String input) {
        int alphabetLen = ALPHABET.length();

        input = StringUtils.reverse(input);
        int inputLen = input.length() - 1;

        long output = 0;
        for (int i = 0; i <= inputLen; i++) {
            long pow = (long) Math.pow((double) alphabetLen, (double) (inputLen - i));
            output += (ALPHABET.indexOf(input.substring(i, i + 1)) * pow);
        }
        return remov(output);
    }

    /**
     * 对数
     *
     * @param value
     * @param base
     * @return
     */
    private static double log(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    /**
     * 移位操作，用于数字混淆
     *
     * @param input
     * @return
     */
    private static long mov(long input) {
        long output = (input & 0xff000000);
        output += (input & 0x0000ff00) << 8;
        output += (input & 0x00ff0000) >> 8;
        output += (input & 0x0000000f) << 4;
        output += (input & 0x000000f0) >> 4;
        output ^= 31415926;
        return output;
    }

    /**
     * 还原移位操作，用于数字混淆
     *
     * @param input
     * @return
     */
    private static long remov(long input) {
        input ^= 31415926;
        long output = (input & 0xff000000);
        output += (input & 0x00ff0000) >> 8;
        output += (input & 0x0000ff00) << 8;
        output += (input & 0x000000f0) >> 4;
        output += (input & 0x0000000f) << 4;
        return output;
    }

    /**
     * 将 ALPHABET 顺序打乱
     *
     * @return
     */
    private static String newAlphabet() {
        char[] alphabetChars = ALPHABET.toCharArray();
        Random random = new Random();
        int rInt = 0;
        char mChar = 0;
        for (int i = 0; i < alphabetChars.length; i++) {
            rInt = random.nextInt(alphabetChars.length);
            mChar = alphabetChars[i];
            alphabetChars[i] = alphabetChars[rInt];
            alphabetChars[rInt] = mChar;
        }

        StringBuffer news = new StringBuffer();
        for (int i = 0; i < alphabetChars.length; i++) {
            news.append(alphabetChars[i]);
        }

        return news.toString();
    }

    /**
     * 用于在程序里配置编码因子
     *
     * @param alphabet
     */
    public void setAlphabet(String alphabet) {
        IdHelper.ALPHABET = alphabet;
    }

    public static void main(String[] args) {
        System.out.println(toAlphabet(Long.MAX_VALUE));
        System.out.println(toAlphabet(1));
        System.out.println(toAlphabet(2));
        System.out.println(toAlphabet(3));
        System.out.println(toAlphabet(100));

        System.out.println(toDigital("SOWpUfnbDsr"));
        System.out.println(toDigital("01vYd"));
        System.out.println(toDigital("VevYd"));
        System.out.println(toDigital("1evYd"));
        System.out.println(toDigital("jevYd"));

        // 将 ALPHABET 顺序打乱
        // System.out.println(newAlphabet());

    }
}
