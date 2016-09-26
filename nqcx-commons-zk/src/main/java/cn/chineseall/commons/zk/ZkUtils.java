/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package cn.chineseall.commons.zk;

/**
 * @author naqichuan 16/2/29 13:16
 */
public class ZkUtils {

    public static String getLastPath(String path) {
        if (path == null || path.length() == 0 || path.indexOf("/") == -1)
            return path;

        return path.substring(path.lastIndexOf(path) + 1);
    }
}
