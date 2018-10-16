package com.joneill.snowmusic.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.joneill.snowmusic.song.Queue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by josep on 10/19/2016.
 */
public class SavedInformationPrefs {
    private SharedPreferences sharedPreferences;
    private Activity mActivity;
    private HashMap<String, Object> data;

    public static final String INT_CURR_TAB_INDEX = "com.joneill.snowmusic.INT_curr_tab_index";
    public static final String OBJECT_CURR_QUEUE = "com.joneill.snowmusic.OBJECT_curr_queue";
    public static final String INT_CURR_QUEUE_SONG_INDEX = "com.joneill.snowmusic.INT_curr_queue_song_index";
    public static final String INT_CURR_SONG_POS = "com.joneill.snowmusic.INT_curr_song_pos";

    public SavedInformationPrefs(Activity mActivity) {
        this.mActivity = mActivity;
        sharedPreferences = mActivity.getSharedPreferences("com.joneill.snowmusic.instances", Context.MODE_PRIVATE);
        data = new HashMap<>();

        Map<String, ?> keys = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            data.put(key, value);
        }

        createDefaultData();
    }

    public void saveData() {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
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
            } else if (key.contains("OBJECT")) {
                if(key.equals(OBJECT_CURR_QUEUE)) {
                    Gson gson = new Gson();
                    //List<String> queue = (Queue) value;
                    //List<Long> songIds = queue.getSongIdList();
                    //String json = gson.toJson(songIds);
                    String json = (String) value;
                    sharedPreferences.edit().putString(key, json).apply();
                    Log.e("test", "joneill ran once");
                }
            }
        }
    }

    public void createDefaultData() {
        putData(INT_CURR_TAB_INDEX, 0);
        String queue = new Gson().toJson(new Queue().getSongIdList());
        putData(OBJECT_CURR_QUEUE, queue);
        putData(INT_CURR_QUEUE_SONG_INDEX, 0);
        putData(INT_CURR_SONG_POS, 0);
    }

    private void putData(String key, Object value) {
        if (data.get(key) == null) {
            data.put(key, value);
        }
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}