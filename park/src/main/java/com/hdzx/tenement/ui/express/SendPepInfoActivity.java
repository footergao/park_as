package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.hdzx.tenement.R;

/**
 * Created by anchendong on 15/7/27.
 */
public class SendPepInfoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_sendpepinfo);

    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_sendpepinfo_back:
                finish();
                break;
            case R.id.btn_express_sendpep_next:
                //寄件申请-寄件人信息-下一步
                Intent intentSendInfo=new Intent(this,ReceivePepInfoActivity.class);
                startActivity(intentSendInfo);
                break;
            default:
                break;
        }
    }
}
