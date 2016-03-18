package com.hdzx.tenement.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 
 * @author Jesley
 *
 */
public class NetUtils
{

	/*
	 * private static final String TAG = "NetUtils"; private static Uri
	 * PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	 */

	/**
	 * 检查网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context)
	{
		/*
		 * // 区分ＷＩＦＩ和ＭＯＢＩＬＥ boolean isWifi = isWIFIConnectivity(context);
		 * boolean isApn = isAPNConnectivity(context); if (isApn == false &&
		 * isWifi == false) { // 无网络 return false; } if (isApn) { // 区分Wap和Net
		 * // 获取apn配置信息：代理的ip和端口，如果非空wap，空net //readApn(context); } return true;
		 */
		return isNetworkAvailable(context);
	}

	public static boolean isNetworkAvailable(Context ctx)
	{
		try
		{
			ConnectivityManager cm = (ConnectivityManager) ctx
			        .getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			return (info != null && info.isConnected());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取代理ip和端口
	 */
	/*
	 * private static void readApn(Context context) { // 读取是当前正在处于连接状态的Apn的配置信息
	 * // 读取方式：联系人 ContentResolver contentResolver =
	 * context.getContentResolver(); Cursor query =
	 * contentResolver.query(PREFERRED_APN_URI, null, null, null, null); if
	 * (query != null && query.moveToFirst()) { GloableParams.PROXY_IP =
	 * query.getString(query.getColumnIndex("proxy")); GloableParams.PROXY_PORT
	 * = query.getInt(query.getColumnIndex("port")); Log.i(TAG,
	 * "ip:"+GloableParams.PROXY_IP+"port:"+GloableParams.PROXY_PORT); }
	 * 
	 * }
	 */

	/**
	 * 判断用户的网络是否为wifi连接
	 * 
	 * @param context
	 * @return
	 */
	/*
	 * public static boolean isWIFIConnectivity(Context context) {
	 * ConnectivityManager manager = (ConnectivityManager)
	 * context.getSystemService(Context.CONNECTIVITY_SERVICE); NetworkInfo
	 * networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); if
	 * (networkInfo != null) return networkInfo.isConnected(); return false;
	 * 
	 * }
	 */

	/**
	 * 判断用户的网络是否为(Mobile(apn))(wap,net)连接
	 * 
	 * @param context
	 * @return
	 */
	/*
	 * public static boolean isAPNConnectivity(Context context) {
	 * ConnectivityManager manager = (ConnectivityManager)
	 * context.getSystemService(Context.CONNECTIVITY_SERVICE); NetworkInfo
	 * networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); if
	 * (networkInfo != null) return networkInfo.isConnected(); return false;
	 * 
	 * }
	 */






	/**
	 * 将ip的整数形式转换成ip形式
	 *
	 * @param ipInt
	 * @return
	 */
	public static String int2ip(int ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	/**
	 * 获取当前ip地址
	 *
	 * @param context
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int i = wifiInfo.getIpAddress();
			return int2ip(i);
		} catch (Exception ex) {
			return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
		}
		// return null;
	}

}
