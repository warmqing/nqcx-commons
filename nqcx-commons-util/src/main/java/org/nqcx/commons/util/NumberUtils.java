/*
 * Copyright 2014 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.util;

import java.util.regex.Pattern;

/**
 * @author naqichuan 2014年9月9日 下午12:46:12
 */
public class NumberUtils {

    public static String NUMBER_PATTERN = "[0-9]*";

    public static boolean isNumber(String str) {
        return str == null ? false : Pattern.compile(NUMBER_PATTERN).matcher(str).matches();
    }

    public static void main(String[] args) {
        String testString = "12";

        System.out.println(NumberUtils.isNumber(testString));
    }
}
