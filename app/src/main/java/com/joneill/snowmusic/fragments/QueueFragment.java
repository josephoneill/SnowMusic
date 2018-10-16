package com.joneill.snowmusic.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.adapters.ItemTouchCallback;
import com.joneill.snowmusic.adapters.QueueAdapter;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.interfaces.OnStartDragListener;
import com.joneill.snowmusic.service.MusicService;
import com.joneill.snowmusic.song.Queue;
import com.joneill.snowmusic.song.SongPlayerPanel;
import com.joneill.snowmusic.views.ThemedToolbar;
import com.joneill.snowmusic.widgets.ThemedFragment;

/**
 * Created by joseph on 1/29/15.
 */
public class QueueFragment extends ThemedFragment implements OnStartDragListener {
    private RecyclerView recyclerView;
    private Queue queue;
    private Bitmap defaultBitmap;
    private QueueAdapter mAdapter;
    private ItemTouchHelper.Callback callback;
    private ItemTouchHelper touchHelper;
    private MusicService musicService;
    private Intent playIntent;
    private SongPlayerPanel songPanel;
    private ThemedToolbar toolbar;
    private boolean musicBound = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_queue_page, container, false);

        instantiate(rootView);


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(mAdapter);
        //Utils.sortSongs(queue.getQueueList());

        return rootView;
    }

    private void instantiate(View rootView) {

        songPanel = SongDataHolder.getInstance().getSongPanel();
        queue = songPanel.getMusicService().getQueue();

        toolbar = (ThemedToolbar) rootView.findViewById(R.id.queue_page_toolbar);
        toolbar.setTitle("Queue");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_material));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mAdapter = new QueueAdapter(songPanel, getActivity(), this);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvQueue);

        callback = new ItemTouchCallback(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.getSongPanel().onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.getSongPanel().onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }
}


