package com.joneill.snowmusic.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.joneill.snowmusic.fragments.ArtistPageFragment;
import com.joneill.snowmusic.fragments.GenrePageFragment;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Artist;
import com.joneill.snowmusic.song.Genre;
import com.joneill.snowmusic.song.SongPlayerPanel;

import java.util.List;

/**
 * Created by joseph on 3/1/15.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private Context context;
    private List<Artist> artistsData;
    private SongPlayerPanel songPanel;
    private Fragment fragment;
    private View layout;

    public ArtistAdapter(Context context, SongPlayerPanel songPanel, List<Artist> artistsData, Fragment fragment, View layout) {
        this.context = context;
        this.songPanel = songPanel;
        this.artistsData = artistsData;
        this.fragment = fragment;
        this.layout = layout;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        if(artistsData.get(position).getSongs().size() == 0) { return; }
        final Artist artist = artistsData.get(position);
        artist.setPositionInArtistList(position);
        viewHolder.imgGenreArt.setImageBitmap(artist.getSongs().get(0).getAlbumArt());
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animation.setDuration(1000);
        viewHolder.imgGenreArt.setAnimation(animation);
        viewHolder.txtGenreTitle.setText(artistsData.get(position).getArtist());
        Album initialAlbum = artist.getSongs().get(0).getAlbum();

        if (Settings.getInstance().getSettingsData().get(Settings.BOOL_ARTIST_CARD_USE_PALETTE) != null) {
            if ((boolean) Settings.getInstance().getSettingsData().get(Settings.BOOL_ARTIST_CARD_USE_PALETTE)) {
                if (initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_MAIN) != null) {
                    viewHolder.itemView.setBackgroundColor(initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_MAIN));
                    viewHolder.txtGenreTitle.setTextColor(initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_TITLE_TEXT));
                    if(initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT) == null) { return; }
                    viewHolder.txtGenreArtist.setTextColor(initialAlbum.getPaletteColors().get(Album.PALETTE_COLOR_BODY_TEXT));
                } else {
                    viewHolder.itemView.setBackgroundColor(Color.WHITE);
                    viewHolder.txtGenreTitle.setTextColor(Color.BLACK);
                    viewHolder.txtGenreArtist.setTextColor(Color.BLACK);
                }
            } else {
                int color = (int) Settings.getInstance().getSettingsData()
                        .get(Settings.INT_ARTIST_CARD_COLOR);
                viewHolder.itemView.setBackgroundColor(color);
                if(Utils.useWhiteFont(color)) {
                    viewHolder.txtGenreTitle.setTextColor(Color.WHITE);
                    viewHolder.txtGenreArtist.setTextColor(Color.WHITE);
                } else {
                    viewHolder.txtGenreTitle.setTextColor(Color.BLACK);
                    viewHolder.txtGenreArtist.setTextColor(Color.BLACK);
                }
            }
        }
    }

    // Inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //public SelectableRoundedImageView imgThemePreview;
        public ImageView imgGenreArt;
        public TextView txtGenreTitle;
        public TextView txtGenreArtist;
        public View itemView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgGenreArt = (ImageView) itemLayoutView.findViewById(R.id.genre_card_genre_art);
            txtGenreTitle = (TextView) itemLayoutView.findViewById(R.id.genre_card_genre_title);
            txtGenreArtist = (TextView) itemLayoutView.findViewById(R.id.genre_card_genre_artist);
            itemView = (View) itemLayoutView.findViewById(R.id.genre_card);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                Artist artist = artistsData.get(getPosition());

               // Create new fragment to add (Fragment B)
                Bundle args = new Bundle();
                args.putParcelable(ArtistPageFragment.ARTIST_PARCLEABLE, artist);
                ArtistPageFragment replaceFragment = new ArtistPageFragment();
                replaceFragment.setArguments(args);

                // Add Fragment B
                FragmentTransaction ft = fragment.getActivity().getSupportFragmentManager().beginTransaction()
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
        return artistsData.size();
    }

    public SongPlayerPanel getSongPanel() {
        return songPanel;
    }


    public List<Artist> getArtistsData() {
        return artistsData;
    }

    public void setArtistsData(List<Artist> artistsData) {
        this.artistsData = artistsData;
    }
}