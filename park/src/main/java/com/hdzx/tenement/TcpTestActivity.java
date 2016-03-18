package com.hdzx.tenement;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.hdzx.tenement.tcp.TcpCallbackInterface;
import com.hdzx.tenement.tcp.TcpClient;
import com.hdzx.tenement.tcp.TcpThread;

import java.io.UnsupportedEncodingException;

/**
 * Created by anchendong on 15/6/30.
 */
public class TcpTestActivity extends Activity implements TcpCallbackInterface {

    private TextView sendMsg;

    private TextView recMsg;

    private Button sendButton;

    private Handler handler;

    private ReceiveBroadCast receiveBroadCast;  //广播实例

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tcp_test);
        sendMsg = (TextView) findViewById(R.id.tcp_sendmsg_txt);
        recMsg = (TextView) findViewById(R.id.tcp_recmsg_txt);
        sendButton = (Button) findViewById(R.id.tcp_sendmsg_button);

        //app启动，启动长连接线程
        TcpThread tcpThread = new TcpThread(getApplicationContext());
        new Thread(tcpThread).start();

        //注册广播
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("listentcp");    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(receiveBroadCast, filter);


        final TcpClient tcpClient = new TcpClient();
        tcpClient.setCallback(this);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tcpClient.sendMsg(sendMsg.getText().toString().getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x0001:
                        Bundle bundle = msg.getData();
                        recMsg.setText(new String(bundle.getByteArray("msg")));
                        break;
                }
                super.handleMessage(msg);
            }
        };

    }


    @Override
    public void tcpCallback(byte[] msgRec) {
        Message message = handler.obtainMessage();
        message.what = 0x0001;
        Bundle bundle = new Bundle();
        bundle.putByteArray("msg", msgRec);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //得到广播中得到的数据，并显示出来
//            Log.i("test-",new String(intent.getByteArrayExtra("recmsg")));
            recMsg.setText(new String(intent.getByteArrayExtra("recmsg")));
        }

    }

}
