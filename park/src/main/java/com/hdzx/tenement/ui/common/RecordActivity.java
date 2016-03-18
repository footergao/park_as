package com.hdzx.tenement.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hdzx.tenement.R;
import com.hdzx.tenement.common.vo.MediaDataHolder;
import com.hdzx.tenement.utils.Logger;
import com.zc.RecordDemo.MyAudioRecorder;

/**
 * 录音界面
 * 
 * @author Administrator
 *
 */
public class RecordActivity extends Activity {
	private static int MAX_TIME = 60; // 最长录制时间，单位秒，0为无时间限制
	private static int MIX_TIME = 1; // 最短录制时间，单位秒，0为无时间限制，建议设为1

	private static int RECORD_NO = 0; // 不在录音
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音

	private static int RECODE_STATE = 0; // 录音的状态

	private static float recodeTime = 0.0f; // 录音的时间

	// private String voicePath;// 当前录音的路径
	/** MyRecorder */
	// private MyRecorder recorder;
	private MyAudioRecorder recorder;

	/** 更新时间的线程 */
	private Thread timeThread;

	private static final String TAG = "RecordActivity";
	private SeekBar seekBar;
	private TextView tv_time;
	private ImageButton btnPress;

	private MediaDataHolder dataHolder;
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenement_main_hi_grab_recording);
		initView();
	}

	@Override
	protected void onResume() {
		recorder.prepare();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause");
		recorder.release();
	}

	private void initView() {
		context = this;
		recorder = new MyAudioRecorder();
		tv_time = (TextView) findViewById(R.id.tv_recording_time);
		seekBar = (SeekBar) findViewById(R.id.sb_record);
		btnPress = (ImageButton) findViewById(R.id.btn_press_to_talk);

		seekBar.setMax(MAX_TIME);
		seekBar.setEnabled(false);
		seekBar.setProgress(0);
		seekBar.setOnSeekBarChangeListener(onSeekbar);
		btnPress.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下
					// Logger.v(TAG, "----按下");
					VoiceStart();
					break;
				case MotionEvent.ACTION_MOVE:
					// Logger.v(TAG, "----ACTION_MOVE");
					if (event.getY() < -100) {
						VoiceStop();
					}
					break;
				case MotionEvent.ACTION_UP:// 离开
					// Logger.v(TAG, "----离开");
					VoiceStop();
					break;
				}
				return false;
			}
		});
	}

	// 第一个sekbar
	private OnSeekBarChangeListener onSeekbar = new OnSeekBarChangeListener() {

		@Override
		// 当游标移动停止的时候调用该方法
		public void onStopTrackingTouch(SeekBar seekBar) {
			// 设置标记为需要刷新
		}

		@Override
		// 当游标开始移动时调用该方法
		public void onStartTrackingTouch(SeekBar seekBar) {
			// 停止刷新
		}

		@Override
		// 当进度条游标被改变或者进度条改变时调用该方法
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// 更改textView的内容
			tv_time.setText("");
		}

	};

	private void VoiceStart() {
		Logger.v(TAG, "------VoiceStart");
		// 如果当前不是正在录音状态，开始录音
		if (RECODE_STATE != RECORD_ING) {
			// recorder = new MyRecorder(voiceName);
			// voicePath = recorder.getPath();
			RECODE_STATE = RECORD_ING;
			// 开始录音
			recorder.startRecording();
			// 计时线程
			myThread();
		}
	}

	protected void refresh() {
		// 重新设置进度条当前的值
		seekBar.setProgress(seekBar.getProgress() + 1);
	}

	/** 录音计时线程 */
	private void myThread() {
		timeThread = new Thread(ImageThread);
		timeThread.start();
	}

	/** 录音线程 */
	private Runnable ImageThread = new Runnable() {

		@Override
		public void run() {
			recodeTime = 0.0f;
			// 如果是正在录音状态
			while (RECODE_STATE == RECORD_ING) {
				if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
					handler.sendEmptyMessage(0x10);// 超时了
				} else {
					try {
						Thread.sleep(1000);

						recodeTime += 1.0;
						if (RECODE_STATE == RECORD_ING) {
							handler.sendEmptyMessage(0x11);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}

		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0x10:
					// 录音超过15秒自动停止,录音状态设为语音完成
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						// 如果录音图标正在显示的话,关闭显示图标
						// recordingContainer.setVisibility(View.INVISIBLE);

						// 停止录音
						recorder.stopRecording();

						// 如果录音时长小于1秒，显示录音失败的图标
						if (recodeTime < 1.0) {
							showWarnToast();
							// timeText.setText("");
							RECODE_STATE = RECORD_NO;
						} else {
							// timeText.setText("录音时间:" + ((int) recodeTime));
						}
					}
					break;

				case 0x11:
					// timeText.setText("");
					// float result = 60 - recodeTime;
					// dialog_tv.setText((int)result+"S");
					// setDialogImage();
					seekBar.setProgress((int) recodeTime);
					tv_time.setText((int) recodeTime+"''");
					break;
				}
			};
		};
	};

	private void VoiceStop() {
		Logger.v(TAG, "------VoiceStop");
		// 如果是正在录音
		if (RECODE_STATE == RECORD_ING && recorder != null) {
			Logger.v(TAG, "------VoiceStop ing ");
			RECODE_STATE = RECODE_ED;

			// 停止录音
			recorder.stopRecording();

			if (recodeTime < MIX_TIME) {
				showWarnToast();
				RECODE_STATE = RECORD_NO;
			} else {
				// TODO 完成退出
				dataHolder = recorder.getDataHolder();
				dataHolder.setText(tv_time.getText().toString().trim());
				Intent data = new Intent();
				data.putExtra("dataHolder", dataHolder);
				data.putExtra("uri", dataHolder.getUri());
				data.putExtra("path", recorder.getmEncodedFile()
						.getAbsolutePath());
				setResult(RESULT_OK, data);
				finish();
			}
		}
	}

	private void showWarnToast() {
		Toast toast = new Toast(RecordActivity.this);
		LinearLayout linearLayout = new LinearLayout(RecordActivity.this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(20, 20, 20, 20);

		// 定义一个ImageView
		ImageView imageView = new ImageView(RecordActivity.this);
		imageView.setImageResource(R.drawable.iv_bad); // 图标

		TextView mTv = new TextView(RecordActivity.this);
		mTv.setText("时间太短   录音失败");
		mTv.setTextSize(14);
		mTv.setTextColor(Color.BLACK);// 字体颜色
		// mTv.setPadding(0, 10, 0, 0);

		// 将ImageView和ToastView合并到Layout中
		linearLayout.addView(imageView);
		linearLayout.addView(mTv);
		linearLayout.setGravity(Gravity.CENTER);// 内容居中
		linearLayout.setBackgroundColor(getResources().getColor(R.color.grey));
		// 设置自定义toast的背景

		toast.setView(linearLayout);
		toast.setGravity(Gravity.CENTER, 0, 0);// 起点位置为中间 100为向下移100dp
		toast.show();

	}

	public void onClick(View view) {
		finish();
	}
}
