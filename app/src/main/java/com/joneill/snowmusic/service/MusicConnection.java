package com.joneill.snowmusic.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.joneill.snowmusic.helper.SongHelper;

/**
 * Created by josep on 8/4/2016.
 */
public class MusicConnection implements ServiceConnection {
    private boolean musicBound;
    private boolean isReceiverRunning;

    public static interface OnMusicServicePreparedCallback {
        void onMusicServicePrepared(MusicService musicService);
    }

    private OnMusicServicePreparedCallback callback;

    public MusicConnection(OnMusicServicePreparedCallback callback) {
        musicBound = false;
        this.callback = callback;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
        //Get service
        MusicService musicService = binder.getService();
        SongHelper.getInstance().setMusicService(musicService);
        if (isReceiverRunning) {
            try {
                musicService.unregisterReceiver(SongHelper.getInstance().getMusicControlReceiver());
                //musicService.bindMusicReceiver(SongHelper.getInstance().getMusicControlReceiver(), SongHelper.getInstance().getMusicControlsFilter());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error", "Error unregistering receiver, error is : " + e.getMessage());
            }
        } else {
            musicService.bindMusicReceiver(SongHelper.getInstance().getMusicControlReceiver(), SongHelper.getInstance().getMusicControlsFilter());
        }
        if (SongHelper.getInstance().getCurrSongList() != null) {
            musicService.setList(SongHelper.getInstance().getCurrSongList());
        }
        if (SongHelper.getInstance().getSongPlayerPanel() != null) {
            SongHelper.getInstance().getSongPlayerPanel().setMusicService(musicService);
        }
        callback.onMusicServicePrepared(musicService);
        musicBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicBound = false;
    }

    public void setReceiverRunning(boolean isReceiverRunning) {
        this.isReceiverRunning = isReceiverRunning;
    }
}
