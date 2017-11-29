package com.example.maxgirkins.songle;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.maxgirkins.songle.Songle.songle;

public class word_bag extends AppCompatActivity {
    private Song song;
    protected final String TAG = "WordBagActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_bag);
    }
    @Override
    protected void onResume(){
        super.onResume();
        song = songle.getSongsWhenExist().getActiveSong();
        Log.i(TAG, song.getTitle());
        String lyrics1 = song.toString();
        TextView lyrics = findViewById(R.id.textView_lyrics);
        lyrics.setText(lyrics1);
        lyrics.setMovementMethod(new ScrollingMovementMethod());
        Log.i(TAG, song.getCompletedLyricsCount().toString());
        Button guessButton = findViewById(R.id.word_bag_guess_button);
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goGuess = new Intent(getApplicationContext(), Guess.class);
                startActivity(goGuess);
            }
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
}
