package com.hdzx.tenement.mine.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.vo.MediaDataHolder;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.photo.Configs;
import com.hdzx.tenement.photo.FileTools;
import com.hdzx.tenement.photo.PictureUtil;
import com.hdzx.tenement.photo.SelectHeadTools;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.CalendarUtils;
import com.hdzx.tenement.utils.CommonUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.widget.TenDatePicker;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MinePersonInfoActivity extends Activity implements
		IContentReportor {
	private static final int ACTIVITY_REQUEST_CODE_IMAGE = 100;

	private static final int ACTIVITY_REQUEST_CODE_ = 101;

	private static final int ACTIVITY_REQUEST_CODE_RESIZE = 102;

	private static final int ACTIVITY_REQUEST_CODE_NICK = 103;

	private static final int ACTIVITY_REQUEST_CODE_SEX = 104;

	private static final int ACTIVITY_REQUEST_CODE_SIGN = 105;

	private static final int ACTIVITY_REQUEST_CODE_AUTH = 106;

	private static final int ACTIVITY_REQUEST_CODE_MOBILE = 107;

	private static final String IMAGE_FILE_NAME = "header.jpg";

	private static final String REQUEST_CODE_PUT_HEADER_IMAGE = "request_code_put_header_image";

	private static final String REQUEST_CODE_HEADER_IMAGE = "request_code_header_image";

	UserBasic userBasic = null;

	private ImageView backIv = null;

	private View headerLayout = null;

	private PopupWindow popup = null;

	private HttpAsyncTask headerImageTask = null;

	private ImageView headerImageView = null;

	private View birthdayLayout = null;

	private View nickLayout = null;

	private View sign_layout = null;

	private View nameLayout = null;

	private View viewInterval1 = null;

	private View sexLayout = null;

	private View viewInterval2 = null;

	private View certNoLayout = null;

	private View authorizenLayout = null;

	private TextView tv_authorizen = null;

	private TextView birthdayTV = null;

	private TenDatePicker tenDatePicker = null;

	private TextView nickTv = null;

	private TextView signatrueTv = null;

	private TextView phoneTv = null;

	private TextView nameTv = null;

	private TextView sexTv = null;

	private TextView certNoTv = null;

	private ProgressDialog progressDialog = null;

	private static final String SET_BIRTHDAY = "birthday";

	private MediaDataHolder mediaDataHolder = null;

	private LinearLayout lay_mobile;
	
	String photo = Environment.getExternalStorageDirectory()
			+ "/" + IMAGE_FILE_NAME;
	
	private Uri photoUri = null;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null && intent.getSerializableExtra("UserBasic") != null) {
			userBasic = (UserBasic) intent.getSerializableExtra("UserBasic");
			setContentView(R.layout.tenement_main_mine_person_info);

			initView();
			initAuthorizenView();
			initPopupWindow();
		} else {
			finish();
		}
	}

	private void initView() {
		backIv = (ImageView) findViewById(R.id.back_iv);
		backIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		headerLayout = this.findViewById(R.id.header_layout);
		headerLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				if (popup == null) {
//					initPopupWindow();
//				}
//				WindowManager.LayoutParams params = getWindow().getAttributes();
//				params.alpha = 0.5f;
//				getWindow().setAttributes(params);
//				popup.setOutsideTouchable(true);
//
//				popup.showAtLocation(headerLayout, Gravity.BOTTOM
//						| Gravity.LEFT, 0, 0);
				
				
				setImagePhoto();
			}
		});

		nickLayout = this.findViewById(R.id.nick_layout);
		nickLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MinePersonInfoActivity.this,
						MinePersonInfoNickActivity.class);
				intent.putExtra("nick", userBasic.getNickerName());
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_NICK);
			}
		});

		sign_layout = this.findViewById(R.id.sign_layout);
		sign_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MinePersonInfoActivity.this,
						MinePersonInfoSignActivity.class);
				intent.putExtra("sign", userBasic.getSign());
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_SIGN);
			}
		});

		birthdayLayout = this.findViewById(R.id.birthday_layout);
		birthdayLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				initTenDatePicker();
				WindowManager.LayoutParams params = getWindow().getAttributes();
				params.alpha = 0.5f;
				getWindow().setAttributes(params);
				tenDatePicker.showAtLocation(birthdayLayout, Gravity.BOTTOM, 0,
						0);
			}
		});

		lay_mobile = (LinearLayout) findViewById(R.id.lay_mobile);
//		lay_mobile.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(MinePersonInfoActivity.this,
//						MinePersonInfoMobileActivity.class);
//				intent.putExtra("mobile", userBasic.getMobilePhone());
//				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_MOBILE);
//			}
//		});

		nameLayout = this.findViewById(R.id.name_layout);

		viewInterval1 = this.findViewById(R.id.tableRow_interval1);

		sexLayout = this.findViewById(R.id.sex_layout);
		sexLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MinePersonInfoActivity.this,
						MinePersonInfoSexActivity.class);
				intent.putExtra("sexCode", userBasic.getSex());
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_SEX);
			}
		});

		viewInterval2 = this.findViewById(R.id.tableRow_interval2);

		certNoLayout = this.findViewById(R.id.cert_no_layout);

		authorizenLayout = this.findViewById(R.id.authorizen_layout);
		tv_authorizen = (TextView) findViewById(R.id.tv_authorizen);

		// 头像
		headerImageView = (ImageView) this.findViewById(R.id.header_imageView);
		displayImage(UserSession.getInstance().getImageHost() + userBasic.getHeadphoto());

		// 昵称
		nickTv = (TextView) this.findViewById(R.id.nick_tv);
		nickTv.setText(userBasic.getNickerName());

		// 生日
		birthdayTV = (TextView) this.findViewById(R.id.birthday_tv);

		if (userBasic.getBirthday() != null)
			birthdayTV.setText(userBasic.getBirthday().substring(0, 10));

		// 签名
		signatrueTv = (TextView) this.findViewById(R.id.signature_tv);
		String sign = userBasic.getSign();
		if (sign == null || "".equals(sign.trim())) {
			sign = "这个家伙很懒惰，他什么话也没说！";
			signatrueTv.setText(sign);
		} else {
			signatrueTv.setText(sign);
		}

		// 电话
		phoneTv = (TextView) this.findViewById(R.id.phone_tv);
		phoneTv.setText(CommonUtil.hiden4CharBefor4(userBasic.getMobilePhone(),
				"*"));

		// 姓名
		nameTv = (TextView) this.findViewById(R.id.name_tv);
		nameTv.setText(userBasic.getRealName());

		// 性别
		sexTv = (TextView) this.findViewById(R.id.sex_tv);
		String sex = CommonUtil.getSexFromCode(userBasic.getSex());
		if (sex != null) {
			sexTv.setText(sex);
		}

		// 证件号码
		certNoTv = (TextView) this.findViewById(R.id.cert_no_tv);
		certNoTv.setText(CommonUtil.hiden4CharBefor4(userBasic.getIdcard(), "*"));

		authorizenLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MinePersonInfoActivity.this,
						MinePersonInfoAuthActivity.class);
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_AUTH);
			}
		});

	}

	private void initAuthorizenView() {
		// 审核状态:1-审核中，0-审核不通过，2-审核通过
		if ("2".equals(userBasic.getLevel())) {
			nameLayout.setVisibility(View.VISIBLE);
			viewInterval1.setVisibility(View.VISIBLE);
			sexLayout.setVisibility(View.VISIBLE);
			viewInterval2.setVisibility(View.VISIBLE);
			certNoLayout.setVisibility(View.VISIBLE);
			authorizenLayout.setVisibility(View.GONE);
		} else if ("1".equals(userBasic.getLevel())) {
			nameLayout.setVisibility(View.GONE);
			viewInterval1.setVisibility(View.GONE);
			sexLayout.setVisibility(View.GONE);
			viewInterval2.setVisibility(View.GONE);
			certNoLayout.setVisibility(View.GONE);
			authorizenLayout.setVisibility(View.VISIBLE);
			tv_authorizen.setText("实名审核中");
			authorizenLayout.setEnabled(false);
		} else {
			nameLayout.setVisibility(View.GONE);
			viewInterval1.setVisibility(View.GONE);
			sexLayout.setVisibility(View.GONE);
			viewInterval2.setVisibility(View.GONE);
			certNoLayout.setVisibility(View.GONE);
			authorizenLayout.setVisibility(View.VISIBLE);
		}
	}

	private void initTenDatePicker() {
		if (tenDatePicker == null) {
			tenDatePicker = new TenDatePicker(MinePersonInfoActivity.this);
			tenDatePicker.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					if (tenDatePicker != null) {
						// 设置透明度（这是窗体本身的透明度，非背景）
						// alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
						WindowManager.LayoutParams params = getWindow()
								.getAttributes();
						params.alpha = 1.0f;
						getWindow().setAttributes(params);
						tenDatePicker.dismiss();
					}
				}
			});

			tenDatePicker.setLeftListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (tenDatePicker != null) {
						tenDatePicker.dismiss();
					}
				}
			});

			tenDatePicker.setRightListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (tenDatePicker != null) {
						Calendar calendar = tenDatePicker.getPickedCalendar();
						DateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String dateStr = format.format(calendar.getTime());
						
						
						Calendar todayCalendar = Calendar.getInstance();
						if(calendar.after(todayCalendar)){
							
							Toast.makeText(MinePersonInfoActivity.this, "所选日期不能比当前时间晚", Toast.LENGTH_SHORT).show();
							return;
						}
						
						birthdayTV.setText(dateStr.substring(0, 10));
						makeUpdateBirthdayRequest(calendar.getTimeInMillis()+"");

						tenDatePicker.dismiss();
					}
				}
			});
		}

	}

	protected void initPopupWindow() {
		View view = getLayoutInflater().inflate(
				R.layout.tenement_main_image_selector, null);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);

		popup = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popup.setBackgroundDrawable(new PaintDrawable(0));

		popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				if (popup != null) {
					// 设置透明度（这是窗体本身的透明度，非背景）
					// alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
					WindowManager.LayoutParams params = getWindow()
							.getAttributes();
					params.alpha = 1.0f;
					getWindow().setAttributes(params);
					popup.dismiss();
				}
			}
		});

		Button taking_pictures = (Button) view
				.findViewById(R.id.taking_pictures);
		Button gallery = (Button) view.findViewById(R.id.select_from_photo);
		Button cancle = (Button) view.findViewById(R.id.add_cancel);
		taking_pictures.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				set_paizhaoshangchuan(v);
				popup.dismiss();
			}
		});

		gallery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 相册
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent,
						ACTIVITY_REQUEST_CODE_IMAGE);
				popup.dismiss();
			}
		});

		cancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				popup.dismiss();
			}
		});
	}
	

	/**
	 * 拍照上传
	 */
	public void set_paizhaoshangchuan(View v) {

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
			startActivityForResult(intentFromCapture, ACTIVITY_REQUEST_CODE_);
		} catch (Exception e) {

			Toast.makeText(this, "sdcard无效或没有插入!",
					Toast.LENGTH_SHORT).show();
		}

	}
	/**
	 * 裁剪图片
	 * 
	 * @param data
	 */
//	private void resizeImage(Uri data) {
		
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(data, "image/*");
//		// crop为true是设置在开启的intent中设置显示的view可以剪裁
//        intent.putExtra("crop", "true");
//		intent.putExtra("crop", "true");
//		// aspectX aspectY 是宽高的比例
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputX", 150);
//		intent.putExtra("outputY", 150);
////		intent.putExtra("return-data", false);// 不返回缩略图
//		intent.putExtra("return-data", true);
//		intent.putExtra("scale", true);// 缩放
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//		startActivityForResult(intent, ACTIVITY_REQUEST_CODE_RESIZE);
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("gl", "onActivityResult");
			Cursor cursor;
			switch (requestCode) {
//			case ACTIVITY_REQUEST_CODE_IMAGE:
//				if (data != null) {
//				cursor = this.getContentResolver().query(data.getData(), null,
//						null, null, null);
//				if (cursor.moveToNext()) {
//					String strImagePath = cursor.getString(cursor
//							.getColumnIndex(MediaStore.Images.Media.DATA));
//					createImageMediaData(Uri.fromFile(new File(strImagePath)),
//							false);
//					// resizeImage(data.getData());
//					displayImage(strImagePath);
//					makeUploadFileRequest();
//				}
//				}
//				break;
//
//			case ACTIVITY_REQUEST_CODE_:
//				if (Environment.getExternalStorageState().equals(
//						Environment.MEDIA_MOUNTED)) {
//					createImageMediaData(Uri.fromFile(new File(photo)),
//							false);
//					// resizeImage(data.getData());
//					displayImage(photo);
//					makeUploadFileRequest();
//				} else {
//					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
//							.show();
//				}
//				break;
//
//			case ACTIVITY_REQUEST_CODE_RESIZE:
//				if (data != null) {
//					// Log.v("gl", "data=="+data.getStringExtra("output"));
//					createImageMediaData(Uri.parse(data
//							.getStringExtra(MediaStore.EXTRA_OUTPUT)), false);
//					makeUploadFileRequest();
//					// makeUploadRequest(data);
//				}
//				break;
			//------------------------------------------------------------------------------------------				
			case Configs.SystemPicture.PHOTO_REQUEST_TAKEPHOTO:
				if (photoUri != null) {
					SelectHeadTools.startPhotoZoom(this, photoUri, 200,Configs.SystemPicture.CROP_PIC_NAME);
				} else {
					try {
						photoUri = FileTools.getUriByFileDirAndFileName(
								Configs.SystemPicture.SAVE_DIRECTORY,
								Configs.SystemPicture.SAVE_PIC_NAME);

						SelectHeadTools.startPhotoZoom(this, photoUri, 200,Configs.SystemPicture.CROP_PIC_NAME);
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
				if (photoUri != null) {
					
					Log.v("gl", "photoUri"+photoUri);
					File file = FileTools.getFileByUri(this, photoUri);
					
					Uri fileUri = Uri.fromFile(file);
					Log.v("gl", "fileUri"+fileUri);
					SelectHeadTools.startPhotoZoom(this, fileUri, 200,Configs.SystemPicture.CROP_PIC_NAME);
				}
				break;
			case Configs.SystemPicture.PHOTO_REQUEST_CUT:
				
				if (data == null)
					return;
				Bitmap bit = data.getExtras().getParcelable("data");
				headerImageView.setImageBitmap(bit);
				
				if (photoUri != null) {
					File file = FileTools.getFileByUri(this, photoUri);
					
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
				
				makeUploadFileRequest();
				break;
				
				
			case ACTIVITY_REQUEST_CODE_NICK:
				if (data != null) {
					String nick = data.getStringExtra("nick");
					if (nick != null) {
						userBasic.setNickerName(nick);
						nickTv.setText(nick);
					}
				}
				break;
			case ACTIVITY_REQUEST_CODE_SIGN:
				if (data != null) {
					String sign = data.getStringExtra("sign");
					if (sign != null) {
						userBasic.setSign(sign);
						signatrueTv.setText(sign);
					}
				}
				break;

			case ACTIVITY_REQUEST_CODE_SEX:
				if (data != null) {
					String code = data.getStringExtra("sexCode");
					if (code != null) {
						userBasic.setSex(code);
						String sex = CommonUtil.getSexFromCode(code);
						if (sex != null) {
							sexTv.setText(sex);
						}
					}
				}
				break;
			case ACTIVITY_REQUEST_CODE_AUTH:
				if (data != null) {
					String code = data.getStringExtra("auth");
					if (code != null) {
						userBasic.setLevel(code);
						initAuthorizenView();
					}
				}
				break;
			case ACTIVITY_REQUEST_CODE_MOBILE:
				if (data != null) {
					String code = data.getStringExtra("mobile");
					if (code != null) {
						userBasic.setMobilePhone(code);
						phoneTv.setText(CommonUtil.hiden4CharBefor4(code, "*"));
					}
				}
				break;
			}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void createImageMediaData(Uri uri, boolean isDelete) {
		if (mediaDataHolder == null) {
			mediaDataHolder = new MediaDataHolder();
		}

		mediaDataHolder.setType(Contants.MEDIA_TYPE.IMAGE);
		mediaDataHolder.setUri(uri);
		mediaDataHolder.setDelete(isDelete);
	}

	private void deleteMediaDataHolder() {
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

	private void displayImage(String url) {
		Log.v("gl", "url"+url);
		
		if (mediaDataHolder != null) {
			Log.v("gl", "mediaDataHolder != null");
			
			if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.IMAGE) {
				
				Log.v("gl", "path=="+mediaDataHolder.getUri().toString());
				
				ImageLoader.getInstance().displayImage(
						mediaDataHolder.getUri().toString(), headerImageView,
						MyApplication.getInstance().getSimpleOptions());
			}
		} else {
			Log.v("gl", "mediaDataHolder = null");
			if (url != null && !url.trim().equals(""))
				ImageLoader.getInstance().displayImage(url, headerImageView,
						MyApplication.getInstance().getSimpleOptions());
			else
				headerImageView.setImageResource(R.drawable.default_header);
		}
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
			} else if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.AUDIO) {
				contentType = "audio/" + suffix;
			} else if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.AUDIO) {
				if ("mp4".equalsIgnoreCase(suffix)) {
					contentType = "video/mpeg4";
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
				rp.put("use", Contants.OssFilePath.USER_HEAD.getIndex());

				RequestContentTemplate reqContent = new RequestContentTemplate();
				reqContent.setRequestTicket(true);

				HttpRequestEntity httpRequestEntity = new HttpRequestEntity(
						reqContent, Contants.SERVER_HOST,
						Contants.PROTOCOL_COMMAND.UPLOAD_FILE.getValue());
				httpRequestEntity
						.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);
				httpRequestEntity.setRequestParams(rp);
				httpRequestEntity.setRequestCode(REQUEST_CODE_HEADER_IMAGE);
				httpRequestEntity.setHasData(false);
				HttpAsyncTask task = new HttpAsyncTask(this, this);
				task.execute(httpRequestEntity);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void makeUpdateHeadPhotoRequest(String uri) {
		{
			RequestContentTemplate reqContent = new RequestContentTemplate();
			reqContent.setEncryptoType(CryptoTyepEnum.aes);

			reqContent.appendData(
					Contants.PROTOCOL_REQ_BODY_DATA.headphoto.name(), uri);
			reqContent.isRequestTicket();

			HttpRequestEntity httpRequestEntity = new HttpRequestEntity(
					reqContent, Contants.SERVER_HOST,
					Contants.PROTOCOL_COMMAND.UPDATE_PERSON_INFO.getValue());
			httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
			httpRequestEntity.setRequestCode(REQUEST_CODE_PUT_HEADER_IMAGE);
			headerImageTask = new HttpAsyncTask(this, this);
			headerImageTask.execute(httpRequestEntity);
		}
	}

	private void makeUpdateBirthdayRequest(String birthday) {
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);

		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.birthday.name(),
				birthday);
		reqContent.isRequestTicket();

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.UPDATE_PERSON_INFO.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(SET_BIRTHDAY);
		headerImageTask = new HttpAsyncTask(this, this);
		headerImageTask.execute(httpRequestEntity);
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

			if (REQUEST_CODE_PUT_HEADER_IMAGE.equals(responseContent
					.getResponseCode())) {
				Object data = responseContent.getData();
				System.out.println("data=" + data);
				if (data != null && !"".equals(data) && data instanceof String) {
					String url = (String) data;
					displayImage(url);
				}
			} else if (SET_BIRTHDAY.equals(responseContent.getResponseCode())) {
				String jsonStr = responseContent.getData().toString();
				Log.v("gl", "birthday:" + jsonStr);
			} else if (REQUEST_CODE_HEADER_IMAGE.equals(responseContent
					.getResponseCode())) {
				String data = responseContent.getData().toString();
				System.out.println("REQUEST_CODE_HEADER_IMAGE==" + data);
				data = data.replace("[", "");
				data = data.replace("]", "");
				makeUpdateHeadPhotoRequest(data);

			}
		}

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
	
	
	
	@Override
	public void onPause() {
	    super.onPause();
	    if ((progressDialog != null) && progressDialog.isShowing())
	    	progressDialog.dismiss();
	    progressDialog = null;
	}
	
}
