package com.joneill.snowmusic.chromecast;

import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joseph on 2/20/15.
 */
public class MusicWebServer extends NanoHTTPD {
    private InputStream songFis;
    private String mimeType;
    private int port;

    public static final Map<String, String> MIME_TYPES = new HashMap<String, String>() {{
        put("css", "text/css");
        put("htm", "text/html");
        put("html", "text/html");
        put("xml", "text/xml");
        put("java", "text/x-java-source, text/java");
        put("md", "text/plain");
        put("txt", "text/plain");
        put("asc", "text/plain");
        put("gif", "image/gif");
        put("jpg", "image/jpeg");
        put("jpeg", "image/jpeg");
        put("png", "image/png");
        put("mp3", "audio/mpeg");
        put("m3u", "audio/mpeg-url");
        put("mp4", "video/mp4");
        put("ogv", "video/ogg");
        put("flv", "video/x-flv");
        put("mov", "video/quicktime");
        put("swf", "application/x-shockwave-flash");
        put("js", "application/javascript");
        put("pdf", "application/pdf");
        put("doc", "application/msword");
        put("ogg", "application/x-ogg");
        put("zip", "application/octet-stream");
        put("exe", "application/octet-stream");
        put("class", "application/octet-stream");
    }};

    //Use port 8080 for music file
    //Use port 8000 for album file
    public MusicWebServer(InputStream fis, int port, String mimeType) {
        super(port);
        this.songFis = fis;
        this.mimeType = mimeType;
        this.port = port;
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        String answer = "";
        Log.e("Client IP THIS", session.getHeaders().get("http-client-ip"));
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, mimeType, songFis);
    }

    public int getPort() {
        return port;
    }
}