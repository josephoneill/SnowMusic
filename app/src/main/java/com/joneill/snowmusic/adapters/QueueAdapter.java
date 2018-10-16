package com.joneill.snowmusic.adapters;

import android.app.Activity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.interfaces.ItemTouchHelperAdapter;
import com.joneill.snowmusic.interfaces.OnStartDragListener;
import com.joneill.snowmusic.song.Queue;
import com.joneill.snowmusic.song.Song;
import com.joneill.snowmusic.song.SongPlayerPanel;

import java.util.Collections;
import java.util.List;

/**
 * Created by josep_000 on 8/21/2014.
 */
public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private SongPlayerPanel songPanel;
    private Activity mActivity;
    private List<Song> songsData;
    private final OnStartDragListener mDragStartListener;

    public QueueAdapter(SongPlayerPanel songPanel, Activity mActivity, OnStartDragListener dragStartListener) {
        this.songPanel = songPanel;
        this.mActivity = mActivity;
        songsData = songPanel.getMusicService().getQueue().getQueueList();
        mDragStartListener = dragStartListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.queue_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        List<Song> songsData = songPanel.getMusicService().getQueue().getQueueList();
        //Get items from xml and replace it with song data

        //Convert our album bitmaps into circles. We create a seperate object for this
        //because the circlizeBitmap method recycles the bitmap
        viewHolder.txtViewTitle.setText(songsData.get(position).getTitle());
        viewHolder.txtViewArtist.setText(songsData.get(position).getArtist());
        viewHolder.txtViewDuration.setText(songsData.get(position).getDurationString());
        viewHolder.handleView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });
    }

    // Inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView handleView;
        public TextView txtViewTitle;
        public TextView txtViewArtist;
        public TextView txtViewDuration;
        public ImageButton menuImageButton;
        public View songItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            handleView = (ImageView) itemLayoutView.findViewById(R.id.item_holder);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            txtViewArtist = (TextView) itemLayoutView.findViewById(R.id.item_artist);
            txtViewDuration = (TextView) itemLayoutView.findViewById(R.id.item_duration);
            menuImageButton = (ImageButton) itemLayoutView.findViewById(R.id.song_frag_overflow_menu);
            songItem = (View) itemLayoutView.findViewById(R.id.song_item);
            songItem.setOnClickListener(this);
            menuImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.song_frag_overflow_menu) {
                PopupMenu popupMenu = new PopupMenu(mActivity, v);
                popupMenu.getMenuInflater().inflate(R.menu.song_item_popup_menu, popupMenu.getMenu());
                popupMenu.show();
            } else {
                if (songPanel.getMusicService().getList() != songsData) {
                    songPanel.getMusicService().setList(songsData);
                }
                songPanel.openSongInPanel(true, songsData.get(getPosition()), false);
                songPanel.startSong(getPosition());
            }
        }
    }

    @Override
    public void onItemDismiss(int position) {
        //Call the original queue because java is pass-by-value and not reference
        //songPanel.getMusicService().getQueue().getQueueList().remove(position);
        songsData.remove(position);
        Queue queue = songPanel.getMusicService().getQueue();
        queue.setQueueList(songsData);
        int currSongIndex = queue.getCurrSongIndex();
        if(position < currSongIndex) {
            queue.setCurrSongIndex(currSongIndex - 1);
        }
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        boolean increased = false;
        Queue queue = songPanel.getMusicService().getQueue();
        int currSongIndex = queue.getCurrSongIndex();
        if (fromPosition < toPosition) {
            //for(int i = fromPosition; i < toPosition; i++) {
                Collections.swap(songPanel.getMusicService().getQueue().getQueueList(), fromPosition, fromPosition + 1);
                if(fromPosition + 1 == currSongIndex) {
                    queue.setCurrSongIndex(fromPosition);
                }
            //}
            increased = true;
        } else {
            //for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(songPanel.getMusicService().getQueue().getQueueList(), fromPosition, fromPosition - 1);
                if(fromPosition - 1 == currSongIndex) {
                    queue.setCurrSongIndex(fromPosition);
                }
            //}
            increased = false;
        }

        if(currSongIndex == fromPosition) {
            queue.setCurrSongIndex(toPosition);
        }

        Log.e("Pos", String.valueOf(queue.getCurrSongIndex()));


        //adjustQueue(increased, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    private void adjustQueue(boolean increased, int from, int to) {
        Queue queue = songPanel.getMusicService().getQueue();
        int currSongIndex = queue.getCurrSongIndex();
        if(from == currSongIndex) {
            queue.setCurrSongIndex(to);
        } else if (increased) {
            queue.setCurrSongIndex(currSongIndex--);
        } else {
            queue.setCurrSongIndex(currSongIndex++);
        }
        Log.e("Pos", String.valueOf(queue.getCurrSongIndex()));
    }


    // Return the size of your songsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return songPanel.getMusicService().getQueue().getQueueList().size();
    }

    public SongPlayerPanel getSongPanel() {
        return songPanel;
    }
}