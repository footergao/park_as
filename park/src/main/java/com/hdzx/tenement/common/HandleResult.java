package com.hdzx.tenement.common;

public class HandleResult
{
	private boolean isSucess = true;
	
	private String message =  null;

	public boolean isSucess()
	{
		return isSucess;
	}
	
	public boolean isFail()
	{
		return !isSucess;
	}

	public void setSucess(boolean isSucess)
	{
		this.isSucess = isSucess;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		isSucess = false;
		this.message = message;
	}
}
