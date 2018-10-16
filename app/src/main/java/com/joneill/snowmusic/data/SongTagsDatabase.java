package com.joneill.snowmusic.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.joneill.snowmusic.song.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep on 8/25/2016.
 */
public class SongTagsDatabase extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "tags";
    // Table Identifier
    private static final String SONG = "song";
    // Tables

    //Playlist variables
    private static final String KEY_SONG_ID = "songId";
    private static final String KEY_TAG_DATA = "_data";
    private static final int LOCATION_SONG_ID = 0;
    private static final int LOCATION_TAG_DATA = 1;

    private SQLiteDatabase db;
    private Context context;

    public SongTagsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.db = getWritableDatabase();
    }

    public void populateTagsTable(int songId, String tag) {
        String TABLE_NAME = SONG + String.valueOf(songId);
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                KEY_SONG_ID + " INTEGER, " +
                KEY_TAG_DATA + " TEXT, " +
                "UNIQUE(" + KEY_SONG_ID + ", " + KEY_TAG_DATA + "))";

        db.execSQL(CREATE_TABLE);

        /*ContentValues values = new ContentValues();
        values.put(KEY_SONG_ID, songId);
        values.put(KEY_TAG_DATA, tag);


        //Insert data into table
        db.insert(TABLE_NAME,
                null, //nullColumnHack,
                values); */


        String INSERT_DATA = "INSERT OR IGNORE INTO " + TABLE_NAME + " (" + KEY_SONG_ID + ", " + KEY_TAG_DATA + ") " +
                "VALUES(" + String.valueOf(songId) + ", " + tag + ")";
        db.execSQL(INSERT_DATA);

        //this.close();
    }

    public void editTagsEntry(String oldTag, String newTag, int songId) {
        String TABLE_NAME = SONG + String.valueOf(songId);

        ContentValues cv = new ContentValues();
        cv.put(KEY_TAG_DATA , newTag);

        db.update(TABLE_NAME, cv, KEY_TAG_DATA + "=" + "'" + oldTag + "'", null);
    }


    public List<Song> getSongsByTag(String tag) {
        List<String> tables = getAllTables();
        List<Song> songs = new ArrayList<Song>();

        for(String table : tables) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE _data=?", new String[]{tag});

            Song song = null;
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                song = SongDataHolder.getInstance().getSongById(cursor.getInt(LOCATION_SONG_ID));
                songs.add(song);
            }
        }
        return songs;
    }

    public List<String> getTagsBySong(String id) {
        String TABLE_NAME = SONG + id;
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                KEY_SONG_ID + " INTEGER, " +
                KEY_TAG_DATA + " TEXT, " +
                "UNIQUE(" + KEY_SONG_ID + ", " + KEY_TAG_DATA + "))";

        db.execSQL(CREATE_TABLE);

        List<String> tags = new ArrayList<String>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, new String[]{});
        if (cursor != null && cursor.getCount() != 0) {            cursor.moveToFirst();
            cursor.moveToFirst();
            while(cursor.moveToNext()) {
                tags.add(cursor.getString(LOCATION_TAG_DATA));
                Log.e("joneill", "joneill tag is " + cursor.getString(LOCATION_TAG_DATA));
            }
        }

        return tags;
    }

    private List<String> getAllTables() {
        List<String> tables = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                tables.add(c.getString(0));
                c.moveToNext();
            }
        }

        return tables;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String CREATE_ALBUMART_TABLE = "CREATE TABLE IF NOT EXISTS " + ALBUMART_TABLE + " ( " +
                KEY_SONG_ID + " INTEGER, " +
                KEY_ALBUMART_DATA + " TEXT )";

        db.execSQL(CREATE_ALBUMART_TABLE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
