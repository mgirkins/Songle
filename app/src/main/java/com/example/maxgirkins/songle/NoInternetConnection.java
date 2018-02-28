package com.example.maxgirkins.songle;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.maxgirkins.songle.Songle.songle;

public class NoInternetConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("No Internet Connection!");
        setContentView(R.layout.activity_no_internet_connection);
        draw();
    }
    //override back button so user cn't go to main screen without internet connection
    @Override
    public void onBackPressed() {
    }

    private void draw() {
        Button try_again = findViewById(R.id.no_internet_try_again_btn);
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternet(getApplicationContext())) {
                    songle.onCreate();
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry, still no internet :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Boolean isInternet(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
