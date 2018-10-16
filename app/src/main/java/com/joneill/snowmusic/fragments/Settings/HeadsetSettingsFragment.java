package com.joneill.snowmusic.fragments.Settings;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.dialogs.SongPanelSettingsPopup;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.receiver.MusicControlReceiver;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.widgets.ThemedFragment;

/**
 * Created by josep_000 on 7/26/2015.
 */
public class HeadsetSettingsFragment extends ThemedFragment implements View.OnClickListener {
    private Settings settings;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_settings_headset, container, false);
        settings = Settings.getInstance();
        setupItems(rootView);
        return rootView;
    }

    private void setupItems(View rootView) {
        View checkItemView;
        CheckBox cb;

        checkItemView = rootView.findViewById(R.id.sh_pauseondis);
        checkItemView.setOnClickListener(this);
        cb = (CheckBox) rootView.findViewById(R.id.sh_cb_pauseondis);
        cb.setChecked((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_PAUSE_ON_DIS));
        cb.setOnClickListener(this);

        checkItemView = rootView.findViewById(R.id.sh_resoncon);
        checkItemView.setOnClickListener(this);
        cb = (CheckBox) rootView.findViewById(R.id.sh_cb_resoncon);
        cb.setChecked((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_RES_ON_CON));
        cb.setOnClickListener(this);

        checkItemView = rootView.findViewById(R.id.sh_use_quick_pause);
        checkItemView.setOnClickListener(this);
        cb = (CheckBox) rootView.findViewById(R.id.sh_cb_use_quick_pause);
        cb.setChecked((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_USE_QUICK_PAUSE));
        cb.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        CheckBox cb;
        switch (v.getId()) {
            case R.id.sh_pauseondis:
                cb = (CheckBox) v.findViewById(R.id.sh_cb_pauseondis);
                cb.setChecked(!cb.isChecked());
                cb.callOnClick();
                break;
            case R.id.sh_cb_pauseondis:
                cb = (CheckBox) v;
                settings.getSettingsData().put(Settings.BOOL_PAUSE_ON_DIS, cb.isChecked());
                settings.getCallbacks().get(MusicControlReceiver.MCW_SETTINGS_CALLBACK_KEY).onSettingsChanged(Settings.BOOL_PAUSE_ON_DIS);
                settings.saveSettings();
                break;
            case R.id.sh_resoncon:
                cb = (CheckBox) v.findViewById(R.id.sh_cb_resoncon);
                cb.setChecked(!cb.isChecked());
                cb.callOnClick();
                break;
            case R.id.sh_cb_resoncon:
                cb = (CheckBox) v;
                settings.getSettingsData().put(Settings.BOOL_RES_ON_CON, cb.isChecked());
                settings.getCallbacks().get(MusicControlReceiver.MCW_SETTINGS_CALLBACK_KEY).onSettingsChanged(Settings.BOOL_RES_ON_CON);
                settings.saveSettings();
                break;
            case R.id.sh_use_quick_pause:
                cb = (CheckBox) v.findViewById(R.id.sh_cb_use_quick_pause);
                cb.setChecked(!cb.isChecked());
                cb.callOnClick();
                break;
            case R.id.sh_cb_use_quick_pause:
                cb = (CheckBox) v;
                settings.getSettingsData().put(Settings.BOOL_USE_QUICK_PAUSE, cb.isChecked());
                settings.getCallbacks().get(MusicControlReceiver.MCW_SETTINGS_CALLBACK_KEY).onSettingsChanged(Settings.BOOL_USE_QUICK_PAUSE);
                settings.saveSettings();
                break;
        }
    }
}
