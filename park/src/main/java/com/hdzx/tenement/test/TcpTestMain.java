package com.hdzx.tenement.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.tcp.SocketClient;
import com.hdzx.tenement.tcp.TcpContants;
import com.hdzx.tenement.tcp.protocol.TcpCallback;
import com.hdzx.tenement.tcp.protocol.TcpReceiveTamplate;
import com.hdzx.tenement.tcp.protocol.TcpSendEntity;
import com.hdzx.tenement.tcp.protocol.TcpSendTemplate;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.PreferencesUtils;

/**
 * 
 * @author Jesley
 *
 */
public class TcpTestMain extends Activity implements TcpCallback
{
    private EditText addressEditView = null;
    
    private EditText portEditView = null;
    
    private TextView displayTextView = null;
    
    private BroadcastReceiver receiver = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.tcp_test_main);
        addressEditView = (EditText) this.findViewById(R.id.address_editText);
        portEditView = (EditText) this.findViewById(R.id.port_editView);
        displayTextView = (TextView) this.findViewById(R.id.display_textView);
        
        receiver = new BroadcastReceiver()
        {

            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (intent != null)
                {
                    TcpReceiveTamplate template = (TcpReceiveTamplate) intent.getSerializableExtra(TcpContants.DATA_KEY_RESPONSE_TEMPLATE);
                    String text = "receive broadcast::" + template.getText() + "(error:" + template.getErrorMsg() + ")";
                    printlnText(text);
                    
                    if (TcpContants.ResponseCode.CODE_0x00D0.equals(template.getCode()))
                    {
                        UserSession.getInstance().setValidAesKey(false);
                    }
                    else if (TcpContants.ResponseCode.CODE_0x00D1.equals(template.getCode()))
                    {
                        UserSession.getInstance().setAccessTicket(null);
                    }
                    else if (TcpContants.ResponseCode.CODE_0x00AO.equals(template.getCode()))
                    {
                        PreferencesUtils.getInstance().saveString(TcpTestMain.this, Contants.PREFERENCES_KEY.usn.name(), "");
                        PreferencesUtils.getInstance().saveString(TcpTestMain.this, Contants.PREFERENCES_KEY.psw.name(), "");
                    }
                }
            }
            
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(TcpContants.TCP_RESPONSE_BROADCAST);
        this.registerReceiver(receiver, filter);
        
        SocketClient.getInstance().init(this);
    }
    
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.button1:
                TcpContants.SERVER_DOMAIN = addressEditView.getText().toString().trim();
                if (TcpContants.SERVER_DOMAIN == null || "".equals(TcpContants.SERVER_DOMAIN))
                {
                    Toast.makeText(this, "地址为空", Toast.LENGTH_LONG).show();;
                    break;
                }
                
                try
                {
                    TcpContants.SERVER_PORT = Integer.parseInt(portEditView.getText().toString().trim());
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "端口不正确", Toast.LENGTH_LONG).show();;
                    break;
                }
                
                SocketClient.getInstance().startService();
                break;
                
            case R.id.button2:
                SocketClient.getInstance().stopService();
                break;
                
            case R.id.button3:
                TcpSendTemplate template = new TcpSendTemplate();
                template.setRequest();
                template.appendData("name", "朱小明");
                template.appendData("description", "小鲜肉，高富帅。");
                template.setOperation("WY0001");
                SocketClient.getInstance().offerTcpSendTemplate(template);
                break;
                
            case R.id.button4:
                template = new TcpSendTemplate();
                template.setRequest();
                template.appendData("name", "zhuxiaoming");
                template.appendData("description", "your description");
                template.setServerCallback(true);
                template.setSeq("2000");
                template.setOperation("WY0001");
                TcpSendEntity entity = new TcpSendEntity(template, this);
                SocketClient.getInstance().offerSendTcpEntity(entity);
                break;
                
            case R.id.button5:
                displayTextView.setText("");
                break;
                
            case R.id.button6:
                UserSession.getInstance().setValidAesKey(false);
                break;
                
            case R.id.button7:
                UserSession.getInstance().setAccessTicket(null);
                break;
        }
    }
    
    private void printlnText(String text)
    {
        displayTextView.setText(displayTextView.getText() + text + "\n");
    }

    @Override
    public void processCallbackData(TcpReceiveTamplate template)
    {
        String text = "receive answer::" + template.getText() + "(error:" + template.getErrorMsg() + ")";
        printlnText(text);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }
}
