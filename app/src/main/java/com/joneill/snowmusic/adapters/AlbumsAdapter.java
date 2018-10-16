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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.fragments.AlbumPageFragment;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.SongPlayerPanel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by joseph on 3/1/15.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {
    private Context context;
    private List<Album> albumsData;
    private SongPlayerPanel songPanel;
    private Fragment fragment;
    private View layout;
    public static final String TRANSITION_NAME_ALBUM = "trans_album_card_";
    private HashMap<Integer, String> transitionHashMap;

    public AlbumsAdapter(Context context, SongPlayerPanel songPanel, List<Album> albumsData, Fragment fragment, View layout) {
        this.context = context;
        this.songPanel = songPanel;
        this.albumsData = albumsData;
        this.fragment = fragment;
        this.layout = layout;
        transitionHashMap = new HashMap<Integer, String>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final Album album = albumsData.get(position);
        String transName = "trans_album_card_" + album.getAlbumId();
        album.setPositionInAlbumList(position);
        viewHolder.imgAlbumArt.setImageBitmap(
                albumsData.get(position).getAlbumArt());
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animation.setDuration(1000);
        viewHolder.imgAlbumArt.setAnimation(animation);
        ViewCompat.setTransitionName(viewHolder.imgAlbumArt, transName);
        viewHolder.txtAlbumTitle.setText(albumsData.get(position).getTitle());
        viewHolder.txtAlbumArtist.setText(albumsData.get(position).getArtist());

        if (Settings.getInstance().getSettingsData().get(Settings.BOOL_ALBUM_CARD_USE_PALETTE) != null) {
            if ((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_ALBUM_CARD_USE_PALETTE)) {
                if (album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN) != null) {
                    viewHolder.itemView.setBackgroundColor(album.getPaletteColors().get(Album.PALETTE_COLOR_MAIN));
                    viewHolder.txtAlbumTitle.setTextColor(album.getPaletteColors().get(Album.PALETTE_COLOR_TITLE_TEXT));
                    if(album.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT) == null) { return; }
                    viewHolder.txtAlbumArtist.setTextColor(album.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT));
                } else {
                    viewHolder.itemView.setBackgroundColor(Color.WHITE);
                    viewHolder.txtAlbumTitle.setTextColor(Color.BLACK);
                    viewHolder.txtAlbumArtist.setTextColor(Color.BLACK);
                }
            } else {
                int color = (int) Settings.getInstance().getSettingsData()
                        .get(Settings.INT_ALBUM_CARD_COLOR);
                viewHolder.itemView.setBackgroundColor(color);
                if(Utils.useWhiteFont(color)) {
                    viewHolder.txtAlbumTitle.setTextColor(Color.WHITE);
                    viewHolder.txtAlbumArtist.setTextColor(Color.WHITE);
                } else {
                    viewHolder.txtAlbumTitle.setTextColor(Color.BLACK);
                    viewHolder.txtAlbumArtist.setTextColor(Color.BLACK);
                }
            }
        }
    }

    // Inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //public SelectableRoundedImageView imgThemePreview;
        public ImageView imgAlbumArt;
        public TextView txtAlbumTitle;
        public TextView txtAlbumArtist;
        public View itemView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgAlbumArt = (ImageView) itemLayoutView.findViewById(R.id.album_card_album_art);
            txtAlbumTitle = (TextView) itemLayoutView.findViewById(R.id.album_card_album_title);
            txtAlbumArtist = (TextView) itemLayoutView.findViewById(R.id.album_card_album_artist);
            itemView = (View) itemLayoutView.findViewById(R.id.album_card);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                Album album = albumsData.get(getPosition());
                String transName = TRANSITION_NAME_ALBUM + String.valueOf(album.getAlbumId());

                // Create new fragment to add (Fragment B)
                Bundle args = new Bundle();
                args.putParcelable(AlbumPageFragment.ALBUM_PARCLEABLE, album);
                args.putString(TRANSITION_NAME_ALBUM, transName);
                AlbumPageFragment replaceFragment = new AlbumPageFragment();
                replaceFragment.setArguments(args);

                setupTransition(transName, replaceFragment, imgAlbumArt);

                // Add Fragment B
                FragmentTransaction ft = fragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .addSharedElement(imgAlbumArt, imgAlbumArt.getTransitionName())
                        .replace(R.id.content_frame, replaceFragment)
                        .addToBackStack("this");
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
        if(albumsData == null) { return 0; }
        return albumsData.size();
    }

    public SongPlayerPanel getSongPanel() {
        return songPanel;
    }
}