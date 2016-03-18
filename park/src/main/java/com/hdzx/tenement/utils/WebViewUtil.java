package com.hdzx.tenement.utils;

public class WebViewUtil {

	public static String formateString(String load) {

		String flag = "";
		if (load.contains("?"))
			flag = "&";
		else
			flag = "?";
		
		
		return String
				.format("%s" + "%s" + "visitChannel=" + "%s", load, flag,
						Contants.APPID + "-" + Contants.OS_AND + "-"
								+ Contants.VERSION);
	}
}
