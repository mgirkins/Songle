package com.example.maxgirkins.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.example.maxgirkins.songle.Songle.songle;

public class Completed_songs_activity extends AppCompatActivity {
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
        addInfoToScreen();
    }

    private void addInfoToScreen(){
        Log.i(TAG, songle.getSongsFirstRun().getNumSongs().toString());
        TextView helpMessage = new TextView(this);
        helpMessage.setText(R.string.completed_songs_no_songs_message);
        if (songle.getSongs().getCompletedSongsCount() == 0){
            l.addView(helpMessage);
        }
        for (int i = 0; i<songle.getSongs().getNumSongs(); i++){
            Song s = songle.getSongs().getAllSongs().get(i);
            if (s.isCompleted()){
                TextView t = new TextView(this);
                t.setText(songle.getSongs().getAllSongs().get(i).getTitle());
                l.addView(t);
                ProgressBar p = new ProgressBar(this,null,R.style.Widget_AppCompat_ProgressBar_Horizontal);
                p.setMax(s.getLyrics().size());
                p.setProgress(s.getCompletedLyricsCount());
                l.addView(p);
            } else {
                String redact = "";
                for (int j=0; j<s.getTitle().length(); j++){
                    redact += '\u25A0';
                }
                TextView t = new TextView(this);
                t.setText(redact);
                l.addView(t);
            }

        }
    }
}
