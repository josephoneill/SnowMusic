package com.joneill.snowmusic.fragments.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.adapters.LibraryPagerAdapter;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.widgets.ThemedFragment;

/**
 * Created by josep on 10/6/2016.
 */
public class BgColorsSettingsFragment extends ThemedFragment implements View.OnClickListener {
    private Settings settings;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_settings_bgcolors, container, false);

        settings = Settings.getInstance();
        View item;
        ImageView itemColorPreview;

        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_color_albums_frag), (ImageView) rootView.findViewById(R.id.bg_color_album_preview),
                Settings.INT_ALBUMS_FRAG_BG);
        Utils.loadColorItem(this, rootView.findViewById(R.id.toolbar_color_albums_frag), (ImageView) rootView.findViewById(R.id.toolbar_color_album_preview),
                Settings.INT_ALBUM_TOOLBAR_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.card_color_albums_frag), (ImageView) rootView.findViewById(R.id.card_color_album_preview),
                Settings.INT_ALBUM_CARD_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_color_genres_frag), (ImageView) rootView.findViewById(R.id.bg_color_genre_preview),
                Settings.INT_GENRES_FRAG_BG);
        Utils.loadColorItem(this, rootView.findViewById(R.id.toolbar_color_genres_frag), (ImageView) rootView.findViewById(R.id.toolbar_color_genre_preview),
                Settings.INT_GENRE_TOOLBAR_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.card_color_genre_frag), (ImageView) rootView.findViewById(R.id.card_color_genre_preview),
                Settings.INT_GENRE_CARD_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_color_artists_frag), (ImageView) rootView.findViewById(R.id.bg_color_artist_preview),
                Settings.INT_ARTISTS_FRAG_BG);
        Utils.loadColorItem(this, rootView.findViewById(R.id.toolbar_color_artists_frag), (ImageView) rootView.findViewById(R.id.toolbar_color_artist_preview),
                Settings.INT_ARTIST_TOOLBAR_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.card_color_artist_frag), (ImageView) rootView.findViewById(R.id.card_color_artist_preview),
                Settings.INT_ARTIST_CARD_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_color_songs_frag), (ImageView) rootView.findViewById(R.id.bg_color_song_preview),
                Settings.INT_SONGS_FRAG_BG);
        Utils.loadColorItem(this, rootView.findViewById(R.id.toolbar_color_songs_frag), (ImageView) rootView.findViewById(R.id.toolbar_color_song_preview),
                Settings.INT_SONG_TOOLBAR_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.bg_color_playlists_frag), (ImageView) rootView.findViewById(R.id.bg_color_playlist_preview),
                Settings.INT_PLAYLISTS_FRAG_BG);
        Utils.loadColorItem(this, rootView.findViewById(R.id.toolbar_color_playlists_frag), (ImageView) rootView.findViewById(R.id.toolbar_color_playlist_preview),
                Settings.INT_PLAYLIST_TOOLBAR_COLOR);
        Utils.loadColorItem(this, rootView.findViewById(R.id.card_color_playlist_frag), (ImageView) rootView.findViewById(R.id.card_color_playlist_preview),
                Settings.INT_PLAYLIST_CARD_COLOR);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bg_color_albums_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_album_frag_bg),
                        Settings.INT_ALBUMS_FRAG_BG,
                        (ImageView) v.findViewById(R.id.bg_color_album_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.toolbar_color_albums_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.col_album_frag_toolbar),
                        Settings.INT_ALBUM_TOOLBAR_COLOR,
                        (ImageView) v.findViewById(R.id.toolbar_color_album_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.card_color_albums_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.col_album_frag_card),
                        Settings.INT_ALBUM_CARD_COLOR, Settings.BOOL_ALBUM_CARD_USE_PALETTE,
                        (ImageView) v.findViewById(R.id.card_color_album_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.bg_color_genres_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_genre_frag_bg),
                        Settings.INT_GENRES_FRAG_BG,
                        (ImageView) v.findViewById(R.id.bg_color_genre_preview),  LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.toolbar_color_genres_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.col_genre_frag_toolbar),
                        Settings.INT_GENRE_TOOLBAR_COLOR,
                        (ImageView) v.findViewById(R.id.toolbar_color_genre_preview),  LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.card_color_genre_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_genre_frag_bg),
                        Settings.INT_GENRE_CARD_COLOR, Settings.BOOL_GENRE_CARD_USE_PALETTE,
                        (ImageView) v.findViewById(R.id.bg_color_genre_preview),  LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.bg_color_artists_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_artist_frag_bg),
                        Settings.INT_ARTISTS_FRAG_BG,
                        (ImageView) v.findViewById(R.id.bg_color_artist_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.toolbar_color_artists_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.col_artist_frag_toolbar),
                        Settings.INT_ARTIST_TOOLBAR_COLOR,
                        (ImageView) v.findViewById(R.id.toolbar_color_artist_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.card_color_artist_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.col_artist_frag_card),
                        Settings.INT_ARTIST_CARD_COLOR, Settings.BOOL_ARTIST_CARD_USE_PALETTE,
                        (ImageView) v.findViewById(R.id.card_color_artist_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.bg_color_songs_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_song_frag_bg),
                        Settings.INT_SONGS_FRAG_BG,
                        (ImageView) v.findViewById(R.id.bg_color_song_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.toolbar_color_songs_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.col_song_frag_toolbar),
                        Settings.INT_SONG_TOOLBAR_COLOR,
                        (ImageView) v.findViewById(R.id.toolbar_color_song_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.bg_color_playlists_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.cd_playlist_frag_bg),
                        Settings.INT_PLAYLISTS_FRAG_BG,
                        (ImageView) v.findViewById(R.id.bg_color_playlist_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.toolbar_color_playlists_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.col_playlist_frag_toolbar),
                        Settings.INT_PLAYLIST_TOOLBAR_COLOR,
                        (ImageView) v.findViewById(R.id.toolbar_color_playlist_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
            case R.id.card_color_playlist_frag:
                Utils.openColorTypeDialog(getActivity(), v, getResources().getString(R.string.col_playlist_frag_card),
                        Settings.INT_PLAYLIST_CARD_COLOR, Settings.BOOL_PLAYLIST_CARD_USE_PALETTE,
                        (ImageView) v.findViewById(R.id.card_color_playlist_preview), LibraryPagerAdapter.FRAGMENT_SETTINGS_CALLBACK_KEY,
                        null);
                break;
        }
    }
}
