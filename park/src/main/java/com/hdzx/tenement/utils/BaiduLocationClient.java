package com.hdzx.tenement.utils;

import android.content.Context;
import com.baidu.location.LocationClient;

public class BaiduLocationClient
{
    public LocationClient locationClient = null;
    
    private static final BaiduLocationClient instance = new BaiduLocationClient();
    
    private BaiduLocationClient()
    {
        
    }
    
    public static BaiduLocationClient getInstance()
    {
        return instance;
    }
    
    public void init(Context context)
    {
        if (locationClient == null)
        {
            locationClient = new LocationClient(context);
        }
    }
    
    public LocationClient getClient()
    {
        return locationClient;
    }
}
