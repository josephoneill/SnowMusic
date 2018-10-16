package com.joneill.snowmusic.interfaces;

/**
 * Created by josep_000 on 2/12/2016.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}