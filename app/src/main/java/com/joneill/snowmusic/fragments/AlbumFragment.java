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

import com.astuetz.PagerSlidingTabStrip;
import com.joneill.snowmusic.R;
import com.joneill.snowmusic.adapters.AlbumsAdapter;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.widgets.ThemedFragment;

import java.util.List;

/**
 * Created by joseph on 2/15/15.
 */
public class AlbumFragment extends ThemedFragment {
    private View view;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private AlbumsAdapter mAdapter;
    private List<Album> albumsData;
    private SongPlayerPanel songPanel;
    private ImageView ivAlbumArt;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_album, container, false);
        this.view = rootView;

        init(rootView);
        setupGridView();

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new LoadArtRunnable(), 1000);

        return rootView;
    }

    private void init (View rootView) {
        songPanel = SongDataHolder.getInstance().getSongPanel();
        albumsData = SongDataHolder.getInstance().getAlbumsData();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvAlbums);
        mAdapter = new AlbumsAdapter(getActivity().getApplicationContext(), songPanel, albumsData, this, rootView);
        loadSettings(rootView);
        //AdaptersHelper.getInstance().addAdapter(AdaptersHelper.ADAPTER_TYPES.ALL_ALBUMS, mAdapter);
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

    private class LoadArtRunnable implements Runnable {
        @Override
        public void run() {
            for(int i = 0; i < albumsData.size(); i++) {
                Album album = albumsData.get(i);
                if(album.isArtLoaded()) {
                    mAdapter.notifyItemChanged(album.getPositionInAlbumList());
                    album.setIsArtLoaded(false);
                }
            }
            handler.postDelayed(this, 1000);
            if (SongDataHolder.getInstance().isAlbumArtLoaded()) {
                handler.removeCallbacks(this);
            }
        }
    }

    @Override
    public void loadSettings(View rootView) {
        bgColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_ALBUMS_FRAG_BG);
        toolbarColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_ALBUM_TOOLBAR_COLOR);
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
