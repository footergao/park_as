package com.hdzx.tenement.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.hdzx.tenement.R;

/**
 * Created by anchendong on 15/7/30.
 * 快件收发-通知手机管理-为什么添加对话框
 */
public class NotePhoneDialog extends Dialog {

    /**
     * 提示信息
     */
    private String info;

    public NotePhoneDialog(Context context, String info) {
        super(context);
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_express_phonemanage_whyadd_dialog);
        getWindow().setBackgroundDrawable(new BitmapDrawable());

        TextView textView = (TextView) findViewById(R.id.txt_express_phonemanage_whyadd_info);
        textView.setText(info);

        //关闭按钮
        Button button = (Button) findViewById(R.id.btn_express_phonemanage_dialog_close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotePhoneDialog.this.dismiss();
            }
        });
    }

}
