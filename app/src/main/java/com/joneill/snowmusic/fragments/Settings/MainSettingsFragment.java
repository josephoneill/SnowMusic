package com.joneill.snowmusic.fragments.Settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.joneill.snowmusic.R;
import com.joneill.snowmusic.themes.ThemeManager;
import com.joneill.snowmusic.widgets.ThemedFragment;

/**
 * Created by josep_000 on 7/26/2015.
 */
public class MainSettingsFragment extends ThemedFragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_settings_main, container, false);

        setupIcons(rootView);

        RelativeLayout itemLayout;

        itemLayout = (RelativeLayout) rootView.findViewById(R.id.settings_interface_item);
        itemLayout.setOnClickListener(this);

        itemLayout = (RelativeLayout) rootView.findViewById(R.id.settings_functionality_item);
        itemLayout.setOnClickListener(this);

        itemLayout = (RelativeLayout) rootView.findViewById(R.id.settings_headset_item);
        itemLayout.setOnClickListener(this);

        itemLayout = (RelativeLayout) rootView.findViewById(R.id.settings_notification_item);
        itemLayout.setOnClickListener(this);


        return rootView;
    }

    private void setupIcons(View rootView) {
        ImageView[] ivIcons = {
                (ImageView) rootView.findViewById(R.id.settings_interface_icon),
                (ImageView) rootView.findViewById(R.id.settings_lookfeel_icon),
                (ImageView) rootView.findViewById(R.id.settings_functionality_icon),
                (ImageView) rootView.findViewById(R.id.settings_headset_icon),
                (ImageView) rootView.findViewById(R.id.settings_notification_icon)
        };
        colorizeIcons(ivIcons);
    }

    private void colorizeIcons(ImageView[] ivs) {
        for(ImageView ivIcon : ivs) {
            ivIcon.setColorFilter((int) ThemeManager.getInstance().getThemeData().get("primary_color"));
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch(v.getId()) {
            case R.id.settings_interface_item:
                ft.replace(R.id.settings_fragment_container, new InterfaceSettingsFragment());
                ft.commit();
                break;
            case R.id.settings_lookfeel_item:
                break;
            case R.id.settings_functionality_item:
                ft.replace(R.id.settings_fragment_container, new FunctionalitySettingsFragment());
                ft.commit();
                break;
            case R.id.settings_headset_item:
                ft.replace(R.id.settings_fragment_container, new HeadsetSettingsFragment());
                ft.commit();
                break;
            case R.id.settings_notification_item:
                ft.replace(R.id.settings_fragment_container, new NotificationSettingsFragment());
                ft.commit();
        }
    }
}
