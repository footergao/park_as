package com.hdzx.tenement.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class Logger {
	private static final String LOG_FILENAME = "My_lib_";
	private static final boolean DEBUG = true;
	private static final boolean LOG2FILE = false;

	public static void i(String tag, String msg) {
		if (DEBUG)
			Log.i(tag, msg);
		if (LOG2FILE) {
			writeLog2File(tag + " -- " + msg);
		}
	}

	public static void v(String tag, String msg) {
		if (DEBUG)
			Log.v(tag, msg);
		if (LOG2FILE) {
			writeLog2File(tag + " -- " + msg);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG)
			Log.w(tag, msg);
		if (LOG2FILE) {
			writeLog2File(tag + " -- " + msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG)
			Log.e(tag, msg);
		if (LOG2FILE) {
			writeLog2File(tag + " -- " + msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG)
			Log.d(tag, msg);
		if (LOG2FILE) {
			writeLog2File(tag + " -- " + msg);
		}
	}

	@SuppressLint({ "SimpleDateFormat", "SdCardPath" })
	public static void writeException2ErrorLog(Context context, Throwable ex)
			throws FileNotFoundException, IOException {
		String versionName = "";
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		FileOutputStream fos = new FileOutputStream("/sdcard/"
				+ context.getPackageName() + "_exp.log", true);
		PrintStream ps = new PrintStream(fos);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());
		ps.write('\n');
		ps.write('\n');
		ps.write("*****************************************************"
				.getBytes());
		ps.write('\n');
		ps.write(time.getBytes());
		ps.write('\n');
		ps.write(Build.MODEL.getBytes());
		ps.write('\t');
		ps.write(("System_Version:" + Build.VERSION.RELEASE).getBytes());
		ps.write('\t');
		ps.write(("App_Version:" + versionName).getBytes());
		ps.write('\n');
		ex.printStackTrace(ps);
		ps.write("*****************************************************"
				.getBytes());
		ps.write('\n');
		ps.write('\n');

		ps.flush();
		ps.close();
	}

	@SuppressLint({ "SdCardPath", "SimpleDateFormat" })
	public static void writeLog2File(String log) {
		try {
			FileWriter ps = new FileWriter("/sdcard/" + LOG_FILENAME
					+ "_log.log", true);
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String time = format.format(new Date());
			ps.write('\n');
			ps.write(time);
			ps.write('\t');
			ps.write(log);
			ps.write('\n');

			ps.flush();
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
