package com.joneill.snowmusic.receiver;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.joneill.snowmusic.song.SongPlayerPanel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by josep on 8/8/2016.
 */
public class HeadsetIntercept {
    private static final int TIMER_INTERVAL = 351;
    public static final float INTERVAL_DELAY = 350;
    private SongPlayerPanel songPanel;
    private Runnable runnable;
    private boolean isFirstClick;
    private boolean isRunning;
    private boolean isClickOver;
    private boolean useQuickPause;
    private int clicks;
    //in ms
    private float timeSinceLastClick;

    private Handler mediaHandler;

    public HeadsetIntercept() {
        initRunnable();
        isFirstClick = true;
        isRunning = true;
        mediaHandler = new Handler();
    }

    private void initRunnable() {
        runnable = new Runnable() {
            public void run() {
                if (timeSinceLastClick > INTERVAL_DELAY || isClickOver) {
                    performCommand(clicks);
                    isClickOver = true;
                    clicks = 0;
                    timeSinceLastClick = 0;
                } else {
                    timeSinceLastClick += TIMER_INTERVAL;
                }
                if(isRunning) {
                    mediaHandler.postDelayed(runnable, TIMER_INTERVAL);
                }
            }
        };
    }

    private void performCommand(int clicks) {
        if (songPanel == null) {
            Log.e("Snow Music: IS NULL", "SongPlayerPanel is null. Set SongPlayerPanel");
            return;
        }

        switch (clicks) {
            case 1:
                if (!useQuickPause) {
                    onPlay();
                }
                break;
            case 2:
                onNext();
                break;
            case 3:
                onPrev();
                break;
        }
    }

    private void onPlay() {
        if (songPanel.getMusicService().isPlaying()) {
            Log.e("joneill", "joneill PAUSE IS FROM HEADEST INTERCEPT");
            songPanel.pauseSong();
        } else {
            songPanel.playSong();
        }
    }

    private void onNext() {
        songPanel.getMusicService().playNext(true);
        songPanel.getMusicService().openSongInPanel(true, true, songPanel, false);
    }

    private void onPrev() {
        songPanel.getMusicService().playPrev(true);
        songPanel.getMusicService().openSongInPanel(false, true, songPanel, false);
    }

    public void addClick() {
        if (isFirstClick) {
            mediaHandler.post(runnable);
            isRunning = true;
            isFirstClick = false;
        }
        clicks++;
        timeSinceLastClick = 0;
        if (clicks == 1 && useQuickPause) {
            onPlay();
            isClickOver = false;
        }
    }

    public boolean isUseQuickPause() {
        return useQuickPause;
    }

    public void setUseQuickPause(boolean useQuickPause) {
        this.useQuickPause = useQuickPause;
    }

    public void onDestroy() {
        isRunning = false;
    }

    public void setSongPanel(SongPlayerPanel songPanel) {
        this.songPanel = songPanel;
    }
}
