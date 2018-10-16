package com.joneill.snowmusic.dialogs;

import android.app.Dialog;
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
import android.widget.PopupMenu;
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
import com.joneill.snowmusic.helper.AlbumArtDownloader;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Song;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

/**
 * Created by josep on 8/27/2016.
 */
public class DownloadAlbumPopup extends PopupWindow implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner aaSizeSpinner;
    private ProgressDialog progress;
    private RequestQueue queue;
    private Context context;
    private AlbumArtDownloader albumArtDownloader;

    private CheckBox cbSkipExArt;

    public DownloadAlbumPopup(Context context) {
        super(((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_download_albumart, null),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.context = context;
        init();
        setupViews();
    }

    private void setupViews() {
        Button btnDownload = (Button) getContentView().findViewById(R.id.btn_dialog_da_download);
        btnDownload.setOnClickListener(this);

        cbSkipExArt = (CheckBox) getContentView().findViewById(R.id.cb_skip_existing_album);
        albumArtDownloader.setSkipExArt(cbSkipExArt.isChecked());
        cbSkipExArt.setOnClickListener(this);
        aaSizeSpinner = (Spinner) getContentView().findViewById(R.id.download_aa_size_spinner);
        aaSizeSpinner.setOnItemSelectedListener(this);
    }

    private void init() {
        progress = new ProgressDialog(context);
        albumArtDownloader = new AlbumArtDownloader(context, progress);
    }

    public void downloadAlbumArt() {
        try {
            progress.setTitle("Downloading Album Art");
            progress.setMessage("Please wait while downloading...");
            progress.show();
            albumArtDownloader.fetchAlbumFromLastFM(false, false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_da_download:
                downloadAlbumArt();
                break;
            case R.id.cb_skip_existing_album:
                albumArtDownloader.setSkipExArt(cbSkipExArt.isChecked());
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        albumArtDownloader.setArtSize(adapterView.getItemAtPosition(i).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
