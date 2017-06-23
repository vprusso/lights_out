package com.example.captainhampton.lightsout;

import android.content.Intent;
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
            Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);
            startActivity(playIntent);
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

    public void displayBannerAd() {
        AdView adView = (AdView)findViewById(R.id.mainAdView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        //AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
    }
}