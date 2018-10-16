package com.joneill.snowmusic.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.joneill.snowmusic.helper.BitmapHelper;
import com.joneill.snowmusic.song.Playlist;
import com.joneill.snowmusic.song.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep on 8/25/2016.
 */
public class SongAlbumArtDatabase extends SQLiteOpenHelper {
    private boolean hasDroppedGunTable;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "albumart";
    // Tables
    private static final String ALBUMART_TABLE = "albumart";

    //Playlist variables
    private static final String KEY_SONG_ID = "id";
    private static final String KEY_ALBUMART_DATA = "_data";
    private static final int LOCATION_SONG_ID = 0;
    private static final int LOCATION_ALBUMART_DATA = 1;

    private SQLiteDatabase db;
    private Context context;

    public SongAlbumArtDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.db = getWritableDatabase();
    }

    private void createTables() {

    }

    public void populateAlbumArtTable(int id, String path) {
        String CREATE_ALBUMART_TABLE = "CREATE TABLE IF NOT EXISTS " + ALBUMART_TABLE + " ( " +
                KEY_SONG_ID + " INTEGER, " +
                KEY_ALBUMART_DATA + " TEXT )";

        db.execSQL(CREATE_ALBUMART_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_SONG_ID, id);
        values.put(KEY_ALBUMART_DATA, path);

        //Insert data into table
        db.insert(ALBUMART_TABLE,
                null, //nullColumnHack,
                values);

        //this.close();
    }


    public Uri getAlbumArtUri(String id) {
        Uri uri = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + ALBUMART_TABLE + " WHERE id=?", new String[]{id});

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            uri = Uri.fromFile(new File(cursor.getString(LOCATION_ALBUMART_DATA)));

        }
        return uri;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALBUMART_TABLE = "CREATE TABLE IF NOT EXISTS " + ALBUMART_TABLE + " ( " +
                KEY_SONG_ID + " INTEGER, " +
                KEY_ALBUMART_DATA + " TEXT )";

        db.execSQL(CREATE_ALBUMART_TABLE);
    }
    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
