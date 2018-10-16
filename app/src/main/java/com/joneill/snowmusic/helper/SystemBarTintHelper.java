package com.joneill.snowmusic.helper;

import android.app.Activity;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by joseph on 1/11/15.
 */
public class SystemBarTintHelper {
    private SystemBarTintManager tintManager;

    public SystemBarTintHelper(Activity mActivity) {
        tintManager = new SystemBarTintManager(mActivity);
        tintManager.setStatusBarTintEnabled(true);
    }

    public void setStatusBarColor(float alpha, int color) {
        tintManager.setTintColor(color);
        tintManager.setStatusBarAlpha(alpha);
    }

    public void setStatusBarAlpha(float alpha) {
        tintManager.setStatusBarAlpha(alpha);
    }

    public SystemBarTintManager getTintManager() {
        return tintManager;
    }

    public void setTintManager(SystemBarTintManager tintManager) {
        this.tintManager = tintManager;
    }

}
