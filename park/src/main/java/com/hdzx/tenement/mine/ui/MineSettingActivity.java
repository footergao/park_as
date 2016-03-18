package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.hdzx.tenement.R;
import com.hdzx.tenement.ui.common.AboutActivity;

public class MineSettingActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_main_mine_setting);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_main_mine_setting_back:
                finish();
                break;
            case R.id.lay_main_mine_security:
                // 账户安全
                Intent intent = new Intent(this, MineSettingSecurityActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_about:
                // 关于
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
