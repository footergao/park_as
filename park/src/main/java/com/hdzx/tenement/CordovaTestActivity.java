package com.hdzx.tenement;

import android.content.Intent;
import android.os.Bundle;
import org.apache.cordova.CordovaActivity;

public class CordovaTestActivity extends CordovaActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        if (intent != null) {
            launchUrl = intent.getStringExtra("url");
            activityTitle = intent.getStringExtra("title");
            isDisplayTopBar = intent.getBooleanExtra("display_top_bar", false);  // false: 隐藏头  true: 显示头
        }
        super.init();
        loadUrl("file:///android_asset/www/cordovaplugintest.html");
    }

    private void analysis(String message) {
        Intent intent = new Intent();
        intent.setAction("bc.test102");
        intent.putExtra("name", "动态的");
        sendBroadcast(intent);
    }
}
