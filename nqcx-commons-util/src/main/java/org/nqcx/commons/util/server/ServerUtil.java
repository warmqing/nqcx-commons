/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.util.server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author nqcx 2013-4-3 下午6:02:53
 * 
 */
public class ServerUtil {
	public static String serverIp() {
		String serverip = "0.0.0.0";
		try {
			InetAddress inet = InetAddress.getLocalHost();
			serverip = inet.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serverip;
	}

	public static Set<String> serverIps() {
		try {
			Set<String> ips = new HashSet<String>();
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface
						.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						ips.add(ip.getHostAddress());
					}
				}
			}
			return ips;
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(ServerUtil.serverIp());
	}
}
