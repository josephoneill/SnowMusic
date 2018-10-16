package com.joneill.snowmusic.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.fragments.PlaylistPageFragment;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Playlist;
import com.joneill.snowmusic.song.SongPlayerPanel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by joseph on 3/1/15.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private Context context;
    private List<Playlist> playlistsData;
    private SongPlayerPanel songPanel;
    private Fragment fragment;
    private View layout;
    public static final String TRANSITION_NAME_ALBUM = "trans_playlist_card_";
    private HashMap<Integer, String> transitionHashMap;

    public PlaylistAdapter(Context context, SongPlayerPanel songPanel, List<Playlist> playlistsData, Fragment fragment, View layout) {
        this.context = context;
        this.songPanel = songPanel;
        this.playlistsData = playlistsData;
        this.fragment = fragment;
        this.layout = layout;
        transitionHashMap = new HashMap<Integer, String>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        if(playlistsData.get(position).getSongsList().size() == 0) { return; }
        final Playlist playlist = playlistsData.get(position);
        final Album initialAlbum = playlistsData.get(position).getSongsList().get(0).getAlbum();
        String transName = "trans_playlist_card_" + playlist.getId();
        playlist.setPositionInPlaylistList(position);
        viewHolder.imgPlaylistArt.setImageBitmap(initialAlbum.getAlbumArt());
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animation.setDuration(1000);
        viewHolder.imgPlaylistArt.setAnimation(animation);
        ViewCompat.setTransitionName(viewHolder.imgPlaylistArt, transName);
        viewHolder.txtPlaylistTitle.setText(playlistsData.get(position).getName());
        viewHolder.txtPlaylistArtist.setText(playlistsData.get(position).getName());

        if (Settings.getInstance().getSettingsData().get(Settings.BOOL_PLAYLIST_CARD_USE_PALETTE) != null) {
            if ((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_PLAYLIST_CARD_USE_PALETTE)) {
                if (initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_MAIN) != null) {
                    viewHolder.itemView.setBackgroundColor(initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_MAIN));
                    viewHolder.txtPlaylistTitle.setTextColor(initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_TITLE_TEXT));
                    if(initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT) == null) { return; }
                    viewHolder.txtPlaylistArtist.setTextColor(initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT));
                } else {
                    viewHolder.itemView.setBackgroundColor(Color.WHITE);
                    viewHolder.txtPlaylistTitle.setTextColor(Color.BLACK);
                    viewHolder.txtPlaylistArtist.setTextColor(Color.BLACK);
                }
            } else {
                int color = (int) Settings.getInstance().getSettingsData()
                        .get(Settings.INT_PLAYLIST_CARD_COLOR);
                viewHolder.itemView.setBackgroundColor(color);
                if(Utils.useWhiteFont(color)) {
                    viewHolder.txtPlaylistTitle.setTextColor(Color.WHITE);
                    viewHolder.txtPlaylistArtist.setTextColor(Color.WHITE);
                } else {
                    viewHolder.txtPlaylistTitle.setTextColor(Color.BLACK);
                    viewHolder.txtPlaylistArtist.setTextColor(Color.BLACK);
                }
            }
        }
    }

    // Inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //public SelectableRoundedImageView imgThemePreview;
        public ImageView imgPlaylistArt;
        public TextView txtPlaylistTitle;
        public TextView txtPlaylistArtist;
        public View itemView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgPlaylistArt = (ImageView) itemLayoutView.findViewById(R.id.playlist_card_playlist_art);
            txtPlaylistTitle = (TextView) itemLayoutView.findViewById(R.id.playlist_card_playlist_title);
            txtPlaylistArtist = (TextView) itemLayoutView.findViewById(R.id.playlist_card_playlist_artist);
            itemView = (View) itemLayoutView.findViewById(R.id.playlist_card);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                Playlist playlist = playlistsData.get(getPosition());
                String transName = TRANSITION_NAME_ALBUM + String.valueOf(playlist.getId());

               // Create new fragment to add (Fragment B)
                Bundle args = new Bundle();
                args.putParcelable(PlaylistPageFragment.PLAYLIST_PARCLEABLE, playlist);
                args.putString(TRANSITION_NAME_ALBUM, transName);
                PlaylistPageFragment replaceFragment = new PlaylistPageFragment();
                replaceFragment.setArguments(args);

                setupTransition(transName, replaceFragment, imgPlaylistArt);

                // Add Fragment B
                FragmentTransaction ft = fragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .addSharedElement(imgPlaylistArt, imgPlaylistArt.getTransitionName())
                        .replace(R.id.content_frame, replaceFragment)
                        .addToBackStack(null);
                ft.commit();
        }

        private void setupTransition(String transName, Fragment fragment, ImageView imageView) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementEnterTransition(new FragTransition());
                fragment.setEnterTransition(new Fade());
                fragment.setExitTransition(new Fade());
                fragment.setSharedElementReturnTransition(new FragTransition());
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public class FragTransition extends TransitionSet {
            public FragTransition() {
                setOrdering(ORDERING_TOGETHER);
                addTransition(new ChangeBounds()).addTransition(new ChangeTransform()).addTransition(new ChangeImageTransform());
            }
        }

    }

 /*   public void sortSongs() {
        Collections.sort(songsData, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }*/

    // Return the size of your songsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return playlistsData.size();
    }

    public SongPlayerPanel getSongPanel() {
        return songPanel;
    }


    public List<Playlist> getPlaylistsData() {
        return playlistsData;
    }

    public void setPlaylistsData(List<Playlist> playlistData) {
        this.playlistsData = playlistData;
    }
}