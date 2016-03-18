package com.hdzx.tenement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.AlgorithmUtil;
import com.hdzx.tenement.utils.BeansUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anchendong on 15/7/8.
 */
public class HttpTestActivity extends Activity implements IContentReportor {

    private TextView textViewAccount = null;

    private TextView textViewPassword = null;

    private Button buttonRegist = null;

    private HttpAsyncTask httpAsyncTask = null;

    private Button buttonGetTicket = null;

    private Button buttonChangePassword = null;

    private Button buttonRePassword = null;

    private Button buttonLogout = null;

    private Button buttonCheckmail = null;

    private Button buttonCheckId = null;

    private Button buttonCheckAccount = null;

    private Button buttonGetBasic = null;

    private Button buttonGetBasicInfo = null;

    private Button buttonSetBasic = null;

    private Button buttonSetBasicInfo = null;


    private Button buttonCordova;
    /**
     * 获取AES标示
     */
    private static final String GET_AES = "aes";

    /**
     * 登录获取票据标示
     */
    private static final String LOGIN = "login";

    /**
     * 登出
     */
    private static final String LOGOUT = "logout";

    /**
     * 注册标示
     */
    private static final String REGIST = "regist";

    /**
     * 修改密码
     */
    private static final String SET_PASSWORD = "changepassword";

    /**
     * 重置密码
     */
    private static final String RE_PASSWORD = "repassword";

    /**
     * 验证邮箱是否存在
     */
    private static final String CHECK_EMAIL = "checkemail";

    /**
     * 检查身份证
     */
    private static final String CHECK_ID = "checkid";

    /**
     * 验证账户
     */
    private static final String CHECK_ACCOUNT = "checkaccount";

    /**
     * 获取用户主表信息
     */
    private static final String GET_BASIC = "getbasic";

    /**
     * 获取用户基本信息
     */
    private static final String GET_BASICINFO = "getbasicinfo";

    /**
     * 更新用户主表信息
     */
    private static final String SET_BASIC = "setbasic";

    /**
     * 更新用户基本信息
     */
    private static final String SET_BASICINFO = "setbasicinfo";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.http_test);
        textViewAccount = (TextView) findViewById(R.id.account_txt);
        textViewPassword = (TextView) findViewById(R.id.password_txt);
        buttonRegist = (Button) findViewById(R.id.regist_button);
        buttonGetTicket = (Button) findViewById(R.id.getticket_button);
        buttonChangePassword = (Button) findViewById(R.id.changepassword_button);
        buttonRePassword = (Button) findViewById(R.id.repassword_button);
        buttonLogout = (Button) findViewById(R.id.loginout_button);
        buttonCheckmail = (Button) findViewById(R.id.checkemail_button);
        buttonCheckId = (Button) findViewById(R.id.checkid_button);
        buttonCheckAccount = (Button) findViewById(R.id.checkaccount_button);
        buttonGetBasic = (Button) findViewById(R.id.getbasic_button);
        buttonGetBasicInfo = (Button) findViewById(R.id.getbasicinfo_button);
        buttonSetBasic = (Button) findViewById(R.id.updatebasic_button);
        buttonSetBasicInfo = (Button) findViewById(R.id.updatebasicinfo_button);
        buttonCordova=(Button)findViewById(R.id.cordova_button);

        //HEARD
        RequestContentTemplate reqContent = new RequestContentTemplate();
        //请求不使用AES加密
        reqContent.setEncryptoType(CryptoTyepEnum.none);

        //BODY
        Map<String, Object> dataMap = new HashMap<String, Object>();
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.imei.name(), tm.getDeviceId());
        dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.imsi.name(), tm.getSubscriberId());
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), "");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);

        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_AES.getValue());
        httpRequestEntity.setRequestCode(GET_AES);
        //返回使用RSA密钥解密
        reqContent.setEncryptoType(CryptoTyepEnum.rsa);
        httpAsyncTask = new HttpAsyncTask(this, this);
        httpAsyncTask.execute(httpRequestEntity);

        buttonRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                //请求使用AES加密
                reqContent.setEncryptoType(CryptoTyepEnum.aes);

                //BODY
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.mobilePhone.name(), "18888888812");
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.password.name(), AlgorithmUtil.SHA1("123456"));
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.rePassword.name(), AlgorithmUtil.SHA1("123456"));
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.captchas.name(), "0000");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), "");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.REGIST.getValue());
                httpRequestEntity.setRequestCode(REGIST);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //登录
        buttonGetTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                //请求使用AES加密
                reqContent.setEncryptoType(CryptoTyepEnum.aes);

                //BODY
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.loginName.name(), "18662521963");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.password.name(), AlgorithmUtil.SHA1("111111"));
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.type.name(), Contants.LONGIN_TYPE_LOG);

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.LOGIN.getValue());
                httpRequestEntity.setRequestCode(LOGIN);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //登出
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                //请求使用AES加密
                reqContent.setEncryptoType(CryptoTyepEnum.none);

                //BODY
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), UserSession.getInstance().getAccessTicket());
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), "");

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.LOGOUT.getValue());
                httpRequestEntity.setRequestCode(LOGOUT);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //修改密码
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(CryptoTyepEnum.none);//请求使用AES加密

                //BODY
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.newPassword.name(), AlgorithmUtil.SHA1("123456"));
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.oldPassword.name(), AlgorithmUtil.SHA1("123456"));
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), UserSession.getInstance().getAccessTicket());
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.SET_PASSWORD.getValue());
                httpRequestEntity.setRequestCode(SET_PASSWORD);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //重置密码
        buttonRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(CryptoTyepEnum.none);//请求使用AES加密

                //BODY
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.newPassword.name(), AlgorithmUtil.SHA1("123456"));
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.mobilePhone.name(), "18888888812");
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.captchas.name(), "000");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), "");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.RE_PASSWORD.getValue());
                httpRequestEntity.setRequestCode(RE_PASSWORD);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //检查邮箱
        buttonCheckmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(CryptoTyepEnum.none);//请求使用AES加密

                //BODY
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.email.name(), "123@qq.com");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), "");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.CHECK_EMAIL.getValue());
                httpRequestEntity.setRequestCode(CHECK_EMAIL);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //检查身份
        buttonCheckId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(CryptoTyepEnum.none);//请求使用AES加密

                //BODY
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.idcard.name(), "123456789");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), "");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.CHECK_ID.getValue());
                httpRequestEntity.setRequestCode(CHECK_ID);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //检查登录名
        buttonCheckAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(CryptoTyepEnum.none);//请求使用AES加密

                //BODY
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put(Contants.PROTOCOL_REQ_BODY_DATA.loginName.name(), "18888888812");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), "");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.CHECK_LOGINNAME.getValue());
                httpRequestEntity.setRequestCode(CHECK_ACCOUNT);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //获取用户主表信息
        buttonGetBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(CryptoTyepEnum.none);//请求使用AES加密

                //BODY
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), UserSession.getInstance().getAccessTicket());
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), "");

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_USETBASIC.getValue());
                httpRequestEntity.setRequestCode(GET_BASIC);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //获取用户基本信息
        buttonGetBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(CryptoTyepEnum.none);//请求使用AES加密

                //BODY
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), UserSession.getInstance().getAccessTicket());
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), "");

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_PERSON_INFO.getValue());
                httpRequestEntity.setRequestCode(GET_BASICINFO);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //更新主表信息
        buttonSetBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //更新用户基本信息
        buttonSetBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(CryptoTyepEnum.none);//请求使用AES加密

                //BODY
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("userName","胖子");
                dataMap.put("birthday","2015-09-07");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), UserSession.getInstance().getAccessTicket());
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.SET_USETBASICINFO.getValue());
                httpRequestEntity.setRequestCode(SET_BASIC);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });

        //更新用户主表信息
        buttonSetBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HEARD
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(CryptoTyepEnum.none);//请求使用AES加密

                //BODY
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("level","2");
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(), UserSession.getInstance().getAccessTicket());
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);

                //SEND
                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.SET_USETBASIC.getValue());
                httpRequestEntity.setRequestCode(SET_BASICINFO);
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), HttpTestActivity.this);
                httpAsyncTask.execute(httpRequestEntity);
            }
        });


        //跳转到cordova
        buttonCordova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),CordovaTestActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void reportBackContent(ResponseContentTamplate responseContent) {
        String rtnCode = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
        if ("".equals(rtnCode) || rtnCode == null) {
            Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT).show();
        } else if (!"000000".equals(rtnCode)) {
            String rtnMsg = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
            Toast.makeText(getApplicationContext(), rtnMsg, Toast.LENGTH_SHORT).show();
        } else {
            if (responseContent.getResponseCode().equals(GET_AES)) {
                UserSession.getInstance().setAesKey((String) responseContent.getInMapData(Contants.PROTOCOL_RESP_BODY.aes.name()));
                UserSession.getInstance().setSessionId((String) responseContent.getInMapData(Contants.PROTOCOL_RESP_BODY.sessionid.name()));
                UserSession.getInstance().setImageHost((String) responseContent.getInMapData(Contants.PROTOCOL_RESP_BODY.imghost.name()));
            } else if (responseContent.getResponseCode().equals(LOGIN)) {
                //登录
                String ticket = (String) responseContent.getData();
                UserSession.getInstance().setAccessTicket(ticket);
                if ("".equals(ticket) || ticket == null) {
                    Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                }
            } else if (responseContent.getResponseCode().equals(LOGOUT)) {
                //登出
                Boolean rs = (Boolean) responseContent.getData();
                if (rs) {
                    Toast.makeText(getApplicationContext(), "登出成功", Toast.LENGTH_SHORT).show();
                }
            } else if (responseContent.getResponseCode().equals(REGIST)) {
                //注册
                Boolean rs = (Boolean) responseContent.getData();
                if (rs) {
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                }

            } else if (responseContent.getResponseCode().equals(SET_PASSWORD)) {
                //修改登录密码-需要登录
                Boolean rs = (Boolean) responseContent.getData();
                if (rs) {
                    Toast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_SHORT).show();
                }
            } else if (responseContent.getResponseCode().equals(RE_PASSWORD)) {
                //重置密码-不需要登录
                Boolean rs = (Boolean) responseContent.getData();
                if (rs) {
                    Toast.makeText(getApplicationContext(), "重置密码成功", Toast.LENGTH_SHORT).show();
                }
            } else if (responseContent.getResponseCode().equals(CHECK_EMAIL)) {
                //检查邮箱-不需要登录
                Boolean rs = (Boolean) responseContent.getData();
                if (rs) {
                    Toast.makeText(getApplicationContext(), "存在这个邮箱", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "没有这个邮箱", Toast.LENGTH_SHORT).show();
                }
            } else if (responseContent.getResponseCode().equals(CHECK_ID)) {
                //检查身份证-不需要登录
                Boolean rs = (Boolean) responseContent.getData();
                if (rs) {
                    Toast.makeText(getApplicationContext(), "存在这个身份证", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "没有这个身份证", Toast.LENGTH_SHORT).show();
                }
            } else if (responseContent.getResponseCode().equals(CHECK_ACCOUNT)) {
                //检查账号-不需要登录
                Boolean rs = (Boolean) responseContent.getData();
                if (rs) {
                    Toast.makeText(getApplicationContext(), "存在这个账号", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "不存在这个账号", Toast.LENGTH_SHORT).show();
                }
            } else if (responseContent.getResponseCode().equals(GET_BASIC)) {
                //获取用户主表信息
                UserBasic userBasic = (UserBasic) BeansUtil.map2Bean((Map<String, String>) responseContent.getData(), UserBasic.class);
                UserSession.getInstance().setUserBasic(userBasic);
            } else if (responseContent.getResponseCode().equals(GET_BASICINFO)) {
                //获取用户全部信息
                UserBasic userBasicInfo = (UserBasic) BeansUtil.map2Bean((Map<String, String>) responseContent.getData(), UserBasic.class);
                UserSession.getInstance().setUserBasic(userBasicInfo);
            }else if(responseContent.getResponseCode().equals(SET_BASIC)){
                //更新用户基本信息
                Boolean rs = (Boolean) responseContent.getData();
                if (rs) {
                    Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();
                }
            }else if(responseContent.getResponseCode().equals(SET_BASICINFO)){
                //更新用户基本信息
                Boolean rs = (Boolean) responseContent.getData();
                if (rs) {
                    Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
