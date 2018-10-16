package com.joneill.snowmusic.fragments.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.receiver.MusicControlReceiver;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.widgets.ThemedFragment;

/**
 * Created by josep_000 on 7/26/2015.
 */
public class NotificationSettingsFragment extends ThemedFragment implements View.OnClickListener {
    private Settings settings;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_settings_notification, container, false);
        settings = Settings.getInstance();

        setupItems(rootView);

        return rootView;
    }

    private void setupItems(View rootView) {
        Utils.loadColorItem(this, rootView.findViewById(R.id.sn_bg_color), (ImageView) rootView.findViewById(R.id.sn_bg_preview),
                Settings.INT_NOTIF_BG_COLOR, Settings.BOOL_NOTIF_USE_PALETTE, false);

        View checkItemView;
        CheckBox cb;

        checkItemView = rootView.findViewById(R.id.sn_show_progress);
        checkItemView.setOnClickListener(this);
        cb = (CheckBox) rootView.findViewById(R.id.sn_cb_show_progress);
        cb.setChecked((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_NOTIF_SHOW_PROGRESS));
        cb.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        CheckBox cb;
        switch (v.getId()) {
            case R.id.sn_bg_color:
                Utils.openColorTypeDialog(getActivity(), v, "Set Notification Background Color",
                        Settings.INT_NOTIF_BG_COLOR, Settings.BOOL_NOTIF_USE_PALETTE,
                        (ImageView) v.findViewById(R.id.sn_bg_preview), "",
                        null);
                break;
            case R.id.sn_show_progress:
                cb = (CheckBox) v.findViewById(R.id.sn_cb_show_progress);
                cb.setChecked(!cb.isChecked());
                cb.callOnClick();
                break;
            case R.id.sn_cb_show_progress:
                cb = (CheckBox) v;
                settings.getSettingsData().put(Settings.BOOL_NOTIF_SHOW_PROGRESS, cb.isChecked());
                settings.getCallbacks().get(SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY).onSettingsChanged(Settings.BOOL_NOTIF_SHOW_PROGRESS);
                settings.saveSettings();
                break;
        }
    }
}
