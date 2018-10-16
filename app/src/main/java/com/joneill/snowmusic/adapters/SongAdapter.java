package com.joneill.snowmusic.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.activities.EditSongInfoActivity;
import com.joneill.snowmusic.activities.TagsActivity;
import com.joneill.snowmusic.fragments.SongsFragment;
import com.joneill.snowmusic.helper.AlbumArtDownloader;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.dialogs.PlaylistPopup;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by josep_000 on 8/21/2014.
 */
public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Song> songsData;

    private Fragment fragment;
    private SongPlayerPanel songPanel;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public SongAdapter(Fragment fragment, SongPlayerPanel songPanel, List<Song> songsData, Context context) {
        this.fragment = fragment;
        this.songPanel = songPanel;
        this.songsData = songsData;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        /*View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);*/

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.new_song_item_layout, parent, false);
            SongViewHolder viewHolder = new SongViewHolder(v);
            return viewHolder;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.song_list_header_layout, parent, false);
            SongHeaderViewHolder viewHolder = new SongHeaderViewHolder(v);
            return viewHolder;
        } else {
            return null;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //Because of the header
        position -= 1;
        if (viewHolder instanceof SongViewHolder) {
            int bgColor = (int) Settings.getInstance().getSettingsData().get(Settings.INT_SONGS_FRAG_BG);
            //Get items from xml and replace it with song data
            SongViewHolder holder = (SongViewHolder) viewHolder;

            //Convert our album bitmaps into circles. We create a seperate object for this
            //because the circlizeBitmap method recycles the bitmap
            //Bitmap albumArt = BitmapHelper.getInstance().circlizeBitmap(songsData.get(position).getImage());
            Bitmap albumArt = songsData.get(position).getAlbumArt();
            holder.imgAlbumArt.setImageBitmap(albumArt);
            holder.txtViewTitle.setText(songsData.get(position).getTitle());
            holder.txtViewArtist.setText(songsData.get(position).getArtist());
            holder.txtViewDuration.setText(songsData.get(position).getDurationString());

            if(Utils.useWhiteFont(bgColor)) {
                holder.txtViewTitle.setTextColor(Color.WHITE);
                holder.txtViewArtist.setTextColor(Color.WHITE);
                holder.txtViewDuration.setTextColor(Color.WHITE);
            } else {
                holder.txtViewTitle.setTextColor(Color.BLACK);
                holder.txtViewArtist.setTextColor(Color.BLACK);
                holder.txtViewDuration.setTextColor(Color.BLACK);
            }

        } else if (viewHolder instanceof SongHeaderViewHolder) {

        }
    }

    // Inner class to hold a reference to each item of RecyclerView
    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgAlbumArt;
        public TextView txtViewTitle;
        public TextView txtViewArtist;
        public TextView txtViewDuration;
        public ImageButton menuImageButton;
        public View songItem;

        public SongViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgAlbumArt = (ImageView) itemLayoutView.findViewById(R.id.item_album_art);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            txtViewArtist = (TextView) itemLayoutView.findViewById(R.id.item_artist);
            txtViewDuration = (TextView) itemLayoutView.findViewById(R.id.item_duration);
            menuImageButton = (ImageButton) itemLayoutView.findViewById(R.id.song_frag_overflow_menu);
            songItem = (View) itemLayoutView.findViewById(R.id.song_item);
            songItem.setOnClickListener(this);
            menuImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            final int position = getAdapterPosition() - 1;
            if (v.getId() == R.id.song_frag_overflow_menu) {
                final PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.song_item_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //-1 because of header
                        switch (item.getItemId()) {
                            case R.id.song_menu_i1:
                                playSong(position);
                                break;
                            case R.id.song_menu_i2:
                                PlaylistPopup popup = new PlaylistPopup(context, songsData.get(position).getId());
                                int xOffset = (int) -popup.getWidth() + (int) Utils.convertDpToPixels(20, context);
                                int yOffset = (int) -popup.getHeight() / 2 - (int) Utils.convertDpToPixels(33, context);
                                popup.showAsDropDown(v, xOffset, yOffset);
                                popup.setFocusable(true);
                                popup.update();
                                break;
                            case R.id.song_menu_i3:
                                Song song = songsData.get(position);
                                Intent i = new Intent(context, EditSongInfoActivity.class);
                                i.putExtra(EditSongInfoActivity.SONG_PARCLEABLE, song);
                                i.putExtra(EditSongInfoActivity.SONG_POSITION, position);
                                fragment.startActivityForResult(i, SongsFragment.SONG_RESULT_CODE);
                                break;
                            case R.id.song_menu_i4:
                                ProgressDialog dialog = new ProgressDialog(context);
                                AlbumArtDownloader albumArtDownloader = new AlbumArtDownloader(context, dialog);
                                List<Song> songs = new ArrayList();
                                songs.add(songsData.get(position));
                                albumArtDownloader.setSongsList(songs);
                                dialog.setTitle("Downloading Album Art");
                                dialog.setMessage("Please wait while downloading...");
                                dialog.show();
                                try {
                                    albumArtDownloader.fetchAlbumFromLastFM(true, true);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case R.id.song_menu_i5:
                                Intent tags = new Intent(context, TagsActivity.class);
                                tags.putExtra(TagsActivity.SONG_PARCLEABLE, songsData.get(position));
                                fragment.startActivityForResult(tags, SongsFragment.SONG_RESULT_CODE);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            } else {
                playSong(position);
            }
        }
    }

    private void playSong(int position) {
        if (songPanel.getMusicService() == null) {
            return;
        }
        if (songPanel.getMusicService().getList() != songsData) {
            songPanel.getMusicService().setList(songsData);
        }
        if(songPanel.startSong(position)) {
            Log.e("true", "joneill TRUE");
            songPanel.openSongInPanel(true, songsData.get(position), false);
        }
    }

    public class SongHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View headerItem;

        public SongHeaderViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            headerItem = (View) itemLayoutView.findViewById(R.id.song_header_item);
            headerItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            List<Song> songs = new ArrayList<>(songsData);
            Collections.shuffle(songs);
            songPanel.getMusicService().setList(songs);
            if(songPanel.startSong(0)) {
                songPanel.openSongInPanel(true, songPanel.getMusicService().getList().get(0), false);
            }

        }
    }

    // Return the size of your songsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return songsData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position - 1;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public SongPlayerPanel getSongPanel() {
        return songPanel;
    }

    public List<Song> getSongsData() {
        return songsData;
    }

    public void setSongsData(List<Song> songsData, boolean sort) {
        this.songsData = songsData;
        if (sort) {
            Utils.sortSongs(songsData);
        }
    }
}