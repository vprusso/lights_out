package com.example.captainhampton.lightsout;

import android.content.Intent;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    Utils utils;
    CircleButton circleButtonPlay, circleButtonHowToPlay, circleButtonAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayBannerAd();
        setupVariables();

        utils.checkFirstRun();
    }

    private void setupVariables() {

        circleButtonPlay = (CircleButton)findViewById(R.id.circleButtonPlay);
        circleButtonPlay.setOnClickListener(this);

        circleButtonHowToPlay = (CircleButton)findViewById(R.id.circleButtonHowToPlay);
        circleButtonHowToPlay.setOnClickListener(this);

        circleButtonAbout = (CircleButton)findViewById(R.id.circleButtonAbout);
        circleButtonAbout.setOnClickListener(this);

        utils = new Utils(this);
    }

    public void onClick(View v) {

        if (circleButtonPlay.isPressed()) {
            Intent levelDimSelectIntent = new Intent(MainActivity.this, LevelDimSelect.class);
            startActivity(levelDimSelectIntent);
        }

        if (circleButtonHowToPlay.isPressed()) {
            Intent howToPlayIntent = new Intent(MainActivity.this, HowToPlayActivity.class);
            startActivity(howToPlayIntent);
        }

        if (circleButtonAbout.isPressed()) {
            Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        }
    }

    public void displayBannerAd() {
        AdView adView = (AdView)findViewById(R.id.mainAdView);
        //AdView adView = new AdView(this);
        //adView.setAdSize(AdSize.BANNER);
        //adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
    }
}
