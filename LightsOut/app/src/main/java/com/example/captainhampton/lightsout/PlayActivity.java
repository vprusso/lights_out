package com.example.captainhampton.lightsout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity implements OnClickListener {

    int NUM_ROWS = 4;
    int NUM_COLS = 4;

    Button buttonHome, buttonHint, buttonReset;
    Button[][] lights = new Button[NUM_ROWS][NUM_COLS];
    TextView textViewNumMoves, textViewLevelTime;

    boolean[][] light_states = new boolean[NUM_ROWS][NUM_COLS];
    int level_num = 0;
    int num_moves;
    long level_time; // System.nanoTime()
    private Solver solver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level);

        solver = new Solver(NUM_ROWS, NUM_COLS);
        setupVariables();

    }

    private void setupVariables() {

        TableLayout tableLayoutBoard = (TableLayout)findViewById(R.id.tableLayoutBoard);

        buttonHome = (Button)findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(this);

        buttonReset = (Button)findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(this);

        buttonHint = (Button)findViewById(R.id.buttonHint);
        buttonHint.setOnClickListener(this);

        textViewNumMoves = (TextView)findViewById(R.id.textViewNumMoves);
        textViewLevelTime = (TextView)findViewById(R.id.textViewLevelTime);

        for (int i = 0; i < NUM_ROWS; i++) {
            TableRow tableRowBoard = new TableRow(this);
            tableRowBoard.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            tableLayoutBoard.addView(tableRowBoard);
            for (int j = 0; j < NUM_COLS; j++) {

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

                        Button button = lights[x][y];

                        if (button.isPressed()) {
                            pressedLights(x, y);
                            clearSolution();
                            num_moves++;
                            textViewNumMoves.setText(String.format("%d", num_moves));
                            textViewLevelTime.setText(String.format("%d", level_time));
                        }

                        if (checkVictory()) {
                            // TODO : victory dance
                            level_num++;
                            if (level_num <= Levels.getLevels(NUM_ROWS, NUM_COLS).length) {
                                setLevel(level_num);
                                setupBoard();
                            } else {
                                // TODO
                            }
                        }

                    }
                });

                tableRowBoard.addView(button);
                lights[x][y] = button;

            }
        }

        setupBoard();


    }

    private void setLevel(int lvl) {
        level_num = lvl;
    }

    private void resetTimer() {
        level_time = 0;
        textViewLevelTime.setText(String.format("%d", level_time));
    }

    private void resetNumMoves() {
        num_moves = 0;
        textViewNumMoves.setText(String.format("%d", num_moves));
    }

    private void activateButton(int x, int y) {
        light_states[x][y] = Boolean.TRUE;
        lights[x][y].setBackgroundColor(Color.RED);
    }

    private void deactivateButton(int x, int y) {
        light_states[x][y] = Boolean.FALSE;
        lights[x][y].setBackgroundColor(Color.WHITE);
    }

    private void flipLight(int x, int y) {
        if (isLightActive(x, y))
            deactivateButton(x,y);
        else
            activateButton(x, y);
    }

    public boolean isLightOutOfBounds(int x, int y) {
        return ( x >= NUM_ROWS || x < 0 || y >= NUM_COLS || y < 0 );
    }

    private boolean isLightActive(int x, int y) {
        return ( light_states[x][y] == Boolean.TRUE );
    }

    private void pressedLights(int x, int y) {
        int top = x - 1;
        int bot = x + 1;
        int left = y - 1;
        int right = y + 1;

        flipLight(x,y);

        if (!isLightOutOfBounds(top, y))
            flipLight(top, y);


        if (!isLightOutOfBounds(bot, y))
            flipLight(bot, y);


        if (!isLightOutOfBounds(x, left))
            flipLight(x, left);


        if (!isLightOutOfBounds(x, right))
            flipLight(x, right);

    }

    private void clearBoard() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                deactivateButton(i, j);
            }
        }
        resetTimer();
        resetNumMoves();
    }

    private void setupBoard() {

        clearBoard();
        clearSolution();


        for (int i = 0; i < Levels.getLevels(NUM_ROWS,NUM_COLS)[level_num].length; i++) {
            int x = Levels.getLevels(NUM_ROWS,NUM_COLS)[level_num][i][0];
            int y = Levels.getLevels(NUM_ROWS,NUM_COLS)[level_num][i][1];

            activateButton(x, y);
        }
    }

    private boolean checkVictory() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (isLightActive(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void clearSolution() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                lights[i][j].setTextColor(Color.BLACK);
            }
        }
    }

    private void showSolution() {
        boolean[][] solution = solver.calculateWinningConfig(light_states);

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (solution[i][j] == Boolean.TRUE)
                    lights[i][j].setTextColor(Color.YELLOW);
            }
        }

    }

    @Override
    public void onClick(View v) {

        if (buttonReset.isPressed()) {
            setupBoard();
        }

        if (buttonHint.isPressed()) {
            showSolution();
        }

    }
}
