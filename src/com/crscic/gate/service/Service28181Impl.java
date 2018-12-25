package com.crscic.gate.service;

import java.util.Properties;

import javax.sip.SipFactory;
import javax.sip.message.Request;

import com.crscic.gate.config.IConfig;
import com.crscic.gate.connector.IConnector;
import com.crscic.gate.connector.SipConnectorImpl;
import com.crscic.gate.data.sip.Sip;
import com.crscic.gate.exception.ConnectException;
import com.crscic.gate.exception.DataGenerateException;
import com.crscic.gate.log.Log;

/**
 * 
 * @author zhaokai 2018年12月17日 下午6:22:21
 */
public class Service28181Impl implements IService
{
	@Override
	public byte[] getStream()
	{
		return new byte[] { 0x04, 0x03, 0x02, 0x01 };
	}

	@Override
	public void send(byte[] stream)
	{
		System.out.println("send " + stream + " by 28181");
	}

	@Override
	public void startService(IConfig config, IConnector senderConnector, IConnector recvConnector)
	{
		System.out.println("init28181----mean ready to rise the event");
		// 1.initialize
		Properties properties = new Properties();
		properties.setProperty("javax.sip.OUTBOUND_PROXY", config.getProperty("sender.ip") + ":"
				+ config.getProperty("sender.port") + "/" + config.getProperty("sender.type"));
		properties.setProperty("javax.sip.STACK_NAME", this.getClass().getSimpleName());
		properties.setProperty("gov.nist.javax.sip.MAX_MESSAGE_SIZE", "1048576"); // 1MB
		// Drop the client connection after we are done with the transaction.
		properties.setProperty("gov.nist.javax.sip.CACHE_CLIENT_CONNECTIONS", "false");
		properties.setProperty("local.ip", config.getProperty("me.ip"));
		properties.setProperty("local.port", config.getProperty("me.sip.port"));

		try
		{
			// Create SipStack object
			SipFactory sipFactory = SipFactory.getInstance();
			sipFactory.setPathName("gov.nist");

			SipConnectorImpl sender = (SipConnectorImpl) senderConnector;
			sender.setProperties(properties);
			sender.openConnect();

			// TODO：2.send register
			Sip senderSip = new Sip(sipFactory, sender);
			// JainSipTest listener = this;
			// sipProvider.addSipListener(listener);

			Request request = senderSip.createRegister(config, "");
			senderConnector.send(request);
			
			
			
			// TODO：3.parse register
			// TODO：4.resent register
			// TODO：5.invite
			// TODO：6.parse response and get stream
			// TODO：7.raise event
//			SendListener listener = new SendListener(this);
//			IService.addSendListener(listener);
//			for (SendListener s : IService.listeners)
//				s.send(new Event(this));
//			
			
			
			SipConnectorImpl receiver = (SipConnectorImpl) recvConnector;
		}
		catch (ConnectException e)
		{
			Log.error(e);
		}
		catch (DataGenerateException e)
		{
			Log.error(e);
		}
	}

}
