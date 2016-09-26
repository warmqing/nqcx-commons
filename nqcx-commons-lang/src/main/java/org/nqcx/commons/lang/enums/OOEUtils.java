/*
 * Copyright 2015 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.lang.enums;

/**
 * Object operate enum
 *
 * @author naqichuan 15/11/2 10:58
 */
public class OOEUtils {

    /**
     * 判断指定的 ooe 是否在 ooes 数组中
     *
     * @param ooes
     * @param ooe
     * @param <T>
     * @return
     */
    public static <T> boolean contain(T[] ooes, T ooe) {
        if (ooes == null || ooe == null)
            return false;

        for (T n : ooes) {
            if (n != null && n == ooe)
                return true;
        }

        return false;
    }
}
