package com.example.maxgirkins.songle;

import android.os.AsyncTask;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.maxgirkins.songle.Songle.songle;

// class to get all the info about the lyrics
public class DownloadSongLyrics extends AsyncTask<String, Void, List<Lyric>> {
    private static final String TAG = "DownloaderA";
    private List<Lyric> s = new ArrayList<>();
    @Override
    protected void onPostExecute(List<Lyric> lyrics) {
        super.onPostExecute(lyrics);
        //add lyrics to the correct song
        songle.getSongs().getActiveSong().setLyrics(lyrics);
        //call onlyrics downloaded to populate maps etc.
        songle.onLyricsDownloaded(lyrics);
    }
    @Override
    protected List<Lyric> doInBackground(String... urls) {
        try {
            //first url is the raw lyrics as text
            //second url is the km file
            //asynctask queues the two so we can execute one after another and ensure correct data
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
            //if raw lyrics, pass to raw lyrics parser
            if (urlString.substring(urlString.length() - 3).equals("txt")) {
                SongLyricParser q = new SongLyricParser();
                s = q.parse(stream);
            }
            //else if kml pass to kml parser
            else if (urlString.substring(urlString.length() - 3).equals("kml")){
                MapInfoParser p = new MapInfoParser(s, songle.getSettings().getDifficulty());
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
