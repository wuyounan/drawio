package com.huigou.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 网络工具类
 * 
 * @author gongmm
 */
public class NetworkUtil {

    public static String[] getIps() {
        List<String> result = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces();
            while (networkInterface.hasMoreElements()) {
                NetworkInterface item = networkInterface.nextElement();
                Enumeration<InetAddress> as = item.getInetAddresses();
                while (as.hasMoreElements()) {
                    InetAddress addr = as.nextElement();
                    if (addr instanceof Inet6Address) {
                        continue;
                    }
                    result.add(addr.getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return result.toArray(new String[0]);
    }

    public static String[] getMacs() {
        List<String> result = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces();
            while (networkInterface.hasMoreElements()) {
                NetworkInterface item = networkInterface.nextElement();
                Enumeration<InetAddress> as = item.getInetAddresses();
                while (as.hasMoreElements()) {
                    InetAddress addr = as.nextElement();

                    if (addr instanceof Inet6Address) {
                        continue;
                    }
                    byte[] mac = item.getHardwareAddress();

                    if (mac == null || mac.length == 0) {
                        continue;
                    }

                    result.add(formatMac(mac));
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return result.toArray(new String[0]);

    }

    public static String formatMac(byte[] mac) throws SocketException {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString().toUpperCase();
    }

}
