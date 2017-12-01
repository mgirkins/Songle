package com.example.maxgirkins.songle;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.example.maxgirkins.songle.Songle.songle;
/**
 * Created by MaxGirkins on 01/12/2017.
 */

public class UserStatisticsActivity extends AppCompatActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        makeProgressBars();
        makeDistanceCounts();
    }
    private void makeProgressBars(){
        ProgressBar total_completion = findViewById(R.id.total_game_completion);
        total_completion.setMax(songle.getSongs().getNumSongs());
        total_completion.setProgress(songle.getSongs().getCompletedSongsCount());
        ProgressBar song_completion = findViewById(R.id.song_completion);
        song_completion.setMax(songle.getSongs().getActiveSong().getLyrics().size());
        song_completion.setProgress(songle.getSongs().getActiveSong().getCompletedLyricsCount());
    }
    private void makeDistanceCounts(){
        TextView total_distance = findViewById(R.id.distance_all_time);
        Double total_dist = songle.getStats().getTotalDistance();
        total_distance.setText(String.format("%.02f",total_dist) + songle.getSettings().getUnits());
        TextView distance_for_song = findViewById(R.id.distance_walked_for_song);
        Double song_dist = songle.getSongs().getActiveSong().getDistanceWalked();
        distance_for_song.setText(String.format("%.02f",song_dist) + songle.getSettings().getUnits() );
    }
}
