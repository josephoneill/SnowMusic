package com.joneill.snowmusic.themes;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by joseph on 1/11/15.
 */
public class Theme {
    private String title;
    private Bitmap previewBitmap;
    private int id;

    public Theme(String title, Bitmap previewBitmap, int id) {
        this.title = title;
        this.previewBitmap = previewBitmap;
        this.id = id;
    }

    public Bitmap getPreviewBitmap() {
        return previewBitmap;
    }

    public void setPreviewBitmap(Bitmap previewBitmap) {
        this.previewBitmap = previewBitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
