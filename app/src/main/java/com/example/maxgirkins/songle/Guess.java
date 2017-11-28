package com.example.maxgirkins.songle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.Date;

import static com.example.maxgirkins.songle.Songle.songle;

public class Guess extends AppCompatActivity {
    SongList songs;
    String[] songTitles;
    private static final String TAG = "GuessActivity";
    Date date = new Date();

    @Override
    protected void onStart() {
        super.onStart();

        songs = songle.getSongsWhenExist();
        songTitles = songs.getTitles().toArray(new String[songs.getNumSongs()]);
        setContentView(R.layout.activity_guess);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, songTitles);
        final AutoCompleteTextView textView = findViewById(R.id.guessAutoCompleteTextView);
        textView.setAdapter(adapter);
        Log.i(TAG,songs.getTitles().toString());
        Button guessButton = findViewById(R.id.guess_button);
        guessButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "onClickCalled");
                Log.i(TAG, textView.getText().toString());
                Log.i(TAG,songs.getTitles().get(songs.getActiveSong().getNum()));
                if (textView.getText().toString().equals(songs.getTitles().get(songs.getActiveSong().getNum()))){

                    Log.i(TAG,"CONGRATULATIONS!!!!");
                    songs.getActiveSong().setCompleted(date.getTime());
                    songs.newActiveSong();
                }
            }
        });
    }
    public void onPause(){
        super.onPause();
        songle.setSongs(songs);
    }


}
