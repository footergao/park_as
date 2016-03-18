package com.hdzx.tenement.ui.seckill;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.hdzx.tenement.R;

/**
 * User: hope chen
 * Date: 2015/8/5
 * Description: 选择/申请服务资格界面
 */
public class SeckillServiceActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_seckill_service);
        ((TextView) findViewById(R.id.txt_nav_title)).setText(R.string.txt_seckill);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_nav_back:
                super.onBackPressed();
                break;
            default:
                break;
        }
    }
}
