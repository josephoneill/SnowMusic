package com.joneill.snowmusic.helper;

/**
 * Created by josep_000 on 7/19/2016.
 */

// TODO: Remove this with interface callbacks
public class TriggersHelper {
    private static final TriggersHelper instance = new TriggersHelper();
    private boolean isNewPlaylistAddded;
    private boolean isSongInfoChanged;

    private TriggersHelper() {}

    public static TriggersHelper getInstance() {
        return instance;
    }

    public boolean isNewPlaylistAddded() {
        return isNewPlaylistAddded;
    }

    public void setNewPlaylistAddded(boolean newPlaylistAddded) {
        isNewPlaylistAddded = newPlaylistAddded;
    }

    public boolean isSongInfoChanged() {
        return isSongInfoChanged;
    }

    public void setSongInfoChanged(boolean songInfoChanged) {
        isSongInfoChanged = songInfoChanged;
    }
}
