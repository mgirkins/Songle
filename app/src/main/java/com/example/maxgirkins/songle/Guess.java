package com.example.maxgirkins.songle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.io.IOException;
import java.util.Date;
import static com.example.maxgirkins.songle.Songle.songle;

public class Guess extends AppCompatActivity {
    String[] songTitles;
    private static final String TAG = "GuessActivity";
    Date date = new Date();
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_guess);

    }
    public void onPause(){
        super.onPause();
        try {
            songle.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onResume(){
        super.onResume();
        songTitles = songle.getSongs().getTitlesAndArtist().toArray(new String[songle.getSongs().getNumSongs()]);
        Log.i(TAG, songle.getSongs().getActiveSong().getArtistAndTitle());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, songTitles);
        final AutoCompleteTextView textView = findViewById(R.id.guessAutoCompleteTextView);
        textView.setAdapter(adapter);
        Log.i(TAG,songle.getSongs().getTitlesAndArtist().toString());
        Button guessButton = findViewById(R.id.guess_button);
        guessButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (textView.getText().toString().equals(songle.getSongs().getActiveSong().getArtistAndTitle())){
                    onCorrect();
                } else {
                    onIncorrect();
                }
            }
        });
    }

    private void onCorrect(){
        Log.i(TAG, songle.getSongs().getNumSongs().toString());
        Log.i(TAG, songle.getSongs().getCompletedSongsCount().toString());
        if (songle.getSongs().getNumSongs() == (songle.getSongs().getCompletedSongsCount()+1)){
            songle.resetProgress();
            Intent goCompleted = new Intent(getApplicationContext(),GameCompleted.class);
            startActivity(goCompleted);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("title", songle.getSongs().getActiveSong().getArtistAndTitle());
            GuessDialog g = new GuessDialog();
            g.setArguments(bundle);
            g.show(this.getFragmentManager(),"GuessDialog");
            songle.getSongs().getActiveSong().setCompleted();
            songle.getSongs().newActiveSong();
        }

    }
    private void onIncorrect(){
        Log.i(TAG, songle.getSongs().getActiveSong().getArtistAndTitle());
        GuessDialogIncorrect g = new GuessDialogIncorrect();
        g.show(this.getFragmentManager(),"GuessDialog");
    }
    public void doPositiveClick(){

        Intent mapsIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mapsIntent);
    }
    public void doNegativeClick(){
        String url = songle.getSongs().getActiveSong().getYoutubeLink();
        Intent lVideoIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(lVideoIntent);
    }
    public void doPositiveClickIncorrect(){
        AutoCompleteTextView textView = findViewById(R.id.guessAutoCompleteTextView);
        textView.setText("");
    }
    public void doNegativeClickIncorrect(){
        Intent mapIntent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mapIntent);
    }


}
