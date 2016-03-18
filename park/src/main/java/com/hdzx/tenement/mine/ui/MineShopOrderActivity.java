package com.hdzx.tenement.mine.ui;

import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.utils.ClearUtils;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.PreferencesUtils;
import com.hdzx.tenement.utils.WebViewUtil;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;

/**
 */
public class MineShopOrderActivity extends Activity implements IContentReportor {

	private HttpAsyncTask httpAsyncTask;
	private String TAG_TGT_AUTO = "tgt";
	private String TAG_ST_AUTO = "st";
	String poster_url = "/m/authc/buyer/orderList?orderState=99";
	String path = "/shiro-cas";
	WebView webview;
	private ImageView img_back;
	private TextView tv_close;
	private Activity activity;
	private int zong = 0;
	private RelativeLayout layout_title;
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenement_main_shop);
		activity = this;

		webview = (WebView) findViewById(R.id.webView);
		tv_close = (TextView) findViewById(R.id.tv_close);
		tv_title = (TextView) findViewById(R.id.tv_title);
		img_back = (ImageView) findViewById(R.id.img_back);
		layout_title = (RelativeLayout) findViewById(R.id.layout_title);
		layout_title.setVisibility(View.VISIBLE);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);

		tv_title.setText("我的购物订单");

		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) { // Handle the error
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				zong++;
				iniView();
				Log.v("gl", "url==" + url);
				return true;
			}
		});

		// 点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
		webview.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) { // 表示按返回键的操作
						webview.goBack(); // 后退
						zong--;
						iniView();
						return true; // 已处理
					}
				}
				return false;
			}
		});

		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (webview.canGoBack()) { // 表示按返回键的操作
					webview.goBack(); // 后退
					zong--;
					iniView();
				} else {
					finish();
				}
			}
		});

		tv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				webview.clearCache(true);
				webview.clearHistory();
				ClearUtils.ClearCookies(activity);
				finish();

			}
		});

		getST(UserSession.getInstance().getAccessTicket());

	}

	public void iniView() {
		// TODO Auto-generated method stub
		Log.v("gl", "zong==" + zong);
		if (zong > 1) {
			tv_close.setVisibility(View.VISIBLE);
		} else {
			tv_close.setVisibility(View.GONE);
		}
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
		} else if (!"000000".equals(rtnCode)) {
			String rtnMsg = (String) responseContent
					.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
			Toast.makeText(activity, rtnMsg, Toast.LENGTH_SHORT).show();
		} else {

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
			} else if (responseContent.getResponseCode().equals(TAG_ST_AUTO)) {

				Object data = responseContent.getData();
				if (data != null && !"".equals(data) && data instanceof String) {
					String st = (String) data;

					String service = UserSession.getInstance().getFrontUrl()
							+ path + "?" + "ticket=" + st + "&rtnurl=";
					
					
					
					String rtnurl = UserSession.getInstance().getFrontUrl() + poster_url;
					rtnurl = WebViewUtil.formateString(rtnurl);
					rtnurl = URLEncoder.encode(rtnurl);
					
					service = service + rtnurl;
					
					webview.clearCache(true);
					webview.clearHistory();
					ClearUtils.ClearCookies(activity);
					
					webview.loadUrl(service);

					Log.v("gl", "url==" + service);

				} else {
					getTGT();
				}
			}
		}
	}
}
