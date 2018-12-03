package com.crscic.gate.config;

/**
 * 
 * @author zhaokai
 * 2018年12月3日 下午1:56:52
 */
public interface IConfig
{
	public void load(String path);
	public String getProperty(String propertyName);
	
}
