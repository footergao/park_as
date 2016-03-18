package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.hdzx.tenement.R;

/**
 * Created by anchendong on 15/7/27.
 */
public class SendReceiveInfoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_sendreceiveinfo);


    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_sendreciveinfo_back:
                finish();
                break;
            case R.id.btn_express_sendreceiveinfo_next:
                //寄件申请-上门取件-下一步
                Intent intentSendPep=new Intent(this,SendPepInfoActivity.class);
                startActivity(intentSendPep);
                break;
            case R.id.lay_express_sendreciveinfo_address:
                //寄件申请-上门取件-地址选择
                Intent intentSelectAdress=new Intent(this,SendReceiveInfoAddressActivity.class);
                startActivity(intentSelectAdress);
                break;
            default:
                break;
        }
    }
}
