package com.joneill.snowmusic.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.fragments.Settings.MainSettingsFragment;
import com.joneill.snowmusic.helper.BitmapHelper;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.SystemBarTintHelper;
import com.joneill.snowmusic.themes.ThemeManager;
import com.joneill.snowmusic.views.ThemedToolbar;
import com.joneill.snowmusic.widgets.ThemedFragmentActivity;

/**
 * Created by josep_000 on 7/25/2015.
 */
public class SettingsActivity extends ThemedFragmentActivity {
    private SystemBarTintHelper tintHelper;
    private ThemedToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tintHelper = new SystemBarTintHelper(this);
        ThemeManager.getInstance().configureStatusBar(this, tintHelper, 1f);

        setContentView(R.layout.activity_settings);

        toolbar = (ThemedToolbar) findViewById(R.id.settings_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_material));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setTitle("Settings");


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.settings_fragment_container, new MainSettingsFragment());
        ft.commit();
    }
}
