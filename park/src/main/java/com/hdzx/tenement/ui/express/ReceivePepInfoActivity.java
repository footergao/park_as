package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.hdzx.tenement.R;

/**
 * Created by anchendong on 15/7/27.
 */
public class ReceivePepInfoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_receivepepinfo);

    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_receivepepinfo_back:
                finish();
                break;
            case R.id.btn_express_receivepep_next:
                //寄件申请-收件人信息-下一步
                Intent intentSure=new Intent(this,SendReceiveSalesActivity.class);
                startActivity(intentSure);
            default:
                break;
        }
    }
}
