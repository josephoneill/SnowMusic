package com.joneill.snowmusic.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.adapters.ArtistAdapter;
import com.joneill.snowmusic.adapters.GenreAdapter;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.TriggersHelper;
import com.joneill.snowmusic.song.Artist;
import com.joneill.snowmusic.song.Genre;
import com.joneill.snowmusic.song.Genre;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.widgets.ThemedFragment;

import java.util.List;

/**
 * Created by josep_000 on 7/13/2016.
 */
public class ArtistFragment extends ThemedFragment {
    private View view;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ArtistAdapter mAdapter;
    private List<Artist> artistsData;
    private SongPlayerPanel songPanel;
    private ImageView ivAlbumArt;
    private Handler handler;
    private boolean hasLoadedArt;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_album, container, false);
        this.view = rootView;

        init(rootView);
        setupGridView();
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new LoadRunnable(), 1000);

        return rootView;
    }

    private void init(View rootView) {
        artistsData = SongDataHolder.getInstance().getArtistData();
        songPanel = SongDataHolder.getInstance().getSongPanel();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvAlbums);
        mAdapter = new ArtistAdapter(getActivity().getApplicationContext(), songPanel, artistsData, this, rootView);
        loadSettings(rootView);
    }


    private void setupGridView() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);

        //Set the number of columns based on size
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewWidth = recyclerView.getMeasuredWidth();
                float cardViewWidth = getActivity().getResources().getDimension(R.dimen.album_card_width);
                int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                gridLayoutManager.setSpanCount(newSpanCount);
                gridLayoutManager.requestLayout();
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private class LoadRunnable implements Runnable {
        @Override
        public void run() {
            handler.postDelayed(this, 1000);
            if (SongDataHolder.getInstance().isAlbumArtLoaded() && !hasLoadedArt) {
                for(int i = 0; i < artistsData.size(); i++) {
                    for(int k = 0; k < artistsData.get(i).getSongs().size(); k++) {
                        mAdapter.notifyItemChanged(artistsData.get(i).getPositionInArtistList());
                    }
                }
                hasLoadedArt = true;
                //handler.removeCallbacks(this);
            }
        }
    }

    @Override
    public void loadSettings(View rootView) {
        bgColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_ARTISTS_FRAG_BG);
        toolbarColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_ARTIST_TOOLBAR_COLOR);
        super.loadSettings(rootView);
    }

    @Override
    public void colorToolbar() {
        super.colorToolbar();
    }

    public void setBackgroundColor(int color) {
        view.setBackgroundColor(color);
    }
}
