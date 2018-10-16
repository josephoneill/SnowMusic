package com.joneill.snowmusic.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.adapters.AlbumsAdapter;
import com.joneill.snowmusic.adapters.PlaylistPageAdapter;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.SystemBarTintHelper;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Playlist;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.views.ThemedToolbar;
import com.joneill.snowmusic.widgets.ThemedFragment;

import java.util.List;

/**
 * Created by joseph on 3/8/15.
 */
public class PlaylistPageFragment extends ThemedFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private RecyclerView recyclerView;
    // private ParallaxRecyclerAdapter<Song> parallaxAdapter;
    private PlaylistPageAdapter adapter;
    private Playlist playlist;
    private AppBarLayout appbar;
    private ThemedToolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private SongPlayerPanel songPanel;
    private FloatingActionButton albumFab;
    private SystemBarTintHelper tintHelper;

    public boolean isReady;

    public static final String PLAYLIST_PARCLEABLE = "playlistParce";


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        isReady = false;
        View rootView = inflater.inflate(R.layout.fragment_album_page, container, false);
        Bundle b = getArguments();
        playlist = b.getParcelable(PLAYLIST_PARCLEABLE);

        setupToolbar(rootView);
        setupUI(rootView, b);
        isReady = true;
        return rootView;
    }

    private void setupToolbar(final View rootView) {
        appbar = (AppBarLayout) rootView.findViewById(R.id.album_page_appbar);
        toolbar = (ThemedToolbar) rootView.findViewById(R.id.album_page_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.album_collapsing_toolbar);

        AppBarLayout.OnOffsetChangedListener listener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (collapsingToolbar.getHeight() + i < 2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
                } else {
                }
            }
        };

        appbar.addOnOffsetChangedListener(listener);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_material));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        collapsingToolbar.setTitle(playlist.getName());
        toolbar.setTitle("Title");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appbar.setElevation(0);
            toolbar.setElevation(0);
            collapsingToolbar.setElevation(0);
        }
        setToolbarColor();
    }

    private void setToolbarColor() {
        Album album = playlist.getSongsList().get(0).getAlbum();
        if (Settings.getInstance().getSettingsData().get(Settings.BOOL_ALP_HEADER_USE_PALETTE +
                String.valueOf(album.getAlbumId())) != null) {
            int color = 0;
            if ((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_ALP_HEADER_USE_PALETTE +
                    String.valueOf(album.getAlbumId()))) {
                if (album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN) != null) {
                    color = album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN);
                }
            } else {
                color = (int) Settings.getInstance().getSettingsData().get(Settings.INT_ALP_HEADER_COLOR
                        + String.valueOf(album.getAlbumId()));
            }
            int toolbarColor = color;
            collapsingToolbar.setContentScrimColor(toolbarColor);
            collapsingToolbar.setStatusBarScrimColor(toolbarColor);
            if (Utils.useWhiteFont(toolbarColor)) {
                collapsingToolbar.setExpandedTitleColor(Color.WHITE);
                collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
            } else {
                collapsingToolbar.setExpandedTitleColor(Color.BLACK);
                collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
            }
        }
    }


    private void setupUI(View rootView, Bundle b) {
        Album album = playlist.getSongsList().get(0).getAlbum();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.album_page_recyclerview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        songPanel = SongDataHolder.getInstance().getSongPanel();
        albumFab = (FloatingActionButton) rootView.findViewById(R.id.albumFab);
        albumFab.setOnClickListener(this);

        adapter = new PlaylistPageAdapter(SongDataHolder.getInstance().getSongPanel(), playlist, getActivity(), collapsingToolbar);
        String transitionName = b.getString(AlbumsAdapter.TRANSITION_NAME_ALBUM);

        ImageView ivAlbumArt = (ImageView) rootView.findViewById(R.id.album_page_album_art);


        if (album.getResAlbumArts().get(Album.IMAGE_HIGH_RES) != null) {
            ivAlbumArt.setImageBitmap(album.getResAlbumArts().get(Album.IMAGE_HIGH_RES));
        } else {
            ivAlbumArt.setImageBitmap(album.getAlbumArt());
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivAlbumArt.setTransitionName(transitionName);
        }

        recyclerView.setAdapter(adapter);
        Utils.sortSongs(album.getSongs());
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.albumFab:
                List<Song> songs = playlist.getSongsList();
                if (songPanel.getMusicService().getList() != songs) {
                    songPanel.getMusicService().setList(songs);
                }

                //Start the album with the first song
                songPanel.openSongInPanel(true, songPanel.getMusicService().getQueue().getSong(0), true);
                songPanel.startSong(0);
                break;
        }
    }
}
