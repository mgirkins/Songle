package com.example.maxgirkins.songle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;

public class Guess extends AppCompatActivity {
    private static final String[] COUNTRIES = new String[] {
            "Bohemian Rhapsody - Queen", "Born To be my baby - Bon Jovi", "Born to be a dancer - Kaiser Chiefs", "Both Sides Now - Joni Mitchell", "I'm Gonna be (500 miles) - The Proclaimers", "Baby one more time - Britney Spears", "Babel - Massive Attack"
    };
    SongList songs;
    Gson g = new Gson();
    private static final String TAG = "GuessActivity";
    Songle songle = (Songle) getApplicationContext();
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_guess);
        songs = songle.getSongs();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
        Log.i(TAG,songs.getNumSongs().toString());
    }



}
