package com.joneill.snowmusic.fragments.Settings;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.adapters.LibraryPagerAdapter;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.interfaces.OnColorSelected;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.widgets.ThemedFragment;

/**
 * Created by josep on 10/6/2016.
 */
public class SPanelSettingsFragment extends ThemedFragment implements View.OnClickListener, OnColorSelected {
    private Settings settings;
    private boolean isPlayBtn;
    private View sppBg;
    private ImageView btnPlayMain;
    private ImageButton ibPrev, ibNext;
    private SeekBar sb;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_settings_spp_design, container, false);


        settings = Settings.getInstance();
        View item;
        ImageView itemColorPreview;

        sppBg = rootView.findViewById(R.id.ds_spp_bg);
        ibPrev = (ImageButton) rootView.findViewById(R.id.ds_btn_PrevMain);
        ibNext = (ImageButton) rootView.findViewById(R.id.ds_btn_NextMain);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_spp_color_item), sppBg,
                (ImageView) rootView.findViewById(R.id.bg_color_spp_preview),
                Settings.INT_SPP_PRIMARY_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_spb_color_item), (ImageView) rootView.findViewById(R.id.bg_color_spb_preview),
                Settings.INT_SONG_BAR_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_btnplay_color_item), (ImageView) rootView.findViewById(R.id.bg_color_btnplay_preview),
                Settings.INT_BTN_PLAY_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_btnpause_color_item), (ImageView) rootView.findViewById(R.id.bg_color_btnpause_preview),
                Settings.INT_BTN_PAUSE_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_btnprev_color_item), ibPrev,
                (ImageView) rootView.findViewById(R.id.bg_color_btnprev_preview),
                Settings.INT_BTN_PREV_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_btnnext_color_item), ibNext,
                (ImageView) rootView.findViewById(R.id.bg_color_btnnext_preview),
                Settings.INT_BTN_NEXT_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.si_seekbar_c_item), (ImageView) rootView.findViewById(R.id.si_seekbar_c_preview),
                Settings.INT_SEEKBAR_COLOR);

        colorPanelPreviews(rootView);

        return rootView;
    }

    private void colorPanelPreviews(View rootView) {
        isPlayBtn = false;

        rootView.findViewById(R.id.ds_btn_PlayMain).setOnClickListener(this);
        btnPlayMain = (ImageView) rootView.findViewById(R.id.ds_iv_PlayMain);
        btnPlayMain.setImageResource(R.drawable.pause_light);
        btnPlayMain.setColorFilter((int) Settings.getInstance().getSettingsData().get(Settings.INT_BTN_PAUSE_COLOR));

        sb = (SeekBar) rootView.findViewById(R.id.ds_sb_MusicPlayer);
        setSeekbarColors((int) Settings.getInstance().getSettingsData().get(Settings.INT_SEEKBAR_COLOR));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bg_spp_color_item:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_spp_color),
                        Settings.INT_SPP_PRIMARY_COLOR, Settings.BOOL_SPP_USE_PALETTE,
                        (ImageView) v.findViewById(R.id.bg_color_spp_preview), SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY,
                        this);
                break;
            case R.id.bg_spb_color_item:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_spb_color),
                        Settings.INT_SONG_BAR_COLOR, Settings.BOOL_SONG_BAR_USE_PALETTE,
                        (ImageView) v.findViewById(R.id.bg_color_spb_preview), SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY,
                        this);
                break;
            case R.id.bg_btnplay_color_item:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_btnplay_color),
                        Settings.INT_BTN_PLAY_COLOR,
                        (ImageView) v.findViewById(R.id.bg_color_btnplay_preview), SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY,
                        this);
                break;
            case R.id.bg_btnpause_color_item:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_btnpause_color), Settings.INT_BTN_PAUSE_COLOR,
                        (ImageView) v.findViewById(R.id.bg_color_btnpause_preview), SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY,
                        this);
                break;
            case R.id.bg_btnprev_color_item:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_btnprev_color),
                        Settings.INT_BTN_PREV_COLOR,
                        (ImageView) v.findViewById(R.id.bg_color_btnprev_preview), SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY,
                        this);
                break;
            case R.id.bg_btnnext_color_item:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_btnnext_color),
                        Settings.INT_BTN_NEXT_COLOR,
                        (ImageView) v.findViewById(R.id.bg_color_btnnext_preview), SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY,
                        this);
                break;
            case R.id.si_seekbar_c_item:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_spb_color),
                        Settings.INT_SEEKBAR_COLOR,
                        (ImageView) v.findViewById(R.id.si_seekbar_c_preview), SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY,
                        this);
                break;

            case R.id.ds_btn_PlayMain:
                isPlayBtn = !isPlayBtn;
                if (isPlayBtn) {
                    btnPlayMain.setImageResource(R.drawable.pause_light);
                    btnPlayMain.setColorFilter((int) Settings.getInstance().getSettingsData().get(Settings.INT_BTN_PAUSE_COLOR));
                } else {
                    btnPlayMain.setImageResource(R.drawable.play_light);
                    btnPlayMain.setColorFilter((int) Settings.getInstance().getSettingsData().get(Settings.INT_BTN_PLAY_COLOR));
                }
                break;
        }
    }

    @Override
    public void onColorSelected(String key, int color) {
        switch(key) {
            case Settings.INT_SPP_PRIMARY_COLOR:
                sppBg.setBackgroundColor(color);
                break;
            case Settings.INT_BTN_PREV_COLOR:
                ibPrev.setColorFilter(color);
                break;
            case Settings.INT_BTN_NEXT_COLOR:
                ibNext.setColorFilter(color);
                break;
            case Settings.INT_BTN_PLAY_COLOR:
                if(isPlayBtn) {
                    btnPlayMain.setColorFilter(color);
                }
                break;
            case Settings.INT_BTN_PAUSE_COLOR:
                if(!isPlayBtn) {
                    btnPlayMain.setColorFilter(color);
                }
                break;
            case Settings.INT_SEEKBAR_COLOR:
                setSeekbarColors(color);
                break;
        }
    }

    private void setSeekbarColors(int color) {
        int thumbColor = Utils.useWhiteFont((int) Settings.getInstance().getSettingsData()
                .get(Settings.INT_SPP_PRIMARY_COLOR)) ? Color.WHITE : Color.BLACK;
        sb.getProgressDrawable().setColorFilter(new PorterDuffColorFilter((int) Settings.getInstance().getSettingsData()
                .get(Settings.INT_SEEKBAR_COLOR), PorterDuff.Mode.SRC_IN));
        sb.getThumb().setColorFilter(new PorterDuffColorFilter(thumbColor, PorterDuff.Mode.SRC_IN));
    }
}
