package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PaintDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.vo.MediaDataHolder;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.photo.Configs;
import com.hdzx.tenement.photo.FileTools;
import com.hdzx.tenement.photo.SelectHeadTools;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DongdongxiaAuthActivity extends Activity implements
		IContentReportor {

	private static final int ACTIVITY_REQUEST_CODE_IMAGE = 100;

	private static final int ACTIVITY_REQUEST_CODE_GALLERY = 101;

	private static final String IMAGE_FILE_NAME = "auth.jpg";

	private TextView titleTv = null;

	private PopupWindow imagePopupWindow = null;

	private ImageView mediaIv = null;

	private MediaDataHolder mediaDataHolder = null;

	private EditText name_text = null;

	private ProgressDialog progressDialog = null;

	private HttpAsyncTask task;

	private static final String REQUEST_CODE_IDCARD_IMAGE = "request_code_idcard_image";

	private static final String REQUEST_CODE_AUTH = "request_code_auth";

	private List<Map<String,String>> list = new ArrayList<Map<String,String>>();

	private Uri photoUri = null;
	
	//type： 001 文本 002 图片 003音频 004视频 010未知
	

	String photo = Environment.getExternalStorageDirectory()
			+ "/" + IMAGE_FILE_NAME;
	String photo_test = Environment.getExternalStorageDirectory()
			+ "/test.jpg";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.tenement_main_ddx_auth);

		initView();
	}

	private void initView() {
		initCommonView();
		displayImage();
	}

	private void initCommonView() {
		titleTv = (TextView) this.findViewById(R.id.titile_tv);
		titleTv.setText(getResources().getText(
				R.string.main_mine_person_info_title_authorizen));

		name_text = (EditText) this.findViewById(R.id.name_text);

		ImageView backIv = (ImageView) this.findViewById(R.id.back_iv);
		backIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		mediaIv = (ImageView) this.findViewById(R.id.media_iv);
		mediaIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

//				initMediaPopupWindow();
				
				setImagePhoto();
			}
		});

		mediaIv.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (mediaDataHolder == null) {
					return false;
				} else {
					new AlertDialog.Builder(DongdongxiaAuthActivity.this)
							.setMessage("是否删除？")
							.setNegativeButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											deleteMediaDataHolder();
											displayImage();
										}
									}).setPositiveButton("取消", null).show();

					return true;
				}
			}

		});

		Button submit = (Button) this.findViewById(R.id.submit_btn);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				closeInputKeyBoard();

				if (name_text.getText() != null
						&& !"".equals(name_text.getText().toString().trim())) {

					String realName = name_text.getText().toString().trim();
					
					Map map = new HashMap<>();
					map.put("expandKey", "申请咚咚侠内容");
					map.put("expandValue", realName);
					map.put("type", "001");
					
					list.add(map);

				} else {

					Toast.makeText(DongdongxiaAuthActivity.this, "请输入申请内容",
							Toast.LENGTH_SHORT).show();
					return;

				}

				if (mediaDataHolder == null) {
					Toast.makeText(DongdongxiaAuthActivity.this, "请上传正面照",
							Toast.LENGTH_SHORT).show();
					return;
				}

				makeUploadFileRequest();
			}
		});
	}


	private void displayImage() {
		if (mediaDataHolder != null) {
			if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.IMAGE) {
				ImageLoader.getInstance().displayImage(
						mediaDataHolder.getUri().toString(), mediaIv,
						MyApplication.getInstance().getSimpleOptions());
				
//				file:///storage/emulated/0/test.jpg
				Log.v("gl","photo_test:"+ photo_test);
				Log.v("gl","exists:"+ new File(photo_test).exists());
				Log.v("gl","path:"+ mediaDataHolder.getUri().toString());
			}
		} else {
				mediaIv.setImageResource(R.drawable.add_media);
		}
	}

	private void createImageMediaData(Uri uri, boolean isDelete) {
		if (mediaDataHolder == null) {
			mediaDataHolder = new MediaDataHolder();
		}

		mediaDataHolder.setType(Contants.MEDIA_TYPE.IMAGE);
		mediaDataHolder.setUri(uri);
		mediaDataHolder.setDelete(isDelete);
	}

	protected void initMediaPopupWindow() {
		if (imagePopupWindow == null) {
			initImagePopupWindow();
		}
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.alpha = 0.5f;
		getWindow().setAttributes(params);
		imagePopupWindow.setOutsideTouchable(true);

		imagePopupWindow.showAtLocation(titleTv, Gravity.BOTTOM | Gravity.LEFT,
				0, 0);
	}

	protected void initImagePopupWindow() {
		View view = getLayoutInflater().inflate(
				R.layout.tenement_main_image_selector, null);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);

		imagePopupWindow = new PopupWindow(view,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		imagePopupWindow.setBackgroundDrawable(new PaintDrawable(0));

		imagePopupWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {

					@Override
					public void onDismiss() {
						if (imagePopupWindow != null) {
							// 设置透明度（这是窗体本身的透明度，非背景）
							// alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
							WindowManager.LayoutParams params = getWindow()
									.getAttributes();
							params.alpha = 1.0f;
							getWindow().setAttributes(params);
						}
					}
				});

		Button taking_pictures = (Button) view
				.findViewById(R.id.taking_pictures);
		Button gallery = (Button) view.findViewById(R.id.select_from_photo);
		Button cancle = (Button) view.findViewById(R.id.add_cancel);
		taking_pictures.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				photo();
				imagePopupWindow.dismiss();
			}
		});

		gallery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 相册
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent,
						ACTIVITY_REQUEST_CODE_GALLERY);
				imagePopupWindow.dismiss();
			}
		});

		cancle.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				imagePopupWindow.dismiss();
			}
		});
	}

	protected void photo() {

		try {
			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// 判断存储卡是否可以用，可用进行存储
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				File file = new File(photo);

				if (file.exists()) {
					file.delete();
				}
				file = new File(photo);
				if (!file.exists()) {
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									IMAGE_FILE_NAME)));
				}
			}
			startActivityForResult(intentFromCapture, ACTIVITY_REQUEST_CODE_IMAGE);
		} catch (Exception e) {

			Toast.makeText(this, "sdcard无效或没有插入!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		} else {
			Cursor cursor;
			switch (requestCode) {
//			case ACTIVITY_REQUEST_CODE_IMAGE:
//				if (Environment.getExternalStorageState().equals(
//						Environment.MEDIA_MOUNTED)) {
//					File temp = new File(photo);
//					createImageMediaData(Uri.fromFile(temp),
//							true);
//					displayImage();
//				} else {
//					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
//							.show();
//				}
//				break;
//
//			case ACTIVITY_REQUEST_CODE_GALLERY:
//				cursor = this.getContentResolver().query(data.getData(), null,
//						null, null, null);
//				if (cursor.moveToNext()) {
//					String strImagePath = cursor.getString(cursor
//							.getColumnIndex(MediaStore.Images.Media.DATA));
//					createImageMediaData(Uri.fromFile(new File(strImagePath)),
//							false);
//					displayImage();
//				}
//				break;
				
			case Configs.SystemPicture.PHOTO_REQUEST_TAKEPHOTO:

				if (photoUri != null) {
					SelectHeadTools.startPhotoZoom(this, photoUri, 600,Configs.SystemPicture.CROP_PIC_NAME);
				} else {
					try {
						photoUri = FileTools.getUriByFileDirAndFileName(
								Configs.SystemPicture.SAVE_DIRECTORY,
								Configs.SystemPicture.SAVE_PIC_NAME);

						SelectHeadTools.startPhotoZoom(this, photoUri, 600,Configs.SystemPicture.CROP_PIC_NAME);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(this, "不能创建图片文件", Toast.LENGTH_SHORT).show();
					}
				}

				break;
			case Configs.SystemPicture.PHOTO_REQUEST_GALLERY:
				if (data == null)
					return;
				photoUri =data.getData();
				SelectHeadTools.startPhotoZoom(this, photoUri, 600,Configs.SystemPicture.CROP_PIC_NAME);
				break;
			case Configs.SystemPicture.PHOTO_REQUEST_CUT:
				if (data == null)
					return;
				Bitmap bit = data.getExtras().getParcelable("data");
				mediaIv.setImageBitmap(bit);
				
				if (photoUri != null) {
					File file = FileTools.getFileByUri(this, photoUri);
					Log.d("gl", "photoUri != null");
					
					Uri fileUri = Uri.fromFile(file);
					createImageMediaData(fileUri,false);
				} else {
					try {
						photoUri = FileTools.getUriByFileDirAndFileName(
								Configs.SystemPicture.SAVE_DIRECTORY,
								Configs.SystemPicture.SAVE_PIC_NAME);

						File file = FileTools.getFileByUri(this, photoUri);
						Log.d("File", file.toString());
						
						Uri fileUri = Uri.fromFile(file);
						createImageMediaData(fileUri,false);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(this, "不能创建图片文件", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void deleteMediaDataHolder() {
		
		Log.v("gl", "deleteMediaDataHolder");
		if (mediaDataHolder != null) {
			if (mediaDataHolder.isDelete()) {
				File f = new File(mediaDataHolder.getUri().getPath());
				if (f.exists()) {
					f.delete();
				}
			}
			mediaDataHolder = null;
		}
	}

	public void onBackPressed() {
		deleteMediaDataHolder();
		super.onBackPressed();
	}

	/**
	 * 上传文件
	 */
	private void makeUploadFileRequest() {
		if (mediaDataHolder != null && mediaDataHolder.getUri() != null) {
			String filePath = mediaDataHolder.getUri().getPath();
			int index = filePath.lastIndexOf(".");
			String suffix = filePath.substring(index + 1);
			File f = new File(filePath);
			String fileName = f.getName();
			String contentType = null;
			if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.IMAGE) {
				if ("jpg".equalsIgnoreCase(suffix)
						|| "jpe".equalsIgnoreCase(suffix)
						|| "jpeg".equalsIgnoreCase(suffix)) {
					contentType = "image/jpeg";
				} else {
					contentType = "image/" + suffix;
				}
			}
			try {
				RequestParams rp = new RequestParams();
				rp.put(Contants.PROTOCOL_REQ_HEAD.sessionid.name(), UserSession
						.getInstance().getSessionId());
				String secret = AESUtils.encrypt(UserSession.getInstance()
						.getAesKey(), UserSession.getInstance().getSessionId());
				rp.put("secret", secret);
				rp.put("myfile", f, contentType, fileName);
				rp.put("use", Contants.OssFilePath.USER_HAIMAN.getIndex());

				RequestContentTemplate reqContent = new RequestContentTemplate();
				reqContent.setRequestTicket(true);

				HttpRequestEntity httpRequestEntity = new HttpRequestEntity(
						reqContent, Contants.SERVER_HOST,
						Contants.PROTOCOL_COMMAND.UPLOAD_FILE.getValue());
				httpRequestEntity
						.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);
				httpRequestEntity.setRequestParams(rp);
				httpRequestEntity.setHasData(false);
				httpRequestEntity.setRequestCode(REQUEST_CODE_IDCARD_IMAGE);
				task = new HttpAsyncTask(this, this);
				task.execute(httpRequestEntity);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void makeUpdateAuthRequest(String idcardUrl, List<Map<String,String>> list) {
		{
			// HEARD
			RequestContentTemplate reqContent = new RequestContentTemplate();
			reqContent.setEncryptoType(CryptoTyepEnum.aes);// 请求使用AES加密
			reqContent.setRequestTicket(true);
			// BODY

			reqContent.appendData(Contants.PROTOCOL_RESP_BODY.expandPro.name(),
					list);
			reqContent.appendData(Contants.PROTOCOL_RESP_BODY.haimanPhoto.name(),
					idcardUrl);

			// SEND
			HttpRequestEntity httpRequestEntity = new HttpRequestEntity(
					reqContent, Contants.SERVER_HOST,
					Contants.PROTOCOL_COMMAND.BUILD_DDX.getValue());
			httpRequestEntity.setRequestCode(REQUEST_CODE_AUTH);
			httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);// 返回使用AES密钥解密

			task = new HttpAsyncTask(this, this);
			task.execute(httpRequestEntity);
		}
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if (rtnCode == null || "".equals(rtnCode)) {
			Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT)
					.show();
		} else if (!rtnCode.equals(Contants.ResponseCode.CODE_000000)) {
			String rtnMsg = (String) responseContent
					.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
			Toast.makeText(getApplicationContext(), rtnMsg, Toast.LENGTH_SHORT)
					.show();
		} else {

			if (REQUEST_CODE_IDCARD_IMAGE.equals(responseContent
					.getResponseCode())) {

				String data = responseContent.getData().toString();
				data = data.replace("[", "");
				data = data.replace("]", "");
				makeUpdateAuthRequest(data, list);

			} else if (REQUEST_CODE_AUTH.equals(responseContent
					.getResponseCode())) {

				finish();

			}
		}

	}

	
	private void closeInputKeyBoard() {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getCurrentFocus()
				.getApplicationWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    if ((progressDialog != null) && progressDialog.isShowing())
	    	progressDialog.dismiss();
	    progressDialog = null;
	}
	
	protected void setImagePhoto(){
		if (!FileTools.hasSdcard()) {
			Toast.makeText(this, "不存在SD卡", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			photoUri = FileTools.getUriByFileDirAndFileName(
					Configs.SystemPicture.SAVE_DIRECTORY,
					Configs.SystemPicture.SAVE_PIC_NAME);
		} catch (IOException e) {
			Toast.makeText(this, "不能创建图片文件", Toast.LENGTH_SHORT).show();
			return;
		}
		SelectHeadTools.openDialog(this, photoUri);
	}
	
}
