package com.joneill.snowmusic.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by josep_000 on 2/13/2016.
 */
public class MusicSlidingUpPanelLayout extends SlidingUpPanelLayout {
    public MusicSlidingUpPanelLayout(Context context) {
        super(context);
    }

    public MusicSlidingUpPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MusicSlidingUpPanelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
