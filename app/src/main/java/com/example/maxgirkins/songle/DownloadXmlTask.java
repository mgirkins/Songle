package com.example.maxgirkins.songle;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadXmlTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "DownloaderA";

    @Override
    protected String doInBackground(String... urls) {
        try {
            loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XMLPullParserException");
        }
        return "";
    }
    @Override
    protected void onPostExecute(String result) {
    }
    private void loadXmlFromNetwork(String urlString) throws
            XmlPullParserException, IOException {
        try (InputStream stream = downloadUrl(urlString)){
            XmlParser p = new XmlParser();
            Log.i(TAG, (p.parse(stream)).toString());
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
