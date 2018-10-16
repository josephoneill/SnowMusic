package com.joneill.snowmusic.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.joneill.snowmusic.song.SongPlayerPanel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by josep_000 on 2/14/2016.
 */
public class SlidingPanelLayout extends LinearLayout implements GestureDetector.OnGestureListener {
    private Context context;
    private SongPlayerPanel songPanel;
    private GestureDetector gestureDetector;
    private boolean shouldExpandOC, shouldCloseOC;

    public SlidingPanelLayout(Context context) {
        super(context);
    }

    public SlidingPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingPanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SlidingPanelLayout(Context context, SongPlayerPanel songPanel) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        if(event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            songPanel.setFirstScrollEvent(true);
            songPanel.snapScrollItem();
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //songPanel.startSong(0);
        return super.dispatchTouchEvent(event);
    }

    public void setSongPanel(SongPlayerPanel songPanel) {
        this.songPanel = songPanel;
    }

    public void startGestureDetector() {
        gestureDetector = new GestureDetector(this);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(songPanel.getmPanelLayout().getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED && shouldExpandOC) {
            songPanel.getmPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        } else if (songPanel.getmPanelLayout().getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED && shouldCloseOC){
            songPanel.getmPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            songPanel.onScroll(e1, e2, distanceX, distanceY);
            return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public boolean isShouldExpandOC() {
        return shouldExpandOC;
    }

    public void setShouldExpandOC(boolean shouldExpandOC) {
        this.shouldExpandOC = shouldExpandOC;
    }

    public boolean isShouldCloseOC() {
        return shouldCloseOC;
    }

    public void setShouldCloseOC(boolean shouldCloseOC) {
        this.shouldCloseOC = shouldCloseOC;
    }
}
