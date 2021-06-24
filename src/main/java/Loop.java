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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Loop {

    private static final String COUNT_KEY = Loop.class.getName() + ".count";
    private static final int COUNT = Integer.getInteger(COUNT_KEY,1);

    private static final String READ_TIMEOUT_KEY = Loop.class.getName() + ".readTimeout";
    private static final int  READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY,10);

    private static final String SNAPLEN_KEY = Loop.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY,65536);

    private Loop() {}

    public static void HandlePackets(InetAddress ip) throws Exception{

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url="jdbc:mysql://localhost:3306/SmartController";

        // 设置要抓包的网卡
        PcapNetworkInterface nif=Pcaps.getDevByAddress(ip);
        // 实例化一个捕获报文的对象 设置报文参数：长度，混杂模式，超时时间
        final PcapHandle handle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,READ_TIMEOUT);

        PacketListener listener= packet -> {
            try {
                InetAddress srcIpAddr,dstIpAddr;
                if(packet.contains(IpPacket.class)){
                    srcIpAddr=packet.get(IpPacket.class).getHeader().getSrcAddr();
                    dstIpAddr=packet.get(IpPacket.class).getHeader().getDstAddr();
                }
                else {
                    srcIpAddr=null;
                    dstIpAddr=null;
                }

                if(srcIpAddr!=null && dstIpAddr!=null){

                    if(srcIpAddr!=ip){
                        String srcIp=(srcIpAddr==null) ? "" : srcIpAddr.toString().substring(1);
                        String dstIp=(dstIpAddr==null) ? "" : dstIpAddr.toString().substring(1);
                        SQLFunction.UpdateDaily(srcIp,dstIp);
                    }

                }

            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        };
        try {
            // 直接使用loop无限循环处理包
            handle.loop(-1,listener); //设置抓包个数  当个数为-1时无限抓包

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handle.close();
    }

    public static void HandlePackets(String nifName) throws Exception{

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url="jdbc:mysql://localhost:3306/SmartController";

        // 设置要抓包的网卡
        PcapNetworkInterface nif=Pcaps.getDevByName(nifName);
        // 实例化一个捕获报文的对象 设置报文参数：长度，混杂模式，超时时间
        final PcapHandle handle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,READ_TIMEOUT);

        PacketListener listener= packet -> {
            try {
                InetAddress srcIpAddr,dstIpAddr;
                if(packet.contains(IpPacket.class)){
                    srcIpAddr=packet.get(IpPacket.class).getHeader().getSrcAddr();
                    dstIpAddr=packet.get(IpPacket.class).getHeader().getDstAddr();
                }
                else {
                    srcIpAddr=null;
                    dstIpAddr=null;
                }

                if(srcIpAddr!=null && dstIpAddr!=null){

                    //if(srcIpAddr!=ip){
                        String srcIp=(srcIpAddr==null) ? "" : srcIpAddr.toString().substring(1);
                        String dstIp=(dstIpAddr==null) ? "" : dstIpAddr.toString().substring(1);
                        if(!srcIp.contains("192.168")){
                            SQLFunction.UpdateDaily(srcIp,dstIp);
                        }

                    //}

                }

            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        };
        try {
            // 直接使用loop无限循环处理包
            handle.loop(-1,listener); //设置抓包个数  当个数为-1时无限抓包

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handle.close();
    } //根据网卡名抓包





}
