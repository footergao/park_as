package com.hdzx.tenement.utils;

import java.io.File;
import java.io.IOException;

import com.hdzx.tenement.common.vo.MediaDataHolder;

import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;

public class MyRecorder {

	private final MediaRecorder recorder = new MediaRecorder();
	private final String path;
	private  Uri uri;
	private MediaDataHolder dataHolder;
	
	private static int SAMPLE_RATE_IN_HZ = 8000;

	public MyRecorder(String name) {
		this.path = sanitizePath(name);
		dataHolder= new MediaDataHolder();
		
	}

	public static String sanitizePath(String name) {
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		if (!name.contains(".")) {
			name += ".3gp";
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/myvoice" + name;
	}

	/** 开始录音 */
	public void start() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		File directory = new File(path).getParentFile();
		
		if (!directory.exists() && !directory.mkdirs()) {
			return;
		}
		createAudioMediaData(Uri.fromFile(new File(path)));
		try {
			// 设置声音的来源
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置声音的输出格式
			recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			// 设置声音的编码格式
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// 设置音频采样率
			recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
			// 设置输出文件
			recorder.setOutputFile(path);
			// 准备录音
			recorder.prepare();
			// 开始录音
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createAudioMediaData(Uri fromFile) {
		dataHolder.setType(Contants.MEDIA_TYPE.AUDIO);
		dataHolder.setUri(fromFile);
		dataHolder.setDelete(false);
	}

	/** 停止录音 */
	public void stop() {
		try {
			// 停止录音
			recorder.stop();
			// 释放资源
			recorder.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getAmplitude() {
		if (recorder != null) {
			return (recorder.getMaxAmplitude());
		} else
			return 0;
	}

	public String getPath() {
		return path;
	}

	public MediaDataHolder getDataHolder() {
		return dataHolder;
	}

	public void setDataHolder(MediaDataHolder dataHolder) {
		this.dataHolder = dataHolder;
	}
	
}
