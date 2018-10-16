package com.joneill.snowmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.service.MusicService;
import com.joneill.snowmusic.song.SongPlayerPanel;

import java.util.Set;

/**
 * Created by josep on 8/4/2016.
 */
public class MusicControlReceiver extends BroadcastReceiver {
    public static final String MCW_SETTINGS_CALLBACK_KEY = "MCW_SETTINGS_KEY";
    private Settings.OnSettingsChanged settingsCallback;
    private boolean pauseOnDis, resOnConn;

    private SongPlayerPanel songPanel;
    private MediaSessionCompat mediaSession;
    private MediaSessionCompat.Token mediaSessionToken;
    private HeadsetIntercept headsetIntercept;
    private MusicService musicService;


    public MusicControlReceiver(MediaSessionCompat mediaSession) {
        this.mediaSession = mediaSession;
        headsetIntercept = new HeadsetIntercept();
        initMediaSession();
        setupSettings();
    }

    public MusicControlReceiver(SongPlayerPanel songPanel, MediaSessionCompat mediaSession) {
        this.songPanel = songPanel;
        this.mediaSession = mediaSession;
        this.musicService = songPanel.getMusicService();
        headsetIntercept = new HeadsetIntercept();
        initMediaSession();
        setupSettings();
    }

    private void initMediaSession() {
        mediaSessionToken = mediaSession.getSessionToken();

        mediaSession.setCallback(mediaSessionCallback);

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        PlaybackStateCompat state = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0)
                .build();

        mediaSession.setPlaybackState(state);
        mediaSession.setActive(true);
    }

    private void setupSettings() {
        pauseOnDis = (boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_PAUSE_ON_DIS);
        resOnConn = (boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_RES_ON_CON);
        headsetIntercept.setUseQuickPause((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_USE_QUICK_PAUSE));
        settingsCallback = new Settings.OnSettingsChanged() {
            @Override
            public void onSettingsChanged(String key) {
                changeSettings(key);
    }
};

        Settings.getInstance().getCallbacks().put(MCW_SETTINGS_CALLBACK_KEY, settingsCallback);
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        if (songPanel != null) {
            if (intent.getAction() == "com.joneill.snowmusic.action.ACTION_PLAY") {
                Log.e("Play", "play");
                if (songPanel.getMusicService().isPlaying()) {
                    songPanel.pauseSong();
                } else {
                    songPanel.playSong();
                }
            } else if (intent.getAction() == "com.joneill.snowmusic.action.ACTION_PREV") {
                songPanel.getMusicService().playPrev(false);
                if (!songPanel.isPlaying() && !songPanel.isSetPlayOnSkip()) {
                    songPanel.getMusicService().openSongInPanel(false, false, songPanel, false);
                } else {
                    songPanel.getMusicService().openSongInPanel(false, true, songPanel, false);
                }
            } else if (intent.getAction() == "com.joneill.snowmusic.action.ACTION_NEXT") {
                songPanel.getMusicService().playNext(false);
                if (!songPanel.isPlaying() && !songPanel.isSetPlayOnSkip()) {
                    songPanel.getMusicService().openSongInPanel(true, false, songPanel, false);
                } else {
                    songPanel.getMusicService().openSongInPanel(true, true, songPanel, false);
                }
            } else if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG) && songPanel.getCurrSong() != null
                    && !isInitialStickyBroadcast()) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        //Unplugged
                        if(songPanel.isPlaying() && pauseOnDis) {
                            Log.e("joneill", "joneill PAUSE IS FROM MCR");
                            songPanel.pauseSong();
                        }
                        break;
                    case 1:
                        //Plugged
                        if(!songPanel.isPlaying() && resOnConn) {
                            songPanel.playSong();
                        }
                        break;
                }
            }
        } else {
            if (intent.getAction() == "com.joneill.snowmusic.action.ACTION_PLAY") {
                Log.e("Play", "play");
                if (musicService.isPlaying()) {
                    musicService.pause();
                } else {
                    if(musicService.getQueue().getQueueList().size() > 0) {
                        musicService.start();
                    }
                }
            } else if (intent.getAction() == "com.joneill.snowmusic.action.ACTION_PREV") {
                musicService.playPrev(false);
                boolean playOnSkip = (boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_PLAY_ON_SKIP);
                if (!musicService.isPlaying() && !playOnSkip) {
                    musicService.pause();
                }
            } else if (intent.getAction() == "com.joneill.snowmusic.action.ACTION_NEXT") {
                musicService.playNext(false);
                boolean playOnSkip = (boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_PLAY_ON_SKIP);
                if (!musicService.isPlaying() && !playOnSkip) {
                    musicService.pause();
                }
            } else if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG) && musicService.getQueue().getQueueList().size() > 0) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        //Unplugged
                        if(musicService.isPlaying() && pauseOnDis) {
                            musicService.pause();
                        }
                        break;
                    case 1:
                        //Plugged
                        if(!musicService.isPlaying() && resOnConn) {
                            musicService.start();
                        }
                        break;
                }
            }
        }
    }

    private MediaSessionCompat.Callback mediaSessionCallback = new MediaSessionCompat.Callback() {
        public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
            return super.onMediaButtonEvent(mediaButtonIntent);
        }

        public void onPause() {
            super.onPause();
        }

        public void onPlay() {
            headsetIntercept.addClick();
        }

        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }
    };

    private void changeSettings(String key) {
        switch (key) {
            case Settings.BOOL_PAUSE_ON_DIS:
                pauseOnDis = (boolean) Settings.getInstance().getSettingsData().get(key);
                break;
            case Settings.BOOL_RES_ON_CON:
                resOnConn = (boolean) Settings.getInstance().getSettingsData().get(key);
                break;
            case Settings.BOOL_USE_QUICK_PAUSE:
                headsetIntercept.setUseQuickPause((boolean) Settings.getInstance().getSettingsData().get(key));
                break;
        }
    }


    public void setSongPanel(SongPlayerPanel songPanel) {
        this.songPanel = songPanel;
        this.musicService = songPanel.getMusicService();
        headsetIntercept.setSongPanel(songPanel);
    }

    public void onDestroy() {
        if (mediaSession != null) {
            mediaSession.release();
        }
        headsetIntercept.onDestroy();
    }

    public void onStop() {

    }
}
