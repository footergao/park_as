package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.CommonUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;

public class MinePersonInfoNickActivity extends Activity implements IContentReportor
{
    private ImageView backIv = null;

    private EditText nickEt = null;

    private TextView saveTv = null;

    private String nick = "";

    private HttpAsyncTask task = null;

    
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_main_mine_person_nick);
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("nick") != null)
        {
            nick = intent.getStringExtra("nick");
        }

        initView();
    }

    private void initView()
    {
        backIv = (ImageView) findViewById(R.id.back_iv);
        backIv.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        saveTv = (TextView) findViewById(R.id.save_tv);
        saveTv.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                nick = nickEt.getText().toString().trim();
                if (nick != null && !"".equals(nick))
                {
                    makeUpdateNickRequest(nick);
                }
                else
                {
                    Toast.makeText(MinePersonInfoNickActivity.this, "请填写你的名字", Toast.LENGTH_LONG).show();
                    ;
                }

            }
        });

        nickEt = (EditText) findViewById(R.id.name_cfet);
        nickEt.setText(nick);
    }

    private void makeUpdateNickRequest(String nick)
    {
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.aes);

        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.nickerName.name(), nick);
        reqContent.isRequestTicket();

        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.UPDATE_PERSON_INFO.getValue());
        httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
        task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);
    }

    @Override
    public void reportBackContent(ResponseContentTamplate responseContent)
    {
        String rtnCode = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
        if ("".equals(rtnCode) || rtnCode == null)
        {
            Toast.makeText(this, "网络异常，请稍后尝试", Toast.LENGTH_SHORT).show();
        }
        else if (!Contants.ResponseCode.CODE_000000.equals(rtnCode))
        {
            String rtnMsg = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
            Toast.makeText(this, rtnMsg, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Object data = responseContent.getData();
            if (data != null && data instanceof Boolean)
            {
                Boolean b = (Boolean) data;
                if (b.booleanValue())
                {
                    Intent intent = this.getIntent();
                    intent.putExtra("nick", nick);
                    this.setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "设置失败，请稍后尝试。", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "服务器异常，请稍后尝试。", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
