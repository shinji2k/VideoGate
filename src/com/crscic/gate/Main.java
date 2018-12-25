package com.crscic.gate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.crscic.gate.config.IConfig;
import com.crscic.gate.connector.IConnector;
import com.crscic.gate.event.listener.SendListener;
import com.crscic.gate.log.Log;
import com.crscic.gate.service.Factory;
import com.crscic.gate.service.IService;

/**
 * 
 * @author zhaokai
 * 2018年12月3日 下午3:59:28
 */
public class Main
{
	public static void main(String[] args)
	{
		try
		{
			Factory factory = Factory.getInstance();
			IConfig config = factory.getConfig();
			
			IConnector senderConnector = factory.getConnector(config.getProperty("sender.type"));
			IConnector recvConnector = factory.getConnector(config.getProperty("receiver.type"));
			
			IService senderService = factory.getService(config.getProperty("sender"));
			IService receiverService = factory.getService(config.getProperty("receiver"));
			
//			IService.addSendListener(new SendListener(receiverService));
			
			senderService.startService(config, senderConnector, recvConnector);
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			Log.error("接口实例化失败", e);
		}
		catch (IOException e)
		{
			Log.error("IConfig加载配置文件失败", e);
		}
		catch (NoSuchMethodException e)
		{
			Log.error("找不到构造函数", e);
		}
		catch (SecurityException e)
		{
			Log.error("反射调用的方法不可见", e);
		}
		catch (IllegalArgumentException e)
		{
			Log.error("反射调用时传递的参数不合法", e);
		}
		catch (InvocationTargetException e)
		{
			Log.error("反射调用方法失败", e);
		}
	}
}
