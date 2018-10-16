package com.joneill.snowmusic.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.data.PlaylistDatabase;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.song.Playlist;
import com.joneill.snowmusic.song.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep_000 on 7/4/2016.
 */
public class PlaylistDialog extends Dialog {
    private PlaylistDatabase playlistDatabase;
    private long songId;
    private Context context;

    public PlaylistDialog(Context context, long songId) {
        super(context);
        this.context = context;
        this.songId = songId;
        init();
    }



    private void init() {
        setContentView(R.layout.dialog_add_to_playlist);
        playlistDatabase = new PlaylistDatabase(context);
        List<Playlist> playlists = playlistDatabase.getPlaylists();
        PlaylistAdapter mAdapter = new PlaylistAdapter(playlists, playlistDatabase, songId);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvAddToPlaylist);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(mAdapter);
    }

    public class PlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private PlaylistDatabase playlistDatabase;
        private List<Playlist> playlistData;
        private long songId;

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        public PlaylistAdapter(List<Playlist> playlistData, PlaylistDatabase playlistDatabase, long songId) {
            playlistDatabase = new PlaylistDatabase(context);
            this.playlistData = playlistData;
            this.playlistDatabase = playlistDatabase;
            this.songId = songId;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.playlist_dialog_item, parent, false);
                PlaylistViewHolder viewHolder = new PlaylistViewHolder(v);
                return viewHolder;
            } else if (viewType == TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.playlist_dialog_item, parent, false);
                PlaylistHeaderViewHolder viewHolder = new PlaylistHeaderViewHolder(v);
                return viewHolder;
            } else {
                return null;
            }
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof PlaylistViewHolder) {
                PlaylistViewHolder holder = (PlaylistViewHolder) viewHolder;
                //position - 1 because of the header
                holder.name.setText(String.valueOf((playlistData.get(position - 1).getName())));
            } else if (viewHolder instanceof PlaylistHeaderViewHolder) {
                PlaylistHeaderViewHolder holder = (PlaylistHeaderViewHolder) viewHolder;
                holder.name.setText("Add new playlist");
            }
        }

        // Inner class to hold a reference to each item of RecyclerView
        public class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView name;

            public PlaylistViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                name = (TextView) itemLayoutView.findViewById(R.id.dialog_playlist_item_name);
                itemLayoutView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }
        }

        public class PlaylistHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView name;

            public PlaylistHeaderViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                name = (TextView) itemLayoutView.findViewById(R.id.dialog_playlist_item_name);
                itemLayoutView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_new_playlist);
                dialog.setTitle(R.string.new_playlist);
            }
        }

        private void createPlaylist() {
            final Song song = SongDataHolder.getInstance().getSongById((int) songId);
            List<Song> songs = new ArrayList<Song>();
            songs.add(song);
            Playlist playlist = new Playlist(playlistData.size() + 1, "ran", songs);
            playlist.save(playlistDatabase);
        }

        @Override
        public int getItemCount() {
            return playlistData.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            }

            return TYPE_ITEM;
        }
    }
}
