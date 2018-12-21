package com.crscic.gate.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;

import com.k.util.CollectionUtils;

/**
 * 
 * @author zhaokai 2018年12月12日 下午3:07:12
 */
public class Test
{
	public static void main(String[] args) throws IOException
	{
		FileResourceLoader fileResourceLoader = new FileResourceLoader("./template", "utf-8");
		Configuration cfg2 = Configuration.defaultConfiguration();
		GroupTemplate gt2 = new GroupTemplate(fileResourceLoader, cfg2);
		Template t2 = gt2.getTemplate("/register.sip");

		t2.binding("sipServerSid", "34020100002000000001");
		t2.binding("sipServerIpPort", "192.168.175.9");
		t2.binding("srcIpPort", "192.168.175.9:5060");
		t2.binding("sipDeviceCode", "34020100002000000001");
		t2.binding("fromIp", "192.168.175.9");
		t2.binding("toIp", "192.168.175.10");
		t2.binding("session", "ms1214-322164710-681262131542511620107-0@172.18.16.3");
		t2.binding("cseq", "1");
		t2.binding("proxyIpPort", "192.168.175.9:5060");

		String str2 = t2.render();
		System.out.println(str2);

		InetAddress inet = InetAddress.getByName("192.168.175.9");
		SocketAddress sockAddr = new InetSocketAddress(inet, 5060);
		DatagramSocket sock = new DatagramSocket(sockAddr);
		InetAddress destAddr = InetAddress.getByName("192.168.175.10");
		DatagramPacket sendPacket = new DatagramPacket(str2.getBytes(), str2.getBytes().length, destAddr, 5060);
		sock.send(sendPacket);

		List<Byte> recvData = new ArrayList<Byte>();
//		while (true)
//		{
			int len = 1024;
			byte[] buff = new byte[len];
			DatagramPacket recePacket = new DatagramPacket(buff, buff.length);
			sock.receive(recePacket);
			String recv = new String(buff);
			System.out.println(recv);
			
			CollectionUtils.copyArrayToList(recvData, buff, recePacket.getLength());
//		}
	}

}
