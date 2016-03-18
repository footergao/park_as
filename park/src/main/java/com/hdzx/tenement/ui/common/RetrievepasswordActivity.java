package com.hdzx.tenement.ui.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hdzx.tenement.R;
import com.hdzx.tenement.component.SwitchView;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.AlgorithmUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.TimeCount;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by anchendong on 15/7/24.
 */
public class RetrievepasswordActivity extends Activity implements IContentReportor {

    private Button buttonVerifyCode;

    /**
     * 手机号码
     */
    private EditText editTextMobile;
    /**
     * 验证码
     */
    private EditText editTextVerify;
    /**
     * 新密码
     */
    private EditText editTextNewPassword;
    /**
     * 新密码开关
     */
    private SwitchView switchViewNewPassword;

    /**
     * normalTime 计时发送验证码间隔.
     */
    private TimeCount normalTime;

    /**
     * http
     */
    private HttpAsyncTask httpAsyncTask;

    /**
     * http tag
     */
    private String TAG_RE_PASSWORD = "repassword";
    private String TAG_CAPTCHAS = "captchas";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_common_retrievepassword);
        buttonVerifyCode = (Button) findViewById(R.id.btn_retrievepassword_getverifycode);
        normalTime = new TimeCount(60000, 1000, buttonVerifyCode);
        switchViewNewPassword = (SwitchView) findViewById(R.id.vw_retrievepassword_switch);
        editTextNewPassword = (EditText) findViewById(R.id.edit_retrievepassword_newpassword);
        editTextMobile = (EditText) findViewById(R.id.edit_retrievepassword_moblie);
        editTextVerify = (EditText) findViewById(R.id.edit_retrievepassword_verify);


        initEvent();
    }

    public void initEvent() {


        //密码开关
        switchViewNewPassword.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                editTextNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                editTextNewPassword.setSelection(editTextNewPassword.getText().toString().length());
                switchViewNewPassword.toggleSwitch(true);
            }

            @Override
            public void toggleToOff() {
                editTextNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editTextNewPassword.setSelection(editTextNewPassword.getText().toString().length());
                switchViewNewPassword.toggleSwitch(false);
            }
        });
    }

    public void onClick(View v) {
        RequestContentTemplate reqContent;
        HttpRequestEntity httpRequestEntity;
        switch (v.getId()) {
            // 返回
            case R.id.lay_common_retrievepassword_back:
                finish();
                break;
            //确认修改密码
            case R.id.btn_retrievepassword:
                String mobile = editTextMobile.getText().toString();
                String verify = editTextVerify.getText().toString();
                String newpassword = editTextNewPassword.getText().toString();

                if (StringUtils.isBlank(mobile)) {
                    Toast.makeText(getApplicationContext(), editTextMobile.getHint(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(verify)) {
                    Toast.makeText(getApplicationContext(), editTextVerify.getHint(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(newpassword)) {
                    Toast.makeText(getApplicationContext(), editTextNewPassword.getHint(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isRightPwd(newpassword)){
    				Toast.makeText(getApplicationContext(), "请输入数字或字母6-20位的密码",
    						Toast.LENGTH_SHORT).show();
    				return;
    			}
                //HEARD
                reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密

                //BODY
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.mobilePhone.name(), mobile);
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.newPassword.name(), AlgorithmUtil.SHA1(newpassword));
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.captchas.name(), verify);

                //SEND
                httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.RE_PASSWORD.getValue());
                httpRequestEntity.setRequestCode(TAG_RE_PASSWORD);
                httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), this);
                httpAsyncTask.execute(httpRequestEntity);

                break;
            //获取验证码
            case R.id.btn_retrievepassword_getverifycode:
                mobile = editTextMobile.getText().toString();
                if (StringUtils.isBlank(mobile)) {
                    Toast.makeText(getApplicationContext(), editTextMobile.getHint(), Toast.LENGTH_SHORT).show();
                    return;
                }else if (!isMobileNO(mobile)) {
    				Toast.makeText(getApplicationContext(), "请输入正确的手机号码",
    						Toast.LENGTH_SHORT).show();
    				return;

    			}

                normalTime.start();
                // HEARD
                reqContent = new RequestContentTemplate();
                reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

                // BODY
                reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.mobilePhone.name(), mobile);

                // SEND
                httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.CAPTCHAS.getValue());
                httpRequestEntity.setRequestCode(TAG_CAPTCHAS);
                httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
                httpAsyncTask = new HttpAsyncTask(v.getContext(), this);
                httpAsyncTask.execute(httpRequestEntity);

                Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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
            if (responseContent.getResponseCode().equals(TAG_RE_PASSWORD)) {
                if ((Boolean)responseContent.getData()){
                    Toast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "修改密码失败", Toast.LENGTH_SHORT).show();
                }
            }
            if (responseContent.getResponseCode().equals(TAG_CAPTCHAS)) {
                Toast.makeText(getApplicationContext(), "发送验证码成功", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    
    public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isRightPwd(String pwd) {
		Pattern p = Pattern.compile("[a-zA-Z0-9]{6,20}");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}
}
