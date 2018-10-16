package com.joneill.snowmusic.data;

import android.graphics.Bitmap;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by josep on 11/12/2016.
 */
public class TLruCache {
    private static TLruCache instance;
    private LruCache<String, Bitmap> mMemoryCache;

    private TLruCache() {
        setUpCache();
    }

    private void setUpCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static TLruCache getInstance() {
        if (instance == null) {
            instance = new TLruCache();
        }
        return instance;
    }

    public LruCache<String, Bitmap> getmMemoryCache() {
        return mMemoryCache;
    }

    public boolean containsBitmap(String key) {
        return mMemoryCache.get(key) != null;
    }

    public void putBitmapInCache(String key, Bitmap bitmap){
        mMemoryCache.put(key, bitmap);
    }

    public Bitmap getBitmap(String key) {
        return mMemoryCache.get(key);
    }
}
