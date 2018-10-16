package com.joneill.snowmusic.song;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joneill.snowmusic.MainActivity;
import com.joneill.snowmusic.R;
import com.joneill.snowmusic.chromecast.MusicChromecastHelper;
import com.joneill.snowmusic.chromecast.MusicWebServer;
import com.joneill.snowmusic.fragments.QueueFragment;
import com.joneill.snowmusic.helper.BitmapHelper;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.SystemBarTintHelper;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.service.MusicService;
import com.joneill.snowmusic.themes.ThemeManager;
import com.joneill.snowmusic.views.SlidingPanelLayout;
import com.joneill.snowmusic.views.ThemedToolbar;
import com.joneill.snowmusic.widgets.MusicSlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by joseph on 1/8/15.
 */
public class SongPlayerPanel implements View.OnClickListener, View.OnLongClickListener {
    private MusicSlidingUpPanelLayout mPanelLayout;
    private SlidingPanelLayout mSlidingPanelLayout;
    private AppCompatActivity mActivity;
    private View mView;
    private boolean startPanelHidden;
    private ThemedToolbar toolbar;
    private MusicService musicService;
    private SystemBarTintHelper tintHelper;
    private Song currSong;
    private ImageButton btnPlayMain, btnPlayPanel, btnNext, btnPrev;
    private ImageView ivPlayMain;
    private int playColor, pauseColor, prevColor, nextColor;
    private TextView currSongTime, totalSongTime;
    private SeekBar musicSeekBar;
    private SeekBarHandler sbHandler;
    private int seekBarMax;
    private boolean isPlaying, isPanelLight, isBarLight, isViewOn, isAppOff, isSeekBarTouched, statusTransparent;
    private MusicChromecastHelper mChrHelper;
    private boolean firstScrollEvent;
    private Animation animation;

    private Settings.OnSettingsChanged settingsCallback;
    public static final String PANEL_SETTINGS_CALLBACK_KEY = "SPP_SETTINGS_KEY";
    private boolean setPlayOnSkip;


    public SongPlayerPanel(AppCompatActivity mActivity, View mView, SystemBarTintHelper tintHelper, View panelLayout, boolean startPanelHidden) {
        this.mActivity = mActivity;
        this.mView = mView;
        this.tintHelper = tintHelper;
        this.mPanelLayout = (MusicSlidingUpPanelLayout) panelLayout;
        this.startPanelHidden = startPanelHidden;
        this.mSlidingPanelLayout = (SlidingPanelLayout) mPanelLayout.findViewById(R.id.dragView);
        currSong = null;
        init();
    }

    public void init() {
        if (startPanelHidden) {
            mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }

        mView.findViewById(R.id.layout_songPanelControls).setOnClickListener(this);

        mChrHelper = ((MainActivity) mActivity).musicChromecastHelper;

        setupPlayerViews();
        setupSettingsCallback();
        initSettings();
    }

    private void setupPlayerViews() {
        statusTransparent = false;
        btnPlayMain = (ImageButton) mView.findViewById(R.id.btn_PlayMain);
        btnPlayPanel = (ImageButton) mView.findViewById(R.id.btn_PlayPanel);
        btnNext = (ImageButton) mView.findViewById(R.id.btn_NextMain);
        btnPrev = (ImageButton) mView.findViewById(R.id.btn_PrevMain);
        ivPlayMain = (ImageView) mView.findViewById(R.id.iv_PlayMain);
        btnPlayMain.setOnClickListener(this);
        btnPlayPanel.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);

        currSongTime = (TextView) mView.findViewById(R.id.tv_current_song_time);
        totalSongTime = (TextView) mView.findViewById(R.id.tv_total_song_time);
        firstScrollEvent = true;

        musicSeekBar = (SeekBar) mView.findViewById(R.id.sb_MusicPlayer);
        musicSeekBar.setOnSeekBarChangeListener(new SeekBarControls());
        mPanelLayout.setPanelSlideListener(panelSlideListener);

        toolbar = (ThemedToolbar) mView.findViewById(R.id.spp_toolbar);
        toolbar.setTitle(mActivity.getResources().getString(R.string.now_playing));
        toolbar.inflateMenu(R.menu.spp_toolbar_menu);
        toolbar.setNavigationIcon(mActivity.getResources().getDrawable(R.drawable.ic_group_expand_00));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.spp_action_queue:
                        openQueue();
                        break;
                }
                return false;
            }
        });
    }

    private void initSettings() {
        setPlayOnSkip = (boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_PLAY_ON_SKIP);
        playColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_BTN_PLAY_COLOR);
        pauseColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_BTN_PREV_COLOR);
        prevColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_BTN_PREV_COLOR);
        nextColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_BTN_NEXT_COLOR);
        if(isPlaying()) {
            ivPlayMain.setColorFilter(pauseColor);
        } else {
            ivPlayMain.setColorFilter(playColor);
        }
        btnPrev.setColorFilter(prevColor);
        btnNext.setColorFilter(nextColor);
    }

    private void initMusicServiceSettings() {
        musicService.setUseProgressBar((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_NOTIF_SHOW_PROGRESS));
        Log.e("set", "joneill music service set");
    }


    public void openSongInPanel(boolean play, Song song, boolean expandPanel) {
        currSong = song;
        if (mPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
            mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        if (expandPanel && mPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
            mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }

        //Set song and artist title
        setPanelTextView(currSong.getTitle(), R.id.tv_slidepanel_song_title);
        setPanelTextView(currSong.getTitle(), R.id.tv_mainSongTitle);
        setPanelTextView(currSong.getArtist(), R.id.tv_slidepanel_song_artist);
        setPanelTextView(currSong.getArtist(), R.id.tv_mainArtistTitle);
        //colorizePanel(currSong.getAlbum());
        if (currSong.isUseSingleArt()) {
            colorizePanel(currSong);
        } else {
            colorizePanel(currSong.getAlbum());
        }

        Album album = currSong.getAlbum();
        if (album == null) {
            return;
        }
        if (album.getPaletteColors().get(Album.PALETTE_COLOR_SECONDARY) != null) {
            int color = album.getPaletteColors().get(Album.PALETTE_COLOR_SECONDARY);
            if (Utils.useWhiteFont(color)) {
                isPanelLight = true;
                if (play) {
                    ivPlayMain.setImageResource(R.drawable.pause_light);
                } else {
                    ivPlayMain.setImageResource(R.drawable.play_light);
                }
                btnNext.setImageResource(R.drawable.next_light);
                btnPrev.setImageResource(R.drawable.prev_light);
            } else {
                isPanelLight = false;
                if (play) {
                    btnPlayMain.setImageResource(R.drawable.pause_circle_light);
                } else {
                    btnPlayMain.setImageResource(R.drawable.play_light);
                }
                btnNext.setImageResource(R.drawable.next_light);
                btnPrev.setImageResource(R.drawable.prev_light);
            }
        }


        //Set main album art
        if (song.getAlbumUri() != null) {
            setAlbumArt(song.getAlbumUri(), R.id.album_art_panel_main);
        } else {
            setAlbumArt(song.getAlbumArt(), R.id.album_art_panel_main);
        }

        //Set thumbnail album art
        setAlbumArt(BitmapHelper.getInstance().circlizeBitmap(currSong.getAlbumArt()), R.id.album_thumbnail);

        //Set song time data
        currSongTime.setText(musicService.getPosition());
        totalSongTime.setText(currSong.getDurationString());

        isPlaying = play;

        seekBarMax = ((int) currSong.getDuration() / 1000) * 3;
        musicSeekBar.setMax(seekBarMax);
        isViewOn = true;
        isSeekBarTouched = false;
        if (play) {
            setControlButtons("pause");
        } else {
            setControlButtons("play");
        }
        if (sbHandler != null) {
            sbHandler.cancel(true);
            while (!sbHandler.isCancelled()) {
            }
        }
        sbHandler = new SeekBarHandler();
        sbHandler.execute();
    }

    public void setPanelTextView(String text, int tvId) {
        TextView textView = (TextView) mActivity.findViewById(tvId);
        textView.setText(text);
    }

    private void colorizePanel(Album album) {
        //colorizePlayerPanel(album);
        colorizePlayerBar(album);
    }

    private void colorizePanel(Song song) {
        colorizePlayerBar(song);
    }

    /*private void colorizePlayerPanel(Album album) {
        View primaryView = mActivity.findViewById(R.id.layout_songPanelControls);
        View secondaryView = mActivity.findViewById(R.id.layout_songInfo);
        TextView title = (TextView) mActivity.findViewById(R.id.tv_mainSongTitle);
        TextView artist = (TextView) mActivity.findViewById(R.id.tv_mainArtistTitle);
        TextView currSongTime = (TextView) mActivity.findViewById(R.id.tv_current_song_time);
        TextView totalSongTime = (TextView) mActivity.findViewById(R.id.tv_total_song_time);

        if ((Boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_SPP_USE_PALETTE)) {
            if (album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN) != null) {
                int color = album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN);
                primaryView.setBackgroundColor(color);
            }

            if (album.getPaletteColors().get(Album.PALETTE_COLOR_SECONDARY) != null) {
                int color = album.getPaletteColors().get(Album.PALETTE_COLOR_SECONDARY);
                secondaryView.setBackgroundColor(color);
                if (Utils.useWhiteFont(color)) {
                    isPanelLight = true;
                    btnPlayMain.setImageResource(R.drawable.pause_circle_light);
                    btnNext.setImageResource(R.drawable.next_light);
                    btnPrev.setImageResource(R.drawable.prev_light);
                } else {
                    isPanelLight = false;
                    btnPlayMain.setImageResource(R.drawable.pause_light);
                    btnNext.setImageResource(R.drawable.next_light);
                    btnPrev.setImageResource(R.drawable.prev_light);
                }
            }

            setTextColor(album, false, true, new TextView[]{title, artist, currSongTime, totalSongTime});
        } else {
            int primaryColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SPP_PRIMARY_COLOR);
            int secondaryColor = Utils.darkenColor(primaryColor, 0.9f);
            primaryView.setBackgroundColor(primaryColor);
            secondaryView.setBackgroundColor(secondaryColor);
            setTextColor(album, Utils.useWhiteFont(primaryColor), false, new TextView[]{title, artist, currSongTime, totalSongTime});
            if (Utils.useWhiteFont(secondaryColor)) {
                isPanelLight = true;
                btnPlayMain.setImageResource(R.drawable.pause_circle_light);
                btnNext.setImageResource(R.drawable.next_light);
                btnPrev.setImageResource(R.drawable.prev_light);
            } else {
                isPanelLight = false;
                btnPlayMain.setImageResource(R.drawable.pause_circle_light);
                btnNext.setImageResource(R.drawable.next_light);
                btnPrev.setImageResource(R.drawable.prev_light);
            }
        }
    } */

    //TODO : Combine into 1 method
    private void colorizePlayerBar(Album album) {
        View panelBar = mActivity.findViewById(R.id.panel_bar);
        View bg = mActivity.findViewById(R.id.dragView);
        TextView barTitle = (TextView) mActivity.findViewById(R.id.tv_slidepanel_song_title);
        TextView barArtist = (TextView) mActivity.findViewById(R.id.tv_slidepanel_song_artist);
        TextView[] panelTextViews = {barTitle, barArtist};

        boolean usePaletteBar = (Boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_SONG_BAR_USE_PALETTE);
        boolean usePalettePanel = (Boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_SPP_USE_PALETTE);
        int panelColor = 0xff0000;
        int barColor = 0xff0000;
        int secondaryColor = 0xff0000;
        if (usePaletteBar || usePalettePanel) {
            if (album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN) != null) {
                barColor = album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN);
                secondaryColor = album.getPaletteColors().get(Album.PALETTE_COLOR_SECONDARY);
                View barBg = usePaletteBar ? panelBar : null;
                View panelBg = usePalettePanel ? bg : null;
                setPanelColor(barBg, panelBg, barColor, barColor, secondaryColor, panelTextViews);
            }
        }

        if (!usePaletteBar || !usePalettePanel) {
            View barBg = usePaletteBar ? null : panelBar;
            View panelBg = usePalettePanel ? null : bg;
            barColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SONG_BAR_COLOR);
            panelColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SPP_PRIMARY_COLOR);
            int sColor = (int) ThemeManager.getInstance().getThemeData().get(ThemeManager.PRIMARY_THEME_COLOR);
            setPanelColor(barBg, panelBg, barColor, panelColor, sColor, panelTextViews);
        }
    }

    private void colorizePlayerBar(final Song song) {
        final View panelBar = mActivity.findViewById(R.id.panel_bar);
        final View bg = mActivity.findViewById(R.id.dragView);
        final TextView barTitle = (TextView) mActivity.findViewById(R.id.tv_slidepanel_song_title);
        final TextView barArtist = (TextView) mActivity.findViewById(R.id.tv_slidepanel_song_artist);
        final TextView[] panelTextViews = {barTitle, barArtist};

        Bitmap artwork = song.getAlbumArt();
        int panelColor = 0xff0000;
        int barColor = 0xff0000;
        final boolean usePaletteBar = (Boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_SONG_BAR_USE_PALETTE);
        final boolean usePalettePanel = (Boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_SPP_USE_PALETTE);

        if (usePaletteBar || usePalettePanel) {
            Palette.from(artwork).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getVibrantSwatch();
                    int color = palette.getVibrantColor(0xff000000 +
                            Integer.parseInt(String.valueOf(ThemeManager.getInstance().getThemeData().get(ThemeManager.PRIMARY_THEME_COLOR)), 16));
                    int secondaryColor = palette.getDarkVibrantColor(0xff000000 +
                            Integer.parseInt(String.valueOf(ThemeManager.getInstance().getThemeData().get(ThemeManager.PRIMARY_THEME_COLOR)), 16));
                    View barBg = usePaletteBar ? panelBar : null;
                    View panelBg = usePalettePanel ? bg : null;

                    setPanelColor(barBg, panelBg, color, color, secondaryColor, panelTextViews);
                }
            });
        }

        if (!usePaletteBar || !usePalettePanel) {
            View barBg = usePaletteBar ? null : panelBar;
            View panelBg = usePalettePanel ? null : bg;
            barColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SONG_BAR_COLOR);
            panelColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SPP_PRIMARY_COLOR);
            int secondaryColor = (int) ThemeManager.getInstance().getThemeData().get(ThemeManager.PRIMARY_THEME_COLOR);
            setPanelColor(barBg, panelBg, barColor, panelColor, secondaryColor, panelTextViews);
        }
    }

    private void setPanelColor(View panelBar, View panelBg, int barColor, int panelColor, int secondaryColor, TextView[] textViews) {
        if (panelBar != null) {
            panelBar.setBackgroundColor(barColor);
        }
        if (panelBg != null) {
            toolbar.setBackgroundColor(panelColor, false);
            panelBg.setBackgroundColor(panelColor);
            setSeekbarColor(secondaryColor, panelColor);
        }
        setTextColor(Utils.useWhiteFont(barColor), textViews);
        if (Utils.useWhiteFont(barColor)) {
            isBarLight = true;
        } else {
            isBarLight = false;
        }

        if(isPlaying) {
            setControlButtons("pause");
        } else {
            setControlButtons("play");
        }
    }

    private void setSeekbarColor(int color, int panelColor) {
        boolean usePalette = (boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_SEEKBAR_USE_PALETTE);
        if(!usePalette) {
            color = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SEEKBAR_COLOR);
        }
        int thumbColor = Utils.useWhiteFont(panelColor) ? Color.WHITE : Color.BLACK;
        musicSeekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        musicSeekBar.getThumb().setColorFilter(new PorterDuffColorFilter(thumbColor, PorterDuff.Mode.SRC_IN));

        btnPlayMain.setColorFilter(color);
    }

    private void setTextColor(Album album, boolean isWhite, boolean usePalette, TextView[] views) {
        if (usePalette && album.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT) != 0) {
            for (int i = 0; i < views.length; i++) {
                views[i].setTextColor(album.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT));
            }
        } else {
            setTextColor(isWhite, views);
        }
    }

    private void setTextColor(boolean isWhite, TextView[] views) {
        if (isWhite) {
            for (int i = 0; i < views.length; i++) {
                views[i].setTextColor(Color.WHITE);
            }
        } else {
            for (int i = 0; i < views.length; i++) {
                views[i].setTextColor(Color.BLACK);
            }
        }
    }

    public void reloadPanelColors() {
        if(currSong != null) {
            if (currSong.isUseSingleArt()) {
                colorizePanel(currSong);
            } else {
                colorizePanel(currSong.getAlbum());
            }
        }
    }

    public void loadPreviousState(Queue queue, int songPos) {
        isPanelLight = true;
        if (musicService != null) {
            musicService.setQueue(queue);
            if (Utils.isMyServiceRunning(MusicService.class, mActivity)) {
                if (!musicService.isPlaying()) {
                    if (musicService.startSong(true)) {
                        openSongInPanel(false, queue.getCurrentSong(), false);
                    }
                    musicService.seek(songPos);
                    setControlButtons("play");
                } else {
                    openSongInPanel(true, queue.getCurrentSong(), false);
                    // playSong();
                    //musicService.start();
                    //isPlaying = true;
                }
                musicSeekBar.setProgress(musicService.getPercentagePlayed(seekBarMax));
            } else {
            }

        }
    }


    public boolean startSong(int index) {
        boolean start = false;
        musicService.setSong(index);

        if (mChrHelper != null && mChrHelper.isConnected()) {
            FileInputStream songFis = null;
            InputStream albumFis = null;
            try {
                songFis = new FileInputStream(currSong.getSongFilePath());
                albumFis = mActivity.getContentResolver().openInputStream(currSong.getAlbumUri());
                //albumFis = new FileInputStream(song.getAlbumUri().getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String clientIp = Utils.getIPAddress(true);
            mChrHelper.serveSong(songFis, MusicWebServer.MIME_TYPES.get("mp3"));
            mChrHelper.serveAlbumArt(albumFis, MusicWebServer.MIME_TYPES.get("jpg"));
            mChrHelper.startSong(songFis, currSong.getTitle(), "http://" + clientIp + ":" + Integer.toString(mChrHelper.getSongWebServer().getPort()) + "/",
                    "http://" + clientIp + ":" + Integer.toString(mChrHelper.getAlbumWebServer().getPort()) + "/", MusicWebServer.MIME_TYPES.get("mp3"), 256, 256);
            start = true;
        } else {
            start = musicService.startSong(false);
        }

        return start;
    }

    //play as in unpause
    public void playSong() {
        if (mChrHelper != null && mChrHelper.isConnected()) {
            mChrHelper.playSong();
        }
        if (musicService != null && !musicService.isPlaying()) {
            musicService.start();
        }
        setControlButtons("pause");
        musicService.setListeners(musicService.contentView);
        try {
            musicService.startForeground(MusicService.NOTIFY_ID, musicService.notification);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        isPlaying = true;
    }

    public void pauseSong() {
        if (mChrHelper != null && mChrHelper.isConnected()) {
            mChrHelper.pauseSong();
        }
        if (musicService != null && musicService.isPlaying()) {
            musicService.pause();
        }
        setControlButtons("play");
        musicService.setListeners(musicService.contentView);
        if (musicService.notification != null) {
            musicService.startForeground(MusicService.NOTIFY_ID, musicService.notification);
        }
        musicService.stopForeground(false);
        //musicService.setListeners(musicService.contentView);
        isPlaying = false;
    }

    private void setControlButtons(String button) {
        switch (button.toUpperCase()) {
            case "PLAY":
                if (isPanelLight) {
                    ivPlayMain.setImageResource(R.drawable.play_light);
                    ivPlayMain.setColorFilter(playColor);
                } else {
                    ivPlayMain.setImageResource(R.drawable.play_dark);
                }

                if (isBarLight) {
                    btnPlayPanel.setImageResource(R.drawable.play_light);
                } else {
                    btnPlayPanel.setImageResource(R.drawable.play_dark);
                }
                break;
            case "PAUSE":
                if (isPanelLight) {
                    ivPlayMain.setImageResource(R.drawable.pause_light);
                    ivPlayMain.setColorFilter(pauseColor);
                } else {
                    ivPlayMain.setImageResource(R.drawable.pause_dark);
                }
                if (isBarLight) {
                    btnPlayPanel.setImageResource(R.drawable.pause_light);
                } else {
                    btnPlayPanel.setImageResource(R.drawable.pause_dark);
                }
                break;
            case "NEXT":
                if (isPanelLight) {
                    btnNext.setImageResource(R.drawable.next_light);
                    btnNext.setColorFilter(nextColor);
                } else {
                    btnNext.setImageResource(R.drawable.next_dark);
                }
                break;
            case "PREV":
                if (isPanelLight) {
                    btnPrev.setImageResource(R.drawable.prev_light);
                    btnPrev.setColorFilter(prevColor);
                } else {
                    btnPrev.setImageResource(R.drawable.prev_dark);
                }
                break;
        }
    }

    private void openQueue() {
        mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        QueueFragment frag = new QueueFragment();
        mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, frag)
                .addToBackStack(null);
        ft.commit();
    }

    public void setAlbumArt(Bitmap albumImage, int id) {
        ImageView v = (ImageView) mActivity.findViewById(id);
        v.setAdjustViewBounds(true);
        v.setImageBitmap(albumImage);
    }

    public void setAlbumArt(Uri uri, int id) {
        ContentResolver res = mActivity.getContentResolver();
        Bitmap albumBitmap = BitmapHelper.getInstance().decodeBitmapFromUri(uri, currSong.getAlbumArt(), res, 1024, 1024);
        ImageView v = (ImageView) mActivity.findViewById(id);
        //v.setAdjustViewBounds(true);
        v.setScaleType(ImageView.ScaleType.CENTER_CROP);
        v.setImageBitmap(albumBitmap);
    }

    @Override
    public boolean onLongClick(View v) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(
                Environment.getExternalStorageDirectory() + "/Download/album.png", options);
        currSong.changeAlbumArt(mActivity, bitmap);


        return false;
    }

    public class SeekBarControls implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mChrHelper != null && mChrHelper.isConnected()) {
                float pos = (progress / (float) seekBarMax) * mChrHelper.getMRemoteMediaPlayer().getStreamDuration();
                final String currPosition = musicService.millisToTime((long) pos);

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currSongTime.setText(currPosition);
                    }
                });
            } else {
                if (musicService == null) return;
                float pos = (progress / (float) seekBarMax) * musicService.getDuration();
                final String currPosition = musicService.millisToTime((long) pos);

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currSongTime.setText(currPosition);
                    }
                });
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarTouched = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarTouched = false;
            if (mChrHelper != null && mChrHelper.isConnected()) {
                float pos = ((float) seekBar.getProgress() / seekBarMax) * mChrHelper.getMRemoteMediaPlayer().getStreamDuration();
                int currPosition = (int) pos;
                mChrHelper.seek((long) currPosition);
            } else {
                if (musicService == null) return;
                float pos = ((float) seekBar.getProgress() / seekBarMax) * musicService.getDuration();
                int currPosition = (int) pos;
                musicService.seek(currPosition);
            }
        }
    }

    public class SeekBarHandler extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while (currSong != null && !isAppOff) {
                if (isPlaying && isViewOn && !isSeekBarTouched) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    onProgressUpdate();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (mChrHelper != null && mChrHelper.isConnected()) {
                musicSeekBar.setProgress(mChrHelper.getPercentagePlayed(seekBarMax));
            } else {
                if (musicService != null) {
                    musicSeekBar.setProgress(musicService.getPercentagePlayed(seekBarMax));
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_PlayMain:
                if (isPlaying) {
                    pauseSong();
                } else {
                    playSong();
                }
                break;

            case R.id.btn_PlayPanel:
                if (isPlaying) {
                    pauseSong();
                } else {
                    playSong();
                }
                break;

            case R.id.btn_NextMain:
                musicService.playNext(false);
                if (!isPlaying && !setPlayOnSkip) {
                    musicService.openSongInPanel(true, false, this, true);
                } else {
                    musicService.openSongInPanel(true, true, this, true);
                }
                break;

            case R.id.btn_PrevMain:
                musicService.playPrev(false);
                if (!isPlaying && !setPlayOnSkip) {
                    musicService.openSongInPanel(false, false, this, true);
                } else {
                    musicService.openSongInPanel(false, true, this, true);
                }
                break;
        }
    }


    private void setupSettingsCallback() {
        settingsCallback = new Settings.OnSettingsChanged() {
            @Override
            public void onSettingsChanged(String key) {
                changeSettings(key);
            }
        };

        Settings.getInstance().getCallbacks().put(PANEL_SETTINGS_CALLBACK_KEY, settingsCallback);
    }

    private void changeSettings(String key) {
        Object value = Settings.getInstance().getSettingsData().get(key);
        switch (key) {
            case Settings.BOOL_PLAY_ON_SKIP:
                setPlayOnSkip = (boolean) value;
                break;
            case Settings.BOOL_SONG_PANEL_CLOSE_OC:
                mSlidingPanelLayout.setShouldCloseOC((boolean) value);
                break;
            case Settings.BOOL_SONG_PANEL_EXPAND_OC:
                mSlidingPanelLayout.setShouldExpandOC((boolean) value);
                break;
            case Settings.BOOL_NOTIF_SHOW_PROGRESS:
                musicService.setUseProgressBar((boolean) value);
                break;
            case Settings.INT_SPP_PRIMARY_COLOR:
                mActivity.findViewById(R.id.dragView).setBackgroundColor((int) value);
                break;
            case Settings.INT_SONG_BAR_COLOR:
                mActivity.findViewById(R.id.panel_bar).setBackgroundColor((int) value);
                break;
            case Settings.INT_BTN_PLAY_COLOR:
                playColor = (int) value;
                if(!isPlaying()) {
                    ivPlayMain.setColorFilter(playColor);
                }
                break;
            case Settings.INT_BTN_PAUSE_COLOR:
                pauseColor = (int) value;
                if(isPlaying()) {
                    ivPlayMain.setColorFilter(playColor);
                }
                break;
            case Settings.INT_BTN_PREV_COLOR:
                prevColor = (int) value;
                btnPrev.setColorFilter(prevColor);
                break;
            case Settings.INT_BTN_NEXT_COLOR:
                nextColor = (int) value;
                btnNext.setColorFilter(nextColor);
                break;
        }
    }

    private SlidingUpPanelLayout.PanelSlideListener panelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            isViewOn = false;
            if (slideOffset > 0) {
                if (mView.findViewById(R.id.panel_bar).getVisibility() != View.GONE) {
                    mView.findViewById(R.id.panel_bar).setVisibility(View.GONE);
                    //mView.findViewById(R.id.next_prev_panel_bar).setVisibility(View.GONE);
                }

            } else {
                if (mView.findViewById(R.id.panel_bar).getVisibility() != View.VISIBLE) {
                    mView.findViewById(R.id.panel_bar).setVisibility(View.VISIBLE);
                   // mView.findViewById(R.id.next_prev_panel_bar).setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onPanelExpanded(View panel) {
            isViewOn = true;
            ((MainActivity) mActivity).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        @Override
        public void onPanelCollapsed(View panel) {
            //mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ((MainActivity) mActivity).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        @Override
        public void onPanelAnchored(View panel) {

        }

        @Override
        public void onPanelHidden(View panel) {

        }
    };

    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
       /* TextView currSongTitle = (TextView) mActivity.findViewById(R.id.tv_slidepanel_song_title);
        TextView currSongArtist = (TextView) mActivity.findViewById(R.id.tv_slidepanel_song_artist);
        ImageView currSongAlbumArt = (ImageView) mActivity.findViewById(R.id.album_thumbnail);*/
        RelativeLayout panel = (RelativeLayout) mActivity.findViewById(R.id.panel_bar);
        ImageView image = (ImageView) mActivity.findViewById(R.id.album_next_thumbnail);
        Queue queue = musicService.getQueue();
        Song nextSong = queue.getSong(queue.getCurrSongIndex() + 1);
        if (nextSong != null) {
            image.setImageBitmap(queue.getSong(queue.getCurrSongIndex() + 1).getAlbumArt());
        }
        LinearLayout.LayoutParams panelMLayoutParams = (LinearLayout.LayoutParams) panel.getLayoutParams();

        panelMLayoutParams.leftMargin = (int) (panelMLayoutParams.leftMargin - distanceX);
        panel.requestLayout();

        if (firstScrollEvent) {
            firstScrollEvent = false;
        }
    }

    public void snapScrollItem() {
        /*final TextView currSongTitle = (TextView) mActivity.findViewById(R.id.tv_slidepanel_song_title);
        final TextView currSongArtist = (TextView) mActivity.findViewById(R.id.tv_slidepanel_song_artist);
        final ImageView currSongAlbumArt = (ImageView) mActivity.findViewById(R.id.album_thumbnail);
        final RelativeLayout.LayoutParams cstMLayoutParams = (RelativeLayout.LayoutParams) currSongTitle.getLayoutParams();
        final RelativeLayout.LayoutParams csaMLayoutParams = (RelativeLayout.LayoutParams) currSongArtist.getLayoutParams();
        final RelativeLayout.LayoutParams csaaMLayoutParams = (RelativeLayout.LayoutParams) currSongAlbumArt.getLayoutParams();*/

        final RelativeLayout panel = (RelativeLayout) mActivity.findViewById(R.id.panel_bar);
        final LinearLayout.LayoutParams panelMLayoutParams = (LinearLayout.LayoutParams) panel.getLayoutParams();
        final int startValue = panelMLayoutParams.leftMargin;
        final int endValue = 0;

        panel.clearAnimation();
        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int leftMarginInterpolatedValue = (int) (startValue + ((endValue) - startValue) * interpolatedTime);
                panelMLayoutParams.leftMargin = leftMarginInterpolatedValue;
                panel.requestLayout();
            }
        };

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                panel.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                panel.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        mView.startAnimation(animation);
    }

    public void onDestroy() {
        isAppOff = true;
        if (sbHandler != null) {
            sbHandler.cancel(true);
            sbHandler = null;
        }
    }

    public void onPause() {
        isViewOn = false;
    }

    public void onResume() {
        isViewOn = true;
    }

    public Song getCurrSong() {
        return currSong;
    }

    public boolean getFirstScrollEvent() {
        return firstScrollEvent;
    }

    public void setFirstScrollEvent(boolean firstScrollEvent) {
        this.firstScrollEvent = firstScrollEvent;
    }

    public SlidingUpPanelLayout getmPanelLayout() {
        return mPanelLayout;
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
        initMusicServiceSettings();
    }

    public SeekBarHandler getSbHandler() {
        return sbHandler;
    }

    public boolean isSetPlayOnSkip() {
        return setPlayOnSkip;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
