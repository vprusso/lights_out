package com.example.captainhampton.lightsout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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

    private void loadSharedPreferences() {

    }

    private void checkFirstRun() {

        final String PREFS_NAME = "UserPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        SharedPreferences.Editor editor = prefs.edit();

        // Check for first run or upgrade
        if (currentVersionCode >= savedVersionCode) {
            // This is just a normal run
            int num_hints = prefs.getInt("num_hints", 0);

            return;

        } else if (savedVersionCode == DOESNT_EXIST) {
            // This is a new install (or the user cleared the shared preferences)
            Log.d("TAG", "NEW INSTALL OR CLEARED SHARED PREFS");
            editor.putInt("num_hints", Constants.INIT_NUM_HINTS).apply();

        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    public void displayBannerAd() {
        AdView adView = (AdView)findViewById(R.id.mainAdView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        //AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
    }
}
