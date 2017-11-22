package com.example.maxgirkins.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class Guess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
    }

    private static final String[] COUNTRIES = new String[] {
            "Bohemian Rhapsody - Queen", "Born To be my baby - Bon Jovi", "Born to be a dancer - Kaiser Chiefs", "Both Sides Now - Joni Mitchell", "I'm Gonna be (500 miles) - The Proclaimers", "Baby one more time - Britney Spears", "Babel - Massive Attack"
    };

}
