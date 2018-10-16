package com.joneill.snowmusic.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joneill.snowmusic.data.SongDataHolder;
import com.joneill.snowmusic.data.TLruCache;
import com.joneill.snowmusic.song.Album;
import com.joneill.snowmusic.song.Song;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by josep on 10/17/2016.
 */
public class AlbumArtDownloader {
    private Context context;
    private RequestQueue queue;
    private int count;
    private ImageLoader imageLoader;
    private Song currentLookedSong;
    private ProgressDialog progress;
    private List<Album> albumList;
    private List<Song> songsList;

    private boolean skipExArt;
    private String artSize;
    private int tries;
    private int size;

    public AlbumArtDownloader(Context context, ProgressDialog progress) {
        this.context = context;
        this.progress = progress;

        albumList = new ArrayList<>();
        songsList = new ArrayList<>();
        albumList = SongDataHolder.getInstance().getAlbumsData();
        songsList = SongDataHolder.getInstance().getSongsData();
        skipExArt = false;
        artSize = "mega";

        queue = Volley.newRequestQueue(context);

        imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }


    public void fetchAlbumFromLastFM(final boolean useSongAsAlbum, final boolean isSingleUsage) throws UnsupportedEncodingException, InterruptedException {
        Log.e("Type", "joneill size is " + artSize);
        if (skipExArt) {
            albumList = removeExisitingAlbums(albumList);
            songsList = removeExisitingSongsAlbums(songsList);
        }

        if (useSongAsAlbum) {
            size = songsList.size();
        } else {
            size = albumList.size();
        }
        tries++;
        for (int i = 0; i < size; i++) {
            // latch.reset();
            final int index = i;
            String artist = "";
            String title = "";
            if (useSongAsAlbum) {
                currentLookedSong = songsList.get(i);
                artist = currentLookedSong.getArtist();
                title = currentLookedSong.getTitle();
            } else {
                currentLookedSong = albumList.get(i).getSongs().get(0);
                Log.e("ID", "joneill id is " + currentLookedSong.getAlbum().getAlbumId());
                artist = currentLookedSong.getAlbum().getArtist();
                title = currentLookedSong.getAlbum().getTitle();
            }
            StringBuilder stringBuilder = new StringBuilder("http://ws.audioscrobbler.com/2.0/");
            stringBuilder.append("?method=album.getinfo");
            stringBuilder.append("&api_key=");
            stringBuilder.append("3fb9cbfa4edccd88fa1d21ff7a784055");
            stringBuilder.append("&artist=" + URLEncoder.encode(artist, "UTF-8"));
            stringBuilder.append("&album=" + URLEncoder.encode(title, "UTF-8"));
            String url = stringBuilder.toString();
            StringRequest req = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (index + 1 == size) {
                                    processData(currentLookedSong, response, true, isSingleUsage, index, size, useSongAsAlbum);
                                } else {
                                    processData(currentLookedSong, response, false, isSingleUsage, index, size, useSongAsAlbum);
                                }

                            } catch (XmlPullParserException e) {
                                incrementDownloadProgress(index, size);
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // handle error response
                            incrementDownloadProgress(index, size);
                            Log.e("Last.fm album error", error.toString());
                            if(isSingleUsage) {
                                Toast.makeText(context, "Song Not Found", Toast.LENGTH_LONG);
                            }
                        }
                    }
            );
            queue.add(req);
            //latch.await();
        }
    }

    private void processData(final Song song, String response, final boolean isLast, final boolean isSingleUsage,
                             final int index, final int size, final boolean useSongAsAlbum) throws XmlPullParserException {
        XmlPullParserFactory xmlFactoryObject = null;
        XmlPullParser myParser = null;
        InputStream stream = new ByteArrayInputStream(response.getBytes());
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(stream, null);
            int event = myParser.getEventType();
            boolean isUrlFound = false;
            String name = myParser.getName();
            while (event != XmlPullParser.END_DOCUMENT) {
                name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("image") && myParser.getAttributeValue(null, "size").equals(artSize)) {
                            isUrlFound = true;
                        } else {
                            //Song not found
                            if(isSingleUsage) {
                                Toast.makeText(context, "Song Not Found", Toast.LENGTH_LONG);
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (isUrlFound) {
                            final String url = myParser.getText();
                            //Log.e("Test", String.valueOf(index) + " joneill and the url is " + url);
                            imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
                                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

                                public void putBitmap(String url, Bitmap bitmap) {
                                    mCache.put(url, bitmap);
                                }

                                public Bitmap getBitmap(String url) {
                                    return mCache.get(url);
                                }
                            });
                            imageLoader.get(url, new ImageLoader.ImageListener() {
                                @Override
                                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                    Bitmap artwork = response.getBitmap();
                                    if (artwork != null) {
                                        Song song = null;
                                        if (useSongAsAlbum) {
                                            song = songsList.get(index);
                                            song.testSaveAlbumBySong(context, artwork);
                                            TLruCache.getInstance().putBitmapInCache(String.valueOf(song.getId()), artwork);
                                        } else {
                                            song = albumList.get(index).getSongs().get(0);
                                            song.changeAlbumArt(context, artwork);
                                            TLruCache.getInstance().putBitmapInCache(String.valueOf(song.getAlbumId()), artwork);
                                        }
                                        for (Song s : song.getAlbum().getSongs()) {
                                            s.setAlbumArt(String.valueOf(s.getAlbumId()));
                                        }
                                        incrementDownloadProgress(index, size);
                                    }
                                }

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    incrementDownloadProgress(index, size);
                                }
                            });
                            isUrlFound = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        break;
                }
                event = myParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            incrementDownloadProgress(index, size);
        } catch (IOException e) {
            e.printStackTrace();
            incrementDownloadProgress(index, size);
        }
    }

    public void incrementDownloadProgress(int index, int size) {
        count += 1;
        progress.setMessage("Downloading " + String.valueOf(count) + " out of " + String.valueOf(size));
        if (count >= size) {
            if (tries < 2) {
                //Try again using song name as album
                count = 0;
                try {
                    fetchAlbumFromLastFM(true, false);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                progress.dismiss();
            }
        }
    }

    //Use iterator to prevent ConcurrentModificationException
    private List<Album> removeExisitingAlbums(List<Album> albums) {
        List<Album> newAlbums = new ArrayList<Album>(albums);
        Iterator<Album> iter = newAlbums.iterator();

        while (iter.hasNext()) {
            Album album = iter.next();

            if (album.getAlbumArt() != SongDataHolder.DEFAULT_BITMAP) {
                iter.remove();
            }
        }

        return newAlbums;
    }

    private List<Song> removeExisitingSongsAlbums(List<Song> songs) {
        List<Song> newSongs = new ArrayList<Song>(songs);
        Iterator<Song> iter = newSongs.iterator();

        while (iter.hasNext()) {
            Song song = iter.next();

            if (song.getAlbumArt() != SongDataHolder.DEFAULT_BITMAP) {
                iter.remove();
            }
        }

        return newSongs;
    }

    public String getArtSize() {
        return artSize;
    }

    public void setArtSize(String artSize) {
        this.artSize = artSize;
    }

    public boolean isSkipExArt() {
        return skipExArt;
    }

    public void setSkipExArt(boolean skipExArt) {
        this.skipExArt = skipExArt;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    public void setSongsList(List<Song> songList) {
        this.songsList = songList;
    }


}
