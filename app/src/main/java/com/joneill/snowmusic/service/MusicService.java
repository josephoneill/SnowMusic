package com.joneill.snowmusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.joneill.snowmusic.MainActivity;
import com.joneill.snowmusic.R;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.data.SongsUserInfo;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.receiver.MusicControlReceiver;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Queue;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.themes.ThemeManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * Created by joseph on 1/9/15.
 */
public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    public static final String NOTIFICATION_EXIT_RECEIVER = "com.joneill.snowmusic.exitnotif_receive";
    private MediaPlayer player;
    private MusicControlReceiver mcReceiver;
    //private List<Song> songs;
    private Queue queue;
    //private int songPos;
    private final IBinder musicBind = new MusicBinder();
    public Notification notification;
    public NotificationCompat.Builder builder;
    public NotificationManager manager;
    public RemoteViews contentView;
    private String songTitle = "";
    private String songArtist = "";
    private Bitmap albumBitmap = null;
    private Boolean onCompleted = false;
    public static final int NOTIFY_ID = 1;
    //If skipped before reaching 25%, song will count as skip
    public static final float SONG_PERCENT_SKIP = 0.25f;

    //Notif Stuff
    private float mProgressStatus = 0;
    private int mLastProgressStatus = 0;
    private boolean runNotifThread;
    private Thread mNotifProgressThread;
    private Handler mNotifProgressHandler;
    private boolean useProgressBar;

    private final BroadcastReceiver notifExitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onDestroy();
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        queue = new Queue();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifProgressHandler = new Handler();
        initMusicPlayer();
    }

    private void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(List<Song> songsList) {
        queue.setQueueList(songsList);
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public List<Song> getList() {
        return queue.getQueueList();
    }

    public boolean startSong(boolean waitForPrepared) {
        player.reset();
        if (queue.getCurrSongIndex() < 0) {
            return false;
        }

        Song selSong = queue.getCurrentSong();

        //Increment song play count
       /* String key = SongsUserInfo.INT_USD_PLAY_COUNT + selSong.getId();
        int playCount = (int) SongsUserInfo.getInstance().getSongsUserInfo().get(key);
        playCount++;

        SongsUserInfo.getInstance().getSongsUserInfo().put(key, playCount);
        SongsUserInfo.getInstance().saveSongUserInfo();
        //end song play count methods
        */

        songTitle = selSong.getTitle();
        songArtist = selSong.getArtist();
        albumBitmap = selSong.getAlbumArt();

        long currSong = selSong.getId();
        //Get Uri of song
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
        try {
            //Tell the player the song to play
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("Exception", "Exception " + e.getMessage());
        }

        //Will prepare the song and call onPrepare of player
        try {
            if(!waitForPrepared) {
                player.prepareAsync();
            } else {
                player.prepare();
            }
        } catch (IllegalStateException e) {
            Log.e("Exception", "IllegalStateException: " + e.getMessage());
            //e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void openSongInPanel(boolean isNext, boolean play, SongPlayerPanel mPanel, boolean expandPanel) {
        if (startSong(false)) {
            mPanel.openSongInPanel(play, queue.getCurrentSong(), expandPanel);
        } else {
            if (isNext) {
                playNext(play);
                mPanel.openSongInPanel(play, queue.getCurrentSong(), expandPanel);
            } else {
                playPrev(play);
                mPanel.openSongInPanel(play, queue.getCurrentSong(), expandPanel);
            }
        }
    }

    public void setSong(int songIndex) {
        queue.setCurrSongIndex(songIndex);
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(queue == null || queue.getCurrentSong() == null) { return; }
        if (mp.getCurrentPosition() >= queue.getCurrentSong().getDuration() - 1000) {
            /** Increment completed song count and song total play duration **/
            String key = SongsUserInfo.INT_COMPLETE_PLAY_COUNT + queue.getCurrentSong().getId();
            int completedCount = (int) SongsUserInfo.getInstance().getSongsUserInfo().get(key);
            completedCount++;
            SongsUserInfo.getInstance().getSongsUserInfo().put(key, completedCount);

            key = SongsUserInfo.INT_TOTAL_SONG_PLAY_DURATION + queue.getCurrentSong().getId();
            int currSongDur = (int) (queue.getCurrentSong().getDuration() / 1000.0f);
            int totalSongPlayDur = (int) SongsUserInfo.getInstance().getSongsUserInfo().get(key);
            totalSongPlayDur += currSongDur;
            SongsUserInfo.getInstance().getSongsUserInfo().put(key, totalSongPlayDur);
            SongsUserInfo.getInstance().saveSongUserInfo();
            /** Finish increments **/
            onCompleted = true;

            seek(0);
            playNext(false);
            openSongInPanel(true, true, SongDataHolder.getInstance().getSongPanel(), false);
            onCompleted = false;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //TODO: Add x number of tries before skipping
        if (extra == -1010) {
            startSong(false);
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        if (!SongDataHolder.getInstance().getSongPanel().isPlaying() && !SongDataHolder.getInstance().getSongPanel().isSetPlayOnSkip()) {

        } else {
            this.player.start();
            runNotifThread = true;
        }
        createNotif();
    }

    private void createNotif() {
        Intent nIntent = new Intent(this, MainActivity.class);
        nIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //eIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0,
                nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent eIntent = new Intent(NOTIFICATION_EXIT_RECEIVER);
        PendingIntent exitPIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, eIntent, 0);
        registerReceiver(notifExitReceiver, new IntentFilter(NOTIFICATION_EXIT_RECEIVER));

        builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(pIntent)
                .setDeleteIntent(exitPIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(songTitle)
                .setContentText(songArtist);


        notification = builder.build();

        contentView = new RemoteViews(this.getPackageName(), R.layout.notification_layout);

        contentView.setProgressBar(R.id.notif_progress_bar, 100, 0, false);
        createNotifThread();

        setListeners(contentView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.bigContentView = contentView;
        } else {
            notification.contentView = contentView;
        }
        //setListeners(contentView);

        startForeground(NOTIFY_ID, notification);
    }

    public void setListeners(RemoteViews contentView) {
        if (contentView == null) {
            return;
        }
        contentView.setImageViewBitmap(R.id.notif_album_art, albumBitmap);
        contentView.setTextViewText(R.id.notif_song_title, songTitle);
        contentView.setTextViewText(R.id.notif_artist_title, songArtist);
        colorizeNotif(contentView);

        if (isPlaying()) {
            contentView.setImageViewResource(R.id.btn_notif_play, R.drawable.pause_light);
        } else {
            contentView.setImageViewResource(R.id.btn_notif_play, R.drawable.play_light);
        }

        Intent prevIntent = new Intent("com.joneill.snowmusic.action.ACTION_PREV");
        Intent playIntent = new Intent("com.joneill.snowmusic.action.ACTION_PLAY");
        Intent nextIntent = new Intent("com.joneill.snowmusic.action.ACTION_NEXT");

        PendingIntent pendingPrevIntent = PendingIntent.getBroadcast(this, 100, prevIntent, 0);
        PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 200, playIntent, 0);
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(this, 300, nextIntent, 0);

        contentView.setOnClickPendingIntent(R.id.btn_notif_prev, pendingPrevIntent);
        contentView.setOnClickPendingIntent(R.id.btn_notif_play, pendingPlayIntent);
        contentView.setOnClickPendingIntent(R.id.btn_notif_next, pendingNextIntent);
    }

    private void colorizeNotif(final RemoteViews contentView) {
        Song song = queue.getCurrentSong();
        if((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_NOTIF_USE_PALETTE)) {
            if (song.isUseSingleArt()) {
                Bitmap artwork = null;
                if(song.getAlbumArt() != null) {
                    artwork = song.getAlbumArt();
                } else {

                }

                Palette.from(artwork).generate(new Palette.PaletteAsyncListener() {
                    int color = 0xff0000;
                    int dividerColor = 0xff000;

                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getVibrantSwatch();
                        color = palette.getVibrantColor(0xff000000 +
                                Integer.parseInt(String.valueOf(ThemeManager.getInstance().getThemeData().get(ThemeManager.PRIMARY_THEME_COLOR)), 16));
                        dividerColor = palette.getMutedColor(0xff000000 +
                                Integer.parseInt(String.valueOf(ThemeManager.getInstance().getThemeData().get(ThemeManager.PRIMARY_THEME_COLOR)), 16));
                        contentView.setInt(R.id.notif_main_view, "setBackgroundColor", color);
                        //contentView.setInt(R.id.notif_divider, "setBackgroundColor", dividerColor);
                        startForeground(NOTIFY_ID, notification);
                        if (!isPlaying()) {
                            stopForeground(false);
                        }
                        //setNotifViewColors(contentView, Utils.useWhiteFont(color), new int[]{R.id.notif_artist_title, R.id.notif_artist_title});
                    }
                });
            } else {
                int color = 0xff0000;
                if ((Boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_SONG_BAR_USE_PALETTE)) {
                    if (song.getAlbum().getPaletteColors().get(Album.PALETTE_COLOR_MAIN) != null) {
                        color = song.getAlbum().getPaletteColors().get(Album.PALETTE_COLOR_MAIN);
                    }
                } else {
                    color = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SONG_BAR_COLOR);
                }
                //contentView.setInt(R.id.notif_main_view, "setBackgroundResource", color);
                //setNotifViewColors(contentView, Utils.useWhiteFont(color), new int[]{R.id.notif_artist_title, R.id.notif_artist_title});
            }
        } else {
            int color = (int) Settings.getInstance().getSettingsData().get(Settings.INT_NOTIF_BG_COLOR);
            contentView.setInt(R.id.notif_main_view, "setBackgroundColor", color);
        }
    }

    private void setNotifViewColors(RemoteViews contentView, boolean useWhite, int[] tvIds) {
        if (useWhite) {
            for (int i = 0; i < tvIds.length; i++) {
                contentView.setTextColor(tvIds[i], Color.WHITE);
            }
            contentView.setImageViewResource(R.id.btn_notif_prev, R.drawable.prev_light);
            contentView.setImageViewResource(R.id.btn_notif_play, R.drawable.play_light);
            contentView.setImageViewResource(R.id.btn_notif_next, R.drawable.next_light);
        } else {
            for (int i = 0; i < tvIds.length; i++) {
                contentView.setTextColor(tvIds[i], Color.BLACK);
            }
            contentView.setImageViewResource(R.id.btn_notif_prev, R.drawable.prev_dark);
            contentView.setImageViewResource(R.id.btn_notif_play, R.drawable.play_dark);
            contentView.setImageViewResource(R.id.btn_notif_next, R.drawable.next_dark);
        }
    }

    private void createNotifThread() {
        if (mNotifProgressThread == null && useProgressBar) {
            runNotifThread = true;
            mNotifProgressThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        //if (player == null || !useProgressBar) {

                        //} else {
                        if(player != null && useProgressBar) {
                            if (runNotifThread) {
                                boolean isPlaying = false;
                                try {
                                    isPlaying = player.isPlaying();
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }
                                if (isPlaying) {
                                    mProgressStatus = getPercentagePlayed();
                                    //TODO: Figure out what this does
                                    String iDontKnowWhatThisDoesButItsNecessary =
                                            "joneill percent is " + String.valueOf((int) mProgressStatus);
                                    if ((int) mProgressStatus > mLastProgressStatus ||
                                            (mLastProgressStatus - (int) mProgressStatus) >= 5) {
                                        mLastProgressStatus++;
                                        mLastProgressStatus = (int) mProgressStatus;
                                        // Update the pro*gress bar
                                        mNotifProgressHandler.post(new Runnable() {
                                            public void run() {
                                                contentView.setProgressBar(R.id.notif_progress_bar, 100, (int) mProgressStatus, false);
                                                startForeground(NOTIFY_ID, notification);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            });
            mNotifProgressThread.start();
        } else {
            mProgressStatus = 0;
            mLastProgressStatus = 0;
        }
    }

    public void playPrev(boolean play) {
        runNotifThread = false;
        if(player.getCurrentPosition() < 2000) {
            queue.playPrevSong();
        }

        if (play) {
            startSong(false);
        }
    }

    public void playNext(boolean play) {
        /*float songPosPercent = player.getCurrentPosition() / player.getDuration();
        int prevSongPos = queue.getCurrSongIndex();*/
        //player.reset();
        runNotifThread = false;
        queue.playNextSong();
        if (play) {
            startSong(false);
        }

        //Check and increment song skip counter
       /* if (songPosPercent < SONG_PERCENT_SKIP && !onCompleted) {
            String key = SongsUserInfo.INT_SKIP_COUNT + queue.getSong(prevSongPos).getId();
            int skipCount = (int) SongsUserInfo.getInstance().getSongsUserInfo().get(key);
            skipCount++;
            SongsUserInfo.getInstance().getSongsUserInfo().put(key, skipCount);
            SongsUserInfo.getInstance().saveSongUserInfo();
        }

        if (!onCompleted) {
            int songPlayedDur = (int) ((queue.getSong(prevSongPos).getDuration() * songPosPercent) / 1000.0f);
            String key = SongsUserInfo.INT_TOTAL_SONG_PLAY_DURATION + queue.getSong(prevSongPos).getId();
            int totalSongPlayDur = (int) SongsUserInfo.getInstance().getSongsUserInfo().get(key);
            totalSongPlayDur += songPlayedDur;
            SongsUserInfo.getInstance().getSongsUserInfo().put(key, totalSongPlayDur);
            SongsUserInfo.getInstance().saveSongUserInfo();
        } */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        if (mcReceiver != null) {
            mcReceiver.onDestroy();
            mcReceiver = null;
        }
        if (mNotifProgressThread != null) {
            runNotifThread = false;
            mNotifProgressThread.interrupt();
        }
        try {
            unregisterReceiver(mcReceiver);
            unregisterReceiver(notifExitReceiver);
            //unbindService();
        } catch (IllegalArgumentException e) {
            // e.printStackTrace();
        }
        stopForeground(true);
        stopSelf();
    }

    public Queue getQueue() {
        return queue;
    }

    //start or unpause
    public void start() {
        player.start();
    }

    public void pause() {
        player.pause();
    }

    public void seek(int position) {
        player.seekTo(position);
    }

    public String getPosition() {
        String pos = millisToTime(player.getCurrentPosition());
        return pos;
    }

    public int getIntPosition() {
        if(player != null) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    public int getPercentagePlayed() {
        float dur = 0;
        float pos = 0;
        if (player == null) {
            return 0;
        }
        if (player.isPlaying()) {
            pos = (float) player.getCurrentPosition();
            dur = (float) player.getDuration();
            if (dur == 0) {
                return 0;
            }
        }
        int percent = (int) ((pos / dur) * 100);
        return percent;
    }

    public int getPercentagePlayed(int seekBarMax) {
        int percent = (int) (((float) player.getCurrentPosition() / (float) player.getDuration()) * seekBarMax);
        return percent;
    }



    //Returns duration in milliseconds
    public int getDuration() {
        return player.getDuration();
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public String millisToTime(long millis) {
        String time = Utils.millisToTime(millis);
        return time;
    }

    public void bindMusicReceiver(MusicControlReceiver receiver, IntentFilter intentFilter) {
        registerReceiver(receiver, intentFilter);
        this.mcReceiver = receiver;
    }

    public MusicControlReceiver getMcReceiver() {
        return mcReceiver;
    }

    public static void setMediaPlayerDataSource(Context context,
                                                MediaPlayer mp, String fileInfo) throws Exception {

        if (fileInfo.startsWith("content://")) {
            try {
                Uri uri = Uri.parse(fileInfo);
                fileInfo = getRingtonePathFromContentUri(context, uri);
            } catch (Exception e) {
            }
        }

        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB)
                try {
                    setMediaPlayerDataSourcePreHoneyComb(context, mp, fileInfo);
                } catch (Exception e) {
                    setMediaPlayerDataSourcePostHoneyComb(context, mp, fileInfo);
                }
            else
                setMediaPlayerDataSourcePostHoneyComb(context, mp, fileInfo);

        } catch (Exception e) {
            try {
                setMediaPlayerDataSourceUsingFileDescriptor(context, mp,
                        fileInfo);
            } catch (Exception ee) {
                String uri = getRingtoneUriFromPath(context, fileInfo);
                mp.reset();
                mp.setDataSource(uri);
            }
        }
    }

    private static void setMediaPlayerDataSourcePreHoneyComb(Context context,
                                                             MediaPlayer mp, String fileInfo) throws Exception {
        mp.reset();
        mp.setDataSource(fileInfo);
    }

    private static void setMediaPlayerDataSourcePostHoneyComb(Context context,
                                                              MediaPlayer mp, String fileInfo) throws Exception {
        mp.reset();
        mp.setDataSource(context, Uri.parse(Uri.encode(fileInfo)));
    }

    private static void setMediaPlayerDataSourceUsingFileDescriptor(
            Context context, MediaPlayer mp, String fileInfo) throws Exception {
        File file = new File(fileInfo);
        FileInputStream inputStream = new FileInputStream(file);
        mp.reset();
        mp.setDataSource(inputStream.getFD());
        inputStream.close();
    }

    private static String getRingtoneUriFromPath(Context context, String path) {
        Uri ringtonesUri = MediaStore.Audio.Media.getContentUriForPath(path);
        Cursor ringtoneCursor = context.getContentResolver().query(
                ringtonesUri, null,
                MediaStore.Audio.Media.DATA + "='" + path + "'", null, null);
        ringtoneCursor.moveToFirst();

        long id = ringtoneCursor.getLong(ringtoneCursor
                .getColumnIndex(MediaStore.Audio.Media._ID));
        ringtoneCursor.close();

        if (!ringtonesUri.toString().endsWith(String.valueOf(id))) {
            return ringtonesUri + "/" + id;
        }
        return ringtonesUri.toString();
    }

    public static String getRingtonePathFromContentUri(Context context,
                                                       Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor ringtoneCursor = context.getContentResolver().query(contentUri,
                proj, null, null, null);
        ringtoneCursor.moveToFirst();

        String path = ringtoneCursor.getString(ringtoneCursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

        ringtoneCursor.close();
        return path;
    }

    public void setUseProgressBar(boolean useProgressBar) {
        this.useProgressBar = useProgressBar;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
