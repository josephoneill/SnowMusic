package com.joneill.snowmusic.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Playlist;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;

/**
 * Created by joseph on 4/1/15.
 */


/**
 * Created by josep_000 on 8/21/2014.
 */
public class PlaylistPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SongPlayerPanel songPanel;
    private Context context;
    private Playlist playlist;
    private CollapsingToolbarLayout toolbar;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public PlaylistPageAdapter(SongPlayerPanel songPanel, Playlist playlist, Context context, CollapsingToolbarLayout toolbar) {
        this.songPanel = songPanel;
        this.playlist = playlist;
        this.context = context;
        this.toolbar = toolbar;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.album_song_item_layout, parent, false);
            PlaylistPageViewHolder viewHolder = new PlaylistPageViewHolder(v);
            return viewHolder;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.album_page_list_header, parent, false);
            PlaylistPageViewHolderHeader viewHolder = new PlaylistPageViewHolderHeader(v);
            return viewHolder;
        } else {
            return null;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Album album = playlist.getSongsList().get(0).getAlbum();
        if (viewHolder instanceof PlaylistPageViewHolder) {
            PlaylistPageViewHolder holder = (PlaylistPageViewHolder) viewHolder;
            Song song = playlist.getSongsList().get(position - 1);
            holder.title.setText(song.getTitle());
            holder.number.setText(Integer.toString(position));
            holder.duration.setText(song.getDurationString());
        } else if (viewHolder instanceof PlaylistPageViewHolderHeader) {
            final PlaylistPageViewHolderHeader holder = (PlaylistPageViewHolderHeader) viewHolder;
            holder.tvAlbum.setText(playlist.getName());
            holder.tvArtist.setText(playlist.getName());

            if (Settings.getInstance().getSettingsData().get(Settings.BOOL_ALP_HEADER_USE_PALETTE +
                    String.valueOf(album.getAlbumId())) != null) {
                if ((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_ALP_HEADER_USE_PALETTE +
                        String.valueOf(album.getAlbumId()))) {

                    if (album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN) != null) {
                        int mainColor = album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN);
                        holder.background.setBackgroundColor(mainColor);
                        holder.tvAlbum.setTextColor(album.getPaletteColors().get(Album.PALETTE_COLOR_TITLE_TEXT));
                        holder.tvArtist.setTextColor(album.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT));
                    } else {
                        holder.background.setBackgroundColor(Color.WHITE);
                        holder.tvAlbum.setTextColor(Color.BLACK);
                        holder.tvArtist.setTextColor(Color.BLACK);
                    }
                } else {
                    int color = (int) Settings.getInstance().getSettingsData().get(Settings.INT_ALP_HEADER_COLOR
                            + String.valueOf(album.getAlbumId()));
                    if (Utils.useWhiteFont(color)) {
                        holder.tvAlbum.setTextColor(Color.WHITE);
                        holder.tvArtist.setTextColor(Color.WHITE);
                    }
                    holder.background.setBackgroundColor(color);
                }
            }
        }
    }

    // Inner class to hold a reference to each item of RecyclerView
    public class PlaylistPageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView number;
        public TextView title;
        public TextView duration;

        public PlaylistPageViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            title = (TextView) itemView.findViewById(R.id.album_list_item_title);
            number = (TextView) itemView.findViewById(R.id.album_list_item_number);
            duration = (TextView) itemView.findViewById(R.id.album_list_item_duration);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getPosition() == 0) {
                v.setVisibility(View.GONE);
            }
            if (songPanel.getMusicService().getList() != playlist.getSongsList()) {
                songPanel.getMusicService().setList(playlist.getSongsList());
            }
            songPanel.openSongInPanel(true, playlist.getSongsList().get(getPosition() - 1), false);
            songPanel.startSong(getPosition() - 1);
        }
    }

    // Return the size of your songsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return playlist.getSongsList().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    public SongPlayerPanel getSongPanel() {
        return songPanel;
    }

    public class PlaylistPageViewHolderHeader extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvArtist;
        public TextView tvAlbum;
        public View background;
        public View clickableBackground;
        // ImageButton menuButton;

        public PlaylistPageViewHolderHeader(View itemView) {
            super(itemView);
            tvArtist = (TextView) itemView.findViewById(R.id.tv_album_page_title);
            tvAlbum = (TextView) itemView.findViewById(R.id.tv_album_page_artist);
            background = (View) itemView.findViewById(R.id.album_page_header_view);
            clickableBackground = (View) itemView.findViewById(R.id.album_page_header_view_clickable);
            clickableBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(context);
            final View view = v;
            final TextView tvAlbum = (TextView) v.findViewById(R.id.tv_album_page_title);
            final TextView tvArtist = (TextView) v.findViewById(R.id.tv_album_page_artist);
            dialog.setContentView(R.layout.dialog_color_type);
            dialog.setTitle("Set Header Color");

            //If palette color is selected
            RadioButton rbPalette = (RadioButton) dialog.findViewById(R.id.rbDialogCTPalette);
            rbPalette.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*int color = album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN);
                    Settings.getInstance().getSettingsData().put(Settings.BOOL_ALP_HEADER_USE_PALETTE
                            + String.valueOf(album.getAlbumId()), true);
                    view.setBackgroundColor(color);
                    setTextColor(color, true, tvAlbum, tvArtist);
                    Settings.getInstance().saveSettings(); */
                    dialog.dismiss();
                }
            });

            //If a custom color is selected
            RadioButton rbCustom = (RadioButton) dialog.findViewById(R.id.rbDialogCTCustom);
            rbCustom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* final String keyValColor = Settings.INT_ALP_HEADER_COLOR + String.valueOf(album.getAlbumId());
                    final String keyValUsePalette = Settings.BOOL_ALP_HEADER_USE_PALETTE + String.valueOf(album.getAlbumId());
                    Dialog colorDialog = Utils.createColorChooserDialog(context);

                    final ColorPicker colorPicker = (ColorPicker) colorDialog.findViewById(R.id.colorPicker);

                    SVBar svBar = (SVBar) colorDialog.findViewById(R.id.svBar);
                    colorPicker.addSVBar(svBar);

                    colorPicker.setColor((int) Settings.getInstance().getSettingsData()
                            .get(Settings.INT_ALP_HEADER_COLOR + String.valueOf(album.getAlbumId())));

                    colorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            int color = colorPicker.getColor();
                            view.setBackgroundColor(color);
                            toolbar.setContentScrimColor(color);
                            toolbar.setStatusBarScrimColor(color);
                            setTextColor(color, false, tvAlbum, tvArtist);
                            Settings.getInstance().getSettingsData().put(keyValColor, colorPicker.getColor());
                            Settings.getInstance().getSettingsData().put(keyValUsePalette, false);
                            Settings.getInstance().saveSettings();
                        }
                    });

                    colorDialog.show();

                    dialog.dismiss(); */
                }
            });

            dialog.show();
        }
    }

    private void setTextColor(int color, boolean usePalette, TextView tvAlbum, TextView tvArtist) {
        Album album = playlist.getSongsList().get(0).getAlbum();
        if (usePalette) {
            tvAlbum.setTextColor(album.getPaletteColors().get(Album.PALETTE_COLOR_TITLE_TEXT));
            tvArtist.setTextColor(album.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT));
        } else {
            if (Utils.useWhiteFont(color)) {
                tvAlbum.setTextColor(Color.WHITE);
                tvArtist.setTextColor(Color.WHITE);
            } else {
                tvAlbum.setTextColor(Color.BLACK);
                tvArtist.setTextColor(Color.BLACK);
            }
        }
    }
}

/*public class AlbumPageAdapter extends ArrayAdapter<Song> {
    private Context context;
    private int layoutResourceId;
    private List<Song> songs;

    public AlbumPageAdapter(Context context, int layoutResourceId, List<Song> songs) {
        super(context, layoutResourceId, songs);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.songs = songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* View row = convertView;
        ViewHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new AlbumPageViewHolder();
            holder.title = (TextView)row.findViewById(R.id.album_list_item_title);
            holder.number = (TextView)row.findViewById(R.id.album_list_item_number);
            holder.duration = (TextView)row.findViewById(R.id.album_list_item_duration);
            //holder.menuButton = (ImageButton)row.findViewById(R.id.album_list_overflow_menu);
            row.setTag(holder);
        } else {
            holder = (AlbumPageViewHolder) row.getTag();
        }

        Song song = songs.get(position);
        holder.title.setText(song.getTitle());
        holder.number.setText(Integer.toString(position + 1));
        holder.duration.setText(song.getDurationString());
        return row;*/
        /*return null;
    }

    public static class AlbumPageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView number;
        public TextView title;
        public TextView duration;
        // ImageButton menuButton;

        public AlbumPageViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView)itemView.findViewById(R.id.album_list_item_title);
            number = (TextView)itemView.findViewById(R.id.album_list_item_number);
            duration = (TextView)itemView.findViewById(R.id.album_list_item_duration);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public static class AlbumPageViewHolderHeader extends RecyclerView.ViewHolder {
        public TextView artist;
        public TextView album;
        // ImageButton menuButton;

        public AlbumPageViewHolderHeader(View itemView) {
            super(itemView);
            artist = (TextView)itemView.findViewById(R.id.tv_album_page_title);
            album = (TextView)itemView.findViewById(R.id.tv_album_page_artist);
        }
    }
}*/
