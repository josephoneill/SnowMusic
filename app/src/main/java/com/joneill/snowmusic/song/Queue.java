package com.joneill.snowmusic.song;

import android.util.Log;

import com.joneill.snowmusic.data.SongDataHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep_000 on 1/3/2016.
 */
public class Queue {
    private List<Song> queueList;
    private Song currSong;
    private int currSongIndex;

    public Queue() {
        queueList = new ArrayList<Song>();
        currSongIndex = 0;
    }

    public void addSongToQueue(Song song) {
        queueList.add(song);
    }

    public void addSongListToQueue(List<Song> songs) {
        for(Song song : songs) {
            queueList.add(song);
        }
    }

    public Song getCurrentSong() {
        if(queueList.size() != 0) {
            return queueList.get(currSongIndex);
        }

        return null;
    }

    public Song getSong(int pos) {
        if(pos < queueList.size()) {
            return queueList.get(pos);
        }
        return null;
    }

    public void playPrevSong() {
        if(queueList.size() == 0) {
            return;
        }

        currSongIndex--;
        if(currSongIndex < 0) {
            currSongIndex = queueList.size() - 1;
        }
    }

    public void playNextSong() {
        if(queueList.size() == 0) {
            return;
        }

        currSongIndex++;
        if(currSongIndex == queueList.size()) {
            currSongIndex = 0;
        }
    }

    public List<Long> getSongIdList() {
        List<Long> songIdList = new ArrayList<>();
        for(Song song : queueList) {
            songIdList.add(song.getId());
        }

        return songIdList;
    }

    public static Queue buildQueue(List<Long> ids, int currSongIndex) {
        Queue queue = new Queue();

        for(long id : ids) {
            Song song = SongDataHolder.getInstance().getSongById((int) id);
            queue.addSongToQueue(song);
        }

        queue.setCurrSongIndex(currSongIndex);

        return queue;
    }

    public Song getCurrSong() {
        return currSong;
    }

    public void setCurrSong(Song currSong) {
        this.currSong = currSong;
    }

    public void clearQueue() {
        queueList.clear();
    }

    public List<Song> getQueueList() {
        return queueList;
    }

    public void setQueueList(List<Song> queueList) {
        this.queueList = queueList;
    }

    public int getCurrSongIndex() {
        return currSongIndex;
    }

    public void setCurrSongIndex(int currSongIndex) {
        this.currSongIndex = currSongIndex;
    }
}
