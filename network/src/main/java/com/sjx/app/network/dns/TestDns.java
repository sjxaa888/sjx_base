package com.sjx.app.network.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Dns;

public class TestDns implements Dns {
    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        List<InetAddress> inetAddresses = new ArrayList<>();
        List<InetAddress> hostNameInetAddresses = null;
        try {
            hostNameInetAddresses = Dns.SYSTEM.lookup(hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (hostNameInetAddresses != null && hostNameInetAddresses.size() > 0) {
            inetAddresses.addAll(hostNameInetAddresses);
        }
        try {
            //添加接口使用的ip地址来解决dns解析失败问题
            inetAddresses.add(InetAddress.getByName("123.123.123.123"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return inetAddresses;
    }
}
