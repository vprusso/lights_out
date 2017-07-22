package com.example.captainhampton.lightsout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class HowToPlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        TextView textViewHowToPlayIntro = (TextView) findViewById(R.id.textViewHowToPlayIntro);
        TextView textViewHowToPlayParagraph = (TextView) findViewById(R.id.textViewHowToPlayParagraph);
        TextView textViewHowToPlayEnding = (TextView) findViewById(R.id.textViewHowToPlayEnding);

        textViewHowToPlayIntro.setText(getResources().getString(R.string.how_to_play_intro));


        textViewHowToPlayParagraph.setText(getResources().getString(R.string.how_to_play_paragraph));

        textViewHowToPlayEnding.setText(getResources().getString(R.string.how_to_play_ending));
    }

}
