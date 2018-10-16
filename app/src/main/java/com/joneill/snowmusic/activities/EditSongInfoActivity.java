package com.joneill.snowmusic.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.helper.BitmapHelper;
import com.joneill.snowmusic.helper.SystemBarTintHelper;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.themes.ThemeManager;
import com.joneill.snowmusic.views.ThemedToolbar;
import com.joneill.snowmusic.widgets.ThemedFragmentActivity;

/**
 * Created by josep on 7/27/2016.
 */
public class EditSongInfoActivity extends ThemedFragmentActivity implements View.OnClickListener {
    private final static int PHOTO_ID = 32;
    public static final String SONG_PARCLEABLE = "songParce";
    public static final String SONG_POSITION = "songPos";
    public static final String ADAPTER_KEY = "adapterKey";

    private Song song;
    private int position;
   // private AdaptersHelper.ADAPTER_TYPES adapterType;
    private ImageView ivAlbum;
    private EditText etTitle, etAlbum, etArtist, etGenre;
    private Button btnAlbumArt;
    private ImageButton btnRotateCW, btnRotateCCW;
    private FloatingActionButton fabSave;
    private Bitmap albumArt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_song_info);
        //ThemeManager.getInstance().configureStatusBar(this, new SystemBarTintHelper(this), 1f);
        // Transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        init();
    }

    private void init() {
        Bundle b = getIntent().getExtras();
        song = b.getParcelable(SONG_PARCLEABLE);
        position = getIntent().getIntExtra(SONG_POSITION, 0);
        //adapterType = (AdaptersHelper.ADAPTER_TYPES) getIntent().getSerializableExtra(ADAPTER_KEY);

        albumArt = BitmapHelper.getInstance().decodeBitmapFromUri(song.getAlbumUri(), song.getAlbumArt(), getContentResolver(), 128, 128);
        setupViews();
    }

    private void setupViews() {
        ThemedToolbar toolbar = (ThemedToolbar) findViewById(R.id.editSong_toolbar);
        toolbar.setTitle(getResources().getString(R.string.edit_song_info));
        btnAlbumArt = (Button) findViewById(R.id.btn_editAlbumArt);
        btnRotateCW = (ImageButton) findViewById(R.id.btnCWRotate);
        btnRotateCCW = (ImageButton) findViewById(R.id.btnCCWRotate);
        fabSave = (FloatingActionButton) findViewById(R.id.editSongFab);
        ivAlbum = (ImageView) findViewById(R.id.iv_editAlbumArt);
        etTitle = (EditText) findViewById(R.id.et_song_title);
        etArtist = (EditText) findViewById(R.id.et_song_artist);
        etAlbum = (EditText) findViewById(R.id.et_song_album);

        ivAlbum.setImageBitmap(albumArt);

        etTitle.setText(song.getTitle());
        etArtist.setText(song.getArtist());
        etAlbum.setText(song.getAlbum().getTitle());
        fabSave.setOnClickListener(this);
        btnAlbumArt.setOnClickListener(this);
        btnRotateCW.setOnClickListener(this);
        btnRotateCCW.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_ID && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            ivAlbum.setImageBitmap(bitmap);
            albumArt = bitmap;

            cursor.close();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.editSongFab:
                song.saveSongInfo(this, etTitle.getText().toString(), etArtist.getText().toString(), etAlbum.getText().toString());
                //song.changeAlbumArt(this, albumArt);
                song.testSaveAlbumBySong(this, albumArt);
                song.setTitle(etTitle.getText().toString());
                song.setArtist(etArtist.getText().toString());
                song.getAlbum().setTitle(etAlbum.getText().toString());
                // AdaptersHelper.getInstance().updateSongAtPos(song, position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(SONG_PARCLEABLE, song);
                returnIntent.putExtra(SONG_POSITION, position);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btn_editAlbumArt:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PHOTO_ID);
                break;
            case R.id.btnCWRotate:
                albumArt = Utils.rotateBitmap(albumArt, 90);
                ivAlbum.setImageBitmap(albumArt);
                break;
            case R.id.btnCCWRotate:
                albumArt = Utils.rotateBitmap(albumArt, -90);
                ivAlbum.setImageBitmap(albumArt);
                break;
        }
    }
}
