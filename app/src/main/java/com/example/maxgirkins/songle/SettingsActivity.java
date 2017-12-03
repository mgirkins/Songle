package com.example.maxgirkins.songle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import static com.example.maxgirkins.songle.Songle.songle;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private final String[] difficulties = {"Easy","Medium", "Hard","Expert","Fiendish"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        SeekBar seekBar = findViewById(R.id.settings_seekBar);
        seekBar.setProgress(4-songle.getSettings().getDifficulty(),true);
        final TextView seekBarValue = findViewById(R.id.settings_difficulty_level);
        seekBarValue.setText(difficulties[4-songle.getSettings().getDifficulty()]);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(difficulties[progress]);
                songle.getSettings().setDifficulty(4-progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        final RadioGroup units = findViewById(R.id.settings_units_radio_group);
        if (songle.getSettings().getUnits().equals("Miles")){
            RadioButton m = findViewById(R.id.radio_miles);
            m.setChecked(true);
        } else {
            RadioButton m = findViewById(R.id.radio_km);
            m.setChecked(true);
        }

        units.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = findViewById(checkedId);
                String text = checkedRadioButton.getText().toString();
                if (text.equals("Km")){
                    songle.getSettings().setUnits("Km");
                } else {
                    songle.getSettings().setUnits("Miles");
                }
            }
        });
        Button give_up = findViewById(R.id.give_up_button);
        give_up.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                songle.getSongs().newActiveSong();
                Intent goMap = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goMap);
            }
        });
        Button reset_progress = findViewById(R.id.reset_progress_button);
        reset_progress.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG,"Reset progress clicked");
                ResetProgressDialog reset = new ResetProgressDialog();
                reset.show(getFragmentManager(),"hola");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        songle.importSongLyrics(songle.getSongs().getActiveSong().getNum(),songle.getSettings().getDifficulty());
        try {
            songle.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doPositiveClickResetBtn() {
        songle.resetProgress();
    }

    public void doNegativeClickResetBtn() {
    }
}
