package com.crscic.gate;

import com.crscic.gate.config.IConfig;
import com.crscic.gate.log.Log;
import com.crscic.gate.service.Factory;

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
			IConfig config = Factory.getConfig();
			System.out.println(config.getClass());
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			Log.error("IConfig接口实例化失败", e);
		}
	}
}
