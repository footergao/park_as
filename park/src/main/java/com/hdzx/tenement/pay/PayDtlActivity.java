package com.hdzx.tenement.pay;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.pay.vo.PayPre;
import com.hdzx.tenement.pay.vo.PayType;
import com.hdzx.tenement.pay.vo.WXPayPre;
import com.hdzx.tenement.pay.wxpay.Util;
import com.hdzx.tenement.pay.zfbpay.PayResult;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.NetUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2016/3/7.
 */
public class PayDtlActivity extends Activity implements IContentReportor {

    private static final String HTTP_TAG_BANK_LIST = "tagbanklist";
    private static final String HTTP_TAG_PAY = "tagpay";
    private HttpAsyncTask task;
    private List<PayType> list;
    private RadioGroup group;
    private int checkedRbtnId = 0;//0,1,2...
    private Button btn_pay;
    private PayType payType;//当前选中交易类型

    //
    private String mer_no = "00000005";
    private String in_source = "02";
    private String order_ccy = "RMB";
    private String order_content = "test";
    private String subject_name = "android";
    private String order_am = "0.01";//订单金额
    private String order_time = "";
    private String order_no = "";
    private String spbill_create_ip = "";

    //
    private IWXAPI api;
    private String wxAppid="wx1b554969a3a9d879";


    //zfb
    private static final int SDK_PAY_FLAG = 1;


    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayDtlActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayDtlActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayDtlActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay_dtl_activity);

        initView();
        initData();
        initAction();
    }


    private void initView() {

        group = (RadioGroup) findViewById(R.id.group_pay_type);
        btn_pay = (Button) findViewById(R.id.btn_pay);

    }

    private void initData() {

        list = new ArrayList<>();

        order_no = getOutTradeNo();
        order_time = getOutTradeTime();

        spbill_create_ip = NetUtils.getLocalIpAddress(this);

        getBankLstRequest();
    }

    private void initAction() {

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Log.v("gl", "checkedId==" + checkedId);
                checkedRbtnId = checkedId;

                payType = list.get(checkedId);

            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payOrder(mer_no, in_source, order_no, subject_name, order_am, order_ccy, order_time, order_content, payType.getBank_no(), spbill_create_ip);

            }


        });
    }

    private void payOrder(String mer_no, String in_source, String order_no, String subject_name, String order_amt, String order_ccy, String order_time, String order_content, String bank_no, String spbill_create_ip) {
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);

        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.mer_no.name(), mer_no);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.in_source.name(), in_source);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.order_no.name(), order_no);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.subject_name.name(), subject_name);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.order_amt.name(), order_amt);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.order_ccy.name(), order_ccy);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.order_time.name(), order_time);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.order_content.name(), order_content);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.bank_no.name(), bank_no);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.spbill_create_ip.name(), spbill_create_ip);

        reqContent.isRequestTicket();

        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(
                reqContent, Contants.SERVER_HOST,
                Contants.PROTOCOL_COMMAND.GET_PAY.getValue());
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);
        httpRequestEntity.setRequestCode(HTTP_TAG_PAY);
        task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);
    }


    private void getBankLstRequest() {
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);

        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.in_source.name(), in_source);
        reqContent.appendData(
                Contants.PROTOCOL_REQ_BODY_DATA.mer_no.name(), mer_no);
        reqContent.isRequestTicket();

        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(
                reqContent, Contants.SERVER_HOST,
                Contants.PROTOCOL_COMMAND.GET_BANK_LST.getValue());
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);
        httpRequestEntity.setRequestCode(HTTP_TAG_BANK_LIST);
        task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);
    }

    @Override
    public void reportBackContent(ResponseContentTamplate responseContent) {
        String rtnCode = (String) responseContent
                .getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
        if (rtnCode == null || "".equals(rtnCode)) {
            Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT)
                    .show();
        } else if (!Contants.ResponseCode.CODE_000000.equals(rtnCode)) {
            Toast.makeText(getApplicationContext(),
                    responseContent.getErrorMsg(), Toast.LENGTH_SHORT).show();
        } else {

            if (HTTP_TAG_BANK_LIST.equals(responseContent
                    .getResponseCode())) {
                String data = responseContent.getDataJson().trim();
                System.out.println("data=" + data);
                if (data != null && !"".equals(data) && data instanceof String) {

                    Gson gson = new Gson();
                    list.clear();
                    list = gson.fromJson(data.toString(),
                            new TypeToken<List<PayType>>() {
                            }.getType());


                    for (int i = 0; i < list.size(); i++) {

                        PayType type = list.get(i);
                        RadioButton rbtn = new RadioButton(this);
                        rbtn.setId(i);
                        rbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        rbtn.setText(type.getRemark());
                        rbtn.setTextColor(getResources().getColor(R.color.black));

                        group.addView(rbtn);
                    }
                }
            } else if (HTTP_TAG_PAY.equals(responseContent
                    .getResponseCode())) {

                String data = responseContent.getData().toString();
                System.out.println("data=" + data);
                if (data != null && !"".equals(data) && data instanceof String) {
                    if (checkedRbtnId == 1) {
                        pay(data);
                    } else {
                        api = WXAPIFactory.createWXAPI(this, wxAppid);
                        // 将该app注册到微信
                        api.registerApp(wxAppid);

                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONObject json = jsonObject.getJSONObject("detail");

                            payWx(json);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        }
    }

    private void payWx(JSONObject json) {
        Button payBtn = (Button) findViewById(R.id.btn_pay);
        payBtn.setEnabled(false);
        Toast.makeText(PayDtlActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        try {
            if (null != json) {
                PayReq req = new PayReq();
                req.appId = json.getString("appid");
                req.partnerId = json.getString("partnerid");
                req.prepayId = json.getString("prepay_id");
                req.nonceStr = json.getString("nonce_str");
                req.timeStamp = json.getString("timestamp");
                req.packageValue = json.getString("package");
                req.sign = json.getString("sign");
                req.extData = "app data"; // optional
                Toast.makeText(PayDtlActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                api.sendReq(req);
            } else {
                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                Toast.makeText(PayDtlActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
            Toast.makeText(PayDtlActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        btn_pay.setEnabled(true);
    }


    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    private String getOutTradeTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String time = format.format(date);
        return time;
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(final String data) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayDtlActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(data, true);
                Log.v("gl", "data===" + data);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
