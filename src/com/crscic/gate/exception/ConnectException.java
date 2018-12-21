package com.crscic.gate.exception;

/**
 * 
 * @author zhaokai
 * 2018年12月21日 下午4:58:35
 */
public class ConnectException extends Exception
{
	private static final long serialVersionUID = 301L;
	
	public ConnectException()
	{
		super();
	}
	
	public ConnectException(String msg)
	{
		super(msg);
	}
	
	public ConnectException(String msg, Exception e)
	{
		super(msg + " " + e.getMessage());
		setStackTrace(e.getStackTrace());
	}

}
