package com.example.captainhampton.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;

public class LevelSelect extends AppCompatActivity implements View.OnClickListener {

    int NUM_ROWS, NUM_COLS, NUM_LEVEL;
    TableLayout tableLayoutLevelSelect;
    Button levelButton;
    TextView textViewLevelSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        setupVariables();
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

    }

    private void initLevelSelectScreen() {
        textViewLevelSelect.setText("Level " + NUM_ROWS + "x" + NUM_COLS);
    }

    private void initLevelSelect() {
        int level_count = 0;
        int total_levels = Levels.getLevels(NUM_ROWS,NUM_COLS).length;

        for (int i = 0; i < total_levels; i++) {
            TableRow tableRowBoard = new TableRow(this);
            tableRowBoard.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            tableLayoutLevelSelect.addView(tableRowBoard);

            levelButton = new Button(this);
            levelButton.setTextSize(20);
            levelButton.setLayoutParams(new TableRow.LayoutParams(
                    250,//TableRow.LayoutParams.MATCH_PARENT,
                    250,//TableRow.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            String level_button_text = level_count + " : " + NUM_ROWS + "x" + NUM_COLS;
            levelButton.setText(level_button_text);
            levelButton.setTag(Integer.toString(level_count));
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

//        for (int i = 0; i < numLevelSelectRows; i++) {
//            TableRow tableRowBoard = new TableRow(this);
//            tableRowBoard.setLayoutParams(new TableLayout.LayoutParams(
//                    TableLayout.LayoutParams.MATCH_PARENT,
//                    TableLayout.LayoutParams.MATCH_PARENT,
//                    1.0f
//            ));
//
//            tableLayoutLevelSelect.addView(tableRowBoard);
//            for (int j = 0; j < numLevelSelectCols; j++) {
//
//                levelButton = new Button(this);
//                levelButton.setLayoutParams(new TableRow.LayoutParams(
//                        TableRow.LayoutParams.MATCH_PARENT,
//                        TableRow.LayoutParams.MATCH_PARENT,
//                        1.0f
//                ));
//
//                levelButton.setText(Integer.toString(level_count));
//                levelButton.setTag(Integer.toString(level_count));
//                level_count += 1;
//
//                // make text not clip on small buttons
//                levelButton.setPadding(0, 0, 0, 0);
//                //levelButton.setBackgroundResource(R.drawable.gradient_background);
//                levelButton.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        //Log.d("TAG", "outcome = " + v.getTag().toString());
//                        NUM_LEVEL = Integer.parseInt(v.getTag().toString());
//                        Intent playLevelIntent = new Intent(LevelSelect.this, PlayActivity.class);
//                        playLevelIntent.putExtra("NUM_ROWS", NUM_ROWS);
//                        playLevelIntent.putExtra("NUM_COLS", NUM_COLS);
//                        playLevelIntent.putExtra("NUM_LEVEL", NUM_LEVEL);
//                        startActivity(playLevelIntent);
//                    }
//                });
//
//                tableRowBoard.addView(levelButton);
//            }
//        }

    }

    @Override
    public void onClick(View v) {

    }
}
