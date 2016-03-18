package com.hdzx.tenement.http.protocol.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppContentTemplateTest
{

	protected Map<String, Object> makeHead()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("head1", "head1-value");
		map.put("head2", "head2-value");
		map.put("head3", "head3-value");
		
		List<String> list = new ArrayList<String>();
		list.add("lsit-value1");
		list.add("lsit-value2");
		list.add("lsit-value3");
		
		map.put("list", list);

		return map;
	}

	protected Map<String, Object> makeBody()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("body1", "body1-value");
		map.put("body2", "body2-value");
		map.put("body3", "body3-value");
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("name", "lengyu");
		map2.put("sex", "女");
		map2.put("address", "江苏省无锡市滨湖区");
		map2.put("age", new Integer(32));
		
		map.put("person", map2);

		return map;
	}

    protected boolean isRequestTicket()
    {
	    return false;
    }
}
