package com.example.maxgirkins.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
        song = songle.getSongs().getActiveSong();
        Log.i(TAG, song.getTitle());
    }
}
