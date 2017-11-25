package com.example.maxgirkins.songle;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownloadMapInfo extends AsyncTask<String, Void, List<Lyric>> {

    private static final String TAG = "DownloaderMapInfo";
    private List<Lyric> b;
    private Integer level;
    public DownloadMapInfo(List<Lyric> s, Integer level){
        this.b = s;
        this.level = level;
    }

    @Override
    protected void onPostExecute(List<Lyric> lyrics) {

        super.onPostExecute(lyrics);
    }

    @Override
    protected List<Lyric> doInBackground(String... urls) {
        try {
            loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XMLPullParserException");
        }
        return null;
    }
    private void loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        try (InputStream stream = downloadUrl(urlString)){
            MapInfoParser p = new MapInfoParser(b, level);
            this.b = p.parse(stream);

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
