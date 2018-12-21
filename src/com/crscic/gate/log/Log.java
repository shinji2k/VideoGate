package com.crscic.gate.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author zhaokai 2017年9月7日 下午2:47:01
 */
public class Log
{
	
	private static <T>  Logger getLogger()
	{
		Throwable t = new Throwable();
//		返回调用类的名称
		String className = t.getStackTrace()[2].getClassName();
		Class<?> c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to create Log instance!",e);
		}
		
		return LoggerFactory.getLogger(c);
	}

	public static void debug(String log)
	{
		getLogger().debug(log);
	}

	/**
	 * 普通日志
	 * 
	 * @param log
	 *            zhaokai 2017年9月12日 下午2:22:05
	 */
	public static void info(String log)
	{
		getLogger().info(log);
	}

	/**
	 * 错误日志，含Exception重载
	 * 
	 * @param log
	 * @param e
	 *            zhaokai 2017年9月12日 下午2:21:48
	 */
	public static void error(String log, Exception e)
	{
		getLogger().error(log, e);
	}

	/**
	 * 错误日志，不含Exception重载
	 * 
	 * @param string
	 * @author ken_8 2017年9月10日 下午10:46:24
	 */
	public static void error(String log)
	{
		getLogger().error(log);
	}

	/**
	 * 警告日志
	 * 
	 * @param log
	 *            zhaokai 2017年9月12日 下午2:28:08
	 */
	public static void warn(String log)
	{
		getLogger().warn(log);
	}

	/**
	 * @param e
	 * @author zhaokai
	 * @create 2018年12月21日 下午5:28:57
	 */
	public static void error(Exception e)
	{
		getLogger().error("", e);
	}

}
