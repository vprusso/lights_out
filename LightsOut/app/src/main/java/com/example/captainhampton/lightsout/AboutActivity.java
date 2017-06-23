package com.example.captainhampton.lightsout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView textViewAboutIntro = (TextView) findViewById(R.id.textViewAboutIntro);
        TextView textViewAboutParagraph = (TextView) findViewById(R.id.textViewAboutParagraph);
        TextView textViewAboutWebsite = (TextView) findViewById(R.id.textViewAboutWebsite);


        textViewAboutIntro.setText("Hey! Thanks for downloading my app.");

        textViewAboutParagraph.setText("I'm just one guy who does this " +
                "for kicks on the weekends, and it's been a really fun project to work on! " +
                "Feel free to reach out to me at the link below, and thanks again for " +
                "downloading!");

        textViewAboutWebsite.setText("http://vprusso.github.io/");

    }

}
