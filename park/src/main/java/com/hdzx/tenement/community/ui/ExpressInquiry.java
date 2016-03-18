package com.hdzx.tenement.community.ui;

import com.hdzx.tenement.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class ExpressInquiry extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.express_inquiry_activity);
		
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("快递查询");
		
		WebView webview = (WebView) findViewById(R.id.webView);
		
		webview.setWebViewClient(new WebViewClient());
		
		webview.loadUrl("http://www.kuaidi100.com/");
		
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_iv:
			finish();
			break;
		default:
			break;
		}
	}

}
