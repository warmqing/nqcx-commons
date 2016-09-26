/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package cn.chineseall.commons.zk;

/**
 * @author naqichuan 16/2/29 12:55
 */
public class ZkConfig {

    private String connetString = "localhost:2181,localhost:2182,localhost:2183";
    private int sessionTimeout = 20000;

    public ZkConfig() {

    }

    public ZkConfig(String connetString, int sessionTimeout) {
        this.connetString = connetString;
        this.sessionTimeout = sessionTimeout;
    }

    public String getConnetString() {
        return connetString;
    }

    public void setConnetString(String connetString) {
        this.connetString = connetString;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
