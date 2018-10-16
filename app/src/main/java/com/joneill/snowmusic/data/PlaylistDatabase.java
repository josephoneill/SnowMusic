package com.joneill.snowmusic.data;

/**
 * Created by josep_000 on 7/3/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.joneill.snowmusic.song.Playlist;
import com.joneill.snowmusic.song.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep_000 on 8/16/2015.
 */
public class PlaylistDatabase extends SQLiteOpenHelper {
    private boolean hasDroppedGunTable;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "playlists";
    // Tables
    private static final String PLAYLIST_TABLE = "PlaylistData";
    private static final String SONGS_TABLE = "Songs";

    //Playlist variables
    private static final String KEY_PLAYLIST_ID = "id";
    private static final String KEY_PLAYLIST_NAME = "name";
    private static final int LOCATION_PLAYLIST_ID = 0;
    private static final int LOCATION_PLAYLIST_NAME = 1;

    //Songs variables
    private static final String KEY_SONG_ID = "id";
    private static final String KEY_SONG_PLAYLIST_ID = "playlistId";
    private static final String KEY_SONG_POSITION = "position";
    private static final int LOCATION_SONG_ID = 0;
    private static final int LOCATION_SONG_PLAYLIST_ID = 1;
    private static final int LOCATION_SONG_POSITION = 2;

    public static final String QUERY_PLAYLISTS = "SELECT * FROM " + PLAYLIST_TABLE;

    private SQLiteDatabase db;

    public PlaylistDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.db = getWritableDatabase();
    }

    private void createTables() {

    }

    public void populatePlaylistTable(int id, String name) {
        String CREATE_PLAYLIST_TABLE = "CREATE TABLE IF NOT EXISTS " + PLAYLIST_TABLE + " ( " +
                KEY_PLAYLIST_ID + " INTEGER, " +
                KEY_PLAYLIST_NAME + " TEXT )";

        db.execSQL(CREATE_PLAYLIST_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYLIST_ID, id);
        values.put(KEY_PLAYLIST_NAME, name);

        //Insert data into table
        db.insert(PLAYLIST_TABLE,
                null, //nullColumnHack,
                values);

        //this.close();
    }

    public void populateSongsTable(int id, int playlistId, int position) {
        String CREATE_SONGS_TABLE = "CREATE TABLE IF NOT EXISTS " + SONGS_TABLE + String.valueOf(playlistId) + " ( " +
                KEY_SONG_ID + " INTEGER, " +
                KEY_SONG_PLAYLIST_ID + " INTEGER, " +
                KEY_SONG_POSITION + " INTEGER )";

        db.execSQL(CREATE_SONGS_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_SONG_ID, id);
        values.put(KEY_SONG_PLAYLIST_ID, playlistId);
        values.put(KEY_SONG_POSITION, position);

        //Insert data into table
        db.insert(SONGS_TABLE + String.valueOf(playlistId),
                null, //nullColumnHack,
                values);

        //this.close();
    }

    public Playlist getPlaylist(String id) {
        Playlist playlist = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + PLAYLIST_TABLE + " WHERE id=?", new String[]{id});

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            //Build the gun
            playlist = buildPlaylist(cursor);
        }
        return playlist;
    }

    public List<Playlist> getPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();

        Cursor tableCursor = db.rawQuery("SELECT * FROM " + PLAYLIST_TABLE, null);

        if (tableCursor.moveToFirst()) {
            do {
                Playlist playlist = buildPlaylist(tableCursor);
                playlists.add(playlist);
            } while (tableCursor.moveToNext());
        }

        return playlists;
    }

    private Playlist buildPlaylist(Cursor cursor) {
        Playlist playlist = new Playlist(0, "", null);

        String id = cursor.getString(LOCATION_PLAYLIST_ID);
        if(id == null) { id = "1"; }
        playlist.setId(Integer.parseInt(id));
        playlist.setName(cursor.getString(LOCATION_PLAYLIST_NAME));
        playlist.setSongsList(getPlaylistSongs(id));
        return playlist;
    }

    private List<Song> getPlaylistSongs(String id) {
        ArrayList<Song> songs = new ArrayList<Song>();

        Cursor tableCursor = db.rawQuery("SELECT * FROM " + SONGS_TABLE + String.valueOf(id) + " WHERE " + KEY_SONG_PLAYLIST_ID + " = ?", new String[]{id});

        if (tableCursor.moveToFirst()) {
            do {
                int songId = Integer.parseInt(tableCursor.getString(LOCATION_SONG_ID));
                Song song = SongDataHolder.getInstance().getSongById(songId);
                songs.add(song);
            } while (tableCursor.moveToNext());
        }

        return songs;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYLIST_TABLE = "CREATE TABLE IF NOT EXISTS " + PLAYLIST_TABLE + " ( " +
                KEY_PLAYLIST_ID + " INTEGER, " +
                KEY_PLAYLIST_NAME + " TEXT )";

        db.execSQL(CREATE_PLAYLIST_TABLE);

        String CREATE_SONGS_TABLE = "CREATE TABLE IF NOT EXISTS " + SONGS_TABLE + " ( " +
                KEY_SONG_ID + " INTEGER, " +
                KEY_SONG_PLAYLIST_ID + " INTEGER, " +
                KEY_SONG_POSITION + " INTEGER )";

        db.execSQL(CREATE_SONGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
