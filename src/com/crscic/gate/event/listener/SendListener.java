package com.crscic.gate.event.listener;

import java.util.EventListener;

import com.crscic.gate.event.Event;
import com.crscic.gate.service.IService;

/**
 * 
 * @author zhaokai
 * 2018年12月18日 下午6:11:42
 */
public class SendListener implements EventListener
{
	private IService service;
	
	public SendListener(IService service) 
	{
		this.service = service;
	}
	
	public void send(Event e)
	{
		IService srcService = e.getSourceService();
		service.send(srcService.getStream());
	}
}
