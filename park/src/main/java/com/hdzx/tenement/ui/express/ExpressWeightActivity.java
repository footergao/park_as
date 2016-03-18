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
import com.hdzx.tenement.vo.ExpressWeightVo;

import java.util.List;

/**
 * Created by anchendong on 15/7/23.
 */
public class ExpressWeightActivity extends Activity implements IContentReportor {

    /**
     * 快递公司 listview
     */
    private ListView listViewExpressWeight;

    /**
     * 快递公司 list
     */
    private List<ExpressWeightVo> listExpressWeight;

    //http
    private HttpAsyncTask httpAsyncTask;

    //http tag
    private String TAG_GETEXPRESSWEIGHT = "getexpressweight";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_weight);
        listViewExpressWeight = (ListView) findViewById(R.id.lv_express_weight);

        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {

        listViewExpressWeight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                ExpressWeightVo expressWeightVo = listExpressWeight.get(position);
                intent.putExtra("expressweight", expressWeightVo.getEdStandardAbove() + "~" + expressWeightVo.getEdStandardBelow());
                intent.putExtra("expresscost", expressWeightVo.getEdCost());
                intent.putExtra("expresstype", expressWeightVo.getEdType());
                setResult(Contants.CODE_EXPRESS_WEIGHT, intent);
                finish();
            }
        });
    }

    /**
     * 快递收发-获取收费标准
     */
    private void getExpressWeight() {
        //HEARD
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密
        //BODY
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.lifecircleId.name(), "生活圈编号");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.edProvince.name(), "所在省");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.edCity.name(), "所在市");

        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_EXPRESS_WEIGHT.getValue());
        httpRequestEntity.setRequestCode(TAG_GETEXPRESSWEIGHT);
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
        httpAsyncTask = new HttpAsyncTask(getApplicationContext(), ExpressWeightActivity.this);
        httpAsyncTask.execute(httpRequestEntity);

    }


    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_weight_back:
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
            if (responseContent.getResponseCode().equals(TAG_GETEXPRESSWEIGHT)) {
                //获取重量
                String jsonStr = responseContent.getDataJson();
                listExpressWeight = gson.fromJson(jsonStr, new TypeToken<List<ExpressWeightVo>>() {
                }.getType());

                ExpressWeightAdapter expressCompanyAdapter = new ExpressWeightAdapter(getApplicationContext(), listExpressWeight);
                listViewExpressWeight.setAdapter(expressCompanyAdapter);

            }

        }
    }
}
