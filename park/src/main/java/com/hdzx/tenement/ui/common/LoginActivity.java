package com.hdzx.tenement.ui.common;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.push.CloudPushService;
import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.deamon.AppDeamon;
import com.hdzx.tenement.component.SwitchView;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.ui.MainActivity;
import com.hdzx.tenement.utils.AlgorithmUtil;
import com.hdzx.tenement.utils.BaiduLocationClient;
import com.hdzx.tenement.utils.BeansUtil;
import com.hdzx.tenement.utils.CommonUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.PreferencesUtils;
import com.hdzx.tenement.utils.TimeCount;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by anchendong on 15/7/15.
 */
public class LoginActivity extends Activity implements IContentReportor {

	/**
	 * 登录，注册tab radiogroup
	 */
	private RadioGroup radioLogin;
	/**
	 * 登录页面
	 */
	private LinearLayout layoutLogin;

	/**
	 * 登录用户名
	 */
	private EditText editViewLoginUsername;

	/**
	 * 登录密码
	 */
	private EditText editViewLoginPassword;

	/**
	 * 登录密码开关
	 */
	private SwitchView swLoginPassword;

	/**
	 * 登录
	 */
	private Button buttonLogin;

	/**
	 * 注册页面
	 */
	private LinearLayout layoutregist;

	/**
	 * 注册手机
	 */
	private EditText editRegistMobile;

	/**
	 * 注册密码
	 */
	private EditText editRegistPassword;

	/**
	 * 注册密码开关
	 */
	private SwitchView swRegistPassword;

	/**
	 * 注册验证码
	 */
	private EditText editViewVerify;

	/**
	 * 注册
	 */
	private Button buttonRegist;

	/**
	 * 注册码
	 */
	private Button buttonCode;

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
	private String TAG_GET_AES = "getaes";

	private String TAG_LOGIN = "login";

	private String TAG_LOGIN_AUTO = "loginauto";

	private String TAG_REGIST = "regist";

	private String TAG_CAPTCHAS = "captchas";

	/**
	 * 登录用户名
	 */
	private String username = "";

	/**
	 * 登录密码
	 */
	private String password = "";
	
	
	private static final String GET_AGAIN_BASICINFO = "getagainbasicinfo";

	private ImageView head_default;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenement_common_login);

		initApp();

		radioLogin = (RadioGroup) findViewById(R.id.radiobtn_login);
		layoutLogin = (LinearLayout) findViewById(R.id.lay_login);
		layoutregist = (LinearLayout) findViewById(R.id.lay_regist);
		editRegistMobile = (EditText) findViewById(R.id.edit_regist_moblie);
		editViewVerify = (EditText) findViewById(R.id.edit_regist_verify);
		swLoginPassword = (SwitchView) findViewById(R.id.vw_login_switch);
		editViewLoginPassword = (EditText) findViewById(R.id.edit_login_password);
		swRegistPassword = (SwitchView) findViewById(R.id.vw_regist_switch);
		editRegistPassword = (EditText) findViewById(R.id.edit_regist_password);
		buttonRegist = (Button) findViewById(R.id.btn_regist);
		buttonLogin = (Button) findViewById(R.id.btn_login);
		editViewLoginUsername = (EditText) findViewById(R.id.edit_login_username);
		buttonCode = (Button) findViewById(R.id.regist_code_button);
		normalTime = new TimeCount(60000, 1000, buttonCode);
		head_default = (ImageView) findViewById(R.id.img_head);

		init();
		initEvent();
	}

	private void init() {
//		makePersonInforRequest(GET_AGAIN_BASICINFO);

		// 查看是否存在账号，密码
		String usn = PreferencesUtils.getInstance().takeString(
				this.getBaseContext(), Contants.PREFERENCES_KEY.usn.name());
		String psw = PreferencesUtils.getInstance().takeString(
				this.getBaseContext(), Contants.PREFERENCES_KEY.psw.name());

		editViewLoginUsername.setText(usn);
		editViewLoginPassword.setText(psw);
		editViewLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		if (StringUtils.isNotBlank(usn) && StringUtils.isNotBlank(psw)) {

			login(usn, psw);

		}

	}

	/**
	 * 初始化界面
	 */
	private void initEvent() {
		// 登录，注册tab
		radioLogin
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.btn_login_tab: {
							layoutLogin.setVisibility(View.VISIBLE);
							layoutregist.setVisibility(View.GONE);
							break;
						}
						case R.id.btn_regist_tab: {
							layoutLogin.setVisibility(View.GONE);
							layoutregist.setVisibility(View.VISIBLE);
							break;
						}
						default:
							break;
						}
					}
				});

		// 登录密码开关
		swLoginPassword
				.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
					@Override
					public void toggleToOn() {
						editViewLoginPassword
								.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						editViewLoginPassword
								.setSelection(editViewLoginPassword.getText()
										.toString().length());
						swLoginPassword.toggleSwitch(true);
					}

					@Override
					public void toggleToOff() {
						editViewLoginPassword
								.setInputType(InputType.TYPE_CLASS_TEXT
										| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						editViewLoginPassword
								.setSelection(editViewLoginPassword.getText()
										.toString().length());
						swLoginPassword.toggleSwitch(false);
					}
				});

		// 注册密码开关
		swRegistPassword
				.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
					@Override
					public void toggleToOn() {
						editRegistPassword
								.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						editRegistPassword.setSelection(editRegistPassword
								.getText().toString().length());
						swRegistPassword.toggleSwitch(true);
					}

					@Override
					public void toggleToOff() {
						editRegistPassword
								.setInputType(InputType.TYPE_CLASS_TEXT
										| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						editRegistPassword.setSelection(editRegistPassword
								.getText().toString().length());
						swRegistPassword.toggleSwitch(false);
					}
				});

	}

	public void onClick(View v) {
		RequestContentTemplate reqContent;
		HttpRequestEntity httpRequestEntity;
		switch (v.getId()) {
		case R.id.btn_regist:
			String mobile = editRegistMobile.getText().toString();
			String passwordRegist = editRegistPassword.getText().toString();
			String verify = editViewVerify.getText().toString();
			if ("".equals(mobile) || mobile == null
					|| "".equals(passwordRegist) || passwordRegist == null
					|| "".equals(verify) || verify == null) {
				Toast.makeText(getApplicationContext(), "手机号、密码和验证码都不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (!isMobileNO(mobile)) {
				Toast.makeText(getApplicationContext(), "请输入正确的手机号码",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (!isRightPwd(passwordRegist)) {
				Toast.makeText(getApplicationContext(), "请输入数字或字母6-20位的密码",
						Toast.LENGTH_SHORT).show();
				return;
			}

			// HEARD
			reqContent = new RequestContentTemplate();
			reqContent.setEncryptoType(CryptoTyepEnum.aes);// 请求使用AES加密

			// BODY
			reqContent.appendData(
					Contants.PROTOCOL_REQ_BODY_DATA.mobilePhone.name(), mobile);
			reqContent.appendData(
					Contants.PROTOCOL_REQ_BODY_DATA.password.name(),
					AlgorithmUtil.SHA1(passwordRegist));
			reqContent.appendData(
					Contants.PROTOCOL_REQ_BODY_DATA.rePassword.name(),
					AlgorithmUtil.SHA1(passwordRegist));
			reqContent.appendData(
					Contants.PROTOCOL_REQ_BODY_DATA.captchas.name(), verify);

			// SEND
			httpRequestEntity = new HttpRequestEntity(reqContent,
					Contants.SERVER_HOST,
					Contants.PROTOCOL_COMMAND.REGIST.getValue());
			httpRequestEntity.setRequestCode(TAG_REGIST);
			httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);// 返回使用AES密钥解密
			httpAsyncTask = new HttpAsyncTask(v.getContext(), this);
			httpAsyncTask.execute(httpRequestEntity);
			break;
		case R.id.btn_login:
			String username = editViewLoginUsername.getText().toString();
			String password = editViewLoginPassword.getText().toString();
			if ("".equals(username) || username == null || "".equals(password)
					|| password == null) {
				Toast.makeText(getApplicationContext(), "用户名密码都不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}

			login(username, password);

			break;
		case R.id.regist_code_button:
			// 获取验证码
			String captchas_mobile = editRegistMobile.getText().toString();
			if ("".equals(captchas_mobile) || captchas_mobile == null) {
				Toast.makeText(getApplicationContext(), "请输入手机号码",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (!isMobileNO(captchas_mobile)) {
				Toast.makeText(getApplicationContext(), "请输入正确的手机号码",
						Toast.LENGTH_SHORT).show();
				return;

			}
			normalTime.start();

			// HEARD
			reqContent = new RequestContentTemplate();
			reqContent.setEncryptoType(CryptoTyepEnum.aes);// 请求使用AES加密

			// BODY
			reqContent.appendData(
					Contants.PROTOCOL_REQ_BODY_DATA.mobilePhone.name(),
					captchas_mobile);

			// SEND
			httpRequestEntity = new HttpRequestEntity(reqContent,
					Contants.SERVER_HOST,
					Contants.PROTOCOL_COMMAND.CAPTCHAS.getValue());
			httpRequestEntity.setRequestCode(TAG_CAPTCHAS);
			httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);// 返回使用AES密钥解密
			httpAsyncTask = new HttpAsyncTask(v.getContext(), this);
			httpAsyncTask.execute(httpRequestEntity);

			Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
			break;
		case R.id.txt_login_retrievepassword:
			Intent intent = new Intent(this, RetrievepasswordActivity.class);
			startActivity(intent);
		default:
			break;
		}

	}

	/**
	 * http回调
	 *
	 * @param responseContent
	 */
	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT)
					.show();
		} else if (!"000000".equals(rtnCode)) {
			String rtnMsg = (String) responseContent
					.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
			Toast.makeText(getApplicationContext(), rtnMsg, Toast.LENGTH_SHORT)
					.show();
		} else {
			if (responseContent.getResponseCode().equals(TAG_LOGIN)) {

				Object data = responseContent.getData();
				if (data != null && !"".equals(data) && data instanceof String) {
					String ticket = (String) data;
					UserSession.getInstance().setAccessTicket(ticket);
					UserSession.getInstance().setLogin(true);

					// 记录账号密码
					PreferencesUtils.getInstance().saveString(
							this.getBaseContext(),
							Contants.PREFERENCES_KEY.usn.name(), username);
					PreferencesUtils.getInstance().saveString(
							this.getBaseContext(),
							Contants.PREFERENCES_KEY.psw.name(), password);

					Intent intent = new Intent(this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(this, "服务器异常，请稍后尝试。", Toast.LENGTH_SHORT)
							.show();
				}
			} else if (responseContent.getResponseCode().equals(TAG_REGIST)) {
				if ((Boolean) responseContent.getData()) {
					Toast.makeText(getApplicationContext(), "注册成功",
							Toast.LENGTH_SHORT).show();
					String mobile = editRegistMobile.getText().toString();
					String passwordRegist = editRegistPassword.getText()
							.toString();

					login(mobile, passwordRegist);
				}
			} else if (responseContent.getResponseCode().equals(TAG_LOGIN_AUTO)) {
				Object data = responseContent.getData();
				if (data != null && !"".equals(data) && data instanceof String) {
					// Toast.makeText(getApplicationContext(), "自动登录成功",
					// Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			} else if (responseContent.getResponseCode().equals(TAG_CAPTCHAS)) {
				Toast.makeText(getApplicationContext(), "发送验证码成功",
						Toast.LENGTH_SHORT).show();
			}else if (responseContent.getResponseCode().equals(
					GET_AGAIN_BASICINFO)) {

				// 获取用户主表信息
				UserBasic userBasicInfo = (UserBasic) BeansUtil.map2Bean(
						(Map<String, String>) responseContent.getData(),
						UserBasic.class);
				UserSession.getInstance().setUserBasic(userBasicInfo);
				String url = UserSession.getInstance().getUserBasic().getHeadphoto();				
				if (url != null && !url.trim().equals(""))
					ImageLoader.getInstance().displayImage(
							UserSession.getInstance().getImageHost() + url,
							head_default, MyApplication.getInstance().getSimpleOptions());
				else
					head_default.setImageResource(R.drawable.default_header);
			}
		}

	}

	protected void login(String us, String pwd) {

		username = us;
		password = pwd;

		String password_encode = AlgorithmUtil.SHA1(pwd);

		// HEARD
		RequestContentTemplate reqContentlogin = new RequestContentTemplate();
		reqContentlogin.setEncryptoType(CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContentlogin.setRequestTicket(false);
		reqContentlogin.appendData(
				Contants.PROTOCOL_REQ_BODY_DATA.loginName.name(), us);
		reqContentlogin.appendData(
				Contants.PROTOCOL_REQ_BODY_DATA.password.name(),
				password_encode);

		// SEND
		HttpRequestEntity httpRequestEntitylogin = new HttpRequestEntity(
				reqContentlogin, Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.LOGIN.getValue());
		httpRequestEntitylogin.setRequestCode(TAG_LOGIN);
		httpRequestEntitylogin.setResponseDecryptoType(CryptoTyepEnum.aes);// 返回使用AES密钥解密
		httpAsyncTask = new HttpAsyncTask(this, LoginActivity.this);
		httpAsyncTask.execute(httpRequestEntitylogin);
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

	private void initApp() {
		MyApplication.setInitContext(this);
		CommonUtil.initUUID(this);
		CommonUtil.initAppVersion(this);
		AppDeamon.getInstance().start(MyApplication.getInitContext());
		BaiduLocationClient.getInstance().init(MyApplication.getInitContext());
	}

	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		 CloudPushService pushService = AlibabaSDK
 				.getService(CloudPushService.class);
         pushService.unbindAccount();
         finish();
         System.exit(0);
	}
	
	
	public void makePersonInforRequest(String requestcode) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(),
				UserSession.getInstance().getAccessTicket());
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY.data.name(), "");

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_PERSON_INFO.getValue());
		httpRequestEntity.setRequestCode(requestcode);
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);// 返回使用AES密钥解密

		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(this, this);
		httpAsyncTask.execute(httpRequestEntity);
	}
}
