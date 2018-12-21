/**
 * 
 */
package com.crscic.gate.connector;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import com.crscic.gate.exception.ConnectException;

/**
 * 
 * @author zhaokai
 * 2017年9月7日 下午3:11:54
 */
public interface IConnector
{
	/**
	 * 发送数据
	 * 
	 * zhaokai
	 * 2017年9月10日 下午2:12:34
	 * @throws UnknownHostException 
	 * @throws IOException 
	 */
	public void send(byte[] data);
	public List<Byte> receive();
	
	/**
	 * 建立连接
	 * @throws UnknownHostException
	 * @throws IOException
	 * zhaokai
	 * 2017年9月10日 下午1:58:03
	 */
	public void openConnect() throws ConnectException;
	
	public boolean isOpen();
	
	public void closeConnect();
	
	public String getRemoteIp();
	
	public String getLocalIp();
	
	public boolean isServer();
	
	public String getType();
	
	public boolean isReconnect();
}
