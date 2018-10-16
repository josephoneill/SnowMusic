package com.joneill.snowmusic.widgets;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.WindowManager;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.helper.SystemBarTintHelper;
import com.joneill.snowmusic.themes.ThemeManager;

/**
 * Created by joseph on 1/13/15.
 */
public class ThemedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set the theme
        ThemeManager.getInstance().loadThemeData(this);
        if(ThemeManager.getInstance().getThemeId() != 0) {
            setTheme(ThemeManager.getInstance().getThemeId());
        }
        super.onCreate(savedInstanceState);
    }
}
