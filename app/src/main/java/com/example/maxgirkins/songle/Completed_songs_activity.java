package com.example.maxgirkins.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.example.maxgirkins.songle.Songle.songle;

public class Completed_songs_activity extends AppCompatActivity {
    //private SongList songs;
    private final String TAG = "CompletedSongsActivity";
    LinearLayout l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_songs_activity);
        l = findViewById(R.id.completed_songs);
    }
    @Override
    protected void  onResume(){
        super.onResume();
        //songs = songle.getSongsWhenExist();
        addInfoToScreen();
    }

    private void addInfoToScreen(){
        Log.i(TAG, "addinfo called");
        Log.i(TAG, songle.getSongs().getNumSongs().toString());
        TextView helpMessage = new TextView(this);
        helpMessage.setText(R.string.completed_songs_no_songs_message);
        if (songle.getSongsWhenExist().getCompletedSongsCount() == 0){
            l.addView(helpMessage);
        }
        for (int i=0; i<songle.getSongsWhenExist().getNumSongs(); i++){
            Song s = songle.getSongsWhenExist().getAllSongs().get(i);
            if (s.isCompleted()){
                TextView t = new TextView(this);
                t.setText(songle.getSongsWhenExist().getAllSongs().get(i).getTitle());
                l.addView(t);
                ProgressBar p = new ProgressBar(this,null,R.style.Widget_AppCompat_ProgressBar_Horizontal);
                p.setMax(s.getLyrics().size());
                p.setProgress(s.getCompletedLyricsCount());
                l.addView(p);
            } else {
                String redact = "";
                for (int j=0; j<s.getTitle().length(); j++){
                    redact += "*";
                }
                TextView t = new TextView(this);
                t.setText(redact);
                l.addView(t);
            }

        }
    }
}
