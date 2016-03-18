package com.hdzx.tenement.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.vo.AdvertisementImage;
import com.hdzx.tenement.common.vo.AdvertisementVO;
import com.hdzx.tenement.community.adapter.SimpleImgTxtAdapter;
import com.hdzx.tenement.community.ui.ArticleDtlActivity;
import com.hdzx.tenement.community.vo.Arcticle;
import com.hdzx.tenement.community.vo.ServiceBean;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.utils.ClearUtils;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.HorizontalListView;
import com.hdzx.tenement.utils.Logger;
import com.hdzx.tenement.utils.MyGridView;
import com.hdzx.tenement.utils.PreferencesUtils;
import com.hdzx.tenement.utils.Task;
import com.hdzx.tenement.utils.WebViewUtil;
import com.hdzx.tenement.vo.CommodityVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 品牌街
 */
@SuppressLint("RtlHardcoded")
public class ShopFragment extends Fragment implements IContentReportor,
		OnClickListener {

	private HttpAsyncTask httpAsyncTask;
	private String TAG_TGT_AUTO = "tgt";
	private String TAG_ST_AUTO = "st";
	/**
	 * HTTP TAG
	 */
	private static final String HTTP_TAG_GET_BUSSINESSLIST = "getbussinesslist_f";
	private static final String HTTP_TAG_GET_ADVERTISEMENT = "http_tag_get_advertisement_f";
	private static final String HTTP_TAG_GET_PAGE_ITEMS_TREE = "http_tag_get_pageitemstree";
	private static final String HTTP_TAG_GET_ARTICLE_LST = "http_tag_get_articlelist";
	String poster_url = "/m/index/index";
	String path = "/shiro-cas";
	WebView webview;
	private Activity activity;

	// 私人定制
	private LinearLayout ll_personal;
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
	/**
	 * 品牌街列表对象
	 */
	private List<CommodityVo> commodityVoList;

	private ScheduledExecutorService scheduledExecutorService;
	private DisplayMetrics displayMetrics = null;

	private MyGridView gridview;
	private String url = "";
	private HorizontalListView horizon_listview;
	private HorizontalListViewAdapter hListViewAdapter;
	private ScrollView scrollView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// View view = inflater.inflate(R.layout.tenement_main_shop, container,
		// false);
		// initWebView(view);

		View view = inflater.inflate(R.layout.tenement_main_street, container,
				false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		dot0 = view.findViewById(R.id.v_dot0);
		dot1 = view.findViewById(R.id.v_dot1);
		dot2 = view.findViewById(R.id.v_dot2);
		dot3 = view.findViewById(R.id.v_dot3);
		dot4 = view.findViewById(R.id.v_dot4);
		viewPagerAd = (ViewPager) view.findViewById(R.id.vp_main_ad);
		ll_personal = (LinearLayout) view.findViewById(R.id.ll_personal);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		displayMetrics = activity.getResources().getDisplayMetrics();

		gridview = (MyGridView) view.findViewById(R.id.gridview);
		horizon_listview = (HorizontalListView) view.findViewById(R.id.horizon_listview);

		viewPagerAd.setFocusable(true);
		viewPagerAd.setFocusableInTouchMode(true);
		viewPagerAd.requestFocus();
		/**
		 * 获取品牌街列表
		 */
		getShopInfo(activity);
		makeServiceInfoRequest();
		makeAdvertisementRquest("16");
		getArticleLst("17");
	}

	public void makeServiceInfoRequest() {
		Task.getPageItemsTree(activity, this, "9", "",
				HTTP_TAG_GET_PAGE_ITEMS_TREE);
	}

	public void makeAdvertisementRquest(String advertiseId) {
		Task.getAdvertiseInfo(getActivity(), this, advertiseId,
				displayMetrics.heightPixels + "*" + displayMetrics.widthPixels,
				HTTP_TAG_GET_ADVERTISEMENT);
	}

	private void getArticleLst(String articleClassId) {
		// TODO Auto-generated method stub
		Task.getArticleList(activity, this, HTTP_TAG_GET_ARTICLE_LST,
				articleClassId);
	}

	/**
	 * 
	 * @param activity
	 */
	private void getShopInfo(Activity activity) {
		Task.getBrandsList(getActivity(), this, HTTP_TAG_GET_BUSSINESSLIST);
	}

	protected void getTGT() {
		// 查看是否存在账号，密码
		String usn = PreferencesUtils.getInstance().takeString(activity,
				Contants.PREFERENCES_KEY.usn.name());
		String psw = PreferencesUtils.getInstance().takeString(activity,
				Contants.PREFERENCES_KEY.psw.name());
		if (StringUtils.isNotBlank(usn) && StringUtils.isNotBlank(psw)) {

			// HEARD
			RequestContentTemplate reqContent = new RequestContentTemplate();
			reqContent.setEncryptoType(CryptoTyepEnum.aes);// 请求使用AES加密

			// BODY
			reqContent.setRequestTicket(false);
			reqContent.appendData(
					Contants.PROTOCOL_REQ_BODY_DATA.loginName.name(), usn);
			reqContent.appendData(
					Contants.PROTOCOL_REQ_BODY_DATA.password.name(), psw);
			reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.type.name(),
					Contants.LONGIN_TYPE_LOG);

			// SEND
			HttpRequestEntity httpRequestEntity = new HttpRequestEntity(
					reqContent, Contants.SERVER_HOST,
					Contants.PROTOCOL_COMMAND.GET_TGT.getValue());
			httpRequestEntity.setRequestCode(TAG_TGT_AUTO);
			httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);// 返回使用AES密钥解密
			httpAsyncTask = new HttpAsyncTask(activity, this);
			httpAsyncTask.execute(httpRequestEntity);
		}

	}

	protected void getST(String tgt) {

		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(false);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.tgt.name(), tgt);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.service.name(),
				UserSession.getInstance().getFrontUrl() + path);

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_ST.getValue());
		httpRequestEntity.setRequestCode(TAG_ST_AUTO);
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);// 返回使用AES密钥解密
		httpAsyncTask = new HttpAsyncTask(activity, this);
		httpAsyncTask.execute(httpRequestEntity);
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			Toast.makeText(activity, "返回为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!"000000".equals(rtnCode)) {
			String rtnMsg = (String) responseContent
					.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
			Toast.makeText(activity, rtnMsg, Toast.LENGTH_SHORT).show();
			return;
		}

		if (responseContent.getResponseCode().equals(TAG_TGT_AUTO)) {
			Object data = responseContent.getData();
			if (data != null && !"".equals(data) && data instanceof String) {
				String tgt = (String) data;
				UserSession.getInstance().setAccessTicket(tgt);
				getST(tgt);
			} else {
				Toast.makeText(activity, "服务器异常，请稍后尝试。", Toast.LENGTH_SHORT)
						.show();
			}
			return;
		}
		if (responseContent.getResponseCode().equals(TAG_ST_AUTO)) {

			Object data = responseContent.getData();
			if (data != null && !"".equals(data) && data instanceof String) {
				String st = (String) data;

				// http://192.168.0.48/leimingtech-front/shiro-cas?ticket=ST-47-Dx7FygYGbqbtuIwxPcS7-cas01.example.org&rtnurl=http://192.168.0.48/leimingtech-front/m/goods/goodsdetail?id=197
				String service = UserSession.getInstance().getFrontUrl() + path
						+ "?" + "ticket=" + st + "&rtnurl="
						+ UserSession.getInstance().getFrontUrl() + poster_url;

				Log.v("gl", "service==" + service);
				webview.clearCache(true);
				webview.clearHistory();
				ClearUtils.ClearCookies(activity);
				webview.loadUrl(service);
			} else {
				getTGT();
			}
			return;
		}
		if (responseContent.getResponseCode()
				.equals(HTTP_TAG_GET_BUSSINESSLIST)) {
			String jsonStr = responseContent.getDataJson();
			Logger.v("加载品牌街商品", "commodityVoList-->"+jsonStr);
			Gson gson = new Gson();
			commodityVoList = gson.fromJson(jsonStr,
					new TypeToken<List<CommodityVo>>() {
					}.getType());
			
			// 加载品牌街商品
			if(!commodityVoList.isEmpty())
				initCompity(this.getView());
			return;
		}
		if (responseContent.getResponseCode().equals(
				HTTP_TAG_GET_PAGE_ITEMS_TREE)) {
			String jsonStr = responseContent.getDataJson();
			Logger.v("加载私人定制", "jsonStr-->");
			Logger.v("", "serviceInfo:" + jsonStr);
			Gson gson = new Gson();
			List<ServiceBean> serviceBeanList = gson.fromJson(jsonStr,
					new TypeToken<List<ServiceBean>>() {
					}.getType());
			if (serviceBeanList != null && !serviceBeanList.isEmpty()) {
				initSubView(ll_personal, serviceBeanList);
			}
			return;
		}
		if (responseContent.getResponseCode()
				.equals(HTTP_TAG_GET_ADVERTISEMENT)) {
			String dataJsonStr = responseContent.getDataJson();
			List<AdvertisementVO> vo = null;
			Gson gson = new Gson();
			try {
				Logger.v("加载广告", "AdvertisementVOList-->");
				vo = gson.fromJson(dataJsonStr,
						new TypeToken<List<AdvertisementVO>>() {
						}.getType());
				if (vo != null && vo.size() > 0) {
					if (vo.get(0).getImage() != null) {
						initAd(vo.get(0).getImage());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if (responseContent.getResponseCode().equals(HTTP_TAG_GET_ARTICLE_LST)) {
			String jsonStr = responseContent.getDataJson();
			Logger.v("", "serviceInfo:" + jsonStr);
			Logger.v("加载选购攻略", "jsonStr-->");
			try {
				JSONObject json = new JSONObject(jsonStr);
				jsonStr = json.get("articleClasses").toString();
				url = json.getString("accessAddress");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Gson gson = new Gson();
			final List<Arcticle> list = gson.fromJson(jsonStr,
					new TypeToken<List<Arcticle>>() {
					}.getType());
			if (list != null && !list.isEmpty()) {
				SimpleImgTxtAdapter adapter = new SimpleImgTxtAdapter(activity,
						list);
				gridview.setAdapter(adapter);

				gridview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (list != null && !list.isEmpty()) {
							String load = url + "?acId="
									+ list.get(arg2).getAcId();
							Intent intent = new Intent();
							intent.setClass(activity, ArticleDtlActivity.class);
							intent.putExtra("url",
									WebViewUtil.formateString(load));
							intent.putExtra("name", list.get(arg2).getAcName());
							startActivity(intent);
						}
					}
				});
			}
			return;
		}
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

	private void initSubView(ViewGroup container,
			List<ServiceBean> serviceBeanList) {
		LinearLayout oneSubServiceLayout = new LinearLayout(this.activity);
		oneSubServiceLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		oneSubServiceLayout.setLayoutParams(layoutParams);

		TextView titleText;
		ImageView imageView;
		LinearLayout onServiceLL;
		LinearLayout.LayoutParams llParams;
		LinearLayout.LayoutParams imageLParams;
		LinearLayout.LayoutParams textLParams;
		int width = displayMetrics.widthPixels / 4;
		int imageWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics);
		for (final ServiceBean item : serviceBeanList) {
			onServiceLL = new LinearLayout(this.activity);
			llParams = new LinearLayout.LayoutParams(width, width);
			onServiceLL.setLayoutParams(llParams);
			onServiceLL.setOrientation(LinearLayout.VERTICAL);
			onServiceLL.setGravity(Gravity.CENTER);
			onServiceLL.setOnClickListener(this);
			onServiceLL.setTag(item);

			imageView = new ImageView(this.activity);
			ImageLoader.getInstance().displayImage(item.getServiceIcon(),
					imageView,  MyApplication.getInstance().getSimpleOptions());
			imageLParams = new LinearLayout.LayoutParams(imageWidth, imageWidth);
			imageView.setLayoutParams(imageLParams);

			titleText = new TextView(this.activity);
			titleText.setText(item.getServiceName());
			titleText.setTextSize(15);
			titleText.setTextColor(Color.BLACK);
			textLParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			textLParams.topMargin = 20;
			titleText.setLayoutParams(textLParams);

			onServiceLL.addView(imageView);
			onServiceLL.addView(titleText);
			
			
			onServiceLL.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					String service = item.getServiceName();
					String service ="私人定制";
					Intent intent = new Intent(activity, PluginActivity.class);
					intent.putExtra(PluginActivity.PARAM_URL,
							Contants.getHost()
									+ "/help/app/server/home_new.html?serversName="
									+ service);
					intent.putExtra(PluginActivity.PARAM_TITLE,
							service);
					intent.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
					startActivity(intent);
				}
			});

			oneSubServiceLayout.addView(onServiceLL);
		}

		container.addView(oneSubServiceLayout);
	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		ServiceBean service = (ServiceBean) view.getTag();
		intent = new Intent(this.activity, PluginActivity.class);
		intent.putExtra(PluginActivity.PARAM_URL,
				Contants.getHost() + "/help/app/server/index.html?serversName="
						+ service.getServiceName());
		intent.putExtra(PluginActivity.PARAM_TITLE, service.getServiceName());
		intent.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
	}

}
