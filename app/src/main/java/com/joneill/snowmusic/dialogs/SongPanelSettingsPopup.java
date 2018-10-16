package com.joneill.snowmusic.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joneill.snowmusic.R;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

/**
 * Created by josep on 8/27/2016.
 */
public class SongPanelSettingsPopup extends PopupWindow implements View.OnClickListener {
    private CheckBox cbExpand, cbClose;
    private Settings settings;

    public SongPanelSettingsPopup(Context context) {
        super(((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_songpanel_settings, null),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        settings = Settings.getInstance();
        setupViews();
    }

    private void setupViews() {
        Button btnSave = (Button) getContentView().findViewById(R.id.btn_dialog_sss_close);
        btnSave.setOnClickListener(this);

        cbExpand = (CheckBox) getContentView().findViewById(R.id.cb_sf_sps_expand);
        cbExpand.setOnClickListener(this);
        cbExpand.setChecked((boolean) settings.getSettingsData().get(Settings.BOOL_SONG_PANEL_EXPAND_OC));
        cbClose = (CheckBox) getContentView().findViewById(R.id.cb_sf_sps_close);
        cbClose.setOnClickListener(this);
        cbClose.setChecked((boolean) settings.getSettingsData().get(Settings.BOOL_SONG_PANEL_CLOSE_OC));
    }

    @Override
    public void onClick(View view) {
        CheckBox cb = null;
        switch (view.getId()) {
            case R.id.btn_dialog_sss_close:
                dismiss();
                break;
            case R.id.cb_sf_sps_expand:
                cb = (CheckBox) view;
                settings.getSettingsData().put(Settings.BOOL_SONG_PANEL_EXPAND_OC, cb.isChecked());
                settings.getCallbacks().get(SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY).onSettingsChanged(Settings.BOOL_SONG_PANEL_EXPAND_OC);
                settings.saveSettings();
                break;
            case R.id.cb_sf_sps_close:
                cb = (CheckBox) view;
                settings.getSettingsData().put(Settings.BOOL_SONG_PANEL_CLOSE_OC, cb.isChecked());
                settings.getCallbacks().get(SongPlayerPanel.PANEL_SETTINGS_CALLBACK_KEY).onSettingsChanged(Settings.BOOL_SONG_PANEL_CLOSE_OC);
                settings.saveSettings();
                break;
        }
    }
}
