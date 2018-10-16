package com.joneill.snowmusic.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by joseph on 1/9/15.
 */

//Make a custom view so we can properly scale the album art
//Credit: Bob Lee and Patrick Boss from stackoverflow
public class AlbumArtImageView extends ImageView {

    public AlbumArtImageView(Context context) {
        super(context);
    }

    public AlbumArtImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumArtImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AlbumArtImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * getDrawable().getIntrinsicHeight() /
                getDrawable().getIntrinsicWidth();
        setMeasuredDimension(width, height);
    }
}
