package com.joneill.snowmusic.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.data.SongTagsDatabase;
import com.joneill.snowmusic.themes.Theme;
import com.joneill.snowmusic.themes.ThemeManager;

import java.util.Collections;
import java.util.List;


/**
 * Created by josep_000 on 8/21/2014.
 */
public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private List<String> tags;
    private Context mContext;
    private int songId;

    private SongTagsDatabase songTagsDatabase;

    public TagsAdapter(List<String> tags, Context mContext, int songId) {
        this.tags = tags;
        this.mContext = mContext;
        this.songId = songId;

        songTagsDatabase = new SongTagsDatabase(mContext);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tvNum.setText(String.valueOf(position));
        viewHolder.tvTag.setText(tags.get(position));
    }

    // Inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvTag;
        public TextView tvNum;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvTag = (TextView) itemLayoutView.findViewById(R.id.tag_title);
            tvNum = (TextView) itemLayoutView.findViewById(R.id.tag_list_item_number);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //ThemeManager.getInstance().applyTheme(themes.get(getPosition()));
            final String currentTag = tags.get(getAdapterPosition());
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle(mContext.getResources().getString(R.string.edit_tag_name));

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View dialogLayout = inflater.inflate(R.layout.dialog_tagging, null);
            final EditText input = (EditText) dialogLayout.findViewById(R.id.et_tag);
            alertDialog.setView(dialogLayout);

            alertDialog.setPositiveButton(mContext.getResources().getString(R.string.add),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String newTag = input.getText().toString();
                            tags.remove(currentTag);
                            add(newTag);
                            songTagsDatabase.editTagsEntry(currentTag, newTag, songId);
                        }
                    });

            alertDialog.setNegativeButton(mContext.getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.create().show();
        }
    }

    public void add(String tag) {
        tags.add(tag);
        Collections.sort(tags);
        notifyDataSetChanged();
    }

    // Return the size of your themes (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tags.size();
    }
}