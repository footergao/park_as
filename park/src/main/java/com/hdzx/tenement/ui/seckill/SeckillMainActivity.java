package com.hdzx.tenement.ui.seckill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.hdzx.tenement.R;

/**
 * User: hope chen
 * Date: 2015/8/4
 * Description: 嗨抢主界面
 */
public class SeckillMainActivity extends Activity implements View.OnClickListener{

    private ListView seckillListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_seckill);
        ((TextView) findViewById(R.id.txt_nav_title)).setText(R.string.txt_seckill);

        seckillListView = (ListView) findViewById(R.id.seckill_lsit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_nav_back:
                super.onBackPressed();
                break;
            case R.id.btn_seckill_more:
                Intent intent = new Intent(SeckillMainActivity.this, SeckillServiceActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
