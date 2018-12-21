package com.crscic.gate.config;

import java.io.IOException;

import com.crscic.gate.utils.XmlHelper;

/**
 * 
 * @author zhaokai
 * 2018年12月3日 下午1:56:52
 */
public interface IConfig
{
	public void init(XmlHelper xml) throws IOException;
	public String getProperty(String propertyName);
	
}
