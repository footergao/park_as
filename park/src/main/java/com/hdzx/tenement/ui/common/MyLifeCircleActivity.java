package com.hdzx.tenement.ui.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.component.SwitchView;
import com.hdzx.tenement.component.SwitchView.OnStateChangedListener;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.adaper.MyLifeCircleSimpleAdapter;
import com.hdzx.tenement.mine.ui.SelectCellActivity;
import com.hdzx.tenement.mine.vo.LifeCircleAddressVo;
import com.hdzx.tenement.ui.MainActivity;
import com.hdzx.tenement.utils.BaiduLocationClient;
import com.hdzx.tenement.utils.BeansUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.PreferencesUtils;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.vo.LifeCircleAddressBean;
import com.hdzx.tenement.widget.CityPicker;

public class MyLifeCircleActivity extends Activity implements IContentReportor,
		OnClickListener {

	private final String TAG = "MyLifeCircleActivity";

	private String GET_LIFE_CIRCLE = "getLifecircle";
	private String GET_LIFE_CIRCLE_BY_KEYWORD = "getLifecircleByKeyword";
	private String GET_MY_LIFE_CIRCLE = "getMyLifecircle";// 获取我的生活圈
	private String UPDATE_NOW_LIFE_CIRCLE = "updateNowLifecircle";// 更新当前生活圈

	private LocationClient locationClient;

	private MyLocationListener locationListener = null;

	private ListView listView;

	private TextView tv_title;

	private TextView tv_location;

	private EditText edt_search;

	/**
	 * 地址选择器
	 */
	private CityPicker cityPick;

	/**
	 * 地址bean
	 */
	private LifeCircleAddressBean addressBean = new LifeCircleAddressBean();

	/**
	 * 定位后获得的生活圈
	 */
	private List<LifeCircleAddressVo> addressVoList = new ArrayList<LifeCircleAddressVo>();
	/**
	 * 获得我的生活圈
	 */
	private List<LifeCircleAddressVo> myLifeCircleList = new ArrayList<LifeCircleAddressVo>();

	private LinearLayout rl_location;

	private MyLifeCircleSimpleAdapter adapter;

	private String search;

	//
	private SwitchView autoPositionSwitch = null;

	private TextView currCityTV = null;

	private List<View> areaList = null;

	private LinearLayout areaLayout = null;

	private RadioGroup radioLogin;

	private LinearLayout lay_my, lay_near;

	private ImageView backIV = null;

	private RelativeLayout lay_title;// 附近生活圈


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenement_common_select_city);

		areaList = new ArrayList<View>();
		initView();

	}

	private void initView() {
		
		lay_title = (RelativeLayout) findViewById(R.id.lay_title);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.txt_my_life_circle);
		lay_title.setVisibility(View.GONE);

		listView = (ListView) findViewById(R.id.lv_my_life_circle);
		tv_location = (TextView) findViewById(R.id.tv_now_position);

		edt_search = (EditText) findViewById(R.id.edt_search_life_circle);

		rl_location = (LinearLayout) findViewById(R.id.rl_location);

		lay_my = (LinearLayout) findViewById(R.id.lay_my);
		lay_near = (LinearLayout) findViewById(R.id.lay_near);

		radioLogin = (RadioGroup) findViewById(R.id.radiobtn_login);
		radioLogin
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.btn_login_tab: {
							lay_my.setVisibility(View.VISIBLE);
							lay_near.setVisibility(View.GONE);
							getMyLifeCircle();
							break;
						}
						case R.id.btn_regist_tab: {
							lay_my.setVisibility(View.GONE);
							lay_near.setVisibility(View.VISIBLE);
							initLocation();
							startPosition();
							break;
						}
						default:
							break;
						}
					}
				});

		autoPositionSwitch = (SwitchView) this
				.findViewById(R.id.position_switch);
		autoPositionSwitch.setOnText(null);
		boolean isOn = PreferencesUtils.getInstance().takeBoolean(
				MyLifeCircleActivity.this,
				Contants.PREFERENCES_KEY.auto_city.name());
		autoPositionSwitch.setState(isOn);
		autoPositionSwitch
				.setOnStateChangedListener(new OnStateChangedListener() {
					@Override
					public void toggleToOn() {
						PreferencesUtils.getInstance()
								.saveBoolean(
										MyLifeCircleActivity.this,
										Contants.PREFERENCES_KEY.auto_city
												.name(), true);
						autoPositionSwitch.toggleSwitch(true);
					}

					@Override
					public void toggleToOff() {
						PreferencesUtils.getInstance().saveBoolean(
								MyLifeCircleActivity.this,
								Contants.PREFERENCES_KEY.auto_city.name(),
								false);
						autoPositionSwitch.toggleSwitch(false);
					}
				});

		currCityTV = (TextView) this.findViewById(R.id.curr_city_textView);
		areaLayout = (LinearLayout) this.findViewById(R.id.area_layout);

		backIV = (ImageView) this.findViewById(R.id.back_imageView);
		backIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}

		});

		getMyLifeCircle();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
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
				if (cityName != null) {
					stopPosition();
					// currCityTV.setText(cityName);
					tv_location.setText(provinceName + "-" + cityName + "-"
							+ districtName);
					getLifeCircle();

					currCityTV.setText(cityName);
				}
			}
		}
	}

	protected void getMyLifeCircle() {

		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_MY_LIFE_CIRCL.getValue());
		httpRequestEntity.setRequestCode(GET_MY_LIFE_CIRCLE);
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		HttpAsyncTask task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}

	private void makeAreaData(List<LifeCircleAddressVo> myLifeCircleList) {
		areaLayout.removeAllViews();
		areaList.clear();
		String userLifecircleId = UserSession.getInstance().getUserBasic()
				.getLifecircleId()
				+ "";

		for (int i = 0; i < myLifeCircleList.size(); i++) {
			final View view = LayoutInflater.from(this).inflate(
					R.layout.tenement_common_position_item, null);
			areaLayout.addView(view);
			final View layoutItem = view.findViewById(R.id.position_item_layout);
			TextView tvItem = (TextView) view
					.findViewById(R.id.area_name_textView);
			TextView tvCityItem = (TextView) view
					.findViewById(R.id.city_name_textView);
			final ImageView iv = (ImageView) view
					.findViewById(R.id.area_selected_ImageView);
			tvCityItem.setText("["
					+ myLifeCircleList.get(i).getLifecircleCity() + "]");
			tvItem.setText(myLifeCircleList.get(i).getLifecircleName());

			areaList.add(areaLayout);

			final String lifecircleId = myLifeCircleList.get(i).getLifecircleId() + "";
			final String regionalId = myLifeCircleList.get(i).getRegionalId() + "";

			layoutItem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					layoutItem.setBackgroundResource(R.drawable.common_position_area_selected);

					iv.setVisibility(View.VISIBLE);

					updateNowLifeCircle(lifecircleId, regionalId);
				}
			});

			if (userLifecircleId.equals(lifecircleId)) {

				layoutItem.setBackgroundResource(R.drawable.common_position_area_selected);

				iv.setVisibility(View.VISIBLE);
				
				
			} else {
				layoutItem.setBackgroundResource(R.drawable.common_position_area_no_select);
				iv.setVisibility(View.INVISIBLE);
				// lastSelectedView.invalidate();
			}

		}
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {

		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			if (this != null) {
				Toast.makeText(this, "网络异常，请稍后尝试", Toast.LENGTH_SHORT).show();
			}
		} else if (!Contants.ResponseCode.CODE_000000.equals(rtnCode)) {
			if (this != null) {

				String rtnMsg = (String) responseContent
						.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
				Toast.makeText(this, rtnMsg, Toast.LENGTH_SHORT).show();
			}
		} else {
			if (GET_LIFE_CIRCLE.equals(responseContent.getResponseCode())) {
				// 获取附近生活圈
				String jsonStr = responseContent.getDataJson().trim();
				Log.v(TAG, "query by location jsonStr:" + jsonStr);
				Gson gson = new Gson();
				addressVoList.clear();
				addressVoList = gson.fromJson(jsonStr,
						new TypeToken<List<LifeCircleAddressVo>>() {
						}.getType());

				showInfo();
			} else if (GET_MY_LIFE_CIRCLE.equals(responseContent
					.getResponseCode())) {
				// 获取我的生活圈
				String jsonStr = responseContent.getDataJson().trim();
				Log.v(TAG, "query my lifecircle jsonStr:" + jsonStr);
				Gson gson = new Gson();
				myLifeCircleList.clear();
				myLifeCircleList = gson.fromJson(jsonStr,
						new TypeToken<List<LifeCircleAddressVo>>() {
						}.getType());

				makeAreaData(myLifeCircleList);
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

				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				setResult(RESULT_OK, intent);
				finish();
				

				Log.v(TAG,
						"update getLifecircleId:"
								+ userBasicInfo.getLifecircleName());
			} else {

				String jsonStr = responseContent.getDataJson();
				Log.v(TAG, "query by key world addressVoList:" + jsonStr);
				Gson gson = new Gson();
				addressVoList.clear();
				addressVoList = gson.fromJson(jsonStr,
						new TypeToken<List<LifeCircleAddressVo>>() {
						}.getType());

				showInfo();
			}

		}
	}

	private void showInfo() {
		// TODO Auto-generated method stub
		adapter = new MyLifeCircleSimpleAdapter(addressVoList, this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LifeCircleAddressVo addressVo = addressVoList.get(position);

				Intent intent = new Intent();
				intent.setClass(MyLifeCircleActivity.this,
						SelectCellActivity.class);
				intent.putExtra("addressVo", addressVo);
				startActivity(intent);
			}
		});
	}

	/**
	 * 根据地址获取lifcircle
	 */
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

	private void initLocationOption() {
		if (locationClient != null) {
			LocationClientOption mOption = new LocationClientOption();
			mOption.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
			mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			mOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
			mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
			mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
			mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
			mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
			mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
			mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
			mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
			mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
			locationClient.setLocOption(mOption);
		}
	}

	private void stopPosition() {
		if (locationClient != null) {
			locationClient.stop();
			locationClient.unRegisterLocationListener(locationListener);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		stopPosition();
	}

	protected void onDestroy() {
		super.onDestroy();
		stopPosition();
	}

	private void initLocation() {
		locationClient = new LocationClient(this);
		locationListener = new MyLocationListener();
		locationClient.registerLocationListener(locationListener);
		initLocationOption();
	}

	private void startPosition() {
		Log.v("gl","start=="+locationClient.isStarted());
		locationClient.start();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_iv:
			finish();
			break;
		case R.id.btn_search:
			queryLifeCircle();
			break;
		case R.id.rl_location:
			Log.i(TAG, "onclick i am here");
			initPicker();
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.alpha = 0.5f;
			getWindow().setAttributes(params);
			cityPick.showAtLocation(rl_location, Gravity.BOTTOM, 0, 0);
			break;

		default:
			break;
		}
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

	private void initPicker() {

		if (cityPick == null) {
			cityPick = new CityPicker(MyLifeCircleActivity.this);
			cityPick.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					if (cityPick != null) {
						// 设置透明度（这是窗体本身的透明度，非背景）
						// alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
						WindowManager.LayoutParams params = getWindow()
								.getAttributes();
						params.alpha = 1.0f;
						getWindow().setAttributes(params);
						cityPick.dismiss();
					}
				}
			});

			cityPick.setLeftListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (cityPick != null) {
						cityPick.dismiss();
					}
				}
			});

			cityPick.setRightListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (cityPick != null) {
						addressBean = cityPick.getLocation();
						tv_location.setText(addressBean.getProvince() + "-"
								+ addressBean.getCity() + "-"
								+ addressBean.getArea());
						getLifeCircle();
						cityPick.dismiss();
					}
				}
			});
		}

	}

	/**
	 * TODO 根据关键字搜索生活圈
	 */
	private void queryLifeCircle() {
		search = edt_search.getText().toString().trim();
		if (TextUtils.isEmpty(search)) {
			return;
		}
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);
		reqContent.appendData("keyword", search);
		reqContent.appendData("province", addressBean.getProvince());
		reqContent.appendData("city", addressBean.getCity());

		reqContent.appendData("lng", addressBean.getLng());
		reqContent.appendData("lat", addressBean.getLat());

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_LIFE_CIRCLE_BY_KEYWORD.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(GET_LIFE_CIRCLE_BY_KEYWORD);
		HttpAsyncTask task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setResult(RESULT_OK, intent);
		finish();
		
	}
}
