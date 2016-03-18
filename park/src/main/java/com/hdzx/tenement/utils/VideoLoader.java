package com.hdzx.tenement.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class VideoLoader {
	private static final String TAG = VideoLoader.class.getCanonicalName();
	private static final int DOWNLOAD_SUCCESS = 0;
	private static final int DOWNLOAD_FAILD = -1;
	private static final int DOWNLOAD_EXIST = 1;
	private static VideoLoader videoLoader;
	// 是否已下载
	private boolean keyState = false;

	public synchronized static VideoLoader getInstance() {
		if (videoLoader == null) {
			videoLoader = new VideoLoader();
		}
		return videoLoader;
	}

	public interface DownloadListener {

		void onSuccess(Message verionMessage);

		void onFailed(String errorMsg);

	}

	/**
	 * 
	 * @param context
	 * @param task
	 * @param loginListener
	 */
	public synchronized void download(final Context context, final Map map,
			final DownloadListener downloadListener) {
		Log.d(TAG, "download...");
		if (downloadListener == null) {
			throw new IllegalArgumentException("downloadListener 为空");
		}

		if (keyState) {
			downloadListener.onSuccess(null);
			return;
		}
		Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				handleDownloadResult(context,downloadListener, msg);
			}
		};
		// xiazai
		asyncDowload(context, mHandler, map);
	}

	private void asyncDowload(final Context context, final Handler handler,
			final Map map) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				downloadVideo(context, handler, map);
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	protected void downloadVideo(Context context, Handler handler, Map task) {
		// TODO Auto-generated method stub
		Map<String, Object> params = task;
		String urlstr = (String) params.get("urlstr");
		String path = (String) params.get("path");
		String fileName = (String) params.get("fileName");

		Message msg = handler.obtainMessage();
		try {
			InputStream inputStream = null;
			FileUtils fileUtils = new FileUtils(context);

			if (fileUtils.isFileExist(path + "/"+fileName)) {
				msg.arg1 = DOWNLOAD_EXIST;
				msg.obj=fileUtils.getFilePath();
				Log.i("downloadVideo", "file---->"+fileUtils.getFilePath());
			} else {
				inputStream = getInputStreamFormUrl(urlstr);
				File resultFile = fileUtils.writeToSDfromInput(path, fileName,
						inputStream);
				if (resultFile == null) {
					msg.arg1 = DOWNLOAD_FAILD;
				}else{
					msg.arg1 = DOWNLOAD_SUCCESS;
					msg.obj=fileUtils.getFilePath();
					Log.i("downloadVideo", "file---->"+fileUtils.getFilePath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.arg1 = DOWNLOAD_FAILD;
		} finally {
			handler.sendMessage(msg);
		}
	}

	protected boolean handleDownloadResult(Context context,
			DownloadListener downloadListener, Message msg) {
		// if (msg.what == Task.TASK_DOWNLOAD) {
		int taskResultCode = msg.arg1;
		switch (taskResultCode) {
		case DOWNLOAD_EXIST:
		case DOWNLOAD_SUCCESS:
			downloadListener.onSuccess(msg);
			break;
		case DOWNLOAD_FAILD:
			keyState = false;
			downloadListener.onFailed(taskResultCode + "");
			break;
		default:
			break;
		}
		return false;
	}

	public static void downloadVideo(Context context, String path, String name,
			String url) {
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));
		// 设置在什么网络情况下进行下载
		request.setAllowedNetworkTypes(Request.NETWORK_WIFI
				| Request.NETWORK_WIFI);
		// 设置通知栏标题
		request.setNotificationVisibility(Request.VISIBILITY_HIDDEN);
		// 用于设置漫游状态下是否可以下载
		request.setAllowedOverRoaming(false);
		// 设置文件存放目录
		request.setDestinationInExternalFilesDir(context,
				Environment.DIRECTORY_DOWNLOADS, path);
		DownloadManager downManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		long id = downManager.enqueue(request);

	}

	/**
	 * 根据URL下载文件，前提是这个文件当中的内容是文本，函数返回值是文件当中的文本内容
	 * 
	 * @param urlstr
	 * @return
	 */
	public String downFile(String urlstr) {
		StringBuffer sb = new StringBuffer();
		BufferedReader buffer = null;
		URL url = null;
		String line = null;
		try {
			// 创建一个URL对象
			url = new URL(urlstr);
			// 根据URL对象创建一个Http连接
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			// 使用IO读取下载的文件数据
			buffer = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 该函数返回整形 -1：代表下载文件错误0 ：下载文件成功1：文件已经存在
	 * 
	 * @param urlstr
	 * @param path
	 * @param fileName
	 * @return
	 */
	public int downFile(Context context, String urlstr, String path,
			String fileName) {
		InputStream inputStream = null;
		FileUtils fileUtils = new FileUtils(context);

		if (fileUtils.isFileExist(path + fileName)) {
			return 1;
		} else {
			inputStream = getInputStreamFormUrl(urlstr);
			File resultFile = fileUtils.writeToSDfromInput(path, fileName,
					inputStream);
			if (resultFile == null) {
				return -1;
			}
		}
		return 0;
	}

	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlstr
	 * @return
	 */
	public InputStream getInputStreamFormUrl(String urlstr) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlstr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			inputStream = urlConn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}
}
