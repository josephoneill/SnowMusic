package com.joneill.snowmusic.widgets;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.themes.ThemeManager;

/**
 * Created by joseph on 1/29/15.
 */
public class ThemedFragment extends Fragment {
    //Settings
    public int bgColor = 0;
    public int toolbarColor = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().loadThemeData(getActivity());
        if(ThemeManager.getInstance().getThemeId() != 0) {
            getActivity().setTheme(ThemeManager.getInstance().getThemeId());
        }
        super.onCreate(savedInstanceState);
    }

    public void loadSettings(View rootView) {
        //bgColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_ALBUMS_FRAG_BG);
        rootView.setBackgroundColor(bgColor);

        //toolbarColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_ARTIST_TOOLBAR_COLOR);
        getActivity().findViewById(R.id.main_toolbar).setBackgroundColor(toolbarColor);
        getActivity().findViewById(R.id.library_tabs).setBackgroundColor(toolbarColor);
    }

    public void setToolbarColor(int color) {
        toolbarColor = color;
        colorToolbar();
    }

    public void colorToolbar() {
        if(getActivity() != null) {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
            TabLayout tabs = (TabLayout) getActivity().findViewById(R.id.library_tabs);
            toolbar.setBackgroundColor(toolbarColor);
            tabs.setBackgroundColor(toolbarColor);
            if(Utils.useWhiteFont(toolbarColor)) {
                toolbar.setTitleTextColor(Color.WHITE);
                tabs.setTabTextColors(Color.WHITE, Color.WHITE);
            } else {
                toolbar.setTitleTextColor(Color.BLACK);
                tabs.setTabTextColors(Color.BLACK, Color.BLACK);
            }
        }
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public int getBgColor() {
        return bgColor;
    }
}
