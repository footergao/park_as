package com.hdzx.tenement.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.push.CloudPushService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.vo.AdvertisementImage;
import com.hdzx.tenement.common.vo.AdvertisementVO;
import com.hdzx.tenement.community.ui.OpenDoorActivity;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.ui.DongdongxiaAuthActivity;
import com.hdzx.tenement.mine.ui.MinePersonInfoAuthActivity;
import com.hdzx.tenement.utils.AlgorithmUtil;
import com.hdzx.tenement.utils.BeansUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.PreferencesUtils;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.HorizontalListView;
import com.hdzx.tenement.vo.CommodityVo;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by anchendong on 15/7/17.
 */
public class HomeFragment extends Fragment implements IContentReportor {

	private View view;
	// 当前图片的索引号
	private int currentItem = 0;

	// 轮播广告的数据
	private List<AdvertisementImage> adList;

	// 滑动的图片集合
	private List<ImageView> imageViewsAd;

	// 图片标题正文的那些点
	private List<View> dots = new ArrayList<View>();
	private List<View> dotList = new ArrayList<View>();

	// 定义的五个指示点
	private View dot0;
	private View dot1;
	private View dot2;
	private View dot3;
	private View dot4;
	// 广告viewpage
	private ViewPager viewPagerAd;

	private ScheduledExecutorService scheduledExecutorService;

	/**
	 * 品牌街列表对象
	 */
	private List<CommodityVo> commodityVoList;


	/**
	 * http
	 */
	private HttpAsyncTask httpAsyncTask;

	private HttpAsyncTask advertisementTask;

	/**
	 * HTTP TAG
	 */
	private static final String HTTP_TAG_GET_BUSSINESSLIST = "getbussinesslist";

	private static final String HTTP_TAG_GET_ADVERTISEMENT = "http_tag_get_advertisement";

	private String GET_CAN_ACCEPT_ORDER_NUM = "getCanAcceptOrderNum";// 获取所有可接订单和当前咚咚侠可接订单数目

	private TextView tv_ordre_num;

	private ImageView img_ddx;

	private String level;

	private String isHelpMan = "0";// 是否是咚咚侠：0-否，1-是
	private static final String GET_AGAIN_BASICINFO = "getagainbasicinfo";
	private Activity activity;
	
	
	
	private HorizontalListView horizon_listview;
	private HorizontalListViewAdapter hListViewAdapter;
	
	private LinearLayout lay_sjkm;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.tenement_main_fragment, container,
				false);
		dot0 = view.findViewById(R.id.v_dot0);
		dot1 = view.findViewById(R.id.v_dot1);
		dot2 = view.findViewById(R.id.v_dot2);
		dot3 = view.findViewById(R.id.v_dot3);
		dot4 = view.findViewById(R.id.v_dot4);

		tv_ordre_num = (TextView) view.findViewById(R.id.tv_ordre_num);
		viewPagerAd = (ViewPager) view.findViewById(R.id.vp_main_ad);

		img_ddx = (ImageView) view.findViewById(R.id.img_ddx);
		
		lay_sjkm = (LinearLayout) view.findViewById(R.id.lay_sjkm);
		
		horizon_listview = (HorizontalListView) view.findViewById(R.id.horizon_listview);

		/**
		 * 初始化界面
		 */
		initView(view);

		/**
		 * 获取品牌街列表
		 */
		getShopInfo(activity);
		/**
		 * 初始化数据
		 */
		initData();

		makeAdvertisementRquest("6");
		// 初始化事件
		initAction();

		return view;
	}

	public void initView(View view) {
		// 广告窗口初始化
	}

	public void initData() {

		if (UserSession.getInstance().getUserBasic() != null) {
			level = UserSession.getInstance().getUserBasic().getLevel();
			isHelpMan = UserSession.getInstance().getUserBasic().getIsHelpMan();

			if (isHelpMan != null && isHelpMan.equals("1"))
				getCanAcceptOrderInfo(activity);
		}
	}

	public void getShopInfo(Context context) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.type.name(), "1");

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_BUSSINESSLIST.getValue());
		httpRequestEntity.setRequestCode(HTTP_TAG_GET_BUSSINESSLIST);
		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		httpAsyncTask = new HttpAsyncTask(context, this);
		httpAsyncTask.execute(httpRequestEntity);
	}

	protected void getCanAcceptOrderInfo(Context context) {

		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_CAN_ACCEPT_ORDER_NUM.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(GET_CAN_ACCEPT_ORDER_NUM);
		HttpAsyncTask task = new HttpAsyncTask(context, this);
		task.execute(httpRequestEntity);
	}

	private void initCompity(View view) {
		if (view == null || view.getContext() == null) {
			return;
		}

		  hListViewAdapter = new HorizontalListViewAdapter(activity,commodityVoList);  
		  horizon_listview.setAdapter(hListViewAdapter);  
		  hListViewAdapter.notifyDataSetChanged();
		  
		  
		  horizon_listview.setOnItemClickListener(new OnItemClickListener() {  
			  
	            @Override  
	            public void onItemClick(AdapterView<?> parent, View view,  
	                    int position, long id) {  

					if (UserSession.getInstance().getUserBasic() != null
							&& UserSession.getInstance().getUserBasic()
									.getLifecircleName() != null
							&& !UserSession.getInstance().getUserBasic()
									.getLifecircleName().trim().equals("")) {

						Intent intent = new Intent(activity,
								ProductDetailActivity.class);
						intent.putExtra("poster", commodityVoList.get(position).getPoster());
						startActivity(intent);
					}
	            }  
	        });  
	}

	/**
	 * 广告窗口初始化
	 *
	 * @param view
	 */
	private void initAd(List<AdvertisementImage> AdvertisementList) {
		// 广告数据
		adList = AdvertisementList;

		imageViewsAd = new ArrayList<ImageView>();

		// 点
		dots.add(dot0);
		dots.add(dot1);
		dots.add(dot2);
		dots.add(dot3);
		dots.add(dot4);
		// 异步加载图片
		// 动态添加图片和下面指示的圆点
		// 初始化图片资源
		for (int i = 0; i < adList.size(); i++) {
			ImageView imageView = new ImageView(activity);
			ImageLoader.getInstance().displayImage(adList.get(i).getImageUrl(),
					imageView, MyApplication.getInstance().getAdOptions());
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageViewsAd.add(imageView);
			dots.get(i).setVisibility(View.VISIBLE);
			dotList.add(dots.get(i));

			Log.v("gl", "ad_ulr" + UserSession.getInstance().getImageHost()
					+ adList.get(i).getImageUrl());
		}

		viewPagerAd.setAdapter(new MainAdAdapter(imageViewsAd, adList,activity));// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPagerAd.setOnPageChangeListener(new MyPageChangeListener());
		
		

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPagerAd.setCurrentItem(currentItem);
		}
	};

	private class ScrollTask implements Runnable {
		@Override
		public void run() {
			synchronized (viewPagerAd) {
				currentItem = (currentItem + 1) % imageViewsAd.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	private class MyPageChangeListener implements
			ViewPager.OnPageChangeListener {

		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {

		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			if (this.activity != null) {
				Toast.makeText(this.activity, "返回为空", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (!"000000".equals(rtnCode)) {
			if (this.activity != null) {
				String rtnMsg = (String) responseContent
						.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
				Toast.makeText(this.activity, rtnMsg, Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			// 获取品牌街列表
			if (responseContent.getResponseCode().equals(
					HTTP_TAG_GET_BUSSINESSLIST)) {
				String jsonStr = responseContent.getDataJson();
				// Log.v("test-", jsonStr);
				Gson gson = new Gson();
				commodityVoList = gson.fromJson(jsonStr,
						new TypeToken<List<CommodityVo>>() {
						}.getType());
				// 加载品牌街商品
				if(!commodityVoList.isEmpty())
					initCompity(this.getView());
			} else if (responseContent.getResponseCode().equals(
					HTTP_TAG_GET_ADVERTISEMENT)) {
				String dataJsonStr = responseContent.getDataJson();
				List<AdvertisementVO> vo = null;
				Gson gson = new Gson();
				try {
					vo = gson.fromJson(dataJsonStr,
							new TypeToken<List<AdvertisementVO>>() {
							}.getType());
					if (vo != null&&vo.size()>0) {
						if (vo.get(0).getImage() != null) {
							initAd(vo.get(0).getImage());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (GET_CAN_ACCEPT_ORDER_NUM.equals(responseContent
					.getResponseCode())) {
				// 获取所有可接订单和当前咚咚侠可接订单数目
				String jsonStr = responseContent.getDataJson().trim();
				try {
					JSONObject json = new JSONObject(jsonStr);
					int selfCan = json.getInt("selfCan");
					int allCan = json.getInt("allCan");

					tv_ordre_num.setText("嗨单数量：" + allCan + "单    " + "可接："
							+ selfCan + "单");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v("gl", "getallnum:" + jsonStr);

			} else if (responseContent.getResponseCode().equals(
					GET_AGAIN_BASICINFO)) {

				// 获取用户主表信息
				UserBasic userBasicInfo = (UserBasic) BeansUtil.map2Bean(
						(Map<String, String>) responseContent.getData(),
						UserBasic.class);
				UserSession.getInstance().setUserBasic(userBasicInfo);

				initData();

			}
		}
	}

	// 测试数据

	// /**
	// * 轮播广播模拟数据
	// *
	// * @return
	// */
	// public static List<AdDomain> getBannerAd() {
	// List<AdDomain> adList = new ArrayList<AdDomain>();
	//
	// AdDomain adDomain = new AdDomain();
	// adDomain.setId("108078");
	// adDomain.setImgUrl("https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=5334057a9e22720e7b9bb1ba1df63e74/4afbfbedab64034f2233621ea9c379310a551d2f.jpg");
	// adList.add(adDomain);
	//
	// AdDomain adDomain2 = new AdDomain();
	// adDomain2.setId("108078");
	// adDomain2.setImgUrl("https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=65ed5fa0d23f8794d3aa1b6eb4263ac6/b8389b504fc2d562729d6cf5e11190ef76c66c15.jpg");
	// adList.add(adDomain2);
	//
	// AdDomain adDomain3 = new AdDomain();
	// adDomain3.setId("108078");
	// adDomain3.setImgUrl("https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=409623c48c13632715b89173f7b294de/0824ab18972bd40764b36ce57d899e510fb309b5.jpg");
	// adList.add(adDomain3);
	//
	// AdDomain adDomain4 = new AdDomain();
	// adDomain4.setId("108078");
	// adDomain4.setImgUrl("https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=c253f7a5af773912c473d6219e24b22a/c8177f3e6709c93d449fbfef993df8dcd10054bc.jpg");
	// adList.add(adDomain4);
	//
	// AdDomain adDomain5 = new AdDomain();
	// adDomain5.setId("108078");
	// adDomain5.setImgUrl("https://ss3.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=1993c68fdf33c895a62bcb3bb72e47c2/5fdf8db1cb1349541f94597c504e9258d1094a1b.jpg");
	// adList.add(adDomain5);
	//
	// return adList;
	// }

	public void makeAdvertisementRquest(String advertiseId) {

		DisplayMetrics displayMetrics = activity.getResources()
				.getDisplayMetrics();
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);

		reqContent.appendData(
				Contants.PROTOCOL_REQ_BODY_DATA.appAdvertiseId.name(),
				advertiseId);
		reqContent.appendData(
				Contants.PROTOCOL_REQ_BODY_DATA.resolution.name(),
				displayMetrics.heightPixels + "*" + displayMetrics.widthPixels);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_ADVERTISEMENT.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(HTTP_TAG_GET_ADVERTISEMENT);
		advertisementTask = new HttpAsyncTask(activity, this);
		advertisementTask.execute(httpRequestEntity);
	}

	public void makePersonInforRequest(String requestcode) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(),
				UserSession.getInstance().getAccessTicket());
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), "");

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_PERSON_INFO.getValue());
		httpRequestEntity.setRequestCode(requestcode);
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);// 返回使用AES密钥解密

		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(activity, this);
		httpAsyncTask.execute(httpRequestEntity);

	}

	/**
	 * 初始化事件
	 * 
	 * @param never_back_pressed
	 */
	public void initAction() {
		/*
		 * (view.findViewById(R.id.seckill)) .setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(activity, SeckillMainActivity.class); startActivity(intent); }
		 * });
		 */

		img_ddx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (UserSession.getInstance().getUserBasic() != null
						&& UserSession.getInstance().getUserBasic()
								.getLifecircleName() != null
						&& !UserSession.getInstance().getUserBasic()
								.getLifecircleName().trim().equals("")) {
					if (isHelpMan != null && isHelpMan.equals("1")) {

						if (checkPackage("com.hdzx.tenement.manager")) {
							ComponentName comp = new ComponentName(
									"com.hdzx.tenement.manager",
									"com.hdzx.tenement.manager.ui.GuideActivity");
							
							String usn = PreferencesUtils.getInstance().takeString(
									activity, Contants.PREFERENCES_KEY.usn.name());
							String psw = PreferencesUtils.getInstance().takeString(
									activity, Contants.PREFERENCES_KEY.psw.name());

							String password_encode = AlgorithmUtil.SHA1(psw);
							Intent intent = new Intent();
							intent.putExtra("username", usn);
							intent.putExtra("password", password_encode);
							intent.setComponent(comp);
							intent.setAction("android.intent.action.VIEW");
							startActivity(intent);

							
							CloudPushService pushService = AlibabaSDK
					    				.getService(CloudPushService.class);
					        pushService.unbindAccount();
							activity.finish();
							System.exit(0);
						} else {

							Toast.makeText(activity, "没找到咚咚侠应用",
									Toast.LENGTH_LONG).show();

						}
					} else {

						initAuthorizenView();

					}
				}
			}
		});
		
		
		
		
		lay_sjkm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (UserSession.getInstance().getUserBasic() != null
						&& UserSession.getInstance().getUserBasic()
								.getLifecircleName() != null
						&& !UserSession.getInstance().getUserBasic()
								.getLifecircleName().trim().equals("")) {
					if (isHelpMan != null && isHelpMan.equals("1")) {

						Intent intentSjkm = new Intent(activity,
								OpenDoorActivity.class);
						activity.startActivity(intentSjkm);
						
						
					} else {

						initAuthorizenView();

					}
				}
			}
		});
	}

	private void initAuthorizenView() {
		// 审核状态:1-审核中，0-审核不通过，2-审核通过
		if (level != null && "2".equals(level)) {
			Intent intentAuth = new Intent(activity,
					DongdongxiaAuthActivity.class);
			startActivity(intentAuth);
		} else if (level != null && "1".equals(level)) {
			Toast.makeText(activity, "实名审核中", Toast.LENGTH_LONG).show();
		} else {

			new AlertDialog.Builder(activity)
					.setMessage("您还没有实名认证，前去认证？")
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									Intent intentAuth = new Intent(activity,
											MinePersonInfoAuthActivity.class);
									startActivity(intentAuth);
								}
							})
					.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).show();

		}
	}

	/**
	 * 
	 * 检测该包名所对应的应用是否存在
	 * 
	 * @param packageName
	 * 
	 * @return
	 */

	public boolean checkPackage(String packageName)

	{

		if (packageName == null || "".equals(packageName))

			return false;

		try

		{

			activity.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);

			return true;

		}

		catch (NameNotFoundException e)

		{

			return false;

		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("gl", "onResume");
		makePersonInforRequest(GET_AGAIN_BASICINFO);
	}
}
