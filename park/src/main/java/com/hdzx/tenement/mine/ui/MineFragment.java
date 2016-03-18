package com.hdzx.tenement.mine.ui;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.ui.PluginActivity;
import com.hdzx.tenement.ui.common.LoginActivity;
import com.hdzx.tenement.utils.BeansUtil;
import com.hdzx.tenement.utils.CommonUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.PreferencesUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MineFragment extends Fragment implements IContentReportor {

	private int REQUEST_CODE = 10000;
	private TextView tv_nicker_name, tv_mobile;
	private ImageView header_imageView;
	public ImageView img_new_post;
	/**
	 * 获取用户基本信息,封装UserBasic
	 */
	private static final String GET_BASICINFO = "getbasicinfo";
	private static final String GET_AGAIN_BASICINFO = "getagainbasicinfo";
	private Activity activity;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tenement_main_mine, container,
				false);
		initView(view);

		initData();

		return view;
	}

	private void initView(View view) {
		View header = view.findViewById(R.id.header_layout);

		tv_nicker_name = (TextView) view.findViewById(R.id.tv_niker_name);
		tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);
		header_imageView = (ImageView) view.findViewById(R.id.header_imageView);
		img_new_post = (ImageView) view.findViewById(R.id.img_new_post);
		showPostFlag();
		
		
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				makePersonInforRequest(GET_BASICINFO);
			}
		});

		View tableRowSetting = view.findViewById(R.id.tableRow8);
		tableRowSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						MineSettingActivity.class);
				startActivity(intent);
			}
		});

		/* 我的地址簿 */
		View mineAddressLayout = view.findViewById(R.id.mine_address_layout);
		mineAddressLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						MineAddressActivity.class);
				intent.putExtra("isEdit", true);
				startActivity(intent);
			}
		});

		// 我的生活圈
		View tableRowMyLifeCircle = view.findViewById(R.id.tableRow2);
		tableRowMyLifeCircle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						MineLifeCricleActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});

		// 我的车辆
		View tableRowCar = view.findViewById(R.id.tableRow5);
		tableRowCar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						MineCarActivity.class);
				startActivity(intent);
			}
		});

		// 我的排行
		View tableRowRankingList = view.findViewById(R.id.tableRow6);
		tableRowRankingList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						MineRankingListActivity.class);
				startActivity(intent);
			}
		});

		// 我的订单
		View tableRowOrder = view.findViewById(R.id.tableRow1);
		tableRowOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(v.getContext(),
				// MineOrderActivity.class);
				Intent intent = new Intent(v.getContext(), PluginActivity.class);
				intent.putExtra(PluginActivity.PARAM_URL, Contants.getHost()
						+ "/help/app/server/orderlist.html");
				intent.putExtra(PluginActivity.PARAM_TITLE,
						getString(R.string.main_mine_home_my_order_other));
				intent.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
				startActivity(intent);
			}
		});

		// 我的购物订单
		View tableRowShopOrder = view.findViewById(R.id.tableRow1_1);
		tableRowShopOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(v.getContext(),
				// MineOrderActivity.class);
				Intent intent = new Intent(v.getContext(),
						MineShopOrderActivity.class);
				intent.putExtra(PluginActivity.PARAM_URL, "/m/authc/buyer/orderList?orderState=99");
				startActivity(intent);
			}
		});

		// 我的帖子
		View tableRowPostsList = view.findViewById(R.id.tableRow4);
		tableRowPostsList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						MinePostsActivity.class);
				startActivity(intent);
				//
				// Toast.makeText(activity, "敬请期待！", Toast.LENGTH_SHORT)
				// .show();
			}
		});

		// 注销
		View tabRowLogout = view.findViewById(R.id.tr_logout);
		tabRowLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 清除登录缓存
				PreferencesUtils.getInstance().remove(
						MineFragment.this.activity.getBaseContext(),
						Contants.PREFERENCES_KEY.psw.name());
				Intent intent = new Intent(v.getContext(), LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				activity.finish();
			}
		});
	}

	private void initData() {

		String nicker_name = "";
		String mobile = "";
		String url = "";
		if (UserSession.getInstance().getUserBasic() != null) {
			nicker_name = UserSession.getInstance().getUserBasic()
					.getNickerName();
			mobile = UserSession.getInstance().getUserBasic().getMobilePhone();
			url = UserSession.getInstance().getUserBasic().getHeadphoto();

			if (url != null && !url.trim().equals(""))
				ImageLoader.getInstance().displayImage(
						UserSession.getInstance().getImageHost() + url,
						header_imageView, MyApplication.getInstance().getSimpleOptions());
			else
				header_imageView.setImageResource(R.drawable.default_header);
		}

		if (nicker_name != null && !nicker_name.trim().equals("")) {

			tv_nicker_name.setText(nicker_name);
			tv_mobile.setText("("
					+ CommonUtil.hiden4CharBefor4(UserSession.getInstance()
							.getUserBasic().getMobilePhone(), "*") + ")");
		} else {
			if (mobile != null && !mobile.trim().equals("")) {

				tv_nicker_name.setText(CommonUtil.hiden4CharBefor4(UserSession
						.getInstance().getUserBasic().getMobilePhone(), "*"));

				tv_mobile.setText("("
						+ CommonUtil.hiden4CharBefor4(UserSession.getInstance()
								.getUserBasic().getMobilePhone(), "*") + ")");
			}
		}
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
		Log.v("gl", "path==" + httpAsyncTask.getEntity().getPath());

	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {

		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			if (this.activity != null) {
				Toast.makeText(this.activity, "网络异常，请稍后尝试", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (!Contants.ResponseCode.CODE_000000.equals(rtnCode)) {
			if (this.activity != null) {
				String rtnMsg = (String) responseContent
						.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
				Toast.makeText(this.activity, rtnMsg, Toast.LENGTH_SHORT)
						.show();
			}
		} else {

			Log.v("gl", "personInfo:" + responseContent.getData());
			// 获取用户主表信息
			UserBasic userBasicInfo = (UserBasic) BeansUtil.map2Bean(
					(Map<String, String>) responseContent.getData(),
					UserBasic.class);
			UserSession.getInstance().setUserBasic(userBasicInfo);

			if (responseContent.getResponseCode().equals(GET_BASICINFO)) {

				Intent i = new Intent(activity, MinePersonInfoActivity.class);
				i.putExtra("UserBasic", userBasicInfo);
				startActivity(i);

			} else if (responseContent.getResponseCode().equals(
					GET_AGAIN_BASICINFO)) {

				initData();

			}
		}
	}

	/**
	 * 设置常用的设置项
	 *
	 * @return
	 */
	public DisplayImageOptions getSimpleOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_header)
				.showImageForEmptyUri(R.drawable.default_header)
				.showImageOnFail(R.drawable.default_header).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;

	}
	
	
	
	
	
	

	
	private void showPostFlag() {
		// TODO Auto-generated method stub
		Log.v("gl", "Contants.isPostMsg==="+Contants.isPostMsg);
		if(Contants.isPostMsg)
			img_new_post.setVisibility(View.VISIBLE);
		else
			img_new_post.setVisibility(View.GONE);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		makePersonInforRequest(GET_AGAIN_BASICINFO);
		showPostFlag();
	}

}
