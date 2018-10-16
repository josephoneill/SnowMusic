package com.joneill.snowmusic.fragments.Settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.dialogs.DownloadAlbumPopup;
import com.joneill.snowmusic.dialogs.SongPanelSettingsPopup;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.widgets.ThemedFragment;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import java.util.Set;

/**
 * Created by josep_000 on 7/26/2015.
 */
public class FunctionalitySettingsFragment extends ThemedFragment implements View.OnClickListener {
    private Settings settings;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_settings_functionality, container, false);
        settings = Settings.getInstance();
        setupItems(rootView);
        return rootView;
    }

    private void setupItems(View rootView) {
        View checkItemView;
        CheckBox cb;

        checkItemView = rootView.findViewById(R.id.sf_b_playonskip);
        cb = (CheckBox) rootView.findViewById(R.id.sf_b_playonskip_cb);
        cb.setChecked((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_PLAY_ON_SKIP));
        checkItemView.setOnClickListener(this);
        cb.setOnClickListener(this);

        View itemView;
        itemView = rootView.findViewById(R.id.sf_songpanelsettings);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        CheckBox cb;
        switch (v.getId()) {
            case R.id.sf_b_playonskip:
                cb = (CheckBox) v.findViewById(R.id.sf_b_playonskip_cb);
                cb.setChecked(!cb.isChecked());
                cb.callOnClick();
                break;
            case R.id.sf_b_playonskip_cb:
                cb = (CheckBox) v;
                settings.getSettingsData().put(Settings.BOOL_PLAY_ON_SKIP, cb.isChecked());
                settings.getCallbacks().get(SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY).onSettingsChanged(Settings.BOOL_PLAY_ON_SKIP);
                settings.saveSettings();
                break;
            case R.id.sf_songpanelsettings:
                SongPanelSettingsPopup popup = new SongPanelSettingsPopup(getActivity());
                popup.showAtLocation(v, Gravity.CENTER, 0, 0);
                popup.setFocusable(true);
                popup.update();
                break;
        }
    }
}
