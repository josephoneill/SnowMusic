package com.joneill.snowmusic.helper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.KeyEvent;

import com.joneill.snowmusic.data.SongAlbumArtDatabase;
import com.joneill.snowmusic.receiver.MusicControlReceiver;
import com.joneill.snowmusic.service.MusicConnection;
import com.joneill.snowmusic.service.MusicService;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;

import java.util.List;

/**
 * Created by josep on 8/4/2016.
 */
public class SongHelper {
    private static final SongHelper instance = new SongHelper();
    private MusicService musicService;
    private SongPlayerPanel songPlayerPanel;
    private List<Song> currSongList;
    private boolean isReceiverRunning;
    private MusicConnection musicConnection;
    private MusicControlReceiver musicControlReceiver;
    private MediaSessionCompat mediaSession;
    private Intent playIntent;
    private IntentFilter musicControlsFilter;
    private SongAlbumArtDatabase albumArtDb;

    private SongHelper() {

    }

    public void init(Activity mActivity, MusicConnection.OnMusicServicePreparedCallback callback) {
        mediaSession = new MediaSessionCompat(mActivity.getApplicationContext(), "SNOW_MUSIC_AUDIO_SESSION");
        musicConnection = new MusicConnection(callback);
        musicControlReceiver = new MusicControlReceiver(mediaSession);
        albumArtDb = new SongAlbumArtDatabase(mActivity.getApplicationContext());
    }

    public void startServices(Activity mActivity) {
        if(playIntent == null) {
            playIntent = new Intent(mActivity, MusicService.class);
        }
        mActivity.startService(playIntent);
        mActivity.startService(new Intent(mActivity, MusicConnection.class));
        mActivity.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);

        musicControlsFilter = new IntentFilter();
        musicControlsFilter.addAction("com.joneill.snowmusic.action.ACTION_PREV");
        musicControlsFilter.addAction("com.joneill.snowmusic.action.ACTION_PLAY");
        musicControlsFilter.addAction("com.joneill.snowmusic.action.ACTION_NEXT");
        musicControlsFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        //mActivity.registerReceiver(musicControlReceiver, musicControlsFilter);
    }


    public class RemoteControlReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
                    // Handle key press.
                }
            }
        }
    }

    public void onStop(Activity mActivity) {
        //mActivity.unbindService(musicConnection);
    }

    public void onDestroy(Activity mActivity) {
        if(mediaSession != null) {
            mediaSession.release();
        }
        mActivity.unbindService(musicConnection);
        /*mActivity.stopService(playIntent);
        mActivity.unregisterReceiver(musicControlReceiver);
        mActivity.unbindService(musicConnection);*/
    }

    public void onPause(Activity mActivity) {
        //mActivity.unregisterReceiver(musicControlReceiver);
    }

    public void onResume(Activity mActivity) {
        //mActivity.registerReceiver(musicControlReceiver, musicControlsFilter);
    }

    public static SongHelper getInstance() {
        return instance;
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    public SongPlayerPanel getSongPlayerPanel() {
        return songPlayerPanel;
    }

    public void setSongPlayerPanel(SongPlayerPanel songPlayerPanel) {
        this.songPlayerPanel = songPlayerPanel;
        musicControlReceiver.setSongPanel(songPlayerPanel);
    }

    public List<Song> getCurrSongList() {
        return currSongList;
    }

    public void setCurrSongList(List<Song> currSongList) {
        this.currSongList = currSongList;
    }

    public MusicControlReceiver getMusicControlReceiver() {
        return musicControlReceiver;
    }

    public void setMusicControlReceiver(MusicControlReceiver musicControlReceiver) {
        this.musicControlReceiver = musicControlReceiver;
    }

    public IntentFilter getMusicControlsFilter() {
        return musicControlsFilter;
    }

    public void setMusicControlsFilter(IntentFilter musicControlsFilter) {
        this.musicControlsFilter = musicControlsFilter;
    }

    public void setReceiverRunning(boolean isReceiverRunning) {
        musicConnection.setReceiverRunning(isReceiverRunning);
    }

    public SongAlbumArtDatabase getAlbumArtDb() {
        return albumArtDb;
    }
}
