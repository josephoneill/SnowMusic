package com.joneill.snowmusic.widgets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.joneill.snowmusic.themes.ThemeManager;

/**
 * Created by joseph on 1/29/15.
 */
public class ThemedFragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        ThemeManager.getInstance().loadThemeData(this);
        if(ThemeManager.getInstance().getThemeId() != 0) {
            setTheme(ThemeManager.getInstance().getThemeId());
        }

        ThemeManager.getInstance().loadThemeColors(this);
        super.onCreate(savedInstanceState);
    }
}
