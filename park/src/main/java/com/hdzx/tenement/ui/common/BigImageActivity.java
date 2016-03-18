package com.hdzx.tenement.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.nostra13.universalimageloader.core.ImageLoader;
public class BigImageActivity extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.big_image_activity);
		
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		Log.v("gl", "path=="+path);
		
		ImageView big = (ImageView) findViewById(R.id.img_big_photo);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("图片详情");
		
//		big.setImageURI(Uri.parse(path));
		ImageLoader.getInstance().displayImage(path, big, MyApplication.getInstance().getSimpleOptions());
		
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
