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
import java.util.concurrent.ExecutionException;

import static com.example.maxgirkins.songle.Songle.songle;
// download the song titles and artists etc.
public class DownloadXmlTask extends AsyncTask<String, Void, SongList> {

    private static final String TAG = "DownloaderA";
    SongList s = new SongList();
    @Override
    protected SongList doInBackground(String... urls) {
        try {
            loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XMLPullParserException");
        }
        return s;
    }

    @Override
    protected void onPostExecute(SongList songList) {
        super.onPostExecute(songList);
    }

    private void loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        try (InputStream stream = downloadUrl(urlString)){
            SongInfoParser p = new SongInfoParser();
            s = p.parse(stream);
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
