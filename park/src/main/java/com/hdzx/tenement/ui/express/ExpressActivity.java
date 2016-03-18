package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.hdzx.tenement.R;

/**
 * Created by anchendong on 15/7/23.
 */
public class ExpressActivity extends Activity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_back:
                finish();
                break;
            case R.id.lay_express_sendreciveinfo:
                Intent intentSendReceiveInfo=new Intent(this,SendReceiveInfoActivity.class);
                startActivity(intentSendReceiveInfo);
                break;
            case R.id.lay_express_waitreceive:
                Intent intentWaitReceive=new Intent(this,WaitReceiveActivity.class);
                startActivity(intentWaitReceive);
                break;
            case R.id.lay_express_phonemanage:
                Intent intentPhonemanage=new Intent(this,PhoneManageActivity.class);
                startActivity(intentPhonemanage);
                break;
            default:
                break;
        }
    }
}
