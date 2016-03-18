package com.hdzx.tenement.http.protocol.impl;

import com.hdzx.tenement.http.protocol.AppContentBuilder;
import org.json.JSONObject;

import java.util.Map;


public class AppContentBuilderImpl implements AppContentBuilder
{
	private Map<String, Object> header = null;
	
	private Map<String, Object> body = null;

	public AppContentBuilderImpl()
    {
	    super();
    }

	public AppContentBuilderImpl(Map<String, Object> header, Map<String, Object> body)
    {
	    super();
	    this.header = header;
	    this.body = body;
    }

	@Override
	public void buildHeader()
	{
		JSONObject json = new JSONObject();

	}

	@Override
	public void buildBody()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getContent()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> getHeader()
	{
		return header;
	}

	public void setHeader(Map<String, Object> header)
	{
		this.header = header;
	}

	public Map<String, Object> getBody()
	{
		return body;
	}

	public void setBody(Map<String, Object> body)
	{
		this.body = body;
	}
	
	public static void main(String[] args) {
		System.out.println("test");
	}
}
