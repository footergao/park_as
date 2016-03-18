package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.hdzx.tenement.R;
import com.hdzx.tenement.ui.common.RetrievepasswordActivity;

/**
 * 账户安全界面
 */
public class MineSettingSecurityActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_main_mine_setting_security);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_main_mine_setting_security_back:
                // 返回
                finish();
                break;
            case R.id.lay_modify_password:
                // 修改密码
                Intent intent = new Intent(this, RetrievepasswordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
