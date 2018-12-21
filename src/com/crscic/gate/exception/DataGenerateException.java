package com.crscic.gate.exception;

/**
 * 
 * @author zhaokai
 * 2018年12月21日 下午4:58:35
 */
public class DataGenerateException extends Exception
{
	private static final long serialVersionUID = 302L;
	
	public DataGenerateException()
	{
		super();
	}
	
	public DataGenerateException(String msg)
	{
		super(msg);
	}
	
	public DataGenerateException(String msg, Exception e)
	{
		super(msg + " " + e.getMessage());
		setStackTrace(e.getStackTrace());
	}

}
