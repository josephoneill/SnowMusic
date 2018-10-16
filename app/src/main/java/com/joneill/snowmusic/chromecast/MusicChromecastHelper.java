package com.joneill.snowmusic.chromecast;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.RemoteMediaPlayer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.images.WebImage;
import com.joneill.snowmusic.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by joseph on 2/20/15.
 */
public class MusicChromecastHelper {
    private Activity mActivity;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;
    private CastDevice mSelectedDevice;
    private GoogleApiClient mApiClient;
    private RemoteMediaPlayer mRemoteMediaPlayer;
    private Cast.Listener mCastClientListener;
    private boolean mWaitingForReconnect = false;
    private boolean mApplicationStarted = false;
    private boolean mSongIsLoaded;
    private boolean mIsPlaying;
    private MusicWebServer songWebServer;
    private MusicWebServer albumWebServer;

    public MusicChromecastHelper(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void init() {
        initMediaRouter();
    }
    private void initMediaRouter() {
        // Configure Cast device discovery
        mMediaRouter = MediaRouter.getInstance(mActivity);
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(mActivity.getString(R.string.app_id)))
                .build();
        mMediaRouterCallback = new MediaRouterCallback();
    }

    private void initCastClientListener() {
        mCastClientListener = new Cast.Listener() {
            @Override
            public void onApplicationStatusChanged() {
            }

            @Override
            public void onVolumeChanged() {
            }

            @Override
            public void onApplicationDisconnected(int statusCode) {
                teardown();
            }
        };
    }

    private void initRemoteMediaPlayer() {
        mRemoteMediaPlayer = new RemoteMediaPlayer();
        mRemoteMediaPlayer.setOnStatusUpdatedListener(new RemoteMediaPlayer.OnStatusUpdatedListener() {
            @Override
            public void onStatusUpdated() {
                MediaStatus mediaStatus = mRemoteMediaPlayer.getMediaStatus();
                mIsPlaying = mediaStatus.getPlayerState() == MediaStatus.PLAYER_STATE_PLAYING;
            }
        });

        mRemoteMediaPlayer.setOnMetadataUpdatedListener(new RemoteMediaPlayer.OnMetadataUpdatedListener() {
            @Override
            public void onMetadataUpdated() {
                Log.e("Updated", "MetaDataUpdated");
            }
        });
    }

    /*private void controlVideo() {
        if (mRemoteMediaPlayer == null || !mVideoIsLoaded)
            return;

        if (mIsPlaying) {
            mRemoteMediaPlayer.pause(mApiClient);
            mButton.setText(getString(R.string.resume_video));
        } else {
            mRemoteMediaPlayer.play(mApiClient);
            mButton.setText(getString(R.string.pause_video));
        }
    }*/

    public void startSong(FileInputStream songFis, String title, String songUrl, String albumUrl, String contentType, int width, int height) {
        MediaMetadata mediaMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK);
        mediaMetadata.putString(MediaMetadata.KEY_TITLE, title);
        albumUrl = albumUrl + "?timestamp=" + System.currentTimeMillis();
        mediaMetadata.addImage(new WebImage(Uri.parse(albumUrl)));

        MediaInfo mediaInfo = new MediaInfo.Builder(songUrl)
                .setContentType(contentType)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setMetadata(mediaMetadata)
                .build();

        try {
            mRemoteMediaPlayer.load(mApiClient, mediaInfo, true)
                    .setResultCallback(new ResultCallback<RemoteMediaPlayer.MediaChannelResult>() {
                        @Override
                        public void onResult(RemoteMediaPlayer.MediaChannelResult mediaChannelResult) {
                            if (mediaChannelResult.getStatus().isSuccess()) {
                                mSongIsLoaded = true;
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    public void onResume() {
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback, MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
    }

    //Run before super
    public void onPause() {
        if (mActivity.isFinishing()) {
            // End media router discovery
            mMediaRouter.removeCallback(mMediaRouterCallback);
        }
    }

    private class MediaRouterCallback extends MediaRouter.Callback {

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            initCastClientListener();
            initRemoteMediaPlayer();

            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());

            launchReceiver();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            teardown();
            mSelectedDevice = null;
            mSongIsLoaded = false;
        }
    }

    private void launchReceiver() {
        Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
                .builder(mSelectedDevice, mCastClientListener);

        ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks();
        ConnectionFailedListener mConnectionFailedListener = new ConnectionFailedListener();
        mApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(Cast.API, apiOptionsBuilder.build())
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mConnectionFailedListener)
                .build();
        mApiClient.connect();
    }

    private class ConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks {

        @Override
        public void onConnected(Bundle hint) {
            if (mWaitingForReconnect) {
                mWaitingForReconnect = false;
                reconnectChannels(hint);
            } else {
                try {
                    Cast.CastApi.launchApplication(mApiClient, mActivity.getString(R.string.app_id), false)
                            .setResultCallback(new ResultCallback<Cast.ApplicationConnectionResult>() {
                                                   @Override
                                                   public void onResult(Cast.ApplicationConnectionResult applicationConnectionResult) {
                                                       Status status = applicationConnectionResult.getStatus();
                                                       if (status.isSuccess()) {
                                                           //Values that can be useful for storing/logic
                                                           ApplicationMetadata applicationMetadata = applicationConnectionResult.getApplicationMetadata();
                                                           String sessionId = applicationConnectionResult.getSessionId();
                                                           String applicationStatus = applicationConnectionResult.getApplicationStatus();
                                                           boolean wasLaunched = applicationConnectionResult.getWasLaunched();

                                                           mApplicationStarted = true;
                                                           reconnectChannels(null);
                                                       }
                                                   }
                                               }
                            );
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            mWaitingForReconnect = true;
        }
    }

    private void reconnectChannels(Bundle hint) {
        if ((hint != null) && hint.getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
            //Log.e( TAG, "App is no longer running" );
            teardown();
        } else {
            try {
                Cast.CastApi.setMessageReceivedCallbacks(mApiClient, mRemoteMediaPlayer.getNamespace(), mRemoteMediaPlayer);
            } catch (IOException e) {
                //Log.e( TAG, "Exception while creating media channel ", e );
            } catch (NullPointerException e) {
                //Log.e( TAG, "Something wasn't reinitialized for reconnectChannels" );
            }
        }
    }

    private class ConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            teardown();
        }
    }

    public void serveSong(FileInputStream songFis, String contentType) {
        if (songWebServer != null && songWebServer.isAlive()) {
            songWebServer.stop();
        }
        songWebServer = new MusicWebServer(songFis, 8080, contentType);
        try {
            songWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serveAlbumArt(InputStream albumFis, String contentType) {
        if (albumWebServer != null && albumWebServer.isAlive()) {
            albumWebServer.stop();
            albumWebServer = null;
        }
        albumWebServer = new MusicWebServer(albumFis, 8000, contentType);
        try {
            albumWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCreateOptionsMenu(Menu menu) {
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
    }


    private void teardown() {
        if (mApiClient != null) {
            if (mApplicationStarted) {
                try {
                    Cast.CastApi.stopApplication(mApiClient);
                    if (mRemoteMediaPlayer != null) {
                        Cast.CastApi.removeMessageReceivedCallbacks(mApiClient, mRemoteMediaPlayer.getNamespace());
                        mRemoteMediaPlayer = null;
                    }
                } catch (IOException e) {
                    //Log.e( TAG, "Exception while removing application " + e );
                }
                mApplicationStarted = false;
            }
            if (mApiClient.isConnected())
                mApiClient.disconnect();
            mApiClient = null;
        }
        mSelectedDevice = null;
        mSongIsLoaded = false;
    }

    //Run before super
    public void onDestroy() {
        teardown();
    }

    public boolean isConnected() {
        if(mApiClient != null && mApiClient.isConnected()) {
            return true;
        }
        return false;
    }

    public void pauseSong() {
        if(mRemoteMediaPlayer != null && mApiClient != null && isConnected()) {
            mRemoteMediaPlayer.pause(mApiClient);
        }
    }
    public void playSong() {
        if(mRemoteMediaPlayer != null && mApiClient != null && isConnected()) {
            mRemoteMediaPlayer.play(mApiClient);
        }
    }

    public void seek(final long position) {
        if(mRemoteMediaPlayer != null && mApiClient != null && isConnected()) {
            mRemoteMediaPlayer.seek(mApiClient, 100000, RemoteMediaPlayer.RESUME_STATE_UNCHANGED)
                    .setResultCallback(new ResultCallback<RemoteMediaPlayer.MediaChannelResult>() {
                        @Override
                        public void onResult(RemoteMediaPlayer.MediaChannelResult mediaChannelResult) {
                            Status status = mediaChannelResult.getStatus();
                            if(status.isSuccess()) {
                                Toast.makeText(mActivity.getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("Seek errors:", status.getStatusMessage() + "   code: " + status.getStatusCode());
                            }
                        }
                    });
        }
    }

    public int getPercentagePlayed(int seekBarMax) {
        int percent = (int) (((float) mRemoteMediaPlayer.getApproximateStreamPosition() / (float) mRemoteMediaPlayer.getStreamDuration()) * seekBarMax);
        return percent;
    }

    public MusicWebServer getSongWebServer() {
        return songWebServer;
    }

    public MusicWebServer getAlbumWebServer() {
        return albumWebServer;
    }

    public RemoteMediaPlayer getMRemoteMediaPlayer() { return mRemoteMediaPlayer; }
}