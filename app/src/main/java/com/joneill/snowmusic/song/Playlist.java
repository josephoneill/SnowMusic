package com.joneill.snowmusic.song;

import android.os.Parcel;
import android.os.Parcelable;

import com.joneill.snowmusic.data.PlaylistDatabase;

import java.util.List;

/**
 * Created by josep_000 on 7/3/2016.
 */
public class Playlist implements Parcelable {
    private int id;
    private String name;
    private List<Song> songsList;
    private int positionInPlaylistList;

    public Playlist(int id, String name, List<Song> songsList) {
        this.id = id;
        this.name = name;
        this.songsList = songsList;
    }


    public void save(PlaylistDatabase database) {
        database.populatePlaylistTable(id, name);
        for(int i = 0; i < songsList.size(); i++) {
            database.populateSongsTable((int) songsList.get(i).getId(), id, i);
        }
    }

    public void addSong(PlaylistDatabase database, Song song) {
        songsList.add(song);
        database.populateSongsTable((int) song.getId(), id, songsList.size());
    }

    public boolean containsSong(Song song) {
        return songsList.contains(song);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongsList() {
        return songsList;
    }

    public void setSongsList(List<Song> songsList) {
        this.songsList = songsList;
    }

    public int getPositionInPlaylistList() {
        return positionInPlaylistList;
    }

    public void setPositionInPlaylistList(int positionInPlaylistList) {
        this.positionInPlaylistList = positionInPlaylistList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.songsList);
        dest.writeInt(this.positionInPlaylistList);
    }

    protected Playlist(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.songsList = in.createTypedArrayList(Song.CREATOR);
        this.positionInPlaylistList = in.readInt();
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel source) {
            return new Playlist(source);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
}
