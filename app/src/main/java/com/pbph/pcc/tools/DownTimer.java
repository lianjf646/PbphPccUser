/*
    ShengDao Android Client, DownTimer
    Copyright (c) 2014 ShengDao Tech Company Limited
 */
package com.pbph.pcc.tools;

import android.os.CountDownTimer;

/**
 * [����ʱ��]
 *
 * @author devin.hu
 * @version 1.0
 * @date 2014-12-1
 **/
public class DownTimer {

    private final String TAG = DownTimer.class.getSimpleName();
    private CountDownTimer mCountDownTimer;
    private DownTimerListener listener;


    /**
     * @param time
     */
    public void startDown(long time) {
        startDown(time, 1000);
    }

    /**
     * @param time
     * @param mills
     */
    public void startDown(long time, long mills) {
        mCountDownTimer = new CountDownTimer(time, mills) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (listener != null) {
                    listener.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onFinish();
                }
                if (mCountDownTimer != null) mCountDownTimer.cancel();
            }

        }.start();
    }

    /**
     * [ֹͣ����ʱ����]<BR>
     */
    public void stopDown() {
        if (mCountDownTimer != null) mCountDownTimer.cancel();

        mCountDownTimer = null;
    }

    /**
     * @param listener
     */
    public void setListener(DownTimerListener listener) {
        this.listener = listener;
    }

    public interface DownTimerListener {

        /**
         * @param millisUntilFinished
         */
        public void onTick(long millisUntilFinished);

        /**
         */
        public void onFinish();
    }
}

