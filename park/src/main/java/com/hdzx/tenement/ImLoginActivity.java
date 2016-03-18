package com.hdzx.tenement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.hdzx.tenement.dimclient.ImClient;

public class ImLoginActivity extends Activity {

    private Handler handler;

    private TextView textViewUsername=null;

    private TextView textViewPassword=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.imlogin);
//
//        textViewUsername=(TextView)findViewById(R.id.imlogin_txt_username);
//        textViewPassword=(TextView)findViewById(R.id.imlogin_txt_password);

        handler=new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x0001:
                        Intent intent=new Intent(ImLoginActivity.this,ConversationActivity.class);
                        startActivity(intent);
                        break;
                }
                super.handleMessage(msg);
            }
        };

//        Button buttonLogin = (Button) findViewById(R.id.imlogin_button_login);
//        buttonLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                thread.start();
//            }
//        });


    }

    Thread thread = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    Message message=handler.obtainMessage();
                    message.what=0x0001;
                    handler.sendMessage(message);
                    ImClient.initIm();
                    ImClient.loginIm(textViewUsername.getText().toString(), textViewPassword.getText().toString());
                }
            }
    );



}

