package com.crscic.gate.service;

import org.dom4j.Element;

import com.crscic.gate.config.IConfig;
import com.crscic.gate.utils.XmlHelper;
import com.k.util.StringUtils;

/**
 * 
 * @author zhaokai
 * 2018年12月4日 上午8:42:37
 */
public class Factory
{
	private static XmlHelper xml = new XmlHelper("init/setting.xml");
	public Factory()
	{
	}
	
	public static IConfig getConfig() throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Element configEle = xml.getSingleElement("//IConfig/class");
		String configClass = configEle.getTextTrim();
		if (StringUtils.isNullOrEmpty(configClass))
			return null;
		return (IConfig) Class.forName(configClass).newInstance();
	}
}
