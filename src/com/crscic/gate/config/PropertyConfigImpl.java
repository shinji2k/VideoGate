package com.crscic.gate.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.crscic.gate.utils.XmlHelper;

/**
 * 
 * @author zhaokai 2018年12月3日 下午4:05:52
 */
public class PropertyConfigImpl implements IConfig
{
	private String path;
	private Map<String, String> propertyMap;

	@Override
	public String getProperty(String propertyName)
	{
		return propertyMap == null ? null : propertyMap.get(propertyName);
	}

	@Override
	public void init(XmlHelper xml) throws IOException
	{
		path = xml.getSingleElement("//IConfig/path").getTextTrim();
		Properties prop = new Properties();
		InputStream in = null;
		in = new FileInputStream(path);
		prop.load(in);
		propertyMap = new HashMap<String, String>();
		for (Object key : prop.keySet())
		{
			propertyMap.put((String) key, prop.getProperty((String) key));
		}
	}

}
