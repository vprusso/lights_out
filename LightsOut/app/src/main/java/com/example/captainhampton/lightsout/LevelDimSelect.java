package com.example.captainhampton.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import at.markushi.ui.CircleButton;


public class LevelDimSelect extends AppCompatActivity implements View.OnClickListener {

    CircleButton buttonSelect3x3, buttonSelect4x4, buttonSelect5x5, buttonSelect6x6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_dim_select);

        setupVariables();

    }

    private void setupVariables() {
        buttonSelect3x3 = (CircleButton)findViewById(R.id.buttonSelect3x3);
        buttonSelect3x3.setOnClickListener(this);

        buttonSelect4x4 = (CircleButton)findViewById(R.id.buttonSelect4x4);
        buttonSelect4x4.setOnClickListener(this);

        buttonSelect5x5 = (CircleButton)findViewById(R.id.buttonSelect5x5);
        buttonSelect5x5.setOnClickListener(this);

        buttonSelect6x6 = (CircleButton)findViewById(R.id.buttonSelect6x6);
        buttonSelect6x6.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent intentMainActivity = new Intent(LevelDimSelect.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }


    @Override
    public void onClick(View v) {

        if (buttonSelect3x3.isPressed()) {
            Intent select3x3Intent = new Intent(LevelDimSelect.this, LevelSelect.class);
            select3x3Intent.putExtra("LEVEL_DIM_ID", "3x3");
            startActivity(select3x3Intent);
        }

        if (buttonSelect4x4.isPressed()) {
            Intent select4x4Intent = new Intent(LevelDimSelect.this, LevelSelect.class);
            select4x4Intent.putExtra("LEVEL_DIM_ID", "4x4");
            startActivity(select4x4Intent);
        }

        if (buttonSelect5x5.isPressed()) {
            Intent select5x5Intent = new Intent(LevelDimSelect.this, LevelSelect.class);
            select5x5Intent.putExtra("LEVEL_DIM_ID", "5x5");
            startActivity(select5x5Intent);
        }

        if (buttonSelect6x6.isPressed()) {
            Intent select6x6Intent = new Intent(LevelDimSelect.this, LevelSelect.class);
            select6x6Intent.putExtra("LEVEL_DIM_ID", "6x6");
            startActivity(select6x6Intent);
        }

    }
}
