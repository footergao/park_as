package com.hdzx.tenement.community.ui;

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

import com.hdzx.tenement.R;
import com.hdzx.tenement.utils.ClearUtils;

/**
 */
public class ArticleDtlActivity extends Activity{

	WebView webview;
	private ImageView img_back;
	private TextView tv_close;
	private int zong = 0;
	private RelativeLayout layout_title;
	private TextView tv_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenement_main_shop);
		
		webview = (WebView) findViewById(R.id.webView);
		tv_close = (TextView) findViewById(R.id.tv_close);
		tv_title= (TextView) findViewById(R.id.tv_title);
		img_back = (ImageView) findViewById(R.id.img_back);
		layout_title = (RelativeLayout) findViewById(R.id.layout_title);
		layout_title.setVisibility(View.VISIBLE);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		
		Intent intent =getIntent();
		String url = intent.getStringExtra("url");
		String name = intent.getStringExtra("name");
		Log.v("gl", "url=="+url);
		
		webview.clearCache(true);
		webview.clearHistory();
		ClearUtils.ClearCookies(this);
		webview.loadUrl(url);
		
		tv_title.setText(name);

		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) { // Handle the error
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				zong++;
				iniView();
				Log.v("gl", "url=="+url);
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
				}else{
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
				ClearUtils.ClearCookies(ArticleDtlActivity.this);
				finish();
				
			}
		});
	}


	public void iniView() {
		// TODO Auto-generated method stub
		Log.v("gl", "zong==" + zong);
		if (zong > 0) {
			tv_close.setVisibility(View.VISIBLE);
		} else {
			tv_close.setVisibility(View.GONE);
		}
	}
}
