package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.Contants;

import java.util.List;

/**
 * Created by anchendong on 15/7/23.
 */
public class ExpressCompanyActivity extends Activity implements IContentReportor {

    /**
     * 快递公司 listview
     */
    private ListView listViewExpressCompany;

    /**
     * 快递公司 list
     */
    private List<String> listExpressCompany;

    //http
    private HttpAsyncTask httpAsyncTask;

    //http tag
    private String TAG_GETEXPRESSCOMPANY = "getexpresscompanylist";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_company);
        listViewExpressCompany = (ListView) findViewById(R.id.lv_express_company);


        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        getExpressCompany();

        listViewExpressCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.putExtra("expresscompany", listExpressCompany.get(position));
                setResult(Contants.CODE_EXPRESS_COMPANY, intent);
                finish();
            }
        });
    }

    /**
     * 快递收发-获取快递公司
     */
    private void getExpressCompany() {
        //HEARD
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密
        //BODY
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.lifecircleId.name(), "生活圈编号");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.edProvince.name(), "所在省");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.edCity.name(), "所在市");

        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_EXPRESS_COMPANY.getValue());
        httpRequestEntity.setRequestCode(TAG_GETEXPRESSCOMPANY);
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
        httpAsyncTask = new HttpAsyncTask(getApplicationContext(), ExpressCompanyActivity.this);
        httpAsyncTask.execute(httpRequestEntity);
    }


    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_company_back:
                finish();
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
            Gson gson = new Gson();
            if (responseContent.getResponseCode().equals(TAG_GETEXPRESSCOMPANY)) {
                //获取快递公司列表
                String jsonStr = responseContent.getDataJson();
                listExpressCompany = gson.fromJson(jsonStr, new TypeToken<List<String>>() {
                }.getType());

                ExpressCompanyAdapter expressCompanyAdapter = new ExpressCompanyAdapter(getApplicationContext(), listExpressCompany);
                listViewExpressCompany.setAdapter(expressCompanyAdapter);
            }

        }
    }
}
