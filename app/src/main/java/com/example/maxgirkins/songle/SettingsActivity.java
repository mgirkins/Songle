package com.example.maxgirkins.songle;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import static com.example.maxgirkins.songle.Songle.songle;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
