package com.joneill.snowmusic.song;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.data.TLruCache;
import com.joneill.snowmusic.helper.SongHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Song implements Parcelable {
    private long id;
    private long albumId;
    private long duration;
    private Album album;
    private Uri albumUri;
    private String title;
    private String artist;
    private String durationString;
    private String bitmapKey;
    private String songFilePath;
    private boolean useSingleArt;


    public Song(String bitmapKey, long songId, long albumId, String title, String artist, String durationString, long duration, String songFilePath) {
        this.bitmapKey = bitmapKey;
        this.id = songId;
        this.albumId = albumId;
        this.title = title;
        this.artist = artist;
        this.durationString = durationString;
        this.duration = duration;
        this.songFilePath = songFilePath;
    }

    public void changeAlbumArt(Context context, Bitmap albumArt) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/albumthumbs");
        if (!dir.exists()) {
            dir.mkdir();
            File temp = new File(Environment.getExternalStorageDirectory() + "/albumthumbs/.nomedia");
            try {
                temp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ContentResolver resolver = context.getContentResolver();
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        String file = Environment.getExternalStorageDirectory() + "/albumthumbs/" + String.valueOf(albumId) + ".jpeg";
        File actualFile = new File(file);
        try {
            actualFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!actualFile.exists()) {
        }

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(actualFile);
            albumArt.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        ContentValues values = new ContentValues();
        values.put("album_id", albumId);
        values.put("_data", file);
        resolver.delete(ContentUris.withAppendedId(sArtworkUri, albumId), null, null);
        resolver.insert(sArtworkUri, values);
    }

    public void saveSongInfo(Context context, String title, String artist, String albumTitle) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        if (title != "") {
            values.put(MediaStore.Audio.Media.TITLE, title);
        }
        if (artist != "") {
            values.put(MediaStore.Audio.Media.ARTIST, artist);
        }
        if (albumTitle != "") {
            values.put(MediaStore.Audio.Media.ALBUM, albumTitle);
        }
        resolver.update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Audio.Media._ID + "=?", new String[]{id + ""});
    }

    public void testSaveAlbumBySong(Context context, Bitmap albumArt) {
        if (albumArt == null) {
            return;
        }
        File dir = new File(Environment.getExternalStorageDirectory() + "/albumthumbs");
        if (!dir.exists()) {
            dir.mkdir();
            File temp = new File(Environment.getExternalStorageDirectory() + "/albumthumbs/.nomedia");
            try {
                temp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ContentResolver resolver = context.getContentResolver();
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        String file = Environment.getExternalStorageDirectory() + "/albumthumbs/" + String.valueOf(id) + ".jpeg";
        File actualFile = new File(file);
        try {
            actualFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if(!actualFile.exists()) { return; }

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(actualFile);
            albumArt.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        SongHelper.getInstance().getAlbumArtDb().populateAlbumArtTable((int) id, file);
    }

    public long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return TLruCache.getInstance().getBitmap(bitmapKey);
    }

    /*public void setImage(Bitmap albumArt) {
        this.albumArt = albumArt;
    }*/

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public Bitmap getAlbumArt() {
        Bitmap art = TLruCache.getInstance().getBitmap(bitmapKey);
        if(art == null) {
            art = SongDataHolder.DEFAULT_BITMAP;
        }
        return art;
    }

    /*public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }*/

    public void setAlbumArt(String bitmapKey) {
        this.bitmapKey = bitmapKey;
    }

    public void setImage(String bitmapKey) {
        this.bitmapKey = bitmapKey;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public Uri getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(Uri albumUri) {
        this.albumUri = albumUri;
    }

    public String getSongFilePath() {
        return songFilePath;
    }

    public void setSongFilePath(String songFilePath) {
        this.songFilePath = songFilePath;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
        /*if(albumArt == null) {
            albumArt = album.getAlbumArt();
        }*/
    }

    public void onDestroy() {
        /*if (albumArt != null) {
            albumArt.recycle();
            albumArt = null;
        }*/
    }


    public boolean isUseSingleArt() {
        return useSingleArt;
    }

    public void setUseSingleArt(boolean useSingleArt) {
        this.useSingleArt = useSingleArt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.albumId);
        dest.writeLong(this.duration);
        dest.writeParcelable(this.album, flags);
        dest.writeParcelable(this.albumUri, flags);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeString(this.durationString);
        dest.writeString(this.bitmapKey);
        dest.writeString(this.songFilePath);
        dest.writeByte(this.useSingleArt ? (byte) 1 : (byte) 0);
    }

    protected Song(Parcel in) {
        this.id = in.readLong();
        this.albumId = in.readLong();
        this.duration = in.readLong();
        this.album = in.readParcelable(Album.class.getClassLoader());
        this.albumUri = in.readParcelable(Uri.class.getClassLoader());
        this.title = in.readString();
        this.artist = in.readString();
        this.durationString = in.readString();
        this.bitmapKey = in.readString();
        this.songFilePath = in.readString();
        this.useSingleArt = in.readByte() != 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}