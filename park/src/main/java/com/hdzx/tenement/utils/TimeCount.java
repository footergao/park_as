package com.hdzx.tenement.utils;

// 验证码倒计时

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 计时器.
 */
public class TimeCount extends CountDownTimer {
    /**
     * button 外部按钮.
     */
    private Button button;

    /**
     * @param millisInFuture    总共时间多长
     * @param countDownInterval 时间间隔
     * @param button            对应的按钮
     */
    public TimeCount(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval); // 参数依次为总时长,和计时的时间间隔
        this.button = button;
    }

    @Override
    public void onFinish() { // 计时完毕时触发
        button.setText("重新发送");
        button.setClickable(true);
        button.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) { // 计时过程显示
        button.setClickable(false);
        button.setEnabled(false);
        button.setText("  剩余" + millisUntilFinished / 1000 + "秒  ");
    }
}