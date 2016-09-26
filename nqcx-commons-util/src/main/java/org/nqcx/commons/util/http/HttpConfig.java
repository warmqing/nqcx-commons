/*
 * Copyright 2015 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.util.http;

/**
 * @author 黄保光 Oct 29, 2013 2:29:31 PM
 */
public class HttpConfig {

    public static String CHARESET = "UTF-8";
    public static int CONNECTION_TIMEOUT = 5000;
    public static int SOCKET_TIMEOUT = 10000;

    public void setChareset(String chareset) {
        CHARESET = chareset;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        CONNECTION_TIMEOUT = connectionTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        SOCKET_TIMEOUT = socketTimeout;
    }
}
