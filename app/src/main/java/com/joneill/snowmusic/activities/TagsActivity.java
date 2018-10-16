package com.joneill.snowmusic.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.adapters.TagsAdapter;
import com.joneill.snowmusic.data.SongTagsDatabase;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.themes.ThemeManager;
import com.joneill.snowmusic.views.ThemedToolbar;
import com.joneill.snowmusic.widgets.ThemedFragmentActivity;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by josep on 1/2/2017.
 */
public class TagsActivity extends ThemedFragmentActivity implements View.OnClickListener {
    public static final String SONG_PARCLEABLE = "songParce";

    private Song song;
    private ThemedToolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private SongTagsDatabase songTagsDatabase;
    private TagsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        //ThemeManager.getInstance().configureStatusBar(this, new SystemBarTintHelper(this), 1f);
        // Transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        init();
    }

    private void init() {
        songTagsDatabase = new SongTagsDatabase(this);
        Bundle b = getIntent().getExtras();
        song = b.getParcelable(SONG_PARCLEABLE);

        setupUI();
        recyclerView = (RecyclerView) findViewById(R.id.tags_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add_tag);
        fabAdd.setOnClickListener(this);

        List<String> tags = songTagsDatabase.getTagsBySong(String.valueOf(song.getId()));
        Collections.sort(tags);

        mAdapter = new TagsAdapter(tags, this, (int) song.getId());
        recyclerView.setAdapter(mAdapter);
    }

    private void setupUI() {
        toolbar = (ThemedToolbar) findViewById(R.id.tags_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_material));
        toolbar.setTitle("Tagging");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CircleImageView albumIv = (CircleImageView) findViewById(R.id.act_tag_album_art);
        albumIv.setImageBitmap(song.getAlbumArt());
        TextView songTitle = (TextView) findViewById(R.id.act_tag_song_title);
        songTitle.setText(song.getTitle());
        TextView txtArtist = (TextView) findViewById(R.id.act_tag_song_artist);
        txtArtist.setText(song.getArtist());

        colorizeBg();
    }

    private void colorizeBg() {
        View bg = findViewById(R.id.tagging_bg);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.tags_collapsing_toolbar);
        int color = 0;

        Bitmap albumArt = null;
        if (song.isUseSingleArt() || song.getAlbum().getAlbumArt() == null) {
            albumArt = song.getAlbumArt();
        } else {
            albumArt = song.getAlbum().getAlbumArt();
        }

        generateAndSetBgColor(albumArt, bg, collapsingToolbarLayout);
    }

    private void generateAndSetBgColor(Bitmap artwork, final View bg,
                                       final CollapsingToolbarLayout collapsingToolbar) {
        Palette.from(artwork).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                int color = palette.getVibrantColor(0xff000000 +
                        Integer.parseInt(String.valueOf(ThemeManager.getInstance().getThemeData().get(ThemeManager.PRIMARY_THEME_COLOR)), 16));
                bg.setBackgroundColor(color);
                collapsingToolbar.setContentScrimColor(color);
                collapsingToolbar.setStatusBarScrimColor(color);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add_tag:
                AlertDialog dialog = buildTagDialog();
                dialog.show();
                break;
        }
    }

    private AlertDialog buildTagDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.enter_tag_name));

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_tagging, null);
        final EditText input = (EditText) dialogLayout.findViewById(R.id.et_tag);
        alertDialog.setView(dialogLayout);

        alertDialog.setPositiveButton(getResources().getString(R.string.add),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String tag = input.getText().toString();
                        addTagToList(tag);
                        songTagsDatabase.populateTagsTable((int) song.getId(), tag);
                    }
                });

        alertDialog.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return alertDialog.create();
    }

    private void addTagToList(String tag) {
        mAdapter.add(tag);
    }
}
