package com.joneill.snowmusic.song;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep on 9/3/2016.
 */
public class Genre implements Parcelable {
    private String genre;
    private List<Song> songs;
    private int positionInGenreList;

    public Genre(String genre) {
        this.genre = genre;
        songs = new ArrayList<Song>();
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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
        dest.writeString(this.genre);
        dest.writeTypedList(this.songs);
        dest.writeInt(this.positionInGenreList);
    }

    protected Genre(Parcel in) {
        this.genre = in.readString();
        this.songs = in.createTypedArrayList(Song.CREATOR);
        this.positionInGenreList = in.readInt();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel source) {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    public int getPositionInGenreList() {
        return positionInGenreList;
    }

    public void setPositionInGenreList(int positionInGenreList) {

        this.positionInGenreList = positionInGenreList;
    }
}
