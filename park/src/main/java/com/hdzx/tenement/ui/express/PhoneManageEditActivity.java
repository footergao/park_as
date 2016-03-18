package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.Contants;

/**
 * Created by anchendong on 15/7/28.
 */
public class PhoneManageEditActivity extends Activity implements IContentReportor {

    //http
    private HttpAsyncTask httpAsyncTask;

    //http TAG
    private String TAG_UPADTEPHONELIST = "updatephone";

    /**
     * 姓名
     */
    private EditText editTextNameedit;
    /**
     * 手机
     */
    private TextView textViewPhoneedit;

    /**
     * phoneid
     */
    private int id;

    //等待动画
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_phonemanage_edit);
        editTextNameedit = (EditText) findViewById(R.id.edit_express_phonemanage_editname);
        textViewPhoneedit = (TextView) findViewById(R.id.edit_express_phonemanage_editphone);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("加载中...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);


        initView();
    }

    public void initView() {
        Bundle bundle = getIntent().getExtras();
        editTextNameedit.setText(bundle.getString("name"));
        textViewPhoneedit.setText(bundle.getString("phone"));
        id = bundle.getInt("id");

        editTextNameedit.setSelection(editTextNameedit.getText().toString().length());
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_phonemanageedit_back:
                finish();
                break;
            case R.id.txt_express_phonemanage_edit:
                progressDialog.show();
                //保存
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密
                //BODY
                reqContent.setRequestTicket(true);
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), "001");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.enpId.name(), id);
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.enpName.name(), editTextNameedit.getText().toString());

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.SET_NOTE_PHONE.getValue());
                httpRequestEntity.setRequestCode(TAG_UPADTEPHONELIST);
                httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(getApplicationContext(), PhoneManageEditActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
                break;
            case R.id.img_express_phonemanage_editnamaclear:
                //清空姓名
                editTextNameedit.setText("");
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
            if (responseContent.getResponseCode().equals(TAG_UPADTEPHONELIST)) {
                //更新备注
                progressDialog.dismiss();
                Intent intent = getIntent();
                setResult(Contants.CODE_EXPRESS_EDITPHONE, intent);
                finish();
            }
        }
    }
}
