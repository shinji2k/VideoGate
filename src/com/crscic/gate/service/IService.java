package com.crscic.gate.service;

import java.util.HashSet;
import java.util.Set;

import com.crscic.gate.config.IConfig;
import com.crscic.gate.connector.IConnector;
import com.crscic.gate.event.listener.SendListener;

/**
 * 
 * @author zhaokai 2018年12月17日 下午3:56:21
 */
public interface IService
{
	public static final Set<SendListener> listeners = new HashSet<SendListener>();

	public static void addSendListener(SendListener listener)
	{
		listeners.add(listener);
	};

	public void startService(IConfig config, IConnector senderConnector, IConnector recvConnector);

	public byte[] getStream();

	public void send(byte[] stream);

}
