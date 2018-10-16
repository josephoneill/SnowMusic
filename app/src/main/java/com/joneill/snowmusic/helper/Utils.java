package com.joneill.snowmusic.helper;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.interfaces.OnColorSelected;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by joseph on 2/21/15.
 */
public class Utils {
    public static final int POPUP_WIDTH_DP = 400;
    public static final int POPUP_HEIGHT_DP = 250;

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public static void sortSongs(List<Song> songsData) {
        if (songsData == null) {
            return;
        }
        Collections.sort(songsData, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

    public static Dialog createColorChooserDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.color_picker_dialog);

        Button buttonDone = (Button) dialog.findViewById(R.id.btnColorDialogDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    public static float convertDpToPixels(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static int darkenColor(int color, float alpha) {
        if (color == 0) {
            Log.e("Darken Color Error", "Error: Color is null");
            return Color.WHITE;
        }
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= alpha;
        return Color.HSVToColor(hsv);
    }

    public static boolean useWhiteFont(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if (red * 0.299 + green * 0.587 + blue * 0.114 > 186) {
            return false;
        } else {
            return true;
        }
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static void loadColorItem(View.OnClickListener onClickListener, View item, ImageView itemColorPreview, String keyColor, String keyUsePalette, Boolean applyToAlbums) {
        item.setOnClickListener(onClickListener);
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

    public static void loadColorItem(View.OnClickListener onClickListener, View item, ImageView itemColorPreview, String keyColor) {
        item.setOnClickListener(onClickListener);
        int color;
        Object loadedColor = Settings.getInstance().getSettingsData().get(keyColor);
        if (loadedColor != null) {
            color = (int) loadedColor;
        } else {
            color = Color.WHITE;
        }
        itemColorPreview.setColorFilter(color);
    }

    //applyTo: View to set bg color
    public static void loadColorItem(View.OnClickListener onClickListener, View item, View applyTo, ImageView itemColorPreview, String keyColor) {
        item.setOnClickListener(onClickListener);
        int color;
        Object loadedColor = Settings.getInstance().getSettingsData().get(keyColor);
        if (loadedColor != null) {
            color = (int) loadedColor;
        } else {
            color = Color.WHITE;
        }
        itemColorPreview.setColorFilter(color);
        applyTo.setBackgroundColor(color);
    }

    //ImageButton: ImageButton to apply
    public static void loadColorItem(View.OnClickListener onClickListener, View item, ImageButton imageButton,
                                     ImageView itemColorPreview, String keyColor) {
        item.setOnClickListener(onClickListener);
        int color;
        Object loadedColor = Settings.getInstance().getSettingsData().get(keyColor);
        if (loadedColor != null) {
            color = (int) loadedColor;
        } else {
            color = Color.WHITE;
        }
        itemColorPreview.setColorFilter(color);
        imageButton.setColorFilter(color);
    }

    public static void openColorTypeDialog(final Activity mActivity, View v, String title, final String mainCustomKey,
                                           final String mainPaletteKey, final ImageView colorPreview, final String settingCallbackKey,
                                           final OnColorSelected colorCallback) {
        final Dialog dialog = new Dialog(mActivity);
        final View view = v;
        dialog.setContentView(R.layout.dialog_color_type);
        dialog.setTitle(title);

        RadioButton rbPalette = (RadioButton) dialog.findViewById(R.id.rbDialogCTPalette);
        rbPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Dialog colorDialog = Utils.createColorChooserDialog(mActivity);

                final Button doneButton = (Button) colorDialog.findViewById(R.id.btnColorDialogDone);
                final ImageButton setHexButton = (ImageButton) colorDialog.findViewById(R.id.btnSetHex);
                final EditText hexET = (EditText) colorDialog.findViewById(R.id.et_ColorHex);
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

                setHexButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int color = Color.parseColor(hexET.getText().toString());
                        colorPicker.setColor(color);
                        doneButton.setBackgroundColor(color);
                    }
                });

                colorPicker.setColor(color);
                doneButton.setBackgroundColor(color);

                colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        doneButton.setBackgroundColor(color);
                        String hexColor = String.format("#%06X", (0xFFFFFF & color));
                        hexET.setText(hexColor);
                    }
                });

                colorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Settings.getInstance().getSettingsData().put(mainCustomKey, colorPicker.getColor());
                        Settings.getInstance().getSettingsData().put(mainPaletteKey, false);
                        Settings.getInstance().saveSettings();
                        colorPreview.setColorFilter(colorPicker.getColor());
                        if (!settingCallbackKey.equals("")) {
                            Settings.getInstance().getCallbacks().get(settingCallbackKey).onSettingsChanged(mainCustomKey);
                        }
                        if(colorCallback != null) {
                            colorCallback.onColorSelected(mainPaletteKey, colorPicker.getColor());
                        }
                    }
                });

                colorDialog.show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void openColorTypeDialog(final Activity mActivity, View v, String title, final String mainCustomKey,
                                           final ImageView colorPreview, final String settingCallbackKey, final OnColorSelected colorCallback) {

        Dialog colorDialog = Utils.createColorChooserDialog(mActivity);

        final Button doneButton = (Button) colorDialog.findViewById(R.id.btnColorDialogDone);
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
        doneButton.setBackgroundColor(color);

        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                doneButton.setBackgroundColor(color);
            }
        });

        colorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Settings.getInstance().getSettingsData().put(mainCustomKey, colorPicker.getColor());
                Settings.getInstance().saveSettings();
                colorPreview.setColorFilter(colorPicker.getColor());
                if (!settingCallbackKey.equals("")) {
                    Settings.getInstance().getCallbacks().get(settingCallbackKey).onSettingsChanged(mainCustomKey);
                }
                if(colorCallback != null) {
                    colorCallback.onColorSelected(mainCustomKey, colorPicker.getColor());
                }
            }
        });

        colorDialog.show();
    }

    public static int interpolateColor(int color1, int color2, float progress) {
        int color = (Integer) new ArgbEvaluator().evaluate(progress, color1, color2);
        return color;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap colorizePlayButton(Bitmap bitmap, int color) {
        Bitmap coloredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int[] pixels = new int[coloredBitmap.getHeight()*coloredBitmap.getWidth()];
        int width = coloredBitmap.getWidth();
        int height = coloredBitmap.getHeight();
        int totalSize = width * height;
        coloredBitmap.getPixels(pixels, 0, coloredBitmap.getWidth(), 0, 0, coloredBitmap.getWidth(), coloredBitmap.getHeight());
        //Top
        for(int i = 0; i < width * (height / 4); i++) {
            if (pixels[i] != Color.TRANSPARENT) {
                pixels[i] = color;
            }
        }
        //Left
        for(int i = 0; i < height; i+=width) {
            for(int j = i; j < (i + (height / 4)); j++) {
                if (pixels[i] == Color.WHITE) {
                    pixels[i] = color;
                }
            }
        }
        //Bottom
        for(int i = totalSize - width; i > totalSize - (width * (height / 4)); i--) {
            if (pixels[i] == Color.WHITE) {
                pixels[i] = color;
            }
        }
        //Right
        for(int i = width; i < height; i+=width) {
            for(int j = i; j > (i - (height / 4)); j--) {
                if (pixels[i] == Color.WHITE) {
                    pixels[i] = color;
                }
            }
        }

        coloredBitmap.setPixels(pixels, 0, coloredBitmap.getWidth(), 0, 0, coloredBitmap.getWidth(), coloredBitmap.getHeight());
        return coloredBitmap;
    }

    public static String millisToTime(long millis) {
        String time = "";
        long second = (millis / 1000) % 60;
        int minute = ((int) millis / (1000 * 60)) % 60;
        int hour = (int) TimeUnit.MILLISECONDS.toHours(millis);
        if(hour > 0) {
            if(hour < 10) {
                time = String.format("%d:%02d:%02d", hour, minute, second);
            } else {
                time = String.format("%02d:%02d:%02d", hour, minute, second);
            }
        } else {
            if(minute < 10) {
                time = String.format("%d:%02d", minute, second);
            } else {
                time = String.format("%02d:%02d", minute, second);
            }
        }
        return time;
    }

    public static String cursorGetStringNullCheck(Cursor musicCursor, int i) {
        String value = "";
        if(!musicCursor.isNull(i)) {
            value = musicCursor.getString(i);
            Log.e("value", "joneill value is " + value);
        }
        return value;
    }

    public static long cursorGetLongNullCheck(Cursor musicCursor, int i) {
        long value = 0;
        if(!musicCursor.isNull(i)) {
            value = musicCursor.getLong(i);
        }
        return value;
    }
}
