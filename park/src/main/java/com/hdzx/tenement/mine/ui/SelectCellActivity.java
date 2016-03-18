package com.hdzx.tenement.mine.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.hdzx.tenement.mine.adaper.SelectCellSimpleAdapter;
import com.hdzx.tenement.mine.vo.LifeCircleAddressVo;
import com.hdzx.tenement.ui.MainActivity;
import com.hdzx.tenement.ui.common.MyLifeCircleActivity;
import com.hdzx.tenement.utils.BeansUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.vo.CellVo;
/**
 * 选择小区
 * 
 * @author Administrator
 *
 */
public class SelectCellActivity extends Activity implements IContentReportor {

	private String TAG = "SelectCellActivity";

	private TextView tv_title;
	private LifeCircleAddressVo addressVo = new LifeCircleAddressVo();
	private List<CellVo> cellVoList = new ArrayList<CellVo>();

	private ListView listView;
	private SelectCellSimpleAdapter adapter;
	private LinearLayout rl_location;
	private LinearLayout ll_search;

	private String UPDATE_LIFE_CIRCLE = "updateLifecircle";// 新增当前生活圈

	private String UPDATE_NOW_LIFE_CIRCLE = "updateNowLifecircle";// 更新当前生活圈

	private String lifecircleId = "";// 生活圈
	private String regionalId = "";// 小区

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenement_main_mine_my_life_circle);
		initView();
	}

	private void initView() {

		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.txt_select_cell);
		listView = (ListView) findViewById(R.id.lv_my_life_circle);
		rl_location = (LinearLayout) findViewById(R.id.rl_location);
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
		rl_location.setVisibility(View.GONE);
		ll_search.setVisibility(View.GONE);
		if (getIntent() != null) {
			addressVo = (LifeCircleAddressVo) getIntent().getSerializableExtra(
					"addressVo");
		}
		if (addressVo != null) {
			cellVoList = addressVo.getRegionalInfos();
			lifecircleId = addressVo.getLifecircleId() + "";
			adapter = new SelectCellSimpleAdapter(cellVoList, this);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					new AlertDialog.Builder(SelectCellActivity.this)
							.setTitle("提示")
							.setMessage(
									"是否选择把："
											+ addressVo.getLifecircleName()
											+ "-"
											+ cellVoList.get(position)
													.getRegionalName()
											+ "设置为您的常用生活圈？")
							.setNegativeButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											regionalId = cellVoList.get(
													position).getRegionalId()
													+ "";
											applyLifeCircle(regionalId);
										}
									})
							.setPositiveButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									}).show();

				}
			});
		}
	}

	protected void applyLifeCircle(String regionalId) {

		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);
		reqContent.appendData("operType", "000");// 新增
		reqContent.appendData("lifecircleId", lifecircleId);
		reqContent.appendData("regionalId", regionalId);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.UPDATE_USER_LIFE_CIRCLE.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(UPDATE_LIFE_CIRCLE);
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
			if (UPDATE_LIFE_CIRCLE.equals(responseContent.getResponseCode())) {
				// 新增生活圈-用户生活圈记录编号
				String jsonStr = responseContent.getDataJson().trim();
				Log.v(TAG, "update life circle jsonStr:" + jsonStr);

				updateNowLifeCircle(lifecircleId, regionalId);

				Log.v(TAG, "getLifecircleId:"
						+ UserSession.getInstance().getUserBasic()
								.getLifecircleName());

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
				startActivity(intent);

				Log.v(TAG,
						"update getLifecircleId:"
								+ userBasicInfo.getLifecircleName());
			}
		}
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
}
