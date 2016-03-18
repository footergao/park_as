package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.CommonUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;

public class MinePersonInfoSexActivity extends Activity implements IContentReportor
{
    private String sexCode = null;
    
    private ImageView backIv = null;
    
    private TextView saveTv = null;

    private ViewGroup maleLayout = null;
    
    private ViewGroup fmaleLayout = null;
    
    private ViewGroup unknowLayout = null;
    
    private ViewGroup clickView = null;
    
    
    private HttpAsyncTask task = null;
    
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_main_mine_person_sex);
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("sexCode") != null)
        {
            sexCode = intent.getStringExtra("sexCode");
        }

        initView();
        initOnclickView(sexCode);
    }
    
    private void initOnclickView(String sexCode)
    {
        if ("0".equals(sexCode))
        {
            setSelectState(unknowLayout, true);
            clickView = unknowLayout;
        }
        else if ("1".equals(sexCode))
        {
            setSelectState(maleLayout, true);
            clickView = maleLayout;
        }
        else if ("1".equals(sexCode))
        {
            setSelectState(fmaleLayout, true);
            clickView = fmaleLayout;
        }
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
                if (clickView != null)
                {
                    if (clickView == maleLayout)
                    {
                        sexCode = "1";
                    }
                    else if (clickView == fmaleLayout)
                    {
                        sexCode = "2";
                    }
                    else if (clickView == unknowLayout)
                    {
                        sexCode = "0";
                    }
                    else
                    {
                        Toast.makeText(MinePersonInfoSexActivity.this, "请选择您的性别", Toast.LENGTH_LONG).show();
                        return;
                    }
                    
                    makeUpdateNickRequest(sexCode);
                }
                else
                {
                    Toast.makeText(MinePersonInfoSexActivity.this, "请选择您的性别", Toast.LENGTH_LONG).show();
                }
            }
        });
        
        maleLayout = (ViewGroup) findViewById(R.id.male_layout);
        fmaleLayout = (ViewGroup) findViewById(R.id.fmale_layout);
        unknowLayout = (ViewGroup) findViewById(R.id.unknow_layout);
    }
    
    public void onClick(View view)
    {
        if (view instanceof ViewGroup)
        {
            setSelectState((ViewGroup) view, true);
            if (clickView != null)
            {
                setSelectState(clickView, false);
            }
            clickView = (ViewGroup) view;
        }
    }
    
    private void setSelectState(ViewGroup vg, boolean isSelected)
    {
        View image = vg.getChildAt(1);
        if (image != null)
        {
            if (isSelected)
            {
                image.setVisibility(View.VISIBLE);
            }
            else
            {
                image.setVisibility(View.INVISIBLE);
            }
        }
    }
    
    private void makeUpdateNickRequest(String code)
    {
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.aes);
        
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.sex.name(), code);
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
                    intent.putExtra("sexCode", sexCode);
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
