package com.joneill.snowmusic.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.themes.ThemeManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by josep_000 on 7/25/2015.
 */
public class Settings {
    private static final Settings instance = new Settings();
    private SharedPreferences sharedPreferences;
    private HashMap<String, Object> settingsData;
    private HashMap<String, OnSettingsChanged> callbacks;

    public static final float CURRENT_SETTINGS_VERSION = 1.0f;

    public static final String FLOAT_SETTINGS_VERSION = "com.joneill.snowmusic.FLOAT_settings_version";
    public static final String BOOL_CREATED_ALBUM_SETTINGS = "com.joneill.snowmusic.BOOL_created_album_settings";
    public static final String BOOL_ALP_HEADER_USE_PALETTE = "com.joneill.snowmusic.BOOL_alp_head_use_palette_";

    public static final String INT_ALP_HEADER_COLOR = "com.joneill.snowmusic.INT_alp_header_color_";

    //Background settings for tabs
    public static final String INT_ALBUMS_FRAG_BG = "com.joneill.snowmusic.INT_albums_frag_bg";
    public static final String INT_ARTISTS_FRAG_BG = "com.joneill.snowmusic.INT_artists_frag_bg";
    public static final String INT_GENRES_FRAG_BG = "com.joneill.snowmusic.INT_genres_frag_bg";
    public static final String INT_SONGS_FRAG_BG = "com.joneill.snowmusic.INT_songs_frag_bg";
    public static final String INT_PLAYLISTS_FRAG_BG = "com.joneill.snowmusic.INT_playlists_frag_bg";

    //Song Panel Settings
    public static final String BOOL_SONG_PANEL_EXPAND_OC = "com.joneill.snowmusic.BOOL_song_panel_expand_oc";
    public static final String BOOL_SONG_PANEL_CLOSE_OC = "com.joneill.snowmusic.BOOL_song_panel_close_oc";
    public static final String BOOL_SEEKBAR_USE_PALETTE = "com.joneill.snowmusic.BOOL_seekbar_use_palette";
    public static final String INT_SEEKBAR_COLOR = "com.joneill.snowmusic.INT_seekbar_color";
    public static final String BOOL_PLAY_ON_SKIP = "com.joneill.snowmusic.BOOL_play_on_skip";

    //Song Panel Design
    public static final String BOOL_SPP_USE_PALETTE = "com.joneill.snowmusic.BOOL_spp_use_palette";
    public static final String BOOL_SONG_BAR_USE_PALETTE = "com.joneill.snowmusic.BOOL_song_bar_use_palette";
    public static final String INT_SPP_PRIMARY_COLOR = "com.joneill.snowmusic.INT_spp_primary_color";
    public static final String INT_SONG_BAR_COLOR = "com.joneill.snowmusic.INT_song_bar_color";
    public static final String INT_BTN_PLAY_COLOR = "com.joneill.snowmusic.INT_btn_play_color";
    public static final String INT_BTN_PAUSE_COLOR = "com.joneill.snowmusic.INT_btn_pause_color";
    public static final String INT_BTN_NEXT_COLOR = "com.joneill.snowmusic.INT_btn_next_color";
    public static final String INT_BTN_PREV_COLOR = "com.joneill.snowmusic.INT_btn_prev_color";

    //Headset Settings
    public static final String BOOL_RES_ON_CON = "com.joneill.snowmusic.BOOL_resume_on_connect";
    public static final String BOOL_PAUSE_ON_DIS = "com.joneill.snowmusic.BOOL_pause_on_disconnect";
    public static final String BOOL_USE_QUICK_PAUSE = "com.joneill.snowmusic.BOOL_headset_use_quickpause";

    //Notification Settings
    public static final String BOOL_NOTIF_USE_PALETTE = "com.joneill.snowmusic.BOOL_notif_use_palette";
    public static final String INT_NOTIF_BG_COLOR = "com.joneill.snowmusic.INT_notif_bg_color";
    public static final String BOOL_NOTIF_SHOW_PROGRESS = "com.joneill.snowmusic.BOOL_notif_show_progress";

    //Card Settings
    public static final String BOOL_ALBUM_CARD_USE_PALETTE = "com.joneill.snowmusic.BOOL_album_card_use_palette_";
    public static final String INT_ALBUM_CARD_COLOR = "com.joneill.snowmusic.INT_album_card_color_";
    public static final String BOOL_ARTIST_CARD_USE_PALETTE = "com.joneill.snowmusic.BOOL_artist_card_use_palette_";
    public static final String INT_ARTIST_CARD_COLOR = "com.joneill.snowmusic.INT_artist_card_color_";
    public static final String BOOL_GENRE_CARD_USE_PALETTE = "com.joneill.snowmusic.BOOL_genre_card_use_palette_";
    public static final String INT_GENRE_CARD_COLOR = "com.joneill.snowmusic.INT_genre_card_color_";
    public static final String BOOL_PLAYLIST_CARD_USE_PALETTE = "com.joneill.snowmusic.BOOL_playlist_card_use_palette_";
    public static final String INT_PLAYLIST_CARD_COLOR = "com.joneill.snowmusic.INT_playlist_card_color_";

    //Toolbar colors
    public static final String INT_SINGLE_TOOLBAR_COLOR = "com.joneill.snowmusic.INT_single_toolbar_color";
    public static final String INT_ALBUM_TOOLBAR_COLOR = "com.joneill.snowmusic.INT_album_toolbar_color";
    public static final String INT_ARTIST_TOOLBAR_COLOR = "com.joneill.snowmusic.INT_artist_toolbar_color";
    public static final String INT_GENRE_TOOLBAR_COLOR = "com.joneill.snowmusic.INT_genre_toolbar_color";
    public static final String INT_SONG_TOOLBAR_COLOR = "com.joneill.snowmusic.INT_song_toolbar_color";
    public static final String INT_PLAYLIST_TOOLBAR_COLOR = "com.joneill.snowmusic.INT_playlist_toolbar_color";

    private Settings() {
        settingsData = new HashMap<>();
        callbacks = new HashMap<>();
    }

    //Must be called before you apply a theme
    public void loadSettings(Activity mActivity) {
        sharedPreferences = mActivity.getSharedPreferences("com.joneill.snowmusic", Context.MODE_PRIVATE);

        Map<String, ?> keys = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            settingsData.put(key, value);
        }
    }

    public void saveSettings() {
        for (Map.Entry<String, Object> entry : settingsData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (sharedPreferences == null) {
                Log.e("SharedPreference Error!", "Must call loadTheme() before you can apply a theme!");
                return;
            }

            if (key.contains("INT")) {
                int currValue = (int) value;
                sharedPreferences.edit().putInt(key, currValue).apply();
            } else if (key.contains("STRING")) {
                String currValue = (String) value;
                sharedPreferences.edit().putString(key, currValue).apply();
            } else if (key.contains("BOOL")) {
                boolean currValue = (boolean) value;
                sharedPreferences.edit().putBoolean(key, currValue).apply();
            } else if (key.contains("FLOAT")) {
                float currValue = (float) value;
                sharedPreferences.edit().putFloat(key, currValue).apply();
            }
        }
    }

    public void createGenericSettings(Context context) {
        int primaryColor = (int) ThemeManager.getInstance().getThemeData().get(ThemeManager.PRIMARY_THEME_COLOR);
        putSettingsData(FLOAT_SETTINGS_VERSION, CURRENT_SETTINGS_VERSION);

        putSettingsData(BOOL_PLAY_ON_SKIP, true);
        putSettingsData(BOOL_SONG_PANEL_EXPAND_OC, true);
        putSettingsData(BOOL_SONG_PANEL_CLOSE_OC, false);

        //Frag BGs
        putSettingsData(INT_ALBUMS_FRAG_BG, context.getResources().getColor(R.color.background_gray));
        putSettingsData(INT_ARTISTS_FRAG_BG, context.getResources().getColor(R.color.background_gray));
        putSettingsData(INT_GENRES_FRAG_BG, context.getResources().getColor(R.color.background_gray));
        putSettingsData(INT_SONGS_FRAG_BG, context.getResources().getColor(R.color.background_gray));
        putSettingsData(INT_PLAYLISTS_FRAG_BG, context.getResources().getColor(R.color.background_gray));

        //Headset
        putSettingsData(BOOL_PAUSE_ON_DIS, true);
        putSettingsData(BOOL_RES_ON_CON, false);
        putSettingsData(BOOL_USE_QUICK_PAUSE, true);

        //Notification
        putSettingsData(BOOL_NOTIF_USE_PALETTE, true);
        putSettingsData(INT_NOTIF_BG_COLOR, Color.parseColor("#292725")); //TODO : Find out why I'm doing this
        putSettingsData(BOOL_NOTIF_SHOW_PROGRESS, true);

        //Card
        putSettingsData(BOOL_ALBUM_CARD_USE_PALETTE, true);
        putSettingsData(INT_ALBUM_CARD_COLOR, primaryColor);
        putSettingsData(BOOL_ARTIST_CARD_USE_PALETTE, true);
        putSettingsData(INT_ARTIST_CARD_COLOR, primaryColor);
        putSettingsData(BOOL_GENRE_CARD_USE_PALETTE, true);
        putSettingsData(INT_GENRE_CARD_COLOR, primaryColor);
        putSettingsData(BOOL_PLAYLIST_CARD_USE_PALETTE, true);
        putSettingsData(INT_PLAYLIST_CARD_COLOR, primaryColor);

        //Toolbar Colors
        putSettingsData(INT_SINGLE_TOOLBAR_COLOR, primaryColor);
        putSettingsData(INT_ALBUM_TOOLBAR_COLOR, primaryColor);
        putSettingsData(INT_ARTIST_TOOLBAR_COLOR, primaryColor);
        putSettingsData(INT_GENRE_TOOLBAR_COLOR, primaryColor);
        putSettingsData(INT_SONG_TOOLBAR_COLOR, primaryColor);
        putSettingsData(INT_PLAYLIST_TOOLBAR_COLOR, primaryColor);

        //SPP Design
        putSettingsData(BOOL_SPP_USE_PALETTE, true);
        putSettingsData(INT_SPP_PRIMARY_COLOR, primaryColor);
        putSettingsData(BOOL_SONG_BAR_USE_PALETTE, false);
        putSettingsData(INT_SONG_BAR_COLOR, Color.WHITE);
        putSettingsData(BOOL_SEEKBAR_USE_PALETTE, false);
        putSettingsData(INT_SEEKBAR_COLOR, primaryColor);
        putSettingsData(INT_BTN_PLAY_COLOR, Color.WHITE);
        putSettingsData(INT_BTN_PAUSE_COLOR, Color.WHITE);
        putSettingsData(INT_BTN_PREV_COLOR, Color.WHITE);
        putSettingsData(INT_BTN_NEXT_COLOR, Color.WHITE);


        saveSettings();
    }

    public void createAlbumSettings(List<Album> albums) {
        for (Album album : albums) {
            putSettingsData(BOOL_ALP_HEADER_USE_PALETTE + String.valueOf(album.getAlbumId()), true);
            putSettingsData(INT_ALP_HEADER_COLOR + String.valueOf(album.getAlbumId()), Color.WHITE);
            //putSettingsData(BOOL_ALBUM_CARD_USE_PALETTE + String.valueOf(album.getAlbumId()), true);
            //putSettingsData(INT_ALBUM_CARD_COLOR + String.valueOf(album.getAlbumId()), Color.WHITE);
        }

        settingsData.put(BOOL_CREATED_ALBUM_SETTINGS, true);
        saveSettings();
    }

    private void putSettingsData(String key, Object value) {
        if (settingsData.get(key) == null) {
            settingsData.put(key, value);
        }
    }

    public interface OnSettingsChanged {
        void onSettingsChanged(String key);
    }


    public void addCallback(String key, OnSettingsChanged callback) {
        callbacks.put(key, callback);
    }

    public HashMap<String, OnSettingsChanged> getCallbacks() {
        return callbacks;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static Settings getInstance() {
        return instance;
    }

    public HashMap<String, Object> getSettingsData() {
        return settingsData;
    }
}
