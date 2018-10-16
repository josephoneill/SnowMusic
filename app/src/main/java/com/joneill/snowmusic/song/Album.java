package com.joneill.snowmusic.song;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.joneill.snowmusic.data.SongDataHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by joseph on 2/24/15.
 */
public class Album implements Parcelable {
    private String title;
    private String artist;
    private List<Song> songs;
    private Bitmap albumArt;
    private Uri albumUri;
    private long albumId;
    private HashMap<String, Bitmap> resAlbumArts;
    private HashMap<String, Integer> paletteColors;
    private boolean isArtLoaded;
    private int positionInAlbumList;

    public static final String IMAGE_LOW_RES = "lowres";
    public static final String IMAGE_MED_RES = "medres";
    public static final String IMAGE_HIGH_RES = "highres";
    public static final String IMAGE_ULTRA_HIGH_RES = "ultrahighres";

    public static final String PALETTE_COLOR_MAIN = "paletteColorMain";
    public static final String PALETTE_COLOR_SECONDARY = "paletteColorSecondary";
    public static final String PALETTE_COLOR_TITLE_TEXT = "paletteTitleTextColor";
    public static final String PALETTE_COLOR_BODY_TEXT = "paletteBodyTextColor";

    public Album(String title, String artist, Bitmap albumArt, Uri albumUri, long albumId) {
        this.title = title;
        this.artist = artist;
        this.albumArt = albumArt;
        this.albumUri = albumUri;
        this.albumId = albumId;
        resAlbumArts = new HashMap<>();
        paletteColors = new HashMap<>();
    }

    public Album(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        this.title = data[0];
        this.artist = data[1];
        this.albumUri = Uri.parse(data[2]);
        this.albumId = Long.parseLong(data[3]);
    }

    public void initSongs() {
        if(songs == null) {
            songs = new ArrayList<Song>();
        }
        for(int i = 0; i < SongDataHolder.getInstance().getSongsData().size(); i++) {
            Song song = SongDataHolder.getInstance().getSongsData().get(i);
            if(song.getAlbumId() == albumId) {
                songs.add(song);
                song.setAlbum(this);
            }
        }
    }

    public void onDestroy() {
        if (albumArt != null) {
            //albumArt.recycle();
            //albumArt = null;
        }
    }

    public void addAlbumArtRes(String key, Bitmap bitmap) {
        resAlbumArts.put(key, bitmap);
    }

    public Map<String, Bitmap> getResAlbumArts() {
        return resAlbumArts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.title, this.artist, this.albumUri.toString(), Long.toString(this.albumId)});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }

    public Uri getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(Uri albumUri) {
        this.albumUri = albumUri;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public HashMap<String, Integer> getPaletteColors() {
        return paletteColors;
    }

    public boolean isArtLoaded() {
        return isArtLoaded;
    }

    public void setIsArtLoaded(boolean isArtLoaded) {
        this.isArtLoaded = isArtLoaded;
    }

    public int getPositionInAlbumList() {
        return positionInAlbumList;
    }

    public void setPositionInAlbumList(int positionInAlbumList) {
        this.positionInAlbumList = positionInAlbumList;
    }
}
