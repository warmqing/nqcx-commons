/*
 * Copyright 2014 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.util.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author naqichuan 2014年8月14日 上午11:50:00
 */
public class ServerUtil {

    private final static Logger logger = LoggerFactory.getLogger(ServerUtil.class);

    /**
     * 取服务器IP
     *
     * @return
     */
    public static String serverIp() {
        String serverip = "0.0.0.0";
        try {
            InetAddress inet = InetAddress.getLocalHost();
            serverip = inet.getHostAddress();
        } catch (Exception e) {
            logger.error("", e);
        }
        return serverip;
    }

    /**
     * 取服务器IPS
     *
     * @return
     */
    public static Set<String> serverIps() {
        try {
            Set<String> ips = new HashSet<String>();
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        ips.add(ip.getHostAddress());
                    }
                }
            }
            return ips;
        } catch (SocketException e) {
            logger.error("", e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(ServerUtil.serverIp());
        System.out.println(ServerUtil.serverIps());
    }
}
