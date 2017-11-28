package com.example.maxgirkins.songle;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import static com.example.maxgirkins.songle.Songle.songle;

public class word_bag extends AppCompatActivity {
    private Song song;
    protected final String TAG = "WordBagActivity";
    private MainActivity main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_bag);
    }
    @Override
    protected void onResume(){
        super.onResume();
        song = songle.getSongs().getActiveSong();
        Log.i(TAG, song.getTitle());
        ConstraintLayout c = findViewById(R.id.word_bag_layout);
        String lyrics1 = song.toString();
        TextView lyrics = findViewById(R.id.textView_lyrics);
        lyrics.setText(lyrics1);
        lyrics.setMovementMethod(new ScrollingMovementMethod());
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
}
