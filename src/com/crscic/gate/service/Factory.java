package com.crscic.gate.service;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.dom4j.Element;

import com.crscic.gate.config.IConfig;
import com.crscic.gate.connector.IConnector;
import com.crscic.gate.utils.XmlHelper;
import com.k.util.StringUtils;

/**
 * 
 * @author zhaokai 2018年12月4日 上午8:42:37
 */
public class Factory
{
	private static Factory inst = new Factory();
	private static XmlHelper xml = new XmlHelper("init/setting.xml");
	private static IConfig config;

	private Factory()
	{
	}
	
	public static Factory getInstance()
	{
		if (inst == null)
			inst =  new Factory();
		return inst;
	}

	public IConfig getConfig() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		if (config == null)
		{
			Element configEle = xml.getSingleElement("//IConfig/class");
			String configClass = configEle.getTextTrim();
			if (StringUtils.isNullOrEmpty(configClass))
				return null;
			config = (IConfig) Class.forName(configClass).newInstance();
			config.init(xml);
		}
		return config;
	}
	
	public IConnector getConnector(String connectorName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException
	{
		List<Element> connectorList = xml.getElements("//IConnector/Connector");
		String connectorClass = getClassByName(connectorList, connectorName);
		Constructor<?> constructor =  Class.forName(connectorClass).getConstructor(IConfig.class, String.class);
		return (IConnector) constructor.newInstance(config, connectorName);
	}

	public IService getService(String serviceName) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		List<Element> serviceList = xml.getElements("//IService/Interface");
		String senderClass = getClassByName(serviceList, serviceName);
		return (IService) Class.forName(senderClass).newInstance();
	}

	private String getClassByName(List<Element> list, String name)
	{
		if (list == null || list.size() == 0)
			return null;
		for (Element ele : list)
		{
			if (ele.elementTextTrim("name").equals(name))
				return ele.elementTextTrim("class");
		}
		return null;
	}
}
