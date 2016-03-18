package com.hdzx.tenement.ui.door;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.zxing.encode.QRCodeEncoderUtil;

/**
 * Created by anchendong on 15/7/28.
 */
public class DoorManageActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_door);

        initView();

    }

    private void initView() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        int width = displaySize.x;
        int height = displaySize.y;

        ImageView view = (ImageView) findViewById(R.id.img_qrcode);
        Bitmap bitmap = QRCodeEncoderUtil.create2DCoderBitmap("http://www.hdzx.com", width, width);
        view.setImageBitmap(bitmap);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_door_back:
                finish();
                break;
            case R.id.lay_door_electronicaccesscontrol:
                Intent intentElectronicAccessControl=new Intent(this,ElectronicAccessControlActivity.class);
                startActivity(intentElectronicAccessControl);
            default:
                break;
        }
    }
}
