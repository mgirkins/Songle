package com.example.maxgirkins.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameCompleted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_completed);
        setTitle("Congratulations!");
    }
}
