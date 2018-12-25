package com.crscic.gate.connector;

import java.util.List;
import java.util.Properties;
import java.util.TooManyListenersException;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportNotSupportedException;
import javax.sip.message.Request;

import com.crscic.gate.config.IConfig;
import com.crscic.gate.event.listener.SendListener;
import com.crscic.gate.exception.ConnectException;
import com.crscic.gate.log.Log;

/**
 * 
 * @author zhaokai 2018年12月21日 下午3:05:35
 */
public class SipConnectorImpl implements IConnector
{
	private static final String ERR_MSG = "连接器创建失败-";
	private IConfig config;
	private String transport;
	private Properties properties;
	private ListeningPoint listeningPoint;
	private SipStack sipStack;
	private SipProvider sipProvider;
	private Dialog dialog;

	public SipConnectorImpl(IConfig config, String transport)
	{
		this.config = config;
		this.transport = transport;
	}

	public void setProperties(Properties p)
	{
		properties = p;
	}

	@Override
	public void send(Object data) throws ConnectException
	{
		try
		{
			Request request = (Request) data;
			Log.debug("INVITE with no Authorization sent:\n" + request);
			// Create the client transaction.
			ClientTransaction registerTid = sipProvider.getNewClientTransaction(request);
			registerTid.sendRequest();
			
			dialog = registerTid.getDialog();
		}
		catch (TransactionUnavailableException e)
		{
			throw new ConnectException(ERR_MSG + "ClientTransaction生成错误", e);
		}
		// send the request out.
		catch (SipException e)
		{
			throw new ConnectException(ERR_MSG + "发送失败", e);
		}
	}

	@Override
	public List<Byte> receive()
	{
		return null;
	}

	@Override
	public void openConnect() throws ConnectException
	{
		
		try
		{
			sipStack = SipFactory.getInstance().createSipStack(properties);
			listeningPoint = sipStack.createListeningPoint(config.getProperty("me.ip"),
					Integer.parseInt(config.getProperty("me.sip.port")), transport);
			sipProvider = sipStack.createSipProvider(listeningPoint);
			sipProvider.addSipListener(new SendListener(this));
		}
		catch (PeerUnavailableException e)
		{
			throw new ConnectException(ERR_MSG + "IP地址" + config.getProperty("me.ip") + " 错误", e);
		}
		catch (NumberFormatException e)
		{
			throw new ConnectException(ERR_MSG + "端口：" + config.getProperty("me.sip.port") + " 格式错误", e);
		}
		catch (TransportNotSupportedException e)
		{
			throw new ConnectException(ERR_MSG + "协议:" + transport + " 不受支持", e);
		}
		catch (InvalidArgumentException e)
		{
			throw new ConnectException(ERR_MSG + "参数错误", e);
		}
		catch (ObjectInUseException e)
		{
			throw new ConnectException(ERR_MSG + "Provider对象被占用", e);
		}
		catch (TooManyListenersException e)
		{
			throw new ConnectException(ERR_MSG + "监听器过多", e);
		}
	}

	@Override
	public boolean isOpen()
	{
		return false;
	}

	@Override
	public void closeConnect()
	{
		listeningPoint = null;
	}

	@Override
	public String getRemoteIp()
	{
		return null;
	}

	@Override
	public String getLocalIp()
	{
		return null;
	}

	@Override
	public boolean isServer()
	{
		return false;
	}

	@Override
	public String getType()
	{
		return "Jain-sip";
	}

	@Override
	public boolean isReconnect()
	{
		return false;
	}

	public int getPort()
	{
		return listeningPoint.getPort();
	}

	public ListeningPoint getListeningPoint()
	{
		return listeningPoint;
	}

	public void setUdpListeningPoint(ListeningPoint udpListeningPoint)
	{
		this.listeningPoint = udpListeningPoint;
	}

	public SipProvider getSipProvider()
	{
		return sipProvider;
	}

	public void setSipProvider(SipProvider sipProvider)
	{
		this.sipProvider = sipProvider;
	}

	public Dialog getDialog()
	{
		return dialog;
	}

	public void setDialog(Dialog dialog)
	{
		this.dialog = dialog;
	}

	public SipStack getSipStack()
	{
		return sipStack;
	}

	public void setSipStack(SipStack sipStack)
	{
		this.sipStack = sipStack;
	}

}
