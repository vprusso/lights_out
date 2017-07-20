package com.example.captainhampton.lightsout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        displayBannerAd();

        TextView textViewAboutIntro = (TextView) findViewById(R.id.textViewAboutIntro);
        TextView textViewAboutParagraph = (TextView) findViewById(R.id.textViewAboutParagraph);
        TextView textViewAboutWebsite = (TextView) findViewById(R.id.textViewAboutWebsite);


        textViewAboutIntro.setText(getResources().getString(R.string.about_intro));

        textViewAboutParagraph.setText(getResources().getString(R.string.about_paragraph));

        textViewAboutWebsite.setText(getResources().getString(R.string.my_website));
    }

    public void displayBannerAd() {
        AdView adView = (AdView)findViewById(R.id.aboutAdView);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
    }

}
