package com.example.captainhampton.lightsout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class HowToPlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        TextView textViewHowToPlayIntro = (TextView) findViewById(R.id.textViewHowToPlayIntro);
        TextView textViewHowToPlayParagraph = (TextView) findViewById(R.id.textViewHowToPlayParagraph);
        TextView textViewHowToPlayEnding = (TextView) findViewById(R.id.textViewHowToPlayEnding);

        textViewHowToPlayIntro.setText("Lights Out  is a puzzle game that was originally " +
                                       "produced by Tiger Electronics in 1995. The game consists " +
                                       "of a grid of squares. Each level in the game consists of " +
                                       "some collection of squares in the grid that are either " +
                                       "in the 'on' or 'off' state. For instance, we may " +
                                       "consider the following level:");


        textViewHowToPlayParagraph.setText("where the red colored squares represent an 'on' " +
                                           "state and the white colored squares represent an " +
                                           "'off' state. When one presses a square, this toggles " +
                                           "the pressed square as well as the neighboring " +
                                           "squares to flip to their opposite state. For " +
                                           "instance, if we press the center square in the above " +
                                           "example this toggles the square itself as well as " +
                                           "the top, bottom, left, and right squares to toggle " +
                                           "off.");

        textViewHowToPlayEnding.setText("In this case, all squares are toggled in the 'off' " +
                                        "position. When all squares are in the 'off' state, " +
                                        "we refer to this as the winning condition of the game. " +
                                        "The aim of the game is to turn all the lights out!");

    }

}
