package com.joneill.snowmusic.helper;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by joseph on 1/9/15.
 */
public class BitmapHelper {
    private static final BitmapHelper instance = new BitmapHelper();

    //singleton
    private BitmapHelper() { }

    //This method takes a bitmap and turns it into a circle
    public Bitmap circlizeBitmap(Bitmap bitmap) {
        if(bitmap == null) { return null; };
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);

        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        //In case you wonder why we don't recycle the bitmap passed in,
        //it's because we need to use the original album art later(at least, that's what I guess is the reason :/)

        return circleBitmap;
    }

    public Bitmap colorBitmap(Bitmap bitmap, int color, boolean circlize) {
        Bitmap coloredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        coloredBitmap.eraseColor(color);

        if(circlize) {
            coloredBitmap = circlizeBitmap(coloredBitmap);
        }

        return coloredBitmap;
    }

    public Bitmap decodeBitmapFromUri(Uri uri, Bitmap defaultBitmap, ContentResolver res, int width, int height) {
        Bitmap albumBitmap = null;
        InputStream in = null;
        try {
            in = res.openInputStream(uri);
            albumBitmap = BitmapHelper.getInstance().decodeSampledBitmapFromInputStream(in, res, uri, width, height);
        } catch (FileNotFoundException e) {
            albumBitmap = defaultBitmap;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return albumBitmap;
    }

    public Bitmap decodeSampledBitmapFromInputStream(InputStream is, ContentResolver res, Uri uri,
                                                     int reqWidth, int reqHeight) throws IOException {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, new Rect(0,0,0,0), options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        is.close();
        is = res.openInputStream(uri);
        return BitmapFactory.decodeStream(is, new Rect(0,0,0,0), options);
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                     int reqWidth, int reqHeight) throws IOException {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static BitmapHelper getInstance() {
        return instance;
    }
}
