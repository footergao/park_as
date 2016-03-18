package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.TimeCount;

/**
 * Created by anchendong on 15/7/27.
 */
public class PhoneManageAddActivity extends Activity implements IContentReportor {

    /**
     * normalTime 计时发送验证码间隔.
     */
    private TimeCount normalTime;

    /**
     * 发送验证码
     */
    private Button buttonGetVerify;

    /**
     * 添加姓名
     */
    private EditText editTextAddName;

    /**
     * 添加手机
     */
    private EditText editTextAddPhone;

    /**
     * 验证码
     */
    private EditText editTextVerify;


    /**
     * http
     */
    private HttpAsyncTask httpAsyncTask;

    /**
     * http tag
     */
    private String TAG_ADDPHONE = "addphone";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_phonemanage_add);
        buttonGetVerify = (Button) findViewById(R.id.btn_express_phonemanage_addverify);
        normalTime = new TimeCount(60000, 1000, buttonGetVerify);
        editTextAddName = (EditText) findViewById(R.id.edit_express_phonemanage_addname);
        editTextAddPhone = (EditText) findViewById(R.id.edit_express_phonemanage_addphone);
        editTextVerify = (EditText) findViewById(R.id.edit_express_phonemanage_verify);

    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_phonemanageadd_back:
                finish();
                break;
            case R.id.btn_express_phonemanage_addverify:
                //获取验证码
                normalTime.start();
                Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_express_phonemanage_add:
                //确认添加
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密
                //BODY
                reqContent.setRequestTicket(true);
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.enpName.name(), editTextAddName.getText().toString());
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.enpPhone.name(), editTextAddPhone.getText().toString());
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.enpVerifyCode.name(), editTextVerify.getText().toString());

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.ADD_NOTE_PHONE.getValue());
                httpRequestEntity.setRequestCode(TAG_ADDPHONE);
                httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(getApplicationContext(), PhoneManageAddActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
                break;
            default:
                break;
        }
    }

    /**
     * http 回调
     *
     * @param responseContent
     */
    @Override
    public void reportBackContent(ResponseContentTamplate responseContent) {
        String rtnCode = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
        if ("".equals(rtnCode) || rtnCode == null) {
            Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT).show();
        } else if (!"000000".equals(rtnCode)) {
            String rtnMsg = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
            Toast.makeText(getApplicationContext(), rtnMsg, Toast.LENGTH_SHORT).show();
        } else {
            if (responseContent.getResponseCode().equals(TAG_ADDPHONE)) {
                //新增手机
                Intent intent = getIntent();
                setResult(Contants.CODE_EXPRESS_ADDPHONE, intent);
                finish();
            }
        }
    }
}
