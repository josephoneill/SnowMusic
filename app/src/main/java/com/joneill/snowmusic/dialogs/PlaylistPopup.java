package com.joneill.snowmusic.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.data.PlaylistDatabase;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.helper.TriggersHelper;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Playlist;
import com.joneill.snowmusic.song.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep_000 on 7/4/2016.
 */
public class PlaylistPopup extends PopupWindow {
    private PlaylistDatabase playlistDatabase;
    private long songId;
    private Context context;

    public PlaylistPopup(Context context, long songId) {
        super(((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_add_to_playlist, null),
                (int) Utils.convertDpToPixels(300, context), (int) Utils.convertDpToPixels(264.71f, context));
        this.context = context;
        this.songId = songId;
        init();
    }

    private void init() {
        View bg = this.getContentView().findViewById(R.id.rl_atp_dialog);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.bg_bubble_popup);
        int color = context.getResources().getColor(R.color.background_gray);
        //drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        //bg.setBackground(drawable);

        playlistDatabase = new PlaylistDatabase(context);
        List<Playlist> playlists = playlistDatabase.getPlaylists();
        PlaylistAdapter mAdapter = new PlaylistAdapter(playlists, playlistDatabase, this, songId);
        RecyclerView recyclerView = (RecyclerView) getContentView().findViewById(R.id.rvAddToPlaylist);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(mAdapter);
    }

    public class PlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private PlaylistDatabase playlistDatabase;
        private List<Playlist> playlistData;
        private PopupWindow window;
        private long songId;

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        public PlaylistAdapter(List<Playlist> playlistData, PlaylistDatabase playlistDatabase, PopupWindow window, long songId) {
            playlistDatabase = new PlaylistDatabase(context);
            this.playlistData = playlistData;
            this.playlistDatabase = playlistDatabase;
            this.window = window;
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
                List<Song> songs = new ArrayList<Song>();
                final Song song = SongDataHolder.getInstance().getSongById((int) songId);
                songs.add(song);
                final Playlist playlist = playlistData.get(getAdapterPosition() - 1);
                if(playlist.containsSong(song)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setTitle(context.getResources().getString(R.string.dialog_add_duplicate_to_playlist));
                    builder1.setMessage(context.getResources().getString(R.string.dialog_add_duplicate_to_playlist_desc));
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    playlist.addSong(playlistDatabase, song);
                                    TriggersHelper.getInstance().setNewPlaylistAddded(true);
                                    window.dismiss();
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    playlist.addSong(playlistDatabase, song);
                    TriggersHelper.getInstance().setNewPlaylistAddded(true);
                    window.dismiss();
                }
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
                window.dismiss();
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_new_playlist);
                dialog.setTitle(R.string.new_playlist);

                final EditText et = (EditText) dialog.findViewById(R.id.et_NewPlaylist);
                Button btnSave = (Button) dialog.findViewById(R.id.btn_save_playlist);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(et.getText().toString().equals("")) {
                            Toast.makeText(context, "Playlist Name is Empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        createPlaylist(et.getText().toString());
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

            private void createPlaylist(String title) {
                final Song song = SongDataHolder.getInstance().getSongById((int) songId);
                if(song == null) {
                    Log.e("Song is null", "dang, song is null");
                }
                List<Song> songs = new ArrayList<Song>();
                songs.add(song);
                Playlist playlist = new Playlist(playlistData.size() + 1, title, songs);
                playlist.save(playlistDatabase);
                TriggersHelper.getInstance().setNewPlaylistAddded(true);
            }
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
