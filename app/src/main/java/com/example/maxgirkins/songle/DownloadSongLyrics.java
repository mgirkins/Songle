package com.example.maxgirkins.songle;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DownloadSongLyrics extends AsyncTask<String, Void, List<Lyric>> {

    private static final String TAG = "DownloaderA";
    List<Lyric> s = new ArrayList<>();
    Integer level;
    String songNum;
    SongList songs;
    public DownloadSongLyrics(String songNum,Integer level, SongList s){
        this.level = level;
        this.songNum = songNum;
        this.songs = s;
    }

    @Override
    protected void onPostExecute(List<Lyric> lyrics) {
        songs.getActiveSong().addLyrics(lyrics);
        Log.i("THISIS", lyrics.get(1).toString());
        super.onPostExecute(lyrics);
    }

    @Override
    protected List<Lyric> doInBackground(String... urls) {
        try {
            loadXmlFromNetwork(urls[0]);
            loadXmlFromNetwork(urls[1]);
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XMLPullParserException");
        }

        return this.s;
    }
    private void loadXmlFromNetwork(String urlString) throws
            XmlPullParserException, IOException {

        try (InputStream stream = downloadUrl(urlString)) {
            if (urlString.substring(urlString.length() - 3).equals("txt")) {
                SongLyricParser q = new SongLyricParser();
                Log.i(TAG,"thisisatxt");
                s = q.parse(stream);
            } else if (urlString.substring(urlString.length() - 3).equals("kml")){
                MapInfoParser p = new MapInfoParser(s, level);
                Log.i(TAG,"thisisakml");
                s = p.parse(stream);
            }
        } catch (IOException i) {
            i.printStackTrace();
        }

    }
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
