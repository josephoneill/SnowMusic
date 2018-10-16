package com.joneill.snowmusic.song;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep on 9/3/2016.
 */
public class Artist implements Parcelable {
    private String artist;
    private List<Song> songs;
    private int positionInArtistList;

    public Artist(String artist) {
        this.artist = artist;
        songs = new ArrayList<Song>();
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.artist);
        dest.writeTypedList(this.songs);
        dest.writeInt(this.positionInArtistList);
    }

    protected Artist(Parcel in) {
        this.artist = in.readString();
        this.songs = in.createTypedArrayList(Song.CREATOR);
        this.positionInArtistList = in.readInt();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public int getPositionInArtistList() {
        return positionInArtistList;
    }

    public void setPositionInArtistList(int positionInGenreList) {
        this.positionInArtistList = positionInGenreList;
    }
}
