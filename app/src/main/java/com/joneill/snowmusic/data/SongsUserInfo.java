package com.joneill.snowmusic.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.joneill.snowmusic.song.Song;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by josep_000 on 8/5/2015.
 */
public class SongsUserInfo {
    private static final SongsUserInfo instance = new SongsUserInfo();

    private SharedPreferences sharedPreferences;
    private HashMap<String, Object> songsUserInfo;


    //Keys for the user song info
    public static final String INT_USD_PLAY_COUNT = "com.joneill.snowmusic.sui.INT_usd_play_count_";
    public static final String STRING_DATE_LAST_PLAYED = "com.joneill.snowmusic.sui.STRING_date_last_played_";
    public static final String INT_SKIP_COUNT = "com.joneill.snowmusic.sui.INT_skip_count_";
    public static final String INT_COMPLETE_PLAY_COUNT = "com.joneill.snowmusic.sui.INT_complete_play_count_";
    public static final String INT_TOTAL_SONG_PLAY_DURATION = "com.joneill.snowmusic.sui.INT_total_song_play_duration_";

    private SongsUserInfo() {
        songsUserInfo = new HashMap<>();
    }

    //Must be called before you apply a theme
    public void loadSongsUserInfo(Activity mActivity) {
        sharedPreferences = mActivity.getSharedPreferences("com.joneill.snowmusic.sui", Context.MODE_PRIVATE);

        Map<String, ?> keys = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            songsUserInfo.put(key, value);
        }
    }

    public void saveSongUserInfo() {
        for (Map.Entry<String, Object> entry : songsUserInfo.entrySet()) {
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
            }
        }
    }

    public void createSongsUserInfo(List<Song> songs) {
        for (Song song : songs) {
            putSettingsData(INT_USD_PLAY_COUNT + String.valueOf(song.getId()), 0);
            putSettingsData(STRING_DATE_LAST_PLAYED + String.valueOf(song.getId()), "Never Played");
            putSettingsData(INT_SKIP_COUNT + String.valueOf(song.getId()), 0);
            putSettingsData(INT_COMPLETE_PLAY_COUNT + String.valueOf(song.getId()), 0);
            putSettingsData(INT_TOTAL_SONG_PLAY_DURATION + String.valueOf(song.getId()), 0);
        }

        saveSongUserInfo();
    }

    private void putSettingsData(String key, Object value) {
        if (songsUserInfo.get(key) == null) {
            songsUserInfo.put(key, value);
        }
    }

    public static SongsUserInfo getInstance() {
        return instance;
    }

    public HashMap<String, Object> getSongsUserInfo() {
        return songsUserInfo;
    }
}
