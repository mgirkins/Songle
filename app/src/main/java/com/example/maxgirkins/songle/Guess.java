package com.example.maxgirkins.songle;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import static com.example.maxgirkins.songle.Songle.songle;

public class Guess extends AppCompatActivity {
    SongList songs;
    SharedPreferences settings;
    String[] songTitles;
    private static final String TAG = "GuessActivity";

    @Override
    protected void onStart() {
        super.onStart();
        songs = songle.getSongs();
        settings = songle.getSettings();
        songTitles = songs.getTitles().toArray(new String[songs.getNumSongs()]);
        setContentView(R.layout.activity_guess);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, songTitles);
        AutoCompleteTextView textView = findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
        Log.i(TAG,songs.getTitles().toString());
    }



}
