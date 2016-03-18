package com.hdzx.tenement.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.hdzx.tenement.R;
import com.hdzx.tenement.common.vo.MediaDataHolder;
import com.hdzx.tenement.photo.Configs;
import com.hdzx.tenement.utils.VideoLoader.DownloadListener;

public class DownloadAudio {
	private Context context;
	public MediaPlayer mediaPlayer = null;
	private AnimationDrawable animationDrawable;
	ImageView img_audio;

	public DownloadAudio(Context context, MediaPlayer mediaPlayer,
			AnimationDrawable animationDrawable, ImageView img_audio) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mediaPlayer = mediaPlayer;
		this.animationDrawable = animationDrawable;
		this.img_audio = img_audio;

		if (img_audio != null)
			img_audio.setBackgroundDrawable(animationDrawable);
	}

	public void downloadVideo(String url, String name) {
		final MediaDataHolder video = new MediaDataHolder();
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put("urlstr", url);
		taskParams.put("path", Configs.SystemPicture.SAVE_AUDIO);
		taskParams.put("fileName", name);

		Log.v("gl", "url==" + url);
		Log.v("gl", "fileName==" + name);
		Log.v("gl", "animationDrawable==" + animationDrawable);

		VideoLoader.getInstance().download(context, taskParams,
				new DownloadListener() {

					@Override
					public void onSuccess(Message msg) {
						String path = (String) msg.obj;
						if (!TextUtils.isEmpty(path)) {
							video.setText(path);
						}

						playAudio(video);

						// if (mediaPlayer!=null) {
						// String time=mediaPlayer.getDuration()/1000+"''";
						// // tv_time.setText(time);
						// }

					}

					@Override
					public void onFailed(String errorMsg) {
						Toast.makeText(context, "音频文件下载失败", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	private void initMedia(MediaDataHolder video) {
		try {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			Log.v("gl", "video.getText()" + video.getText());
			File file = new File(video.getText());
			FileInputStream fis = new FileInputStream(file);
			mediaPlayer.setDataSource(fis.getFD());

			mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
			mediaPlayer.prepare();
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

	private void killMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
		}

		if (animationDrawable != null && img_audio != null) {
			img_audio.setImageResource(-1);
			img_audio.setBackgroundDrawable(null);
			if (animationDrawable.isRunning()) {
				animationDrawable.stop();
			}
		}
	}

	private void playAudio(MediaDataHolder video) {
		killMediaPlayer();
		try {
			initMedia(video);
			mediaPlayer.start();
			if (animationDrawable != null && img_audio != null) {
				img_audio.setBackgroundDrawable(animationDrawable);
				animationDrawable.start();
			}

			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub

					Log.v("gl", "onCompletion==");
					if (animationDrawable != null) {
						if (animationDrawable.isRunning()) {
							animationDrawable.stop();
							img_audio.setImageResource(R.drawable.aliwx_chatfrom_play_r03);
							// ImageLoader.getInstance().displayImage("drawable://"
							// + R.drawable.aliwx_chatfrom_play_03, img_audio);
						}
					}
				}
			});

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

}
