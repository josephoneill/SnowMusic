package com.joneill.snowmusic.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.activities.EditSongInfoActivity;
import com.joneill.snowmusic.adapters.SongAdapter;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.service.MusicService;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.widgets.ThemedFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 1/29/15.
 */
public class SongsFragment extends ThemedFragment {
    public static final int SONG_RESULT_CODE = 71;
    private View view;
    private RecyclerView recyclerView;
    private List<Song> songsData;
    private Bitmap defaultBitmap;
    private SongAdapter mAdapter;
    private SongPlayerPanel songPanel;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_song, container, false);

        this.view = rootView;
        songPanel = SongDataHolder.getInstance().getSongPanel();
        songsData = SongDataHolder.getInstance().getSongsData();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvSongs);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadSettings(rootView);

        return rootView;
    }

    @Override
    public void loadSettings(View rootView) {
        bgColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SONGS_FRAG_BG);
        toolbarColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SONG_TOOLBAR_COLOR);
        recyclerView.setBackgroundColor(bgColor);
        super.loadSettings(rootView);
    }

    @Override
    public void colorToolbar() {
        super.colorToolbar();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SongAdapter(this, songPanel, songsData, getContext());
        recyclerView.setAdapter(mAdapter);
        Utils.sortSongs(songsData);
        //AdaptersHelper.getInstance().addAdapter(AdaptersHelper.ADAPTER_TYPES.ALL_SONGS, mAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SONG_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Song result = data.getExtras().getParcelable(EditSongInfoActivity.SONG_PARCLEABLE);
                result.setAlbum(SongDataHolder.getInstance().getActualAlbumById(result.getAlbumId()));
                int position = data.getExtras().getInt(EditSongInfoActivity.SONG_POSITION);
                List<Song> songsData = mAdapter.getSongsData();
                songsData.remove(position);
                songsData.add(result);
                mAdapter.setSongsData(songsData, true);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter.getSongPanel() != null) {
            mAdapter.getSongPanel().onPause();
        }
    }

    @Override
    public void onAttach(Activity mActivity) {
        super.onAttach(mActivity);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter.getSongPanel() != null) {
            mAdapter.getSongPanel().onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Destroying", "destroyed lol");

        //Recycle the bitmaps to save memory
        for (int i = 0; i < songsData.size(); i++) {
            //songsData.get(i).onDestroy();
        }

        if (defaultBitmap != null) {
            //defaultBitmap.recycle();
            //defaultBitmap = null;
        }
    }

    public void setBackgroundColor(int color) {
        view.setBackgroundColor(color);
        recyclerView.setBackgroundColor(color);
    }

    public List<Song> getSongsData() {
        return songsData;
    }

    public SongPlayerPanel getSongPanel() {
        return songPanel;
    }

}


