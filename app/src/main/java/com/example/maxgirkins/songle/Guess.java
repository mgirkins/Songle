package com.example.maxgirkins.songle;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
        Log.i(TAG, "youtube link for active song: " +songs.getActiveSong().getYoutubeLink());
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
                Log.i(TAG,songs.getActiveSong().getArtistAndTitle());
                if (textView.getText().toString().equals(songs.getActiveSong().getArtistAndTitle())){
                    Log.i(TAG,"CONGRATULATIONS!!!!");
                    onCorrect();
                }
            }
        });
    }
    public void onPause(){
        super.onPause();
    }
    public void onResume(){
        super.onResume();
        songs = songle.getSongsWhenExist();
    }

    private void onCorrect(){

        Bundle bundle = new Bundle();
        bundle.putString("title", songs.getActiveSong().getArtistAndTitle());
        bundle.putString("posBtn", "New Game");
        bundle.putString("negBtn", "View on Youtube");
        GuessDialog g = new GuessDialog();
        g.setArguments(bundle);
        g.show(this.getFragmentManager(),"GuessDialog");
    }
    public void doPositiveClick(){
        songs.getActiveSong().setCompleted(date.getTime());
        songs.newActiveSong();
        songle.setSongs(songs);
        Intent mapsIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mapsIntent);
    }
    public void doNegativeClick(){
        String url = songle.getSongsWhenExist().getActiveSong().getYoutubeLink();
        songs.getActiveSong().setCompleted(date.getTime());
        songs.newActiveSong();
        songle.setSongs(songs);
        Intent lVideoIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(lVideoIntent);
    }


}
