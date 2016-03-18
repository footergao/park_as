package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.hdzx.tenement.R;

public class MineOrderDetailActivity extends Activity {


    private TextView textViewTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_main_mine_order_tab_list_detail);
        textViewTitle = (TextView) findViewById(R.id.txt_main_mine_order_detail_title);


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_main_mine_order_detail_back:
                finish();
                break;
            default:
                break;
        }
    }
}
