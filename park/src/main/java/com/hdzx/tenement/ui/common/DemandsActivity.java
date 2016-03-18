package com.hdzx.tenement.ui.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.PaintDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.vo.MediaDataHolder;
import com.hdzx.tenement.community.vo.ExtraInfo;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.photo.Configs;
import com.hdzx.tenement.photo.FileTools;
import com.hdzx.tenement.photo.SelectHeadTools;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.CloseKeyBoard;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.DownloadAudio;
import com.hdzx.tenement.widget.FixGridLayout;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DemandsActivity extends Activity implements IContentReportor {

	private static final int ACTIVITY_REQUEST_CODE_IMAGE = 100;

	private static final int ACTIVITY_REQUEST_CODE_GALLERY = 101;

	private static final int RESULT_CAPTURE_RECORDER_SOUND = 102;

	private static final int REQUEST_CODE_TAKE_VIDEO = 103;

	private static final int REQUEST_CODE_TAKE_VOICE = 104;

	private static final String IMAGE_FILE_NAME = "33322222.jpg";

	private TextView titleTv = null;

	private PopupWindow mediaPopupWindow = null;

	private PopupWindow imagePopupWindow = null;

	private PopupWindow audioAndPicPopupWindow = null;

	private ImageView mediaIv = null;

	private Button recorddingButton = null;
	private MediaRecorder mediaRecorder = null;
	private EditText contentText = null;

	// zhaopian&audio
	private Uri photoUri = null;
	private FixGridLayout lay_pic;
	private List<MediaDataHolder> list_media;// 图片的sd卡存储路径
	private static int SIZE = 200;// tupian-chicun
	private LinearLayout ll_audio;
	private TextView tv_video_time;
	private ImageView img_audio;
	private String voicePath;
	private String REQUEST_UPLOADE_FILE = "request_uploade_file";
	String soaKey;
	List<ExtraInfo> extraInfoList = new ArrayList<ExtraInfo>();
	String urls = "";// 回传的所有图片音频url
	String downAudioUrl = "";
	String downAudioName = "";
	
	//
	private AnimationDrawable animationDrawable;
	private MediaPlayer mediaPlayer = null;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("gl", "onCreate");

		setContentView(R.layout.common_media_layout);
		initView();
		initAudioAndAnim();
		
		
		Intent intent = this.getIntent();
		if (intent != null) {
			// {soaKey=文本, soaValue=y777}
			// bundle = intent.getExtras();
			// String soaKey = bundle.getString("soaKey", "");
			// if(soaKey.equals("文本")){
			// soaKey = bundle.getString("soaValue", "");
			// contentText.setText(soaKey);
			// }

			String list = intent.getStringExtra("list");
			if (isNotBlank(list)) {
				try {
					JSONArray array = new JSONArray(list);

					for (int i = 0; i < array.length(); i++) {
						JSONObject json = array.getJSONObject(i);
						if (json.get("soaKey").equals("文本")) {
							String value = json.get("soaValue").toString();
							contentText.setText(value);
						} else if (json.get("soaKey").equals("图片")) {
							String value = json.get("soaValue").toString();
							value = value.replace("\\", "").trim();
							int index = value.lastIndexOf("/");
							String name = value.substring(index + 1).trim();
							Log.v("gl", "name==" + name);

							ImageView img = new ImageView(this);
							img.setTag("crop_" + name);
							img.setLayoutParams(new LayoutParams(SIZE, SIZE));
							img.setScaleType(ScaleType.FIT_XY);
							
							ImageLoader.getInstance().displayImage(
									UserSession.getInstance().getImageHost()
											+ value, img);

							Log.v("gl", "url=="
									+ UserSession.getInstance().getImageHost()
									+ value);

							lay_pic.removeView(mediaIv);
							lay_pic.addView(img);
							lay_pic.addView(mediaIv);

							urls += value + ",";

							imgAction(img, value);

						} else if (json.get("soaKey").equals("音频")) {

							ll_audio.setVisibility(View.VISIBLE);
							String value = json.get("soaValue").toString();
							value = value.replace("\\", "").trim();
							int index = value.lastIndexOf("/");
							String name = value.substring(index + 1).trim();
							Log.v("gl", "name==" + name);

							downAudioUrl = UserSession.getInstance()
									.getImageHost() + value;
							downAudioName = name;

							urls += value + ",";

						}
					}

					Log.v("gl", "urls==" + urls);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private void initView() {
		initCommonView();
	}

	private void initCommonView() {
		titleTv = (TextView) this.findViewById(R.id.titile_tv);
		titleTv.setText("服务要求");

		img_audio = (ImageView) findViewById(R.id.img_audio);
		tv_video_time = (TextView) findViewById(R.id.tv_video_time);
		ll_audio = (LinearLayout) findViewById(R.id.ll_audio);
		ll_audio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (isNotBlank(downAudioUrl)) {
					
					DownloadAudio down = new DownloadAudio(DemandsActivity.this, mediaPlayer,animationDrawable, img_audio);
    				down.downloadVideo(downAudioUrl, downAudioName);
				} else
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

		recorddingButton = (Button) this.findViewById(R.id.recording_button);
		recorddingButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mediaRecorder != null) {
					mediaRecorder.stop();
					recorddingButton.setVisibility(View.GONE);
				}
			}

		});

		Button submit = (Button) this.findViewById(R.id.submit_btn);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (list_media != null && list_media.size() > 0) {

					makeUploadFileRequest();// 存在新添加的媒体

				} else if ((contentText.getText() != null && !""
						.equals(contentText.getText().toString().trim()))
						|| isNotBlank(urls)) {
					// 文本和回传媒体有一个不为空
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					List<ExtraInfo> extraList = makeExtraInfo(urls);
					Log.v("gl", "extraList==" + extraList);
					Log.v("gl",
							"new Gson().toJson(extraList)"
									+ new Gson().toJson(extraList));
					bundle.putString("EXTRA_LIST", new Gson().toJson(extraList));
					bundle.putString("RTN_CODE",
							Contants.ResponseCode.CODE_000000);
					intent.putExtras(bundle); // 返回地址簿
					DemandsActivity.this.setResult(RESULT_OK, intent);
					DemandsActivity.this.finish();

				} else {
					Toast.makeText(DemandsActivity.this, "请输入文本、图片或者声音。",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
	}

	private void initMediaRecorder() {
		if (mediaRecorder == null) {
			mediaRecorder = new MediaRecorder();
			// 第1步：设置音频来源（MIC表示麦克风）
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 第2步：设置音频输出格式（默认的输出格式）
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			// 第3步：设置音频编码方式（默认的编码方式）
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		}
	}

	private void killMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

	private void createAudioMediaData(Uri uri) {
		MediaDataHolder mediaDataHolder = new MediaDataHolder();

		mediaDataHolder.setType(Contants.MEDIA_TYPE.AUDIO);
		mediaDataHolder.setUri(uri);
		mediaDataHolder.setDelete(true);

		list_media.add(mediaDataHolder);

		ll_audio.setVisibility(View.VISIBLE);
	}

	protected void initMediaPopupWindow() {
		View view = getLayoutInflater().inflate(
				R.layout.common_media_selected_pop, null);
		mediaPopupWindow = new PopupWindow(view,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mediaPopupWindow.setBackgroundDrawable(new PaintDrawable(0));
		mediaPopupWindow.setFocusable(true);
		mediaPopupWindow.setOutsideTouchable(true);

		mediaPopupWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {

					@Override
					public void onDismiss() {
						if (mediaPopupWindow != null) {
							// 设置透明度（这是窗体本身的透明度，非背景）
							// alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
							WindowManager.LayoutParams params = getWindow()
									.getAttributes();
							params.alpha = 1.0f;
							getWindow().setAttributes(params);
						}
					}
				});

		Button imageButton = (Button) view.findViewById(R.id.media_image_btn);
		Button soundButton = (Button) view.findViewById(R.id.media_sound_btn);
		Button vedioButton = (Button) view.findViewById(R.id.media_vedio_btn);
		imageButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mediaPopupWindow.dismiss();
				if (imagePopupWindow == null) {
					initImagePopupWindow();
				}
				WindowManager.LayoutParams params = getWindow().getAttributes();
				params.alpha = 0.5f;
				getWindow().setAttributes(params);
				imagePopupWindow.setOutsideTouchable(true);

				imagePopupWindow.showAtLocation(titleTv, Gravity.BOTTOM
						| Gravity.LEFT, 0, 0);
			}
		});

		soundButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mediaPopupWindow.dismiss();
				Toast.makeText(DemandsActivity.this, "录音已开始，请对准麦克风讲话 ",
						Toast.LENGTH_SHORT).show();
				initMediaRecorder();

				try {
					// 创建一个临时的音频输出文件
					File audioFile = File.createTempFile("record_", ".amr");
					// recordUri = Uri.fromFile(audioFile);

					createAudioMediaData(Uri.fromFile(audioFile));

					// 第4步：指定音频输出文件
					mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
					// 第5步：调用prepare方法
					mediaRecorder.prepare();
					// 第6步：调用start方法开始录音
					mediaRecorder.start();

					recorddingButton.setVisibility(View.VISIBLE);
				} catch (IOException e) {
					killMediaRecorder();
					e.printStackTrace();
				}
			}
		});

		vedioButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mediaPopupWindow.dismiss();
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
				startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
			}
		});
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
				if (alertMost())
					return;

				audioAndPicPopupWindow.dismiss();
				SelectHeadTools.startCamearPicCut(DemandsActivity.this,
						photoUri);
			}
		});

		btn_gallery_photo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (alertMost())
					return;

				// 相册
				audioAndPicPopupWindow.dismiss();
				SelectHeadTools.startImageCaptrue(DemandsActivity.this);
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
				intent.setClass(DemandsActivity.this, RecordActivity.class);
				startActivityForResult(intent, REQUEST_CODE_TAKE_VOICE);// 语音
			}
		});
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
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
			openCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			startActivityForResult(openCameraIntent,
					ACTIVITY_REQUEST_CODE_IMAGE);
		} else {
			Toast.makeText(this, "没有储存卡", Toast.LENGTH_LONG).show();
		}
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
							Configs.SystemPicture.CROP_MEDIA_PIC_NAME);
				} else {
					getPhotoStrAgain();
					SelectHeadTools.startPhotoZoom(this, photoUri, SIZE,
							Configs.SystemPicture.CROP_MEDIA_PIC_NAME);
				}
				break;
			case Configs.SystemPicture.PHOTO_REQUEST_GALLERY:
				if (data == null)
					return;
				photoUri = data.getData();
				for (MediaDataHolder media : list_media) {

					File file = FileTools.getFileByUri(this, photoUri);
					Uri fileUri = Uri.fromFile(file);
					if (fileUri.equals(media.getUri())) {
						Toast.makeText(this, "您已经添加了该文件", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				}

				SelectHeadTools.startPhotoZoom(this, photoUri, SIZE, Configs.SystemPicture.CROP_MEDIA_PIC_NAME);
				break;
			case Configs.SystemPicture.PHOTO_REQUEST_CUT:
				if (data == null)
					return;
				Bitmap bit = data.getExtras().getParcelable("data");

				ImageView img = new ImageView(this);
				img.setTag(Configs.SystemPicture.CROP_MEDIA_PIC_NAME);
				img.setLayoutParams(new LayoutParams(SIZE, SIZE));
				img.setScaleType(ScaleType.FIT_XY);
				img.setImageBitmap(bit);

				lay_pic.removeView(mediaIv);
				lay_pic.addView(img);
				lay_pic.addView(mediaIv);

//				if (isNotPhotoUriNull()) {
//					File file = FileTools.getFileByUri(this, photoUri);
//					Uri fileUri = Uri.fromFile(file);
//					createImageMediaData(fileUri, false);
//					imgAction(img, fileUri);
//				} else {
//					getPhotoStrAgain();
//					File file = FileTools.getFileByUri(this, photoUri);
//					Uri fileUri = Uri.fromFile(file);
//					createImageMediaData(fileUri, false);
//					imgAction(img, fileUri);
//				}
				
				try {
					Uri cut_photoUri = FileTools.getUriByFileDirAndFileName(
							Configs.SystemPicture.SAVE_DIRECTORY,
							Configs.SystemPicture.CROP_MEDIA_PIC_NAME);
					
					Uri big_photoUri = FileTools.getUriByFileDirAndFileName(
							Configs.SystemPicture.SAVE_DIRECTORY,
							Configs.SystemPicture.MEDIA_PIC_NAME);


					File file = FileTools.getFileByUri(this, cut_photoUri);
					Uri fileUri = Uri.fromFile(file);
					
					
					File big_file = FileTools.getFileByUri(this, big_photoUri);
					Uri big_fileUri = Uri.fromFile(big_file);
					
					createImageMediaData(big_fileUri,fileUri, true);
					
					imgAction(img, fileUri);
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(this, "不能创建图片文件", Toast.LENGTH_SHORT).show();
				}
				break;

			case REQUEST_CODE_TAKE_VOICE:// 语音
				MediaDataHolder mediaDataHolder = (MediaDataHolder) data
						.getParcelableExtra("dataHolder");
				voicePath = data.getStringExtra("path");
				
				tv_video_time.setText(mediaDataHolder.getText());
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
				Intent intent = new Intent(DemandsActivity.this,
						BigImageActivity.class);
				intent.putExtra("path", fileUri.toString());
				Log.v("gl", "uri==="+fileUri);
				startActivity(intent);
			}
		});

		img.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				new AlertDialog.Builder(DemandsActivity.this)
						.setMessage("是否删除？")
						.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										lay_pic.removeView(img);

//										for (MediaDataHolder media : list_media) {
//											if (fileUri.equals(media.getUri()))
//												list_media.remove(media);
//										}
										Iterator<MediaDataHolder> iterator = list_media.iterator();
								        while(iterator.hasNext()){
								        	MediaDataHolder media = iterator.next();
								            if(fileUri.equals(media.getUri()))
								                iterator.remove();   //注意这个地方
								                
								        }
										deleteMediaDataHolder(fileUri);

									}
								}).setPositiveButton("取消", null).show();

				return true;
			}
		});

	}

	private void imgAction(final ImageView img, final String value) {
		// TODO Auto-generated method stub
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DemandsActivity.this,
						BigImageActivity.class);
				intent.putExtra("path", UserSession.getInstance().getImageHost()+ value);
				Log.v("gl", "uri==="+value);
				startActivity(intent);
			}
		});

		img.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				new AlertDialog.Builder(DemandsActivity.this)
						.setMessage("是否删除？")
						.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										lay_pic.removeView(img);

										urls = urls.replace(value + ",", "");
										Log.v("gl", "urls==="+urls);

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


	protected void onDestroy() {
		super.onDestroy();
		Log.v("gl", "onDestroy");
		killMediaPlayer();
	}

	public void onBackPressed() {
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
			rp.put("use", Contants.OssFilePath.ESTATE_DATA.getIndex());

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
			HttpAsyncTask task = new HttpAsyncTask(this, this);
			task.execute(httpRequestEntity);
		}
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if (!isNotBlank(rtnCode)) {
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
					Log.v("gl", "fileUrl==" + fileUrl);

					if (isNotBlank(urls)) {

						fileUrl = urls + fileUrl;

					}
					List<ExtraInfo> extraList = makeExtraInfo(fileUrl);
					// bundle.putSerializable("EXTRA_LIST", (Serializable)
					// extraList);
					bundle.putString("EXTRA_LIST", new Gson().toJson(extraList));
					bundle.putString("RTN_CODE", rtnCode);
					intent.putExtras(bundle); // 返回地址簿
					DemandsActivity.this.setResult(RESULT_OK, intent);
					DemandsActivity.this.finish();
				} else {
					Toast.makeText(this, "文件上传失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private List<ExtraInfo> makeExtraInfo(String url) {
		if (isNotBlank(contentText.getText().toString())) {
			ExtraInfo extraInfo = new ExtraInfo("文本", contentText.getText()
					.toString().trim(),"001");
			extraInfoList.add(extraInfo);
		}

		Log.v("gl", "makeExtraInfo==" + url);
		if (isNotBlank(url)) {

			String[] suffixStr = url.split(",");

			for (String s : suffixStr) {

				Log.v("gl", "s==" + s);

				ExtraInfo extraInfo = new ExtraInfo();
				extraInfo.setSoaValue(s);

				if (s.contains("jpg") || s.contains("jpe")
						|| s.contains("jpeg") || s.contains("png")) {
					extraInfo.setSoaKey("图片");
					extraInfo.setSoaType("002");

				} else if (s.contains("mp3")) {
					extraInfo.setSoaKey("音频");
					extraInfo.setSoaType("003");
				} else {
					extraInfo.setSoaKey("未知");
				}
				extraInfoList.add(extraInfo);
			}
		}
		return extraInfoList;
	}

	protected void setImagePhoto() {
		CloseKeyBoard.closeInputKeyBoard(this);

		if (!FileTools.hasSdcard()) {
			Toast.makeText(this, "不存在SD卡", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			
			Configs.SystemPicture.MEDIA_PIC_NAME = System.currentTimeMillis()
					+ ".jpg";
			Configs.SystemPicture.CROP_MEDIA_PIC_NAME = "crop_"
					+ Configs.SystemPicture.MEDIA_PIC_NAME;
			
			photoUri = FileTools.getUriByFileDirAndFileName(
					Configs.SystemPicture.SAVE_DIRECTORY,
					Configs.SystemPicture.MEDIA_PIC_NAME);
			
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

		if (photoUri != null)
			return true;

		return false;
	}

	protected void getPhotoStrAgain() {
		try {
			photoUri = FileTools.getUriByFileDirAndFileName(
					Configs.SystemPicture.SAVE_DIRECTORY, Configs.SystemPicture.MEDIA_PIC_NAME);
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
		if (lay_pic.getChildCount() > 6) {
			Toast.makeText(this, "最多可上传6张图片", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
	
	public boolean isNotBlank(String param) {
		// TODO Auto-generated method stub

		if (param != null && param != null && !param.trim().equals(""))
			return true;

		return false;
	}
	
	private void initAudioAndAnim() {
		// TODO Auto-generated method stub
		mediaPlayer = new MediaPlayer();
		animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.animation_audio);
	}
	
	
	private void createImageMediaData(Uri biguri, Uri fileUri, boolean isDelete) {
		MediaDataHolder	mediaDataHolder = new MediaDataHolder();

		mediaDataHolder.setType(Contants.MEDIA_TYPE.IMAGE);
		mediaDataHolder.setUri(fileUri);
		mediaDataHolder.setBig_uri(biguri);
		mediaDataHolder.setDelete(isDelete);
		
		list_media.add(mediaDataHolder);
		
	}
	private void deleteMediaDataHolder(Uri fileUri) {

		Log.v("gl", "deleteMediaDataHolder");
				File f = new File(fileUri.getPath());
				if (f.exists()) {
					f.delete();
				}
				
				File big_f = new File(fileUri.getPath());
				if (big_f.exists()) {
					big_f.delete();
				}
	}
	
}