package com.crscic.gate.service;

import com.crscic.gate.event.Event;
import com.crscic.gate.event.listener.SendListener;

/**
 * 
 * @author zhaokai 2018年12月18日 下午3:40:00
 */
public class ServiceHcvmsImpl implements IService
{
	@Override
	public byte[] getStream()
	{
		return new byte[] { 0x01, 0x02, 0x03, 0x04 };
	}

	@Override
	public void send(byte[] stream)
	{
		for (int i = 100; i < 1000; i++)
			System.out.println(i);
		System.out.println("send " + stream + " by hcvms");
	}

	@Override
	public void startService()
	{
		System.out.println("initHcvms----mean ready to rise the event");

		for (SendListener s : IService.listeners)
			s.send(new Event(this));
	}

}
