package com.joneill.snowmusic.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LruCache;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.Toast;

import com.joneill.snowmusic.MainActivity;
import com.joneill.snowmusic.R;
import com.joneill.snowmusic.helper.BitmapHelper;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.SongHelper;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Artist;
import com.joneill.snowmusic.song.Genre;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.themes.ThemeManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 2/23/15.
 */
//Universal class via singleton that allows
//us to load the song info once and use it in each fragment
public class SongDataHolder {
    private static SongDataHolder instance = new SongDataHolder();
    public static Bitmap DEFAULT_BITMAP;
    private Cursor musicCursor;
    private MainActivity mActivity;
    private List<Song> songsData;
    private List<Album> albumsData;
    private List<Genre> genreData;
    private List<Artist> artistData;
    private SongPlayerPanel songPanel;
    private boolean isAlbumArtLoaded;

    private SongDataHolder() {
    }

    public void init() {
        if (mActivity == null) {
            Log.e("Activity is null", "Activity is null. Set mActivity before calling init()");
            return;
        }

        try {
            DEFAULT_BITMAP = BitmapHelper.getInstance().decodeSampledBitmapFromResource(mActivity.getApplicationContext().getResources(), R.drawable.albumart, 256, 256);
            TLruCache.getInstance().putBitmapInCache("default", DEFAULT_BITMAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setUpMusicCursor();

        isAlbumArtLoaded = false;
        songsData = new ArrayList<Song>();
        albumsData = new ArrayList<Album>();
        genreData = new ArrayList<Genre>();
        artistData = new ArrayList<Artist>();
    }

    private void setUpMusicCursor() {
        /*** Set up the music cursor ***/
        ContentResolver musicResolver = mActivity.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION};
        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        musicCursor = musicResolver.query(musicUri, cursor_cols, where, null, null);
        /*** End setting up music cursor ***/
    }


    public void loadSongInfo() {
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumIdColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int dataColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            List<Long> albumIds = new ArrayList<Long>();
            List<String> artists = new ArrayList<>();
            do {
                long songId = Utils.cursorGetLongNullCheck(musicCursor, idColumn);
                long albumId = Utils.cursorGetLongNullCheck(musicCursor, albumIdColumn);
                String songTitle = Utils.cursorGetStringNullCheck(musicCursor, titleColumn);
                String songArtist = Utils.cursorGetStringNullCheck(musicCursor, artistColumn);
                String albumName = Utils.cursorGetStringNullCheck(musicCursor, albumColumn);
                String filePath = Utils.cursorGetStringNullCheck(musicCursor, dataColumn);
                long duration = Utils.cursorGetLongNullCheck(musicCursor, durationColumn);

                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);

                String songDuration = Utils.millisToTime(duration);
                Song song = new Song("default", songId, albumId, songTitle, songArtist, songDuration, duration, filePath);
                checkForLocalArt(song);
                songsData.add(song);

                if (!hasAlbumIdBeenRun(albumIds, albumId)) {
                    albumIds.add(albumId);
                    Album album = new Album(albumName, songArtist, DEFAULT_BITMAP, uri, albumId);
                    albumsData.add(album);
                }

                if (!hasArtistBeenAdded(artists, songArtist)) {
                    artists.add(songArtist);
                    Artist artist = new Artist(songArtist);
                    artist.addSong(song);
                    artistData.add(artist);
                } else {
                    for(Artist artistItem : artistData) {
                        if(artistItem.getArtist().equals(songArtist)) {
                            artistItem.addSong(song);
                        }
                    }
                }

                getGenres(song);
            }

            while (musicCursor.moveToNext());
            //Once the loading of the songs is done, load the songs into their albums
            for (int i = 0; i < albumsData.size(); i++) {
                albumsData.get(i).initSongs();
            }
        }

        //Create the song user info(if the info already exists, the data WILL NOT be overwritten)
        SongsUserInfo.getInstance().createSongsUserInfo(songsData);

        //After loading the song data, load the album art on a seperate thread
        new DecodeAlbumArt().execute();
    }

    private void getGenres(Song song) {
        Cursor genresCursor;
        String[] genresProjection = {
                MediaStore.Audio.Genres.NAME,
                MediaStore.Audio.Genres._ID
        };
        Uri uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", (int) song.getId());
        genresCursor = mActivity.getContentResolver().query(uri,
                genresProjection, null, null, null);
        int genre_column_index = genresCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);

        if (genresCursor.moveToFirst()) {
            do {
                boolean doesGenreExist = false;
                String genreName = genresCursor.getString(genre_column_index);
                for(Genre genreItem : genreData) {
                    if(genreItem.getGenre().equals(genreName)) {
                        doesGenreExist = true;
                        genreItem.addSong(song);
                    }
                }
                if(!doesGenreExist) {
                    Genre genre = new Genre(genreName);
                    genre.addSong(song);
                    genreData.add(genre);
                }
            } while (genresCursor.moveToNext());
        }
    }

    private void checkForLocalArt(Song song) {
        Uri uri = SongHelper.getInstance().getAlbumArtDb().getAlbumArtUri(String.valueOf(song.getId()));
        if (uri != null) {
            ContentResolver res = mActivity.getApplicationContext().getContentResolver();
            InputStream in = null;
            Bitmap artwork = null;
            try {
                in = res.openInputStream(uri);
                artwork = BitmapHelper.getInstance().decodeSampledBitmapFromInputStream(in, res, uri, 256, 256);
            } catch (FileNotFoundException e) {
                artwork = DEFAULT_BITMAP;
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            TLruCache.getInstance().putBitmapInCache(String.valueOf(song.getId()), artwork);
            song.setUseSingleArt(true);
            song.setAlbumUri(uri);
            song.setAlbumArt(String.valueOf(song.getId()));
        }
    }


    //Loading the album art takes a few seconds and a lot of memory. To load the application faster,
    //we load the album art after the song data and on a seperate thread.
    private class DecodeAlbumArt extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... parmas) {
            Cursor tempMusicCursor = musicCursor;
            if (tempMusicCursor != null && tempMusicCursor.moveToFirst()) {
                int idColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM_ID);
                int position = -1;
                do {
                    position++;
                    long albumId = musicCursor.getLong(idColumn);
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);

                    if (TLruCache.getInstance().containsBitmap(Long.toString(albumId))) {
                        for (int i = 0; i < songsData.size(); i++) {
                            if (songsData.get(i).getAlbumId() == albumId
                                    && songsData.get(i).getAlbumArt() == DEFAULT_BITMAP) {
                                songsData.get(i).setAlbumUri(uri);
                                songsData.get(i).setAlbumArt(Long.toString(albumId));
                            }
                        }
                    } else {
                        if (mActivity == null) break;
                        ContentResolver res = mActivity.getApplicationContext().getContentResolver();
                        InputStream in = null;
                        Bitmap artwork = null;
                        try {
                            in = res.openInputStream(uri);
                            artwork = BitmapHelper.getInstance().decodeSampledBitmapFromInputStream(in, res, uri, 256, 256);
                            if (artwork != null) {
                                TLruCache.getInstance().putBitmapInCache(Long.toString(albumId), artwork);
                            } else {
                                artwork = DEFAULT_BITMAP;
                            }
                        } catch (FileNotFoundException e) {
                            artwork = DEFAULT_BITMAP;
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                            artwork = DEFAULT_BITMAP;
                        }

                        //Match the id with the song and set album art
                        for (int i = 0; i < songsData.size(); i++) {
                            if (songsData.get(i).getAlbumId() == albumId) {
                               if (songsData.get(i).getAlbumArt() == DEFAULT_BITMAP) {
                                    songsData.get(i).setAlbumUri(uri);
                                    //songsData.get(i).setAlbumArt(artwork);
                                   songsData.get(i).setAlbumArt(Long.toString(albumId));
                               }
                            }
                        }
                        for (int i = 0; i < albumsData.size(); i++) {
                            if (albumsData.get(i).getAlbumId() == albumId) {
                                final Album album = albumsData.get(i);
                                albumsData.get(i).setAlbumArt(artwork);
                                /*albumsData.get(i).addAlbumArtRes(Album.IMAGE_ULTRA_HIGH_RES, BitmapHelper.getInstance().decodeBitmapFromUri(uri, defaultBitmap,
                                        mActivity.getApplicationContext().getContentResolver(), 512, 512));*/
                                album.setIsArtLoaded(true);

                                //Load palette data
                                //Palette.from(album.getResAlbumArts().get(Album.IMAGE_MED_RES)).generate(new Palette.PaletteAsyncListener() {
                                Palette.from(artwork).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch swatch = palette.getVibrantSwatch();
                                        int vibrantColor = palette.getVibrantColor(0xff000000 +
                                                Integer.parseInt(String.valueOf(ThemeManager.getInstance().getThemeData().get(ThemeManager.PRIMARY_THEME_COLOR)), 16));
                                        album.getPaletteColors().put(Album.PALETTE_COLOR_MAIN, vibrantColor);

                                        int darkVibrantColor = Utils.darkenColor(vibrantColor, 0.9f);
                                        album.getPaletteColors().put(Album.PALETTE_COLOR_SECONDARY, darkVibrantColor);
                                        if (swatch != null) {
                                            if (Utils.useWhiteFont(vibrantColor)) {
                                                album.getPaletteColors().put(Album.PALETTE_COLOR_TITLE_TEXT, Color.WHITE);
                                                album.getPaletteColors().put(Album.PALETTE_COLOR_BODY_TEXT, Color.WHITE);
                                            } else {
                                                album.getPaletteColors().put(Album.PALETTE_COLOR_TITLE_TEXT, Color.BLACK);
                                                album.getPaletteColors().put(Album.PALETTE_COLOR_BODY_TEXT, Color.BLACK);
                                            }
                                        } else {
                                            album.getPaletteColors().put(Album.PALETTE_COLOR_TITLE_TEXT, Color.BLACK);
                                            album.getPaletteColors().put(Album.PALETTE_COLOR_BODY_TEXT, Color.BLACK);
                                        }
                                    }
                                });
                            }
                        }
                    }

                }
                while (musicCursor.moveToNext());
                tempMusicCursor.close();
                musicCursor.close();
                Settings.getInstance().createAlbumSettings(albumsData);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isAlbumArtLoaded = true;
            songPanel.reloadPanelColors();
            if (mActivity == null) return;
            else ActivityCompat.startPostponedEnterTransition(mActivity);
            if (songPanel == null) {
                Log.e("Null Error", "songPanel is null. Set songPanel before loading album art");
            } else {
                // If we load a song while the album art hasn't loaded yet, it will stay as the default album art,
                // So we check if a song is loaded and, if so, change the album art
                if (songPanel.getmPanelLayout().getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    Song currSong = songPanel.getCurrSong();
                    songPanel.setAlbumArt(currSong.getAlbumUri(), R.id.album_art_panel_main);
                    songPanel.setAlbumArt(currSong.getAlbumArt(), R.id.album_thumbnail);
                }
            }
            Toast.makeText(mActivity, "Finished Loading Album Art", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(mActivity, "Loading album art. Lag may occur", Toast.LENGTH_LONG).show();
        }
    }

    private boolean hasAlbumIdBeenRun(List<Long> albumIds, Long id) {
        for (int i = 0; i < albumIds.size(); i++) {
            if (albumIds.get(i) == id) {
                return true;
            }
        }
        return false;
    }

    private boolean hasArtistBeenAdded(List<String> artists, String artist) {
        for (int i = 0; i < artists.size(); i++) {
            if (artists.get(i).equals(artist)) {
                return true;
            }
        }
        return false;
    }

    public Bitmap getAlbumById(long id) {
        Bitmap bitmap = null;
        for (int i = 0; i < albumsData.size(); i++) {
            if (albumsData.get(i).getAlbumId() == id) {
                return albumsData.get(i).getAlbumArt();
            }
        }
        return null;
    }

    public Album getActualAlbumById(long id) {
        Bitmap bitmAcap = null;
        for (int i = 0; i < albumsData.size(); i++) {
            if (albumsData.get(i).getAlbumId() == id) {
                return albumsData.get(i);
            }
        }
        return null;
    }

    public Song getSongById(int id) {
        for (int i = 0; i < songsData.size(); i++) {
            if (songsData.get(i).getId() == id) {
                return songsData.get(i);
            }
        }
        return songsData.get(0);
    }


    public void onDestroy() {
        for (Album album : albumsData) {
            album.onDestroy();
        }

        for (Song song : songsData) {
            if(!songPanel.getMusicService().getQueue().getQueueList().contains(song)) {
                song.onDestroy();
            }
        }
    }

    public static SongDataHolder getInstance() {
        return instance;
    }

    public List<Song> getSongsData() {
        return songsData;
    }

    public List<Album> getAlbumsData() {
        return albumsData;
    }

    public void setActivity(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setSongPanel(SongPlayerPanel songPanel) {
        this.songPanel = songPanel;
    }

    public SongPlayerPanel getSongPanel() {
        return songPanel;
    }

    public boolean isAlbumArtLoaded() {
        return isAlbumArtLoaded;
    }

    public List<Genre> getGenreData() {
        return genreData;
    }
    public List<Artist> getArtistData() {
        return artistData;
    }

}
