package com.hdzx.tenement.message;

import java.util.List;
import java.util.logging.Logger;

import org.apache.cordova.LOG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.community.ui.CommunityRepairDtlActivity;
import com.hdzx.tenement.community.ui.CommunitySuggestActivity;
import com.hdzx.tenement.community.ui.CommunitySuggestDtlActivity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.ui.MinePostsDtlActivity;
import com.hdzx.tenement.ui.PluginActivity;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Task;
import com.hdzx.tenement.vo.Components;
import com.hdzx.tenement.vo.MessageBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by anchendong on 15/7/23.
 */
public class MessageCircleDetailActivity extends Activity implements
		IContentReportor, OnClickListener {

	// private String TAG =
	// MessageCircleDetailActivity.class.getCanonicalName();

	private MessageBean messageBean;
	private TextView textViewTitle;
	private TextView tv_title;
	private TextView textViewTime;
	private TextView textViewInfo;
	// private TextView textViewAuthor;
	private ImageView iv_image;
	private Button btn_go;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tenement_messagecircle_info_detail);
		// textViewAuthor = (TextView)
		// findViewById(R.id.txt_messagecircle_info_detail_author);
		textViewTitle = (TextView) findViewById(R.id.txt_messagecircle_info_detail_title);
		tv_title = (TextView) findViewById(R.id.tv_title);
		textViewTime = (TextView) findViewById(R.id.txt_messagecircle_info_detail_time);
		textViewInfo = (TextView) findViewById(R.id.txt_messagecircle_info_detail_context);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		btn_go = (Button) findViewById(R.id.btn_go);
		initView();

	}

	public void initView() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		tv_title.setText(bundle.getString("categoryName"));
		messageBean = (MessageBean) bundle.getSerializable("messageBean");
		textViewTitle.setText(messageBean.getTitle());
		textViewTime.setText(messageBean.getCreateDate());
		textViewInfo.setText(messageBean.getContent());
		List<Components> list = messageBean.getComponents();
		if (null != list && list.size() > 0) {
			for (Components components : list) {
				if ("001".equals(components.getComponentType())) {// 图片

					iv_image.setVisibility(View.VISIBLE);
					DisplayImageOptions options = ((MyApplication) getApplicationContext())
							.getSimpleOptions();
					String photo = components.getComponentContent();
					if (!TextUtils.isEmpty(photo)) {
						com.hdzx.tenement.utils.Logger.v("gl",UserSession.getInstance().getImageHost()
								+ photo);
						ImageLoader.getInstance().displayImage(
								UserSession.getInstance().getImageHost()
										+ photo, iv_image, options);
					}
					
					
					Task.setReadMessage(this, this, messageBean.getMessageId() + "", "");
					
				} else if ("002".equals(components.getComponentType())) {// 按钮
					btn_go.setVisibility(View.VISIBLE);
					Log.v("gl", "components.getComponentContent()=="+components.getComponentContent());
					btn_go.setText(components.getComponentContent());
					btn_go.setOnClickListener(this);
				}
			}
		}
		
	}

	private void ruleOfButton() {
		String type = messageBean.getInteractionType();
		Intent intent = new Intent();
		
		Log.v("gl","messageBean.getInteractionType()=="+messageBean.getInteractionType());
		if (MessageBean.APP_UPDATE.equals(type)) {
			// TODO 跳转到更新页

		}else if (MessageBean.BBS_NEW_MESSAGE.equals(type)) {
			// 社区交流
			startToActivity(MinePostsDtlActivity.class,"threadId",messageBean.getInteraction());
			return;
		}else if (MessageBean.ACTIVE_NOTICE.equals(type)) {
			// 活动汇
			return;
		}else if (MessageBean.BRAND_NOTICE.equals(type)) {
			// 品牌街
			return;
		}else if (MessageBean.APPLY_DDX_SUCCESS.equals(type)) {
			// 成为咚咚侠
			return;
		}else if (MessageBean.ESTATE_ADVICE_TOXBB.equals(type)) {
			startToActivity(CommunitySuggestDtlActivity.class,"adviceId",messageBean.getInteraction());
		}else if (MessageBean.ESTATE_REPAIR_TOXBB.equals(type)) {
			startToActivity(CommunityRepairDtlActivity.class,"repairId",messageBean.getInteraction());
		}else if (MessageBean.TAKE_EXPRESS_NOTICE.equals(type)) {
			// 到件通知-快递收发
			String service = "快递收发";
			intent = new Intent(this, PluginActivity.class);
			intent.putExtra(PluginActivity.PARAM_URL,
					Contants.getHost()
							+ "/help/app/server/index.html?serversName="
							+ service);
			intent.putExtra(PluginActivity.PARAM_TITLE,
					service);
			intent.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
			startActivity(intent);
		}else if (MessageBean.EVALU_ORDER.equals(type)) {
			// 评价
			Intent intent1 = new Intent(this, PluginActivity.class);
			intent1.putExtra(PluginActivity.PARAM_URL, Contants.getHost()
					+ "/help/app/server/orderlist.html");
			intent1.putExtra(PluginActivity.PARAM_TITLE,
					getString(R.string.main_mine_home_my_order_other));
			intent1.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
			startActivity(intent1);
		}else if (MessageBean.GRAB_ORDER.equals(type)) {
			//抢单
			Intent intent1 = new Intent(this, PluginActivity.class);
			intent1.putExtra(PluginActivity.PARAM_URL, Contants.getHost()
					+ "/help/app/server/orderlist.html");
			intent1.putExtra(PluginActivity.PARAM_TITLE,
					getString(R.string.main_mine_home_my_order_other));
			intent1.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
			startActivity(intent1);
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_go:
			Task.setReadMessage(this, this, messageBean.getMessageId() + "", "");
			ruleOfButton();
			break;
		case R.id.back_iv:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {

		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			if (this != null) {
				Toast.makeText(this, "返回为空", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		if (!"000000".equals(rtnCode)) {
			if (this != null) {
				String rtnMsg = (String) responseContent
						.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
				Toast.makeText(this, rtnMsg, Toast.LENGTH_SHORT).show();
			}
			return;
		}
		String jsonStr = responseContent.getDataJson();
		boolean ischeck = Boolean.parseBoolean(jsonStr);
		
		Log.v("gl", "ischeck=="+ischeck);
		if(ischeck)
			ruleOfButton();
	}

	private void startToActivity(Class className, String key, String value) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(key, value);
		intent.setClass(MessageCircleDetailActivity.this, className);
		startActivity(intent);

	}

}
