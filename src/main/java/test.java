//package com.summer.pcap;

import com.sun.jna.Platform;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.Port;
import org.pcap4j.util.MacAddress;
import org.pcap4j.util.NifSelector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {

    private test() {}

    public static void main(String[] args) throws Exception {
        List<String> ipList=List.of("192.168.154.133","127.0.0.1");

        for (String ip:ipList) {
//            Thread t=new Thread(()->{
//                try {
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
            Loop.HandlePackets(InetAddress.getByName(ip));
        }

    }

}
