package com.hdzx.tenement.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hdzx.tenement.CordovaTestActivity;
import com.hdzx.tenement.HttpTestActivity;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.deamon.AppDeamon;
import com.hdzx.tenement.mine.ui.MineLifeCricleActivity;
import com.hdzx.tenement.ui.MainActivity;
import com.hdzx.tenement.ui.common.DemandsActivity;
import com.hdzx.tenement.ui.common.LoginActivity;
import com.hdzx.tenement.utils.BaiduLocationClient;
import com.hdzx.tenement.utils.CommonUtil;
import com.hdzx.tenement.zxing.CaptureActivity;

/**
 * @author Jesley
 */
public class TestMain extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.test_main);

        init();
    }

    private void init() {
        CommonUtil.initUUID(this);
        CommonUtil.initAppVersion(this);
        AppDeamon.getInstance().start(this);
        BaiduLocationClient.getInstance().init(this);
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.button1:
                intent = new Intent(this, TcpTestMain.class);
                this.startActivity(intent);
                break;

            case R.id.button2:
                intent = new Intent(this, HttpTestActivity.class);
                this.startActivity(intent);
                break;

            case R.id.button4:
                intent = new Intent(this, CordovaTestActivity.class);
                this.startActivity(intent);
                break;
            case R.id.button5:
                intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                break;
            case R.id.button6:
                intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                break;
            case R.id.button7:
                intent = new Intent(this, BaiduLocationTest.class);
                this.startActivity(intent);
                break;
            case R.id.button8:
                intent = new Intent(this, MineLifeCricleActivity.class);
                this.startActivity(intent);
                break;
            case R.id.button9:
                intent = new Intent(this, CaptureActivity.class);
                this.startActivity(intent);
                break;
            case R.id.button3:
                break;
            case R.id.buttonMedia:
                intent = new Intent(this, DemandsActivity.class);
                intent.putExtra("title", "asd");
                this.startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
