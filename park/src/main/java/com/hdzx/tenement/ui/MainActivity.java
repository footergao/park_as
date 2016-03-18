package com.hdzx.tenement.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWPushListener;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.login.YWLoginCode;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.aliopenimi.common.Notification;
import com.hdzx.tenement.aliopenimi.sample.CustomConversationHelper;
import com.hdzx.tenement.aliopenimi.sample.LoginSampleHelper;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.community.ui.CommonConvenPhoneListActivity;
import com.hdzx.tenement.community.ui.CommunityFragment;
import com.hdzx.tenement.community.ui.CommunitySuggestActivity;
import com.hdzx.tenement.community.ui.ExpressInquiry;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.message.MainMessageCircleActivity;
import com.hdzx.tenement.mine.ui.ForumPostsActivity;
import com.hdzx.tenement.mine.ui.MineFragment;
import com.hdzx.tenement.mine.vo.LifeCircleAddressVo;
import com.hdzx.tenement.mine.vo.Notice;
import com.hdzx.tenement.pay.PayDtlActivity;
import com.hdzx.tenement.photo.FileTools;
import com.hdzx.tenement.service.AppStatusService;
import com.hdzx.tenement.ui.common.MyLifeCircleActivity;
import com.hdzx.tenement.utils.BaiduLocationClient;
import com.hdzx.tenement.utils.BeansUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.FileUtils;
import com.hdzx.tenement.utils.Logger;
import com.hdzx.tenement.utils.PreferencesUtils;
import com.hdzx.tenement.utils.Task;
import com.hdzx.tenement.vo.LifeCircleAddressBean;
import com.hdzx.tenement.vo.MessageOutlineBean;
import com.hdzx.tenement.vo.MsgCircleVo;
import com.hdzx.tenement.widget.LatLngDistance;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by anchendong on 15/7/17.
 */
public class MainActivity extends FragmentActivity implements IContentReportor {

	/**
	 * 地址下拉选择
	 */
	private TextView textViewSelectAddress;

	/**
	 * 地址选择
	 */
	PopupWindow popupWindow;

	/**
	 * 服务意图
	 */
	Intent intentService;

	/**
	 * 首页fragment
	 */
	private HomeFragment homeFragment;

	/**
	 * 标题栏文字
	 */
	private TextView textViewTitle;

	private ImageView imageViewHome;
	private ImageView imageViewBangbang;
	private ImageView imageViewShop;
	private ImageView imageViewActivity;
	private ImageView imageViewMyself;

	private TextView textViewHome;
	private TextView textViewBangbang;
	private TextView textViewShop;
	private TextView textViewActivity;
	private TextView textViewMyself;

	private TextView txt_main_address;

	private LinearLayout lay_main_title;

	private ImageView img_main_Msgecircle;
	//
	/**
	 * 获取用户基本信息,封装UserBasic
	 */
	private static final String GET_BASICINFO = "getbasicinfo";
	private String HTTP_SET_READ_MSGS = "setReadMessageByCategory";

	// 百度定位
	private LocationClient mLocationClient;
	private MyLocationListener locationListener = null;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";

	private int REQUEST_CODE = 10000;

	// 自动切换生活圈
	private double latitude1 = 0;
	private double lontitude1 = 0;
	private double latitude2 = 0;
	private double lontitude2 = 0;
	boolean flag = true;// 开关两次定位距离测算
	boolean isOn = false;// 自动切换生活圈开关
	private String GET_LIFE_CIRCLE = "getLifecircle";
	private String UPDATE_NOW_LIFE_CIRCLE = "updateNowLifecircle";// 更新当前生活圈
	private int DISTANCE = 1000;// 自动切换生活圈的距离 1000米
	public boolean never_back_pressed = false;// 禁止返回键必须设置生活圈

	private final String TAG = "MainActivity";
	private Fragment f_activity;
	/**
	 * 定位后获得的生活圈
	 */
	private List<LifeCircleAddressVo> addressVoList = new ArrayList<LifeCircleAddressVo>();

	/**
	 * 地址bean
	 */
	private LifeCircleAddressBean addressBean = new LifeCircleAddressBean();

	/* 阿里百川 begin */
	private LoginSampleHelper loginHelper;
	private YWIMKit mIMKit;
	private IYWConversationService mConversationService;
	private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
	private Handler mHandler = new Handler(Looper.getMainLooper());
	/* 阿里百川 end */

	private boolean isNewMsg = true;
	private ImageView img_new_post;
	MineFragment mineFragment;
	private String RQ_MY_POSTS = "rq_my_posts";
	private String fangan = "2";// 方案2 来显示社区交流的红点，方案1是消息圈和我的消息数据已通

	//

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tenement_main);
		textViewTitle = (TextView) findViewById(R.id.txt_main_title);
		imageViewHome = (ImageView) findViewById(R.id.img_main_home);
		textViewHome = (TextView) findViewById(R.id.txt_main_home);
		imageViewBangbang = (ImageView) findViewById(R.id.img_main_bangbang);
		textViewBangbang = (TextView) findViewById(R.id.txt_main_bangbang);
		imageViewShop = (ImageView) findViewById(R.id.img_main_shop);
		textViewShop = (TextView) findViewById(R.id.txt_main_shop);
		imageViewActivity = (ImageView) findViewById(R.id.img_main_ativity);
		textViewActivity = (TextView) findViewById(R.id.txt_main_ativity);
		imageViewMyself = (ImageView) findViewById(R.id.img_main_myself);
		textViewMyself = (TextView) findViewById(R.id.txt_main_myself);
		txt_main_address = (TextView) findViewById(R.id.txt_main_address);
		lay_main_title = (LinearLayout) findViewById(R.id.lay_main_title);

		img_main_Msgecircle = (ImageView) findViewById(R.id.img_main_Msgecircle);
		img_new_post = (ImageView) findViewById(R.id.img_new_post);

		/**
		 * 初始化界面
		 */
		initView();
		initUserData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 错误的
		if (fangan.equals("2")) {
			if (isNewMsg)
				getData(1);
			showPostFlag();

			if (isNewMsg)
				Task.getAllMessageOutline(this, this, true, HTTP_SET_READ_MSGS);
		} else if (fangan.equals("1")) {
			// 正确的
			if (isNewMsg)
				Task.getAllMessageOutline(this, this, true, HTTP_SET_READ_MSGS);
		}

		loginHelper = LoginSampleHelper.getInstance();
		final YWIMKit imKit = loginHelper.getIMKit();
		if (imKit == null) {
			return;
		}
		mConversationService = imKit.getConversationService();

		if (mConversationUnreadChangeListener == null) {
			return;
		}
		// resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
		mConversationUnreadChangeListener.onUnreadChange();

		// 在Tab栏增加会话未读消息变化的全局监听器
		mConversationService
				.addTotalUnreadChangeListener(mConversationUnreadChangeListener);

		// IYWTribeService tribeService = imKit.getTribeService();
		// tribeService.addTribeListener(mTribeChangedListener);
		YWLog.i(TAG, "onResume");

	}

	@Override
	protected void onPause() {
		if (mConversationService != null) {
			mConversationService
					.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
		}
		// mIMKit.getTribeService().removeTribeListener(mTribeChangedListener);
		super.onPause();
	}

	private void addMyPushListener() {
		// 自定义头像和昵称回调初始化(如果不需要自定义头像和昵称，则可以省去)
		// UserProfileSampleHelper.initProfileCallback();

		mIMKit = LoginSampleHelper.getInstance().getIMKit();
		if (mIMKit == null) {
			return;
		}
		mConversationService = mIMKit.getConversationService();

		// 不管之前是否已添加，此时都先移除
		mConversationService.removePushListener(iywPushListener);
		mConversationService.addPushListener(iywPushListener);
	}

	IYWPushListener iywPushListener = new IYWPushListener() {
		// 收到群聊消息时会回调该方法，开发者可以在该方法内更新该会话的未读数
		@Override
		public void onPushMessage(YWTribe arg0, YWMessage arg1) {
			Log.v(TAG, "收到群聊消息-------->" + arg1.getContent());
		}

		// 收到单聊消息时会回调该方法，开发者可以在该方法内更新该会话的未读数
		@Override
		public void onPushMessage(IYWContact arg0, YWMessage message) {
			Log.v("", "收到单聊消息--->getContent():"
					+ message.getMessageBody().getContent() + "\ngetSubType:"
					+ message.getSubType());
			playAudio();

			isNewMsg = true;
			showMsgCircleFlag();

			try {
				JSONObject json = new JSONObject(message.getMessageBody()
						.getContent());
				if (json.getString("category").equals("CATEGORY_COMMUNITY_MSG")) {
					Contants.isPostMsg = true;
					showPostFlag();
				} else if (json.getString("category").equals(
						"REALNAME_CHECK_ACCESS")) {

					UserSession.getInstance().getUserBasic().setLevel("2");

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private void initUserData() {

		addMyPushListener();

		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.none);// 请求使用AES加密

		// BODY
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(),
				UserSession.getInstance().getAccessTicket());
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), "");

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_PERSON_INFO.getValue());
		httpRequestEntity.setRequestCode(GET_BASICINFO);
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);// 返回使用AES密钥解密

		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(this, this);
		httpAsyncTask.execute(httpRequestEntity);

	}

	protected void playAudio() {
		// TODO Auto-generated method stub
		try {
			MediaPlayer mp = MediaPlayer.create(this, R.raw.xbb_defualt);
			if (mp != null) {
				mp.stop();
			}
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initView() {
		// 启动service
		intentService = new Intent(MainActivity.this, AppStatusService.class);
		startService(intentService);

		initBaiduLocation();

		// 初始化imageloader
		initImageLoader();

		setDefaultFragment();

	}

	private void initBaiduLocation() {
		mLocationClient = new LocationClient(this);
		registerLocation();
		initLocation();

		isOn = PreferencesUtils.getInstance().takeBoolean(MainActivity.this,
				Contants.PREFERENCES_KEY.auto_city.name());
		if (isOn)
			mLocationClient.start();// 定位SDK
		// start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
	}

	private void registerLocation() {
		if (mLocationClient != null) {
			locationListener = new MyLocationListener();
			mLocationClient.registerLocationListener(locationListener);
		}
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType(tempcoor);// 可选，默认gcj02，设置返回的定位结果坐标系，
		int span = 1000;
		try {
			span = Integer.valueOf("1000");// 1000ms毫秒
		} catch (Exception e) {
		}
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		// option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		// option.setIsNeedLocationDescribe(true);//
		// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		// option.setIsNeedLocationPoiList(true);//
		// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

		if (mLocationClient != null)
			mLocationClient.setLocOption(option);
		else {
			initBaiduLocation();
		}
	}

	/**
	 * 实现实时位置回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			int locType = location.getLocType();
			if (locType == BDLocation.TypeGpsLocation
					|| locType == BDLocation.TypeNetWorkLocation) {
				String cityName = location.getCity();// 市
				String provinceName = location.getProvince();// 省
				String districtName = location.getDistrict();// 区/县

				addressBean.setCity(cityName);
				addressBean.setProvince(provinceName);
				addressBean.setArea(districtName);
				addressBean.setLat(location.getLatitude() + "");
				addressBean.setLng(location.getLongitude() + "");
			}

			if (flag) {
				if (location.getLatitude() != 0.0
						|| location.getLongitude() != 4.9E-324)
					latitude1 = location.getLatitude();

				if (location.getLongitude() != 0.0
						|| location.getLongitude() != 4.9E-324)
					lontitude1 = location.getLongitude();

				flag = false;
			}

			if (location.getLatitude() != 0.0
					|| location.getLongitude() != 4.9E-324)
				latitude2 = location.getLatitude();

			if (location.getLatitude() != 0.0
					|| location.getLongitude() != 4.9E-324)
				lontitude2 = location.getLongitude();

			// LatLng p1LL = new LatLng(latitude1, lontitude1);
			// LatLng p2LL = new LatLng(latitude2, lontitude2);
			// double distance = DistanceUtil.getDistance(p1LL, p2LL);

			double distance = LatLngDistance.getInstance().GetLongDistance(
					lontitude1, latitude1, lontitude2, latitude2);

			if (distance > DISTANCE && isOn) {// 1000米&&自动切换生活圈
				flag = true;
				getLifeCircle();

			}

			Log.v("gl", "flag===" + flag);
			Log.v("gl", "distance===" + distance);
			Log.v("gl", "BaiduLocationApiDem===" + location.getCity()
					+ location.getProvince());
			Log.v("gl", "lontitude1===" + lontitude1 + "latitude1==="
					+ latitude1);
			Log.v("gl", "lontitude2===" + lontitude2 + "latitude2==="
					+ latitude2);
		}
	}

	private void getLifeCircle() {
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);
		reqContent.appendData("operType", "008");// 008-小区选择使用，009-查询
		reqContent.appendData("province", addressBean.getProvince());
		reqContent.appendData("city", addressBean.getCity());
		if ("全部".equals(addressBean.getArea())) {
			reqContent.appendData("area", "");
		} else {
			reqContent.appendData("area", addressBean.getArea());
		}

		reqContent.appendData("lng", addressBean.getLng());
		reqContent.appendData("lat", addressBean.getLat());

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_LIFE_CIRCLE.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(GET_LIFE_CIRCLE);
		HttpAsyncTask task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}

	@Override
	protected void onStart() {
		super.onStart();
		isOn = PreferencesUtils.getInstance().takeBoolean(MainActivity.this,
				Contants.PREFERENCES_KEY.auto_city.name());

		if (isOn)
			mLocationClient.start();// 定位SDK

		Log.v("gl", "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		stopPosition();
	}

	@Override
		protected void onDestroy() {
		super.onDestroy();
		// 停止监听service
		stopService(intentService);
		//baidu map
		stopPosition();
	}

	private void stopPosition() {
		if (mLocationClient != null) {
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(locationListener);
		}
	}

	/**
	 * 初始化loadimage
	 */
	private void initImageLoader() {
		if (!ImageLoader.getInstance().isInited()) {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					this)
					.threadPoolSize(3)
					// default
					.threadPriority(Thread.NORM_PRIORITY - 2)
					// default
					.denyCacheImageMultipleSizesInMemory()
					.memoryCache(new LruMemoryCache(12 * 1024 * 1024))
					.memoryCacheSize(12 * 1024 * 1024)
					.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.tasksProcessingOrder(QueueProcessingType.LIFO).build();
			ImageLoader.getInstance().init(config);
		}
	}

	/**
	 * 设置默认fragment
	 */
	private void setDefaultFragment() {
		// 设置标题栏
		textViewTitle.setText("首页");

		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		homeFragment = new HomeFragment();
		transaction.replace(R.id.fg_main_content, homeFragment);
		transaction.commit();

		imageViewHome.setSelected(true);
		textViewHome.setSelected(true);
	}

	/**
	 * 点击事件
	 *
	 * @param view
	 */
	public void onClick(View view) {

		if (never_back_pressed) {

			initLifeCircle();

		} else {

			FragmentManager fm = this.getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();

			String service = "";
			switch (view.getId()) {
			case R.id.txt_main_address:
				Intent intentCity = new Intent(this, MyLifeCircleActivity.class);
				this.startActivityForResult(intentCity, REQUEST_CODE);

				// AddressPopup addressPopup=new
				// AddressPopup(MainActivity.this);
				// addressPopup.showPopupWindow(imageViewSelectAddress);
				// initPopWindow();
				break;
			case R.id.lay_main_home:

				textViewTitle.setText("首页");
				homeFragment = new HomeFragment();
				transaction.replace(R.id.fg_main_content, homeFragment);
				transaction.commit();

				imageViewHome.setSelected(true);
				textViewHome.setSelected(true);
				imageViewBangbang.setSelected(false);
				textViewBangbang.setSelected(false);
				imageViewShop.setSelected(false);
				textViewShop.setSelected(false);
				imageViewActivity.setSelected(false);
				textViewActivity.setSelected(false);
				imageViewMyself.setSelected(false);
				textViewMyself.setSelected(false);

				lay_main_title.setVisibility(View.VISIBLE);
				break;
			case R.id.lay_main_bangbang:
				textViewTitle.setText("社区通");
				Fragment f1 = new CommunityFragment();
				transaction.replace(R.id.fg_main_content, f1);
				transaction.commit();

				imageViewHome.setSelected(false);
				textViewHome.setSelected(false);
				imageViewBangbang.setSelected(true);
				textViewBangbang.setSelected(true);
				imageViewShop.setSelected(false);
				textViewShop.setSelected(false);
				imageViewActivity.setSelected(false);
				textViewActivity.setSelected(false);
				imageViewMyself.setSelected(false);
				textViewMyself.setSelected(false);

				lay_main_title.setVisibility(View.VISIBLE);
				break;
			case R.id.lay_main_shop:

				textViewTitle.setText("品牌街");
				Fragment f2 = new ShopFragment();
				transaction.replace(R.id.fg_main_content, f2);
				transaction.commit();

				imageViewHome.setSelected(false);
				textViewHome.setSelected(false);
				imageViewBangbang.setSelected(false);
				textViewBangbang.setSelected(false);
				imageViewShop.setSelected(true);
				textViewShop.setSelected(true);
				imageViewActivity.setSelected(false);
				textViewActivity.setSelected(false);
				imageViewMyself.setSelected(false);
				textViewMyself.setSelected(false);

				lay_main_title.setVisibility(View.VISIBLE);
				break;
			case R.id.lay_main_ativity:
				textViewTitle.setText("活动汇");
				f_activity = new ActivityFragment();
				transaction.replace(R.id.fg_main_content, f_activity);
				transaction.commit();

				imageViewHome.setSelected(false);
				textViewHome.setSelected(false);
				imageViewBangbang.setSelected(false);
				textViewBangbang.setSelected(false);
				imageViewShop.setSelected(false);
				textViewShop.setSelected(false);
				imageViewActivity.setSelected(true);
				textViewActivity.setSelected(true);
				imageViewMyself.setSelected(false);
				textViewMyself.setSelected(false);

				lay_main_title.setVisibility(View.GONE);
				break;

			case R.id.lay_main_myself:
				textViewTitle.setText("我的");
				mineFragment = new MineFragment();
				transaction.replace(R.id.fg_main_content, mineFragment);
				transaction.commit();

				imageViewHome.setSelected(false);
				textViewHome.setSelected(false);
				imageViewBangbang.setSelected(false);
				textViewBangbang.setSelected(false);
				imageViewShop.setSelected(false);
				textViewShop.setSelected(false);
				imageViewActivity.setSelected(false);
				textViewActivity.setSelected(false);
				imageViewMyself.setSelected(true);
				textViewMyself.setSelected(true);

				lay_main_title.setVisibility(View.VISIBLE);
				break;
			// 消息圈
			case R.id.img_main_Msgecircle:
				Intent intentMessageCircle = new Intent(this,
						MainMessageCircleActivity.class);
				this.startActivity(intentMessageCircle);
				break;

			// 便民电话
			case R.id.lay_bmdh:
				Intent intentBmdh = new Intent(this,
						CommonConvenPhoneListActivity.class);
				this.startActivity(intentBmdh);
				break;
			// 家政服务
			case R.id.lay_jzfw:
				service = "家政服务";
				Intent intentJzfw = new Intent(this, PluginActivity.class);
				intentJzfw.putExtra(PluginActivity.PARAM_URL,
						Contants.getHost()
								+ "/help/app/server/index.html?serversName="
								+ service);
				intentJzfw.putExtra(PluginActivity.PARAM_TITLE, service);
				intentJzfw.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
				startActivity(intentJzfw);
				break;
			// 家居维修
			case R.id.lay_jjwx:
				service = "家居维修";
				Intent intentJjwx = new Intent(this, PluginActivity.class);
				intentJjwx.putExtra(PluginActivity.PARAM_URL,
						Contants.getHost()
								+ "/help/app/server/index.html?serversName="
								+ service);
				intentJjwx.putExtra(PluginActivity.PARAM_TITLE, service);
				intentJjwx.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
				startActivity(intentJjwx);
				break;
			// 快递收发
			case R.id.lay_kdsf:
				service = "快递收发";
//				Intent intentKdsf = new Intent(this, PluginActivity.class);
//				intentKdsf.putExtra(PluginActivity.PARAM_URL,
//						Contants.getHost()
//								+ "/help/app/server/index.html?serversName="
//								+ service);
//				intentKdsf.putExtra(PluginActivity.PARAM_TITLE, service);
//				intentKdsf.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
//				startActivity(intentKdsf);
				Intent intentKdsf = new Intent(this, PayDtlActivity.class);
				startActivity(intentKdsf);



				break;

			case R.id.lay_tsjy:
				Intent intentTsjy = new Intent(this,
						CommunitySuggestActivity.class);
				this.startActivity(intentTsjy);
				break;
			case R.id.lay_sqhz:
				Intent intentSqhz = new Intent(this, ForumPostsActivity.class);
				this.startActivity(intentSqhz);
				break;
			// case R.id.lay_sjkm:
			//
			// Intent intentSjkm = new Intent(this,
			// OpenDoorActivity.class);
			// this.startActivity(intentSjkm);
			// break;
			case R.id.lay_kdcx:
				Intent intentKdcx = new Intent(this, ExpressInquiry.class);
				this.startActivity(intentKdcx);
				break;
			default:
				Toast.makeText(getApplicationContext(), "敬请期待!",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT)
					.show();
		} else if (!"000000".equals(rtnCode)) {
			String rtnMsg = (String) responseContent
					.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
			Toast.makeText(getApplicationContext(), rtnMsg, Toast.LENGTH_SHORT)
					.show();
		} else {
			if (responseContent.getResponseCode().equals(GET_BASICINFO)) {
				// 获取用户主表信息
				UserBasic userBasicInfo = (UserBasic) BeansUtil.map2Bean(
						(Map<String, String>) responseContent.getData(),
						UserBasic.class);
				UserSession.getInstance().setUserBasic(userBasicInfo);

				aliImLogion(); // 阿里百川登陆
				initLifeCircle();

				Log.v("gl", "user===" + userBasicInfo.getLifecircleId()
						+ "name==" + userBasicInfo.getLifecircleName());

			} else if (GET_LIFE_CIRCLE
					.equals(responseContent.getResponseCode())) {
				// 获取附近生活圈
				String jsonStr = responseContent.getDataJson().trim();
				Log.v(TAG, "query by location jsonStr:" + jsonStr);
				Gson gson = new Gson();
				addressVoList.clear();
				addressVoList = gson.fromJson(jsonStr,
						new TypeToken<List<LifeCircleAddressVo>>() {
						}.getType());
				for (LifeCircleAddressVo circle : addressVoList) {
					boolean isBindLifecircle = circle.getIsBindLifecircle();
					boolean isInside = circle.getIsInside();
					if (isBindLifecircle && isInside && isOn) {
						if (circle.getRegionalInfos() != null)
							updateNowLifeCircle(circle.getLifecircleId() + "",
									circle.getRegionalInfos().get(0)
											.getRegionalId()
											+ "");
					}
				}

			} else if (UPDATE_NOW_LIFE_CIRCLE.equals(responseContent
					.getResponseCode())) {
				// 更新当前的生活圈
				String jsonStr = responseContent.getDataJson().trim();
				Log.v(TAG, "update now lifecircle jsonStr:" + jsonStr);

				// 获取用户全部信息
				UserBasic userBasicInfo = (UserBasic) BeansUtil.map2Bean(
						(Map<String, String>) responseContent.getData(),
						UserBasic.class);
				UserSession.getInstance().setUserBasic(userBasicInfo);

				Toast.makeText(
						this,
						"您已经开通自动切换我的生活圈" + "\n" + "当前定位的生活圈是["
								+ userBasicInfo.getLifecircleName() + "]"
								+ "\n" + "定位精确度" + DISTANCE + "米",
						Toast.LENGTH_LONG).show();
				initLifeCircle();
				Log.v(TAG,
						"update getLifecircleId:"
								+ userBasicInfo.getLifecircleName());
			} else if (HTTP_SET_READ_MSGS.equals(responseContent
					.getResponseCode())) {
				if (responseContent.getData() != null) {

					String jsonStr = responseContent.getDataJson();
					Gson gson = new Gson();
					MsgCircleVo msgCircleVo = gson.fromJson(jsonStr,
							new TypeToken<MsgCircleVo>() {
							}.getType());
					List<MessageOutlineBean> allOutlineBeans = msgCircleVo
							.getOutlineList();
					int unreadCount = initList(allOutlineBeans);
					Log.v("gl", "unreadCount==" + unreadCount);

					if (unreadCount == 0) {

						isNewMsg = false;
						showMsgCircleFlag();

					} else {

						isNewMsg = true;
						showMsgCircleFlag();

					}

					if (fangan.equals("1")) {
						// woca 非要改成错的必须注释，
						showPostFlag();
					}
				}
			} else if (RQ_MY_POSTS.equals(responseContent.getResponseCode())) {
				String jsonStr = responseContent.getDataJson().trim();
				System.out.println("data=" + jsonStr);
				if (jsonStr != null && !"".equals(jsonStr)
						&& jsonStr instanceof String) {

					try {
						JSONObject json = new JSONObject(jsonStr);
						jsonStr = json.get("data").toString();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Gson gson = new Gson();
					List<Notice> list = gson.fromJson(jsonStr,
							new TypeToken<List<Notice>>() {
							}.getType());

					if (list.isEmpty())
						Contants.isPostMsg = false;
					else
						Contants.isPostMsg = true;

					Log.v("gl", "Contants.isPostMsg=" + Contants.isPostMsg);
				}
				showPostFlag();
			}
		}
	}

	/**
	 * 阿里百川登录
	 */
	private void aliImLogion() {
		// 通知栏相关的初始化
		// NotificationInitSampleHelper.init();
		setTag();

		if (UserSession.getInstance().isaliLogin()) {
			return;// 如果已登录，则不再重复登录
		}
		String userId = UserSession.getInstance().getUserBasic()
				.getXbbTaobaoName();
		String password = UserSession.getInstance().getUserBasic()
				.getXbbTaobaoPass();
		loginHelper.login_Sample(userId, password, new IWxCallback() {
			@Override
			public void onSuccess(Object... arg0) {
				Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
				UserSession.getInstance().setIsaliLogin(true);
				initImiInfo();
			}

			@Override
			public void onProgress(int arg0) {
			}

			@Override
			public void onError(int errorCode, String errorMessage) {
				if (errorCode == YWLoginCode.LOGON_FAIL_INVALIDUSER) { // 若用户不存在，则提示使用游客方式登陆
				} else {
					YWLog.w(TAG, "登录失败，错误码：" + errorCode + "  错误信息："
							+ errorMessage);
					Notification.showToastMsg(MainActivity.this, errorMessage);
				}
			}
		});
	}

	protected void initImiInfo() {

		if (mIMKit == null) {
			return;
		}
		initConversationServiceAndListener();

		// 消息页面增加的常驻项目
		CustomConversationHelper.addCustomConversation("myconversation", null);
		// CustomConversationHelper.addCustomViewConversation("myConversation",
		// "这个会话的展示布局可以自定义");

		// resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
		mConversationUnreadChangeListener.onUnreadChange();

		// 在tab底部状态栏增加会话未读消息变化的全局监听器
		mConversationService
				.addTotalUnreadChangeListener(mConversationUnreadChangeListener);

		// IYWTribeService tribeService = mIMKit.getTribeService();
		// tribeService.addTribeListener(mTribeChangedListener);
	}

	/**
	 * 未读消息小红点提示
	 */
	private void initConversationServiceAndListener() {
		mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {

			@Override
			public void onUnreadChange() {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						LoginSampleHelper loginHelper = LoginSampleHelper
								.getInstance();
						final YWIMKit imKit = loginHelper.getIMKit();
						mConversationService = imKit.getConversationService();
						// int unReadCount =
						// mConversationService.getAllUnreadCount();
						// if (unReadCount > 0) {
						// mUnread.setVisibility(View.VISIBLE);
						// if (unReadCount < 100) {
						// mUnread.setText(unReadCount + "");
						// } else {
						// mUnread.setText("99+");
						// }
						// } else {
						// mUnread.setVisibility(View.INVISIBLE);
						// }
					}
				});
			}
		};
	}

	protected void updateNowLifeCircle(String lifecircleId, String regionalId) {

		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);

		reqContent.appendData("lifecircleId", lifecircleId);
		reqContent.appendData("regionalId", regionalId);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.UPDATE_NOW_LIFE_CIRCLE.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(UPDATE_NOW_LIFE_CIRCLE);
		HttpAsyncTask task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}

	protected void initLifeCircle() {
		if (UserSession.getInstance().getUserBasic() != null
				&& UserSession.getInstance().getUserBasic().getLifecircleName() != null
				&& !UserSession.getInstance().getUserBasic()
						.getLifecircleName().trim().equals("")) {
			Log.v("gl", "getLifecircleName=="
					+ UserSession.getInstance().getUserBasic()
							.getLifecircleName());
			txt_main_address.setText(UserSession.getInstance().getUserBasic()
					.getLifecircleName());

			never_back_pressed = false;

		} else {
			never_back_pressed = true;

			new AlertDialog.Builder(this)
					.setMessage("设置您的生活圈？")
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intentCircle = new Intent(
											MainActivity.this,
											MyLifeCircleActivity.class);
									startActivityForResult(intentCircle,
											REQUEST_CODE);
								}
							}).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("gl", "onActivityResult");
		// if (RESULT_OK == resultCode && REQUEST_CODE == requestCode)
		if (REQUEST_CODE == requestCode) {
			initLifeCircle();
		}
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			CloudPushService pushService = AlibabaSDK
					.getService(CloudPushService.class);
			pushService.unbindAccount();

			File xbb = new File(Environment.getExternalStorageDirectory(),
					"xbb");
			if (!xbb.exists()) { // 判断目录是否存在
				xbb.mkdirs(); // 如果不存在则先创建目录
			}
			File myvoice = new File(Environment.getExternalStorageDirectory(),
					"myvoice");
			if (!myvoice.exists()) {
				myvoice.mkdirs();
			}

			FileUtils.clearFileWithPath(xbb.getAbsolutePath());
			FileUtils.clearFileWithPath(myvoice.getAbsolutePath());

			finish();
			System.exit(0);
		}
	}

	public void refreshActivityFragment() {

		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		f_activity = new ActivityFragment();
		transaction.replace(R.id.fg_main_content, f_activity);
		transaction.commit();

	}

	private void showMsgCircleFlag() {
		// TODO Auto-generated method stub
		if (isNewMsg)
			ImageLoader.getInstance().displayImage(
					"drawable://" + R.drawable.msgcircle_yes,
					img_main_Msgecircle);
		else
			ImageLoader.getInstance().displayImage(
					"drawable://" + R.drawable.msgcircle_no,
					img_main_Msgecircle);

	}

	private void showPostFlag() {
		// TODO Auto-generated method stub
		if (Contants.isPostMsg) {
			img_new_post.setVisibility(View.VISIBLE);
			if (mineFragment != null && mineFragment.img_new_post != null) {
				mineFragment.img_new_post.setVisibility(View.VISIBLE);
			}
		} else {
			img_new_post.setVisibility(View.GONE);
			if (mineFragment != null && mineFragment.img_new_post != null) {
				mineFragment.img_new_post.setVisibility(View.GONE);
			}
		}
	}

	private int initList(List<MessageOutlineBean> allOutlineBeans) {

		int count = 0;
		boolean exist = false;

		for (MessageOutlineBean messageOutlineBean : allOutlineBeans) {

			if (messageOutlineBean.getCategory().equals(
					"CATEGORY_COMMUNITY_MSG")) {

				exist = true;

				if (messageOutlineBean.getUnreadCount() > 0) {

					Contants.isPostMsg = true;

					return messageOutlineBean.getUnreadCount();
				} else {

					Contants.isPostMsg = false;

				}
			} else {

				if (messageOutlineBean.getUnreadCount() > 0) {

					count = messageOutlineBean.getUnreadCount();

				}

				if (!exist) {

					Contants.isPostMsg = false;

				}

			}
		}

		Log.v("gl", "count==" + count);
		Log.v("gl", "isPostMsg==" + Contants.isPostMsg);
		return count;
	}

	private void setTag() {
		Logger.i(TAG, "bindAccount---->start ");
		// TODO Auto-generated method stub
		CloudPushService pushService = AlibabaSDK
				.getService(CloudPushService.class);
		String tag = UserSession.getInstance().getUserBasic()
				.getXbbTaobaoName();
		Logger.v(TAG, "bindAccount-->" + tag);
		pushService.bindAccount(tag, new CommonCallback() {

			@Override
			public void onSuccess() {
				Logger.v(TAG, "bindAccount success");
			}

			@Override
			public void onFailed(String arg0, String arg1) {

			}
		});
	}

	private void getData(int pageIndex) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.pageIndex.name(),
				pageIndex + "");
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.pageSize.name(),
				1 + "");

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_Message_Lst.getValue());
		httpRequestEntity.setRequestCode(RQ_MY_POSTS);
		httpRequestEntity.setHasData(true);

		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		HttpAsyncTask task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}

}