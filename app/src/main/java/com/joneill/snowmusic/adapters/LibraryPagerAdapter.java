package com.joneill.snowmusic.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.joneill.snowmusic.fragments.AlbumFragment;
import com.joneill.snowmusic.fragments.ArtistFragment;
import com.joneill.snowmusic.fragments.GenreFragment;
import com.joneill.snowmusic.fragments.PlaylistFragment;
import com.joneill.snowmusic.fragments.SongsFragment;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.song.Artist;
import com.joneill.snowmusic.widgets.ThemedFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by joseph on 1/29/15.
 */
public class LibraryPagerAdapter extends FragmentPagerAdapter {
    private final String[] TAB_ITEMS = {"ALBUMS", "ARTISTS", "GENRES", "SONGS", "PLAYLISTS"};
    private List<ThemedFragment> fragmentList;
    private HashMap<String, Integer> fragmentPositions;
    private AlbumFragment albumFragment;
    private ArtistFragment artistFragment;
    private GenreFragment genreFragment;
    private SongsFragment songsFragment;
    private PlaylistFragment playlistFragment;

    private Settings.OnSettingsChanged settingsCallback;
    public static final String FRAGMENT_SETTINGS_CALLBACK_KEY = "FRAGMENT_SETTINGS_KEY";
    public static final String ALBUM_FRAG_KEY = "ALBUM_FRAG_POS";
    public static final String ARTIST_FRAG_KEY = "ARTIST_FRAG_POS";
    public static final String GENRE_FRAG_KEY = "GENRE_FRAG_POS";
    public static final String SONG_FRAG_KEY = "SONG_FRAG_POS";
    public static final String PLAYLIST_FRAG_KEY = "PLAYLIST_FRAG_POS";

    public LibraryPagerAdapter(FragmentManager fm) {
        super(fm);
        settingsCallback = new Settings.OnSettingsChanged() {
            @Override
            public void onSettingsChanged(String key) {
                changeSettings(key);
            }
        };
        Settings.getInstance().getCallbacks().put(FRAGMENT_SETTINGS_CALLBACK_KEY, settingsCallback);
        createFragmentList();
    }

    private void createFragmentList() {
        fragmentList = new ArrayList<>();
        fragmentPositions = new HashMap<>();

        albumFragment = new AlbumFragment();
        fragmentList.add(albumFragment);
        fragmentPositions.put(ALBUM_FRAG_KEY, 1);

        artistFragment = new ArtistFragment();
        fragmentList.add(artistFragment);
        fragmentPositions.put(ARTIST_FRAG_KEY, 2);

        genreFragment = new GenreFragment();
        fragmentList.add(genreFragment);
        fragmentPositions.put(GENRE_FRAG_KEY, 3);

        songsFragment = new SongsFragment();
        fragmentList.add(songsFragment);
        fragmentPositions.put(SONG_FRAG_KEY, 4);

        playlistFragment = new PlaylistFragment();
        fragmentList.add(playlistFragment);
        fragmentPositions.put(PLAYLIST_FRAG_KEY, 5);
    }

    private void changeSettings(String key) {
        switch (key) {
            case Settings.INT_ALBUMS_FRAG_BG:
                albumFragment.setBackgroundColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
            case Settings.INT_ALBUM_TOOLBAR_COLOR:
                albumFragment.setToolbarColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
            case Settings.INT_GENRES_FRAG_BG:
                genreFragment.setBackgroundColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
            case Settings.INT_GENRE_TOOLBAR_COLOR:
                genreFragment.setToolbarColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
            case Settings.INT_ARTISTS_FRAG_BG:
                artistFragment.setBackgroundColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
            case Settings.INT_ARTIST_TOOLBAR_COLOR:
                artistFragment.setToolbarColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
            case Settings.INT_SONGS_FRAG_BG:
                songsFragment.setBackgroundColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
            case Settings.INT_SONG_TOOLBAR_COLOR:
                songsFragment.setToolbarColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
            case Settings.INT_PLAYLISTS_FRAG_BG:
                playlistFragment.setBackgroundColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
            case Settings.INT_PLAYLIST_TOOLBAR_COLOR:
                playlistFragment.setToolbarColor((int) Settings.getInstance().getSettingsData().get(key));
                break;
        }
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                return albumFragment;
            case 1:
                return artistFragment;
            case 2:
                return genreFragment;
            case 3:
                return songsFragment;
            case 4:
                return playlistFragment;

            default:return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return TAB_ITEMS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_ITEMS[position];
    }

    public ThemedFragment getFragment(String key) {
        int position = fragmentPositions.get(key);
        return fragmentList.get(position);
    }

    public ThemedFragment getFragment(int position) {
        if(position <= fragmentList.size() - 1 && position >= 0) {
            return fragmentList.get(position);
        }
        return new ThemedFragment();
    }
}
