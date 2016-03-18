package com.hdzx.tenement.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.zxing.encode.QRCodeEncoderUtil;

/**
 * Created by anchendong on 15/7/29.
 */
public class QrDialog extends Dialog {

    private int width;

    public QrDialog(Context context, int width) {
        super(context);
        this.width = width;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_common_qrdialog);
        //设置标题
        //setTitle("门禁二维码");
        //显示二维码
        ImageView view = (ImageView) findViewById(R.id.img_common_qrdialog);
        Bitmap bitmap = QRCodeEncoderUtil.create2DCoderBitmap("http://www.hdzx.com", width, width);
        view.setImageBitmap(bitmap);
        //关闭按钮
        Button button = (Button) findViewById(R.id.btn_dialog_close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QrDialog.this.dismiss();
            }
        });
    }
}
