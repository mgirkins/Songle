package com.example.maxgirkins.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import static com.example.maxgirkins.songle.Songle.songle;

public class Completed_songs_activity extends AppCompatActivity {
    private SongList songs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_songs_activity);
    }
    @Override
    protected void  onResume(){
        super.onResume();
        songs = songle.getSongs();
    }
}
