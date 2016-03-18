package com.hdzx.tenement;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.hdzx.tenement.dimclient.ImClient;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by anchendong on 15/6/18.
 */
public class ConversationActivity extends Activity {

    private Handler handler;

    private TextView textViewRevMsg = null;

    private TextView textViewSendMsg = null;

    private Chat chat = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.conversation);
//
//        textViewRevMsg = (TextView) findViewById(R.id.conversation_txt_revmsg);
//        textViewSendMsg = (TextView) findViewById(R.id.conversation_txt_sendmsg);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (ImClient.getConnection() == null) {
            Log.i("ImClient-", "null------");
            return;

        } else {
            Log.i("ImClient-", "==========");
            chat = ImClient.creatImChat("admin");

        }
        thread.start();
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0x0001:
                        Log.i("ImClient-",msg.getData().getString("msg"));
                        textViewRevMsg.setText(msg.getData().getString("msg"));
                        break;
                }
                super.handleMessage(msg);
            }
        };

//        Button buttonSend = (Button) findViewById(R.id.conversation_button_send);
//        buttonSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                ImClient.sendImMessageString(chat, textViewSendMsg.getText().toString());
//                textViewSendMsg.setText("");
//            }
//        });
    }

    Thread thread = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    chat.addMessageListener(
                            new ChatMessageListener() {
                                @Override
                                public void processMessage(Chat chat, Message message) {
                                    android.os.Message handlemessage = handler.obtainMessage();
                                    Bundle bundle = new Bundle();

                                    Log.i("ImClient-", "Received from 【"
                                            + message.getFrom() + "】 message: "
                                            + message.getBody());
                                    bundle.putString("msg", "Received from 【"
                                            + message.getFrom() + "】 message: "
                                            + message.getBody());
                                    handlemessage.what = 0x0001;
                                    handlemessage.setData(bundle);
                                    handler.sendMessage(handlemessage);
                                }
                            });
                }
            }
    );

}
