package com.hdzx.tenement.ui.common;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.hdzx.tenement.R;

/**
 * User: hope chen
 * Date: 2015/12/25
 * Description: 关于-功能介绍界面
 */
public class AboutIntroActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_about_intro);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.txt_nav_title)).setText(R.string.main_mine_setting_about_intro);
        ((TextView)findViewById(R.id.txt_intro)).setText(Html.fromHtml(getString(R.string.main_mine_setting_about_intro_detail)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_nav_back:
                // 返回
                finish();
                break;
            default:
                break;
        }
    }
}
