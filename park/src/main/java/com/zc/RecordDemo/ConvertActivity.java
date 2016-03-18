package com.zc.RecordDemo;

import com.hdzx.tenement.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * 7/3/14  2:42 PM
 * Created by JustinZhang.
 */
public class ConvertActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ConvertActivity";
    MyAudioRecorder recorder;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        context = this;
        setContentView(R.layout.convert_layout);
        findViewById(R.id.stop_record).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.record).setOnClickListener(this);
        findViewById(R.id.restart).setOnClickListener(this);
        recorder = new MyAudioRecorder();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"onPause");
        recorder.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"ON RESUMME");
        recorder.prepare();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pause:
                recorder.pauseRecording();
                break;
            case R.id.record:
                recorder.startRecording();
                break;
            case R.id.stop_record:
                recorder.stopRecording();
                break;
            case R.id.restart:
                recorder.restartRecording();
                break;
        }
    }
}
