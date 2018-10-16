package com.joneill.snowmusic.fragments.Settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.helper.Settings;
import com.joneill.snowmusic.helper.Utils;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.dialogs.DownloadAlbumPopup;
import com.joneill.snowmusic.widgets.ThemedFragment;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

/**
 * Created by josep_000 on 7/26/2015.
 */
public class InterfaceSettingsFragment extends ThemedFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_settings_interface, container, false);

        init();

        View item;
        ImageView itemColorPreview;

        rootView.findViewById(R.id.si_background_colors_item).setOnClickListener(this);
        rootView.findViewById(R.id.si_spp_colors_item).setOnClickListener(this);
        rootView.findViewById(R.id.si_lastfm_albumart).setOnClickListener(this);

        return rootView;
    }

    private void init() {

    }

    private void loadItem(View item, ImageView itemColorPreview, String keyColor, String keyUsePalette, Boolean applyToAlbums) {
        item.setOnClickListener(this);
        int color;
        Object loadedColor = Settings.getInstance().getSettingsData().get(keyColor +
                (applyToAlbums ? String.valueOf(SongDataHolder.getInstance().getAlbumsData().get(0).getAlbumId()) : ""));
        if (loadedColor != null) {
            color = (int) loadedColor;
        } else {
            color = Color.WHITE;
        }
        boolean usePalette = true;
        Object paletteObj = Settings.getInstance().getSettingsData().get(keyUsePalette);
        if (paletteObj != null) {
            usePalette = (Boolean) paletteObj;
        } else {
            itemColorPreview.setColorFilter(Color.WHITE);
        }
        if (usePalette) {
            itemColorPreview.setColorFilter(Color.WHITE);
        } else {
            itemColorPreview.setColorFilter(color);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.si_background_colors_item:
                ft.replace(R.id.settings_fragment_container, new BgColorsSettingsFragment());
                ft.commit();
                break;
            case R.id.si_spp_colors_item:
                ft.replace(R.id.settings_fragment_container, new SPanelSettingsFragment());
                ft.commit();
                break;
            case R.id.si_lastfm_albumart:
                DownloadAlbumPopup popup = new DownloadAlbumPopup(getActivity());
                popup.showAtLocation(v, Gravity.CENTER, 0, 0);
                popup.setFocusable(true);
                popup.update();
                break;
        }
    }

    //Method to create a dialog that eventually saves the color settings of any interface settings
    //If applyToAllAlbums is true, set album to null.
    private void openColorTypeDialog(View v, String title, final boolean applyToAllAlbums, final Album album, final String mainCustomKey,
                                     final String mainPaletteKey, final ImageView colorPreview) {
        final Dialog dialog = new Dialog(getActivity());
        final View view = v;
        dialog.setContentView(R.layout.dialog_color_type);
        dialog.setTitle(title);

        RadioButton rbPalette = (RadioButton) dialog.findViewById(R.id.rbDialogCTPalette);
        rbPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (applyToAllAlbums) {
                    for (Album album : SongDataHolder.getInstance().getAlbumsData()) {
                        Settings.getInstance().getSettingsData().put(mainPaletteKey
                                + String.valueOf(album.getAlbumId()), true);
                    }
                }
                Settings.getInstance().getSettingsData().put(mainPaletteKey, true);
                Settings.getInstance().saveSettings();
                dialog.dismiss();
                colorPreview.setColorFilter(Color.WHITE);
            }
        });

        RadioButton rbCustom = (RadioButton) dialog.findViewById(R.id.rbDialogCTCustom);
        rbCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog colorDialog = Utils.createColorChooserDialog(getActivity());

                final ColorPicker colorPicker = (ColorPicker) colorDialog.findViewById(R.id.colorPicker);

                SVBar svBar = (SVBar) colorDialog.findViewById(R.id.svBar);
                colorPicker.addSVBar(svBar);

                int color = 0;
                Object loadedColor = Settings.getInstance().getSettingsData()
                        .get(mainCustomKey);

                if (loadedColor != null) {
                    color = (int) loadedColor;
                } else {
                    //color = Color.WHITE;
                }

                colorPicker.setColor(color);

                colorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (applyToAllAlbums) {
                            for (Album album : SongDataHolder.getInstance().getAlbumsData()) {
                                Settings.getInstance().getSettingsData().put(mainCustomKey
                                        + String.valueOf(album.getAlbumId()), colorPicker.getColor());
                                Settings.getInstance().getSettingsData().put(mainPaletteKey
                                        + String.valueOf(album.getAlbumId()), false);
                            }
                        }
                        Settings.getInstance().getSettingsData().put(mainCustomKey, colorPicker.getColor());
                        Settings.getInstance().getSettingsData().put(mainPaletteKey, false);
                        Settings.getInstance().saveSettings();
                        colorPreview.setColorFilter(colorPicker.getColor());
                    }
                });

                colorDialog.show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
