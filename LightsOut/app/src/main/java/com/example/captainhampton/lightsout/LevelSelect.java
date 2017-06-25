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

    Button levelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        setupVariables();
        initLevelSelect();
    }

    private void setupVariables() {

        tableLayoutLevelSelect = (TableLayout)findViewById(R.id.tableLayoutLevelSelect);
        String level_dim_selected = getIntent().getStringExtra("LEVEL_DIM_ID");

        // Split NUM x NUM by "x" character to extract rows and cols.
        String[] level_dim_split = level_dim_selected.split("x");
        NUM_ROWS = Integer.parseInt(level_dim_split[0]);
        NUM_COLS = Integer.parseInt(level_dim_split[1]);

    }

    private void initLevelSelect() {
        int level_count = 0;

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
                levelButton = new Button(this);
                levelButton.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                levelButton.setText(Integer.toString(level_count));
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
        }

    }

    @Override
    public void onClick(View v) {

    }
}
