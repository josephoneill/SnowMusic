package com.joneill.snowmusic.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.themes.Theme;
import com.joneill.snowmusic.themes.ThemeManager;

import java.util.List;


/**
 * Created by josep_000 on 8/21/2014.
 */
public class ThemesAdapter extends RecyclerView.Adapter<ThemesAdapter.ViewHolder> {
    private List<Theme> themes;
    private List<Theme> defaultItem;

    public ThemesAdapter(List<Theme> themes) {
        this.themes = themes;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.theme_card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.imgThemePreview.setImageBitmap(themes.get(position).getPreviewBitmap());
        viewHolder.txtThemeTitle.setText(themes.get(position).getTitle());
    }

    // Inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgThemePreview;
        public TextView txtThemeTitle;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgThemePreview = (ImageView) itemLayoutView.findViewById(R.id.theme_preview);
            txtThemeTitle = (TextView) itemLayoutView.findViewById(R.id.theme_title);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("TAG", "onClick " + getPosition());
            ThemeManager.getInstance().applyTheme(themes.get(getPosition()));
        }
    }

    public void add(Theme theme, int position) {
        themes.add(position, theme);
        notifyItemInserted(position);
    }

    // Return the size of your themes (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return themes.size();
    }
}