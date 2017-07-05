package com.example.captainhampton.lightsout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button buttonPlay, buttonHowToPlay, buttonAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayBannerAd();
        setupVariables();

        checkFirstRun();

    }

    private void setupVariables() {
        buttonPlay = (Button)findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);

        buttonHowToPlay = (Button)findViewById(R.id.buttonHowToPlay);
        buttonHowToPlay.setOnClickListener(this);

        buttonAbout = (Button)findViewById(R.id.buttonAbout);
        buttonAbout.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (buttonPlay.isPressed()) {
            Intent levelDimSelectIntent = new Intent(MainActivity.this, LevelDimSelect.class);
            startActivity(levelDimSelectIntent);
        }

        if (buttonHowToPlay.isPressed()) {
            Intent howToPlayIntent = new Intent(MainActivity.this, HowToPlayActivity.class);
            startActivity(howToPlayIntent);
        }

        if (buttonAbout.isPressed()) {
            Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        }
    }

    private void clearSharedPreferences() {
        getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_FILE, 0).edit().clear().apply();
    }

    private void checkFirstRun() {

        // TODO : Comment this out:
        clearSharedPreferences();

        Boolean isFirstRun = getSharedPreferences(Constants.SHARED_PREFS_FILE, MODE_PRIVATE).getBoolean("IS_FIRST_RUN", true);

        if (isFirstRun) {
            Log.d("TAG", "First run");
            // If it is the first run, set the first run flag to false.
            getSharedPreferences(Constants.SHARED_PREFS_FILE, MODE_PRIVATE).edit().putBoolean("IS_FIRST_RUN", false).apply();

            initLevelSharedPreferences();
            initHintSharedPreferences();

        } else {
            Log.d("TAG", "Not first run");
        }

    }

    private void initLevelSharedPreferences() {
        for (int i = 3; i < 7; i++) {
            for (int j = 0; j < Levels.getLevels(i,i).length; j++) {
                String levelName = String.valueOf(i) + "-" + String.valueOf(i) + "-" + String.valueOf(j);
                getSharedPreferences(Constants.SHARED_PREFS_FILE, MODE_PRIVATE).edit().putString(levelName,"LOSE").apply();
                //Log.d("TAG", String.valueOf(i) + "-" + String.valueOf(i) + "-" + String.valueOf(j));
            }
        }
    }

    private void initHintSharedPreferences() {
        getSharedPreferences(Constants.SHARED_PREFS_FILE, MODE_PRIVATE).edit().putInt("NUM_HINTS", Constants.INIT_NUM_HINTS).apply();
    }

    public void displayBannerAd() {
        AdView adView = (AdView)findViewById(R.id.mainAdView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        //AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
    }
}
