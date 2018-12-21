package com.crscic.gate.event;

import java.util.EventObject;

import com.crscic.gate.service.IService;

/**
 * 
 * @author zhaokai
 * 2018年12月18日 下午6:07:27
 */
public class Event extends EventObject
{
	private static final long serialVersionUID = 0L;
	
	/**
	 * @param source
	 * zhaokai
	 * 2018年12月18日 下午6:07:33
	 */
	public Event(Object source)
	{
		super(source);
	}
	
	public IService getSourceService()
	{
		return (IService) super.getSource();
	}

}
