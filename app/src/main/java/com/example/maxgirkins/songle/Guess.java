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
//activity where user guesses the song they have been collecting lyrics for.
public class Guess extends AppCompatActivity {
    String[] songTitles;
    private static final String TAG = "GuessActivity";
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_guess);

    }
    public void onResume(){
        super.onResume();
        //songtitles given to autocomplete text view so that user can guess easier.
        songTitles = songle.getSongs().getTitlesAndArtist().toArray(new String[songle.getSongs().getNumSongs()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, songTitles);
        final AutoCompleteTextView textView = findViewById(R.id.guessAutoCompleteTextView);
        textView.setAdapter(adapter);
        Button guessButton = findViewById(R.id.guess_button);
        guessButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //check if user has guessed the song correctly
                if (textView.getText().toString().equals(songle.getSongs().getActiveSong().getArtistAndTitle())){
                    onCorrect();
                } else {
                    onIncorrect();
                }
            }
        });
    }
    public void onPause(){
        super.onPause();
        try {
            //guess can alter the data so should save onPause to make sure everything saved up to date.
            songle.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void onCorrect(){
        //if user has completed all songs then reset all progress to be mean, and send them to
        //the completion activity.
        if (songle.getSongs().getNumSongs() == (songle.getSongs().getCompletedSongsCount()+1)){
            songle.resetProgress();
            Intent goCompleted = new Intent(getApplicationContext(),GameCompleted.class);
            startActivity(goCompleted);
        }
        //else trigger a dialog with link to the youtube song, or a new game.
        else {
            Bundle bundle = new Bundle();
            bundle.putString("title", songle.getSongs().getActiveSong().getArtistAndTitle());
            GuessDialog g = new GuessDialog();
            g.setArguments(bundle);
            g.show(this.getFragmentManager(),"GuessDialog");
            //complete current song and set new active song.
            songle.getSongs().getActiveSong().setCompleted();
            songle.getSongs().newActiveSong();
        }

    }
    private void onIncorrect(){
        //trigger dialog with option to try again or keep looking for lyrics
        GuessDialogIncorrect g = new GuessDialogIncorrect();
        g.show(this.getFragmentManager(),"GuessDialog");
    }
    public void doPositiveClick(){
        //on user click new game, go to main activity which should by now be showing a new map.
        Intent mapsIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mapsIntent);
    }
    public void doNegativeClick(){
        //go to youtube link for song.
        String url = songle.getSongs().getActiveSong().getYoutubeLink();
        Intent lVideoIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(lVideoIntent);
    }
    public void doPositiveClickIncorrect(){
        //clear autocomplete text box if user wants to guess again.
        AutoCompleteTextView textView = findViewById(R.id.guessAutoCompleteTextView);
        textView.setText("");
    }
    public void doNegativeClickIncorrect(){
        //if user wants to look for more lyrics then send them back to main map
        Intent mapIntent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mapIntent);
    }


}
