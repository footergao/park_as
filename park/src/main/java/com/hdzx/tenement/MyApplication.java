package com.hdzx.tenement;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.SdkConstants;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.wxlib.util.SysUtil;
import com.hdzx.tenement.aliopenimi.sample.InitHelper;
import com.hdzx.tenement.aliopenimi.sample.LoginSampleHelper;
import com.hdzx.tenement.utils.Logger;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {
	private static final String TAG = "MyApplication";

	// 云旺OpenIM的DEMO用到的Application上下文实例
	private static Context sContext;

	private static Context initContext;// 初始化Dialog重新刷新应用

	static MyApplication app;
	public static Context getInitContext() {
		return initContext;
	}

	public static void setInitContext(Context initContext) {
		MyApplication.initContext = initContext;
	}

	public static Context getContext() {
		return sContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 阿里云推送
		initOneSDK(this);
				
		// Application.onCreate中，首先执行这部分代码,
		// 因为，如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
		// 特别注意:这段代码不能封装到其他方法中，必须在onCreate顶层代码中!
		// 以下代码固定在此处，不要改动
		SysUtil.setApplication(this);
		if (SysUtil.isTCMSServiceProcess(this)) {
			return; // 特别注意：此处return是退出onCreate函数，因此不能封装到其他任何方法中!
		}
		// 以上代码固定在这个位置，不要改动

		sContext = getApplicationContext();
		// 初始化imkit
		LoginSampleHelper.getInstance().initIMKit();
		// 初始化云旺SDK
		InitHelper.initYWSDK(this);
		// 第一个参数是Application Context
		// 这里的APP_KEY即应用创建时申请的APP_KEY
		// YWAPI.init(this, "23249743");
		//

		
		initImageLoader(getApplicationContext());
	}
	
	public static MyApplication getInstance()
    {
		if(app!=null)
			return app;
		else
			app =new MyApplication();
		return app;
    }
	/**
	 * 设置常用的设置项
	 *
	 * @return
	 */
	public DisplayImageOptions getCircleOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_header)
				.showImageForEmptyUri(R.drawable.default_header)
				.showImageOnFail(R.drawable.default_header).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;

	}
	
	
	public DisplayImageOptions getSimpleOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.loading_default)
				.showImageForEmptyUri(R.drawable.loading_default)
				.showImageOnFail(R.drawable.loading_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;

	}
	

	/**
	 * 常用的设置项
	 * 
	 * @return
	 */
	public DisplayImageOptions getDefultOptions() {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.head_default)
				.showImageForEmptyUri(R.drawable.head_default)
				.showImageOnFail(R.drawable.head_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;

	}
	
	public DisplayImageOptions getAdOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ad_default)
				.showImageForEmptyUri(R.drawable.ad_default)
				.showImageOnFail(R.drawable.ad_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;

	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
	/**
	 * 
	 * 阿里云推送
	 * 
	 * @param applicationContext
	 */
	private void initOneSDK(final Context applicationContext) {
		Logger.w(TAG,
				"get App Package Name: " + applicationContext.getPackageName());

		AlibabaSDK.asyncInit(applicationContext, new InitResultCallback() {

			public void onSuccess() {
				Logger.d(TAG, "init onesdk success");
				// alibabaSDK初始化成功后，初始化移动推送通道
				initCloudChannel(applicationContext);
			}

			public void onFailure(int code, String message) {
				Logger.e(TAG, "init onesdk failed : " + message);
			}
		});
	}

	/**
	 * 
	 *
	 * @param applicationContext
	 */
	private void initCloudChannel(Context applicationContext) {
		final CloudPushService pushService = AlibabaSDK
				.getService(CloudPushService.class);
		pushService.setLogLevel(3);
		pushService.register(applicationContext, new CommonCallback() {
			public void onSuccess() {
				Logger.i(TAG,
						"init CloudPushService success, device id: "
								+ pushService.getDeviceId()
								+ ", UtDid: "
								+ pushService.getUTDeviceId()
								+ ", Appkey: "
								+ AlibabaSDK
										.getGlobalProperty(SdkConstants.APP_KEY));
			}

			public void onFailed(String errorCode, String errorMessage) {
				Logger.d(TAG, "init cloudchannel failed. errorcode:" + errorCode
						+ ", errorMessage:" + errorMessage);
			}
		});

	}
}