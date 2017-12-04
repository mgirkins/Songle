package com.example.maxgirkins.songle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.maxgirkins.songle.Songle.songle;

public class Completed_songs_activity extends AppCompatActivity {
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_songs_activity);
        l = findViewById(R.id.completed_songs_listView);
        //make titles and artists list_adapter friendly
        String[] titles = songle.getSongs().getCompletedTitles().toArray(new String[songle.getSongs().getNumSongs()]);
        String[] artists = songle.getSongs().getCompletedArtists().toArray(new String[songle.getSongs().getNumSongs()]);
        final String[] youtubeLinks = songle.getSongs().getCompletedYoutubeLinks().toArray(new String[songle.getSongs().getNumSongs()]);
        //list adapter to populate completed songs view
        completed_songs_list_adapter rows = new completed_songs_list_adapter(this, titles, artists, youtubeLinks);
        l.setAdapter(rows);
        //go to youtube link for song on click
        l.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = youtubeLinks[i];
                Intent lVideoIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(lVideoIntent);
            }

        });
    }
}