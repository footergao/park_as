package com.hdzx.tenement.community.ui;

import org.apache.commons.lang3.StringUtils;
import org.apache.cordova.LOG;
import org.json.JSONException;
import org.json.JSONObject;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.zxing.encode.QRCodeEncoderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OpenDoorActivity extends Activity implements IContentReportor {
	private String RQ_OPEN_DOOR = "rq_open_door";
	private HttpAsyncTask task = null;

	private ImageView big;
	private int width = 0;
	private int height = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.open_door_activity);

		big = (ImageView) findViewById(R.id.img_big_photo);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("手机开门");

		// ImageLoader.getInstance().displayImage("", big,
		// ((MyApplication)getApplication()).getDefultOptions());

		initView();
		updateQr();
	}

	private void initView() {
		WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point displaySize = new Point();
		display.getSize(displaySize);
		width = displaySize.x;
		height = displaySize.y;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_iv:
			finish();
			break;
		case R.id.submit_btn:
			updateQr();
		default:
			break;
		}
	}

	/**
	 * 初始化数据
	 */
	private void updateQr() {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.UPDATE_OPEN_DOOR.getValue());
		httpRequestEntity.setRequestCode(RQ_OPEN_DOOR);
		httpRequestEntity.setHasData(true);

		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		// TODO Auto-generated method stub
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if (StringUtils.isBlank(rtnCode)) {
			Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT)
					.show();
		} else if (!rtnCode.equals(Contants.ResponseCode.CODE_000000)) {
			String rtnMsg = (String) responseContent
					.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
			Toast.makeText(getApplicationContext(), rtnMsg, Toast.LENGTH_SHORT)
					.show();
		} else {
			if (RQ_OPEN_DOOR.equals(responseContent.getResponseCode())) {
				Object data = responseContent.getDataJson();
				try {
					JSONObject json = new JSONObject(data.toString());
					String qr = json.get("doorQrcode").toString();
					Log.v("gl", "qr=="+qr);
					Bitmap bitmap = QRCodeEncoderUtil.create2DCoderBitmap(
							qr, width, height);
					big.setImageBitmap(bitmap);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
