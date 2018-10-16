package com.joneill.snowmusic.themes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.TypedValue;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.helper.SystemBarTintHelper;

import java.util.HashMap;

/**
 * Created by joseph on 1/13/15.
 */


public class ThemeManager {
    private static final ThemeManager instance = new ThemeManager();
    private final String themeIdKey = "com.joneill.snowmusic.themeId";;
    private int themeId;
    private SharedPreferences sharedPreferences;
    private HashMap<String, Object> themeData;

    public static final String PRIMARY_THEME_COLOR = "primary_color";
    public static final String SECONDARY_THEME_COLOR = "secondary_color";


    private ThemeManager(){themeData = new HashMap<String, Object>();}

    //Must be called before you apply a theme
    public void loadThemeData(Activity mActivity) {
        sharedPreferences = mActivity.getSharedPreferences("com.joneill.snowmusic", Context.MODE_PRIVATE);
        themeId = sharedPreferences.getInt(themeIdKey, 0);
        //saveThemeColors(mActivity);
    }

    private void saveThemeColors(Activity mActivity) {
        int[] primaryColorAttr = new int[] {R.attr.primary_theme_color};
        int indexOfAttrColor = 0;
        TypedArray a = mActivity.obtainStyledAttributes(new TypedValue().data, primaryColorAttr);
        int primaryColor = a.getColor(indexOfAttrColor, mActivity.getResources().getColor(R.color.primary_material_skyblue));
        themeData.put(PRIMARY_THEME_COLOR, primaryColor);

        int[] secondaryColorAttr = new int[] {R.attr.secondary_theme_color};
        a = mActivity.obtainStyledAttributes(new TypedValue().data, secondaryColorAttr);
        int secondaryColor = a.getColor(indexOfAttrColor, mActivity.getResources().getColor(R.color.primary_material_skyblue));
        themeData.put(SECONDARY_THEME_COLOR, secondaryColor);

        a.recycle();
    }

    public void loadThemeColors(Activity mActivity) {
        int[] statusBarColorAttr = new int[] {R.attr.primary_theme_color};
        int indexOfAttrColor = 0;
        TypedArray a = mActivity.obtainStyledAttributes(new TypedValue().data, statusBarColorAttr);
        int primaryColor = a.getColor(indexOfAttrColor, mActivity.getResources().getColor(R.color.primary_material_skyblue));
        themeData.put(PRIMARY_THEME_COLOR, primaryColor);
        a.recycle();
    }

    public void applyTheme(Theme theme) {
        if(sharedPreferences == null) {
            Log.e("SharedPreference Error!", "Must call loadTheme() before you can apply a theme!");
            return;
        }

        sharedPreferences.edit().putInt(themeIdKey, theme.getId()).apply();
    }

    public void configureStatusBar(Activity mActivity, SystemBarTintHelper tintHelper, float alpha) {
        /** Set up the status bar **/
        //Get the color of our attribute to apply to the status bar

        //Set the color of the status bar
        tintHelper.setStatusBarColor(alpha, (int) themeData.get(PRIMARY_THEME_COLOR));
        /** End setting up the status bar **/
    }

    public static ThemeManager getInstance() {
        return instance;
    }

    public int getThemeId() {
        return themeId;
    }

    public HashMap<String, Object> getThemeData() {
        return themeData;
    }

    public void setThemeData(HashMap<String, Object> themeData) {
        this.themeData = themeData;
    }
}
