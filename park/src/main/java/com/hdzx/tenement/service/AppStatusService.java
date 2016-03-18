package com.hdzx.tenement.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import com.hdzx.tenement.ui.common.QrDialog;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.DateUtil;
import com.hdzx.tenement.utils.PreferencesUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by anchendong on 15/7/29.
 */
public class AppStatusService extends Service {

    private ActivityManager activityManager;
    private String packageName;
    private boolean isStop = false;
    private int serviceStatus = 0;//0 前台运行 1 后台运行
    private Date date;
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        packageName = this.getPackageName();
        date = new Date();

        //当前service线程创建handle
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x0001:
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point displaySize = new Point();
                        display.getSize(displaySize);
                        int width = displaySize.x;
                        int height = displaySize.y;


                        QrDialog dialog = new QrDialog(getApplicationContext(),height*2/3);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        dialog.show();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread() {
            public void run() {
                try {
                    while (!isStop) {
                        Thread.sleep(1000);

                        if (isAppOnForeground()) {
                            //前台运行
                            if (serviceStatus == 1) {
                                //从后台切换到前台运行
                                long time = DateUtil.diffMinute(date, new Date());
                                if (time < 1) {
                                    //当前设置开启
                                    boolean isOn = PreferencesUtils.getInstance().takeBoolean(getApplicationContext(), Contants.PREFERENCES_KEY.door_status.name());
                                    if (isOn){
                                        Message message = handler.obtainMessage();
                                        message.what = 0x0001;
                                        handler.sendMessage(message);
                                    }
                                }
                            }
                            serviceStatus = 0;
                        } else {
                            //后台运行
                            if (serviceStatus == 0) {
                                //从前台切换到后台
                                date = new Date();
                            }
                            serviceStatus = 1;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the device
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isStop = true;
    }


}
