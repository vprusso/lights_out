package com.example.captainhampton.lightsout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LevelSelect extends AppCompatActivity implements View.OnClickListener {

    int NUM_ROWS, NUM_COLS, NUM_LEVEL;
    TableLayout tableLayoutLevelSelect;
    Button levelButton;
    TextView textViewLevelSelect;
    String sharedLevelPrefs;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        setupVariables();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initLevelSelectScreen();
        initLevelSelect();
        Log.d("TAG", "ONCREATE_LEVELSELECT");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        initLevelSelectScreen();
        initLevelSelect();
    }

    private void setupVariables() {

        tableLayoutLevelSelect = (TableLayout)findViewById(R.id.tableLayoutLevelSelect);
        String level_dim_selected = getIntent().getStringExtra("LEVEL_DIM_ID");

        // Split NUM x NUM by "x" character to extract rows and cols.
        String[] level_dim_split = level_dim_selected.split("x");
        NUM_ROWS = Integer.parseInt(level_dim_split[0]);
        NUM_COLS = Integer.parseInt(level_dim_split[1]);

        textViewLevelSelect = (TextView)findViewById(R.id.textViewLevelSelect);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        sharedLevelPrefs =  String.valueOf(NUM_ROWS) + "-" + String.valueOf(NUM_COLS) + "-" + String.valueOf(NUM_LEVEL);

    }

    private void initLevelSelectScreen() {
        textViewLevelSelect.setText("Level " + NUM_ROWS + "x" + NUM_COLS);
    }

    private void initLevelSelect() {
        int level_count = 0;
        int total_levels = Levels.getLevels(NUM_ROWS,NUM_COLS).length;

        int leftMargin=10;
        int topMargin=2;
        int rightMargin=10;
        int bottomMargin=100;

        for (int i = 0; i < total_levels; i++) {
            TableRow tableRowBoard = new TableRow(this);

            TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
            tableRowBoard.setLayoutParams(tableRowParams);

            tableLayoutLevelSelect.addView(tableRowBoard);

            levelButton = new Button(this);
            levelButton.setTextSize(20);
            levelButton.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            String level_button_text = level_count + " : " + NUM_ROWS + "x" + NUM_COLS;

            String victoryType = sharedPreferences.getString(String.valueOf(NUM_ROWS) + "-" + String.valueOf(NUM_COLS) + "-" + String.valueOf(level_count), "");
            Log.d(String.valueOf(level_count), victoryType);

            levelButton.setTag(Integer.toString(level_count));

            if (victoryType.equals("PERFECT")) {
                Log.d("TAG", "PERFECT");
                levelButton.setText(level_button_text + "PERFECT");
            } else if (victoryType.equals("WIN")) {
                levelButton.setText(level_button_text + "WIN!");
            } else {
                levelButton.setText(level_button_text);
            }

            levelButton.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
            levelButton.setTypeface(null, Typeface.BOLD);

            levelButton.setBackgroundResource(R.drawable.gradient_background);

            level_count += 1;

            // make text not clip on small buttons
            levelButton.setPadding(0, 0, 0, 0);
            levelButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //Log.d("TAG", "outcome = " + v.getTag().toString());
                    NUM_LEVEL = Integer.parseInt(v.getTag().toString());
                    Intent playLevelIntent = new Intent(LevelSelect.this, PlayActivity.class);
                    playLevelIntent.putExtra("NUM_ROWS", NUM_ROWS);
                    playLevelIntent.putExtra("NUM_COLS", NUM_COLS);
                    playLevelIntent.putExtra("NUM_LEVEL", NUM_LEVEL);
                    startActivity(playLevelIntent);
                }
            });

            tableRowBoard.addView(levelButton);
        }

    }

    private void loadUserLevelPreferences() {
        //SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        //String test1 = sharedPreferences.getString(sharedLevelPrefs, "");
        //Log.d("TAG", test1);
    }

    @Override
    public void onClick(View v) {

    }
}
