package com.joneill.snowmusic.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.adapters.ThemesAdapter;
import com.joneill.snowmusic.helper.BitmapHelper;
import com.joneill.snowmusic.helper.SystemBarTintHelper;
import com.joneill.snowmusic.themes.Theme;
import com.joneill.snowmusic.themes.ThemeManager;
import com.joneill.snowmusic.widgets.ThemedActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 1/11/15.
 */
public class ThemesActivity extends ThemedActivity {
    private RecyclerView mRecyclerView;
    private ThemesAdapter mAdapter;
    private SystemBarTintHelper tintHelper;
    private List<Theme> themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        tintHelper = new SystemBarTintHelper(this);
        ThemeManager.getInstance().configureStatusBar(this, tintHelper, 1f);

        setContentView(R.layout.themes_layout);

        mRecyclerView = (RecyclerView)findViewById(R.id.themes_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        themes = new ArrayList<Theme>();
        getDefaultThemes();

        mAdapter = new ThemesAdapter(themes);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getDefaultThemes() {
        int counter = 0;
        int skyBlueId = 0;
        int rubyRedId = 0;
        int emeraldId = 0;
        int royalPurpleId = 0;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            skyBlueId = R.style.MaterialSkyBlueTheme;
            rubyRedId = R.style.MaterialRubyRedTheme;
            emeraldId = R.style.MaterialEmeraldGreenTheme;
            royalPurpleId = R.style.MaterialRoyalPurpleTheme;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            skyBlueId = R.style.HoloSkyBlueTheme;
            rubyRedId = R.style.HoloRubyRedTheme;
            emeraldId = R.style.HoloEmeraldGreenTheme;
        }

        Bitmap defaultThemePreview = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.theme_preview);
        Bitmap blueThemePreview = BitmapHelper.getInstance().colorBitmap(defaultThemePreview, getResources().getColor(R.color.primary_material_skyblue), true);

        Theme blueTheme = new Theme("Sky Blue Theme", blueThemePreview, skyBlueId);
        themes.add(blueTheme);

        Bitmap redThemePreview = BitmapHelper.getInstance().colorBitmap(defaultThemePreview, getResources().getColor(R.color.primary_material_rubyred), true);
        Theme redTheme = new Theme("Ruby Red Theme", redThemePreview, rubyRedId);
        themes.add(redTheme);

        Bitmap greenThemePreview = BitmapHelper.getInstance().colorBitmap(defaultThemePreview, getResources().getColor(R.color.primary_material_emeraldgreen), true);
        Theme greenTheme = new Theme("Emerald Green Theme", greenThemePreview, emeraldId);
        themes.add(greenTheme);

        Bitmap purpleThemePreview = BitmapHelper.getInstance().colorBitmap(defaultThemePreview, getResources().getColor(R.color.primary_material_royalpurple), true);
        Theme purpleTheme = new Theme("Royal Purple Theme", purpleThemePreview, royalPurpleId);
        themes.add(purpleTheme);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}