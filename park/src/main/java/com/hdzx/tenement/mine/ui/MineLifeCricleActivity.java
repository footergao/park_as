package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.adaper.MineLifeCircleListViewAdapter;
import com.hdzx.tenement.mine.vo.LifeCircleAddressVo;
import com.hdzx.tenement.swipemenulistview.SwipeMenu;
import com.hdzx.tenement.swipemenulistview.SwipeMenuCreator;
import com.hdzx.tenement.swipemenulistview.SwipeMenuItem;
import com.hdzx.tenement.swipemenulistview.SwipeMenuListView;
import com.hdzx.tenement.ui.MainActivity;
import com.hdzx.tenement.utils.BeansUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MineLifeCricleActivity extends Activity implements
		IContentReportor, OnClickListener {

	private final String TAG = "MyLifeCircleActivity";

	private String GET_MY_LIFE_CIRCLE = "getMyLifecircle";// 获取我的生活圈
	private String UPDATE_NOW_LIFE_CIRCLE = "updateNowLifecircle";// 更新当前生活圈
	private String UPDATE_LIFE_CIRCLE = "updateLifecircle";// 删除当前生活圈

	private ProgressDialog progressDialog = null;

	/**
	 * 获得我的生活圈
	 */
	private List<LifeCircleAddressVo> myLifeCircleList = new ArrayList<LifeCircleAddressVo>();
	
	private int postion=-1;


	private LinearLayout lay_add_tv = null;

	private ImageView backIV = null;

	private SwipeMenuListView swipeMenuListView;
	private ScrollView scrollView;
	private MineLifeCircleListViewAdapter addressListAdapter = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenement_main_mine_circle);

		initView();

		initAddressListView();
		addAction();

	}

	private void initView() {

		Log.v("gl", "lifecircleId===="
				+ UserSession.getInstance().getUserBasic().getLifecircleId()
				+ "");

		// lay_title = (RelativeLayout) findViewById(R.id.lay_title);
		// tv_title = (TextView) findViewById(R.id.tv_title);
		// tv_title.setText(R.string.txt_my_life_circle);
		// lay_title.setVisibility(View.GONE);

		lay_add_tv = (LinearLayout) this.findViewById(R.id.lay_add_tv);
		scrollView = (ScrollView) findViewById(R.id.scrollview);
		swipeMenuListView = (SwipeMenuListView) findViewById(R.id.address_lv);

		swipeMenuListView.setSwipeScrollView(scrollView);

		backIV = (ImageView) this.findViewById(R.id.back_imageView);
		backIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}

		});
		
		
		lay_add_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent addLifeCircle = new Intent(MineLifeCricleActivity.this, AddLifeCricleActivity.class);
				startActivity(addLifeCircle);
			}
		});

		getMyLifeCircle();
	}

	private void initAddressListView() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(DensityUtil.dip2px(getApplicationContext(),
						90));
				// set item title
				deleteItem.setTitle("删除");
				// set item title fontsize
				deleteItem.setTitleSize(18);
				// set item title font color
				deleteItem.setTitleColor(Color.WHITE);
				// set a icon
				// deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		swipeMenuListView.setMenuCreator(creator);

		// step 2. listener item click event
		swipeMenuListView
				.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						switch (index) {
						case 0:
							
							initLifeCircle(position);
							
							
							break;
						case 1:
							break;
						default:
							break;
						}
						return false;
					}
				});
	}
	
	protected void initLifeCircle(final int pos) {
			new AlertDialog.Builder(this)
					.setMessage("确定删除生活圈？")
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									if(myLifeCircleList.get(pos).getLifecircleId()==UserSession.getInstance().getUserBasic().getLifecircleId())
										Toast.makeText(MineLifeCricleActivity.this, "当前生活圈不能删除", Toast.LENGTH_LONG).show();
									else{
									    updateLifeCircle(myLifeCircleList.get(pos).getAluId()+"",myLifeCircleList.get(pos).getLifecircleId()+"");
									    postion = pos;
									}
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

	/**
	 * 事件定义
	 */
	public void addAction() {
		/* listview item选中事件 */
		swipeMenuListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						addressListAdapter.setSelectedPosition(position);
//						addressListAdapter.notifyDataSetChanged();

						LifeCircleAddressVo item = (LifeCircleAddressVo) parent
								.getItemAtPosition(position);
						updateNowLifeCircle(item.getLifecircleId() + "",
								item.getRegionalId() + "");
					}
				});
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

	
	protected void updateLifeCircle(String aluId,String lifecircleId) {

		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);
		reqContent.appendData("operType", "002");// 删除
		reqContent.appendData("aluId", aluId);
		reqContent.appendData("lifecircleId", lifecircleId);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.UPDATE_USER_LIFE_CIRCLE.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(UPDATE_LIFE_CIRCLE);
		HttpAsyncTask task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
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
			if (GET_MY_LIFE_CIRCLE.equals(responseContent.getResponseCode())) {
				// 获取我的生活圈
				String jsonStr = responseContent.getDataJson().trim();
				Log.v(TAG, "query my lifecircle jsonStr:" + jsonStr);
				Gson gson = new Gson();
				myLifeCircleList.clear();
				myLifeCircleList = gson.fromJson(jsonStr,
						new TypeToken<List<LifeCircleAddressVo>>() {
						}.getType());
				
				String userLifecircleId = UserSession.getInstance().getUserBasic()
						.getLifecircleId()+"";

				addressListAdapter = new MineLifeCircleListViewAdapter(this,
						myLifeCircleList,userLifecircleId+"");
				swipeMenuListView.setAdapter(addressListAdapter);
				addressListAdapter.notifyDataSetChanged();

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
			}else if (UPDATE_LIFE_CIRCLE.equals(responseContent.getResponseCode())) {
				// 删除生活圈-用户生活圈记录编号
				String jsonStr = responseContent.getDataJson().trim();
				Log.v(TAG, "update life circle jsonStr:" + jsonStr);

				Log.v(TAG, "getLifecircleId:"
						+ UserSession.getInstance().getUserBasic()
								.getLifecircleName());
				
				 //删除
				myLifeCircleList.remove(postion);
				addressListAdapter.notifyDataSetChanged();

			} 

		}
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_iv:
			finish();
			break;
		default:
			break;
		}
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
