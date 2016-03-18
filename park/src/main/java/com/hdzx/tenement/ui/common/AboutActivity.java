package com.hdzx.tenement.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.hdzx.tenement.R;

/**
 * User: hope chen
 * Date: 2015/12/24
 * Description: 关于界面
 */
public class AboutActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_about);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.txt_nav_title)).setText(R.string.main_mine_setting_about);
        String version = "";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.txt_appname)).setText(getString(R.string.app_name) + "  " + version);

        /* 绑定事件 */
        findViewById(R.id.lay_intro).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_nav_back:
                // 返回
                finish();
                break;
            case R.id.lay_intro:
                // 功能介绍
                Intent intent = new Intent(this, AboutIntroActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
