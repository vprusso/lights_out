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

    int numLevelSelectRows = 3;
    int numLevelSelectCols = 3;

    TableLayout tableLayoutLevelSelect;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        setupVariables();
        initLevelSelect();

//        Log.d("TAG", "outcome = " + Levels.getLevels(NUM_ROWS, NUM_COLS).length);

    }

    private void setupVariables() {

        tableLayoutLevelSelect = (TableLayout)findViewById(R.id.tableLayoutLevelSelect);
        String level_dim_selected = getIntent().getStringExtra("LEVEL_DIM_ID");

        // Split NUM x NUM by "x" character to extract rows and cols.
        String[] level_dim_split = level_dim_selected.split("x");
        NUM_ROWS = Integer.parseInt(level_dim_split[0]);
        NUM_COLS = Integer.parseInt(level_dim_split[1]);
        NUM_LEVEL = 0;

    }

    private void initLevelSelect() {
        for (int i = 0; i < numLevelSelectRows; i++) {
            TableRow tableRowBoard = new TableRow(this);
            tableRowBoard.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            tableLayoutLevelSelect.addView(tableRowBoard);
            for (int j = 0; j < numLevelSelectCols; j++) {

                final int x = i;
                final int y = j;
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                button.setText("" + x + "," + y);
                // make text not clip on small buttons
                button.setPadding(0, 0, 0, 0);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent levelDimSelectIntent = new Intent(LevelSelect.this, PlayActivity.class);
                        //levelDimSelectIntent.putExtra("NUM_ROWS", NUM_ROWS);
                        //levelDimSelectIntent.putExtra("NUM_COLS", NUM_COLS);
                        //levelDimSelectIntent.putExtra("NUM_LEVEL", NUM_LEVEL);
                        startActivity(levelDimSelectIntent);
                    }
                });

                tableRowBoard.addView(button);
            }
        }

    }

    @Override
    public void onClick(View v) {

    }
}
