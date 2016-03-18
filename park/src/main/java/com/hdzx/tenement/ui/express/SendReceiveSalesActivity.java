package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.Contants;

/**
 * Created by anchendong on 15/7/27.
 */
public class SendReceiveSalesActivity extends Activity implements IContentReportor {

    /**
     * 快递公司名称
     */
    private TextView textViewExpressCompany;

    /**
     * 重量
     */
    private TextView textViewExpressWeight;

    /**
     * 收费标准
     */
    private TextView textViewExpressCost;

    //http
    private HttpAsyncTask httpAsyncTask;

    //http tag
    private String TAG_SUBMITEXPRESSORDER = "submitexpressorder";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_scales);
        textViewExpressCompany = (TextView) findViewById(R.id.txt_express_companyname_out);
        textViewExpressWeight = (TextView) findViewById(R.id.txt_express_weight_out);
        textViewExpressCost = (TextView) findViewById(R.id.txt_express_cost);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Contants.CODE_EXPRESS_COMPANY && resultCode == Contants.CODE_EXPRESS_COMPANY) {
            textViewExpressCompany.setText(data.getStringExtra("expresscompany"));
        } else if (requestCode == Contants.CODE_EXPRESS_WEIGHT && resultCode == Contants.CODE_EXPRESS_WEIGHT) {
            if (data.getStringExtra("expresstype").equals("002")) {
                textViewExpressWeight.setText(data.getStringExtra("expressweight"));
                textViewExpressCost.setText("地区不可送达");
            } else {
                textViewExpressWeight.setText(data.getStringExtra("expressweight"));
                textViewExpressCost.setText(data.getStringExtra("expresscost"));
            }
        }
    }

    /**
     * 提交订单
     */
    private void submitOrder() {
        //HEARD
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密
        //BODY
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.lifecircleId.name(), "生活圈编号");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.edProvince.name(), "所在省");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.edCity.name(), "所在市");

        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.SUBMIT_EXPRESS_ORDER.getValue());
        httpRequestEntity.setRequestCode(TAG_SUBMITEXPRESSORDER);
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
        httpAsyncTask = new HttpAsyncTask(getApplicationContext(), SendReceiveSalesActivity.this);
        httpAsyncTask.execute(httpRequestEntity);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_scales_back:
                finish();
                break;
            case R.id.btn_express_sale_submit:
                //提交
                submitOrder();
                break;
            case R.id.lay_express_company:
                //快递列表
                Intent companyIntent = new Intent(this, ExpressCompanyActivity.class);
                startActivityForResult(companyIntent, Contants.CODE_EXPRESS_COMPANY);
                break;
            case R.id.lay_express_weight:
                //重量
                Intent weightIntent = new Intent(this, ExpressWeightActivity.class);
                startActivityForResult(weightIntent, Contants.CODE_EXPRESS_WEIGHT);
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
            if (responseContent.getResponseCode().equals(TAG_SUBMITEXPRESSORDER)) {
                Intent intent = new Intent(this, ExpressActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }
    }
}
