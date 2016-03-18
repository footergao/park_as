package com.hdzx.tenement.community.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.vo.MediaDataHolder;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.photo.Configs;
import com.hdzx.tenement.photo.FileTools;
import com.hdzx.tenement.photo.SelectHeadTools;
import com.hdzx.tenement.ui.common.RecordActivity;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.CloseKeyBoard;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.widget.FixGridLayout;
import com.loopj.android.http.RequestParams;

public class CommunitySuggestReplyActivity extends Activity implements
		IContentReportor {

	private static final int REQUEST_CODE_TAKE_VOICE = 104;

	private TextView titleTv = null;

	private PopupWindow audioAndPicPopupWindow = null;

	private ImageView mediaIv = null;

	private MediaPlayer mediaPlayer = null;

	private EditText contentText = null;

	// zhaopian&audio
	private Uri photoUri = null;
	private String photoName = "";
	private FixGridLayout lay_pic;
	private List<MediaDataHolder> list_media;// 图片的sd卡存储路径
	private static int SIZE = 200;// tupian-chicun
	private LinearLayout ll_audio;
	private String voicePath;
	private String REQUEST_UPLOADE_FILE = "request_uploade_file";

	//
	private TextView tv_msg;
	private String RQ_SEND_MSG = "rq_send_msg";
	HttpAsyncTask task;
	String replayType="";
	String relationId="";
	String msgType="";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("gl", "onCreate");

		setContentView(R.layout.common_media_layout);

		initCommonView();
	}

	private void initCommonView() {
		
		Intent intent = getIntent();
		replayType = intent.getStringExtra("replayType");
		relationId = intent.getStringExtra("relationId");
		msgType = intent.getStringExtra("msgType");
		
		
		double rId = Double.parseDouble(relationId);
		int id = (int) rId;
		relationId = id+"";
		
		
		
		titleTv = (TextView) this.findViewById(R.id.titile_tv);
		titleTv.setText("回复");

		ll_audio = (LinearLayout) findViewById(R.id.ll_audio);
		ll_audio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				playAudio();
			}
		});

		contentText = (EditText) this.findViewById(R.id.context_text);

		ImageView backIv = (ImageView) this.findViewById(R.id.back_iv);
		backIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		list_media = new ArrayList<MediaDataHolder>();
		mediaIv = (ImageView) this.findViewById(R.id.media_iv);
		mediaIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setImagePhoto();
			}
		});

		// 初始化layout
		lay_pic = (FixGridLayout) findViewById(R.id.lay_pic);

//		ViewGroup.LayoutParams params = lay_pic.getLayoutParams();
//		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//		params.height = SIZE * 4;
//		lay_pic.requestLayout();

		Button submit = (Button) this.findViewById(R.id.submit_btn);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (contentText.getText() != null
						&& !"".equals(contentText.getText().toString().trim())) {

					if (list_media != null && list_media.size() > 0) {

						makeUploadFileRequest();

					} else {

						sendReply("", contentText.getText().toString());

					}
				} else {
					Toast.makeText(CommunitySuggestReplyActivity.this, "请输入内容",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
	}

	private void createImageMediaData(Uri uri, boolean isDelete) {
		MediaDataHolder	mediaDataHolder = new MediaDataHolder();

		mediaDataHolder.setType(Contants.MEDIA_TYPE.IMAGE);
		mediaDataHolder.setUri(uri);
		mediaDataHolder.setDelete(isDelete);

		list_media.add(mediaDataHolder);
	}

	private void createAudioMediaData(Uri uri) {
		MediaDataHolder	mediaDataHolder = new MediaDataHolder();

		mediaDataHolder.setType(Contants.MEDIA_TYPE.AUDIO);
		mediaDataHolder.setUri(uri);
		mediaDataHolder.setDelete(true);

		list_media.add(mediaDataHolder);

		ll_audio.setVisibility(View.VISIBLE);
	}

	protected void initAudioAndPicPopupWindow() {

		View view = getLayoutInflater().inflate(R.layout.release_media, null);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);

		audioAndPicPopupWindow = new PopupWindow(view,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, true);

		Button btn_audio = (Button) view.findViewById(R.id.btn_audio);
		Button btn_close_dialog = (Button) view
				.findViewById(R.id.btn_close_dialog);
		Button btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
		Button btn_gallery_photo = (Button) view
				.findViewById(R.id.btn_gallery_photo);

		btn_take_photo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(alertMost())
					return;
				audioAndPicPopupWindow.dismiss();
				SelectHeadTools.startCamearPicCut(
						CommunitySuggestReplyActivity.this, photoUri);
			}
		});

		btn_gallery_photo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(alertMost())
					return;
				// 相册
				audioAndPicPopupWindow.dismiss();
				SelectHeadTools
						.startImageCaptrue(CommunitySuggestReplyActivity.this);
			}
		});

		btn_close_dialog.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				audioAndPicPopupWindow.dismiss();
			}
		});
		btn_audio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// // TODO Auto-generated method stub
				audioAndPicPopupWindow.dismiss();

				Intent intent = new Intent();
				intent.setClass(CommunitySuggestReplyActivity.this,
						RecordActivity.class);
				startActivityForResult(intent, REQUEST_CODE_TAKE_VOICE);// 语音
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		} else {
			switch (requestCode) {

			case Configs.SystemPicture.PHOTO_REQUEST_TAKEPHOTO:

				if (isNotPhotoUriNull()) {

					SelectHeadTools.startPhotoZoom(this, photoUri, SIZE,
							"crop_" + photoName);
				} else {
					getPhotoStrAgain();
					SelectHeadTools.startPhotoZoom(this, photoUri, SIZE,
							"crop_" + photoName);
				}
				break;
			case Configs.SystemPicture.PHOTO_REQUEST_GALLERY:
				if (data == null)
					return;
				photoUri = data.getData();
				
				for(MediaDataHolder media:list_media){
					
					File file = FileTools.getFileByUri(this, photoUri);
					Uri fileUri = Uri.fromFile(file);
					if(fileUri.equals(media.getUri())){
						Toast.makeText(this, "您已经添加了该文件", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				
				SelectHeadTools.startPhotoZoom(this, photoUri, SIZE, "crop_"
						+ photoName);
				break;
			case Configs.SystemPicture.PHOTO_REQUEST_CUT:
				if (data == null)
					return;
				Bitmap bit = data.getExtras().getParcelable("data");

				ImageView img = new ImageView(this);
				img.setTag("crop_" + photoName);
				img.setLayoutParams(new LayoutParams(SIZE, SIZE));
				img.setImageBitmap(bit);

				lay_pic.removeView(mediaIv);
				lay_pic.addView(img);
				lay_pic.addView(mediaIv);

				if (isNotPhotoUriNull()) {
					File file = FileTools.getFileByUri(this, photoUri);
					Uri fileUri = Uri.fromFile(file);
					createImageMediaData(fileUri, false);
					imgAction(img, fileUri);
				} else {
					getPhotoStrAgain();
					File file = FileTools.getFileByUri(this, photoUri);
					Uri fileUri = Uri.fromFile(file);
					createImageMediaData(fileUri, false);
					imgAction(img, fileUri);
				}

				break;

			case REQUEST_CODE_TAKE_VOICE:// 语音
				MediaDataHolder mediaDataHolder = (MediaDataHolder) data
						.getParcelableExtra("dataHolder");
				voicePath = data.getStringExtra("path");
				Log.v("gl", "voiceHolder-->" + mediaDataHolder.toString());
				createAudioMediaData(mediaDataHolder.getUri());
				break;

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void imgAction(final ImageView img, final Uri fileUri) {
		// TODO Auto-generated method stub
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		img.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				new AlertDialog.Builder(CommunitySuggestReplyActivity.this)
						.setMessage("是否删除？")
						.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										lay_pic.removeView(img);

										for (MediaDataHolder media : list_media) {
											if (fileUri.equals(media.getUri()))
												list_media.remove(media);
										}

									}
								}).setPositiveButton("取消", null).show();

				return true;
			}
		});

	}

	private void killMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
	}

	private void deleteMediaDataHolder() {
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.v("gl", "onDestroy");
		killMediaPlayer();
	}

	public void onBackPressed() {
		deleteMediaDataHolder();
		super.onBackPressed();
	}

	/**
	 * 上传文件
	 */
	private void makeUploadFileRequest() {
		if (list_media != null && list_media.size() > 0) {

			RequestParams rp = new RequestParams();
			rp.put(Contants.PROTOCOL_REQ_HEAD.sessionid.name(), UserSession
					.getInstance().getSessionId());
			String secret = AESUtils.encrypt(UserSession.getInstance()
					.getAesKey(), UserSession.getInstance().getSessionId());
			rp.put("secret", secret);
			rp.put("use",
					Contants.OssFilePath.COMPLAINTS_SUGGESTIONS.getIndex());

			for (int i = 1; i <= list_media.size(); i++) {

				MediaDataHolder mediaDataHolder = list_media.get(i - 1);

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
					rp.put("file" + i, f, contentType, fileName);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Log.v("gl", "rp==" + rp);
			RequestContentTemplate reqContent = new RequestContentTemplate();
			reqContent.setRequestTicket(true);

			HttpRequestEntity httpRequestEntity = new HttpRequestEntity(
					reqContent, Contants.SERVER_HOST,
					Contants.PROTOCOL_COMMAND.UPLOAD_FILE.getValue());
			httpRequestEntity
					.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);
			httpRequestEntity.setRequestParams(rp);
			httpRequestEntity.setHasData(false);
			httpRequestEntity.setRequestCode(REQUEST_UPLOADE_FILE);
			task = new HttpAsyncTask(this, this);
			task.execute(httpRequestEntity);
		}
	}

	/**
	 */
	private void sendReply(String fileurl, String content) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.replayType.name(),
				replayType);
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.msgType.name(),
				msgType);
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.message.name(),
				content);
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.relationId.name(),
				relationId);
		reqContent
				.appendData(Contants.PROTOCOL_RESP_BODY.files.name(), fileurl);

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.SEND_SUGGEST_MSG.getValue());
		httpRequestEntity.setRequestCode(RQ_SEND_MSG);
		httpRequestEntity.setHasData(true);

		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if (StringUtils.isBlank(rtnCode)) {
			Toast.makeText(getApplicationContext(), "文件上传失败",
					Toast.LENGTH_SHORT).show();
		} else if (!rtnCode.equals(Contants.ResponseCode.CODE_000000)) {
			String rtnMsg = (String) responseContent
					.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
			Toast.makeText(getApplicationContext(), rtnMsg, Toast.LENGTH_SHORT)
					.show();
		} else {
			if (REQUEST_UPLOADE_FILE.equals(responseContent.getResponseCode())) {
				Object data = responseContent.getData();
				if (data != null) {
					String fileUrl = data.toString();
					fileUrl = fileUrl.replace("[", "");
					fileUrl = fileUrl.replace("]", "");
					String files = makeExtraInfo(fileUrl);

					if (contentText.getText() != null
							&& !"".equals(contentText.getText().toString()
									.trim()))
						sendReply(files, contentText.getText().toString());
					else
						sendReply(files, "");

				} else {
					Toast.makeText(this, "文件上传失败，请重试", Toast.LENGTH_SHORT)
							.show();
				}
			} else if (RQ_SEND_MSG.equals(responseContent.getResponseCode())) {

				Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
				finish();

			}
		}
	}

	private String makeExtraInfo(String url) {
		String urls = "";

		if (StringUtils.isNotBlank(url)) {

			String[] suffixStr = url.split(",");

			if (suffixStr.length > 0) {
				for (String s : suffixStr) {

					urls += s + "|";
					Log.v("gl", "url==" + url);

				}
			} else {
				return urls;
			}
		}
		return urls;
	}

	protected void setImagePhoto() {
		CloseKeyBoard.closeInputKeyBoard(this);

		if (!FileTools.hasSdcard()) {
			Toast.makeText(this, "不存在SD卡", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			Configs.SystemPicture.CROP_MEDIA_PIC_NAME = System
					.currentTimeMillis() + ".jpg";
			photoName = Configs.SystemPicture.CROP_MEDIA_PIC_NAME;
			Log.v("gl", "photoName==" + photoName);

			photoUri = FileTools.getUriByFileDirAndFileName(
					Configs.SystemPicture.SAVE_DIRECTORY, photoName);
		} catch (IOException e) {
			Toast.makeText(this, "不能创建图片文件", Toast.LENGTH_SHORT).show();
			return;
		}

		if (audioAndPicPopupWindow == null) {

			initAudioAndPicPopupWindow();
		}
		audioAndPicPopupWindow.showAtLocation(titleTv, Gravity.BOTTOM, 0, 0);

		// SelectHeadTools.openDialog(this, photoUri);

	}

	protected boolean isNotPhotoUriNull() {

		if (photoUri != null && photoName != null && !photoName.equals(""))
			return true;

		return false;
	}

	protected void getPhotoStrAgain() {
		try {
			photoName = Configs.SystemPicture.CROP_MEDIA_PIC_NAME;
			photoUri = FileTools.getUriByFileDirAndFileName(
					Configs.SystemPicture.SAVE_DIRECTORY, photoName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "不能创建图片文件", Toast.LENGTH_SHORT).show();
		}

	}

	private void playAudio() {
		killMediaPlayer();
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			File file = new File(voicePath);
			FileInputStream fis = new FileInputStream(file);
			mediaPlayer.setDataSource(fis.getFD());

			mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// configs
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	public boolean alertMost() {
		// TODO Auto-generated method stub
		if(lay_pic.getChildCount()>6){
			Toast.makeText(this, "最多可上传6张图片", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
}