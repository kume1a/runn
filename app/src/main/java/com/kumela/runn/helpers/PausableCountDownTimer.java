package com.kumela.runn.helpers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

@SuppressWarnings({"unused", "RedundantSuppression"})
public abstract class PausableCountDownTimer {

    private static final int INTERVAL = 1000;
    private static final int MSG = 1;

    /**
     * To maintain Timer start and stop status.
     */
    private boolean isRunning;
    /**
     * To maintain Timer resume and pause status.
     */
    private boolean isPaused;

    /**
     * Timer time.
     */
    private long time;
    private long localTime;
    private long interval;
    private Handler handler;

    public PausableCountDownTimer() {
        init(0, INTERVAL);
    }

    public PausableCountDownTimer(long timeInMillis) {
        init(timeInMillis, INTERVAL);
    }

    public PausableCountDownTimer(long timeInMillis, long intervalInMillis) {
        init(timeInMillis, intervalInMillis);
    }

    protected abstract void onTick(long timeRemaining);

    protected abstract void onFinish();

    /**
     * Method to initialize HourGlass.
     *
     * @param time:     Time in milliseconds.
     * @param interval: in milliseconds.
     */
    private void init(long time, long interval) {
        setTime(time);
        setInterval(interval);

        initTimer();
    }

    private void initTimer() {
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == MSG) {
                    if (!isPaused) {

                        if (localTime <= time) {
                            onTick(time - localTime);
                            localTime += interval;
                            sendMessageDelayed(handler.obtainMessage(MSG), interval);
                        } else stop();
                    }
                }
            }
        };
    }


    /**
     * Convenience method to check whether the timer is running or not
     *
     * @return: true if timer is running, else false.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Method to start the timer.
     */
    public void start() {
        if (isRunning)
            return;

        isRunning = true;
        isPaused = false;
        localTime = 0;
        handler.sendMessage(handler.obtainMessage(MSG));
    }

    /**
     * Method to stop the timer.
     */
    public void stop() {
        isRunning = false;
        handler.removeMessages(MSG);
        onFinish();
    }

    /**
     * Method to check whether the timer is paused.
     *
     * @return: true if timer is paused else false.
     */
    public synchronized boolean isPaused() {
        return isPaused;
    }

    /**
     * To pause the timer from Main thread.
     *
     * @param isPaused: true to pause the timer, false to resume.
     */
    private synchronized void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    /**
     * Convenience method to pause the timer.
     */
    public synchronized void pause() {
        setPaused(true);
    }

    /**
     * Convenience method to resume the timer.
     */
    public synchronized void resume() {
        setPaused(false);

        handler.sendMessage(handler.obtainMessage(MSG));
    }

    /**
     * Setter for Time.
     *
     * @param timeInMillis: in milliseconds
     */
    public void setTime(long timeInMillis) {
        if (isRunning)
            return;

        if (this.time <= 0)
            if (timeInMillis < 0)
                timeInMillis *= -1;
        this.time = timeInMillis;
    }

    /**
     * @return remaining time
     */
    public long getRemainingTime() {
        if(isRunning) {
            return this.time;
        }

        return 0;
    }

    public long getPassedTime() {
        return localTime;
    }

    /**
     * Setter for interval.
     *
     * @param intervalInMillis: in milliseconds
     */
    public void setInterval(long intervalInMillis) {
        if (isRunning)
            return;

        if (this.interval <= 0)
            if (intervalInMillis < 0)
                intervalInMillis *= -1;
        this.interval = intervalInMillis;
    }
}
