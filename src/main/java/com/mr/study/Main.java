package com.mr.study;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author zhanxp
 * @version 1.0 2019/7/25
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(getHostIp());
    }

    private static String getHostIp() throws Exception {
        Enumeration<NetworkInterface> allNetInterfaces;
        allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ip instanceof Inet4Address
                        && !ip.isLoopbackAddress()
                        && !ip.getHostAddress().contains(":")) {
                    return ip.getHostAddress();
                }
            }
        }
        return null;
    }
}
