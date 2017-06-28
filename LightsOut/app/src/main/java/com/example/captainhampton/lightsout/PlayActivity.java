package com.example.captainhampton.lightsout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class PlayActivity extends AppCompatActivity implements OnClickListener {

    static int NUM_ROWS, NUM_COLS, NUM_LEVEL;

    Button buttonHint, buttonReset, buttonSolve;
    Button[][] lights;
    TextView textViewNumMoves, textViewLevelTime;
    TableLayout tableLayoutBoard;
    AlertDialog.Builder alertDialogBuilder;

    boolean[][] light_states;
    int num_moves;
    long level_time; // System.nanoTime()
    private Solver solver;

    String sharedLevelPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level);

        NUM_ROWS = getIntent().getIntExtra("NUM_ROWS",0);
        NUM_COLS = getIntent().getIntExtra("NUM_COLS",0);
        NUM_LEVEL = getIntent().getIntExtra("NUM_LEVEL",0);

        lights = new Button[NUM_ROWS][NUM_COLS];
        light_states = new boolean[NUM_ROWS][NUM_COLS];
        sharedLevelPrefs = String.valueOf(NUM_ROWS) + "-" + String.valueOf(NUM_COLS) + "-" + String.valueOf(NUM_LEVEL);

        solver = new Solver(NUM_ROWS, NUM_COLS, NUM_LEVEL);
        setupVariables();
        initBoard();
        setupBoard();

    }

    private void setupVariables() {

        tableLayoutBoard = (TableLayout)findViewById(R.id.tableLayoutBoard);

        buttonReset = (Button)findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(this);

        buttonHint = (Button)findViewById(R.id.buttonHint);
        buttonHint.setOnClickListener(this);

        buttonSolve = (Button)findViewById(R.id.buttonSolve);
        buttonSolve.setOnClickListener(this);

        textViewNumMoves = (TextView)findViewById(R.id.textViewNumMoves);
        textViewLevelTime = (TextView)findViewById(R.id.textViewLevelTime);

    }

    private int findMinimumNumberOfMoves() {
        // TODO : Function that uses solver to determine least amount of moves for winning.
        // If this is satisfied, the color of the level should be different.
        // TODO fixme
        boolean[][] solution = solver.calculateWinningConfig(light_states);

        int minimumMoves = 0;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (solution[i][j] == Boolean.TRUE) {
                    minimumMoves += 1;
                }
            }
        }
        return minimumMoves;
    }

    private void displayVictoryDialogBox() {
        alertDialogBuilder = new AlertDialog.Builder(PlayActivity.this, android.R.style.Theme_Material_Dialog_Alert);

        Random rand = new Random();
        int randomMessageInteger = rand.nextInt(3) + 1;
        String randomMessage;

        if (randomMessageInteger == 1) {
            randomMessage = "Onto the next level?";
        } else if (randomMessageInteger == 2) {
            randomMessage = "Thirsty for more?";
        } else if (randomMessageInteger == 3) {
            randomMessage = "Onward to glory?";
        } else {
            randomMessage = "";
        }

        Log.d("TAG", "outcome = " + findMinimumNumberOfMoves());
        Log.d("TAG", "outcome = " + num_moves);


        if (num_moves == findMinimumNumberOfMoves()) {

            String victoryMessage = "You just beat level " + NUM_LEVEL + " from the " +
                    NUM_ROWS + "x" + NUM_COLS + " set of levels. \n\n" + randomMessage;

            alertDialogBuilder.setTitle("Perfect!")
                    .setMessage(victoryMessage)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NUM_LEVEL++;
                            if (NUM_LEVEL <= Levels.getLevels(NUM_ROWS, NUM_COLS).length) {
                                setLevel(NUM_LEVEL);
                                setupBoard();
                            } else {
                                // TODO
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.btn_star)
                    .show();

        } else {

            String victoryMessage = "You just beat level " + NUM_LEVEL + " from the " +
                    NUM_ROWS + "x" + NUM_COLS + " set of levels. \n\n" + randomMessage;

            alertDialogBuilder.setTitle("Great job!")
                    .setMessage(victoryMessage)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NUM_LEVEL++;
                            if (NUM_LEVEL <= Levels.getLevels(NUM_ROWS, NUM_COLS).length) {
                                setLevel(NUM_LEVEL);
                                setupBoard();
                            } else {
                                // TODO
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.btn_star_big_off)
                    .show();
        }
    }

    private void saveUserLevelPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("levelInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedLevelPrefs, "WIN");
        editor.apply();
    }

    private void initBoard() {


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
                            textViewNumMoves.setText(String.format(Locale.US, "%d", num_moves));
                            textViewLevelTime.setText(String.format(Locale.US, "%d", level_time));
                        }

                        if (checkVictory()) {
                            // TODO : victory dance
                            displayVictoryDialogBox();
                            saveUserLevelPreferences();
                        }

                    }
                });

                tableRowBoard.addView(button);
                lights[x][y] = button;

            }
        }

    }

    private void setLevel(int lvl) {
        NUM_LEVEL = lvl;
    }

    private void resetTimer() {
        level_time = 0;
        textViewLevelTime.setText(String.format(Locale.US, "%d", level_time));
    }

    private void resetNumMoves() {
        num_moves = 0;
        textViewNumMoves.setText(String.format(Locale.US, "%d", num_moves));
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

        for (int i = 0; i < Levels.getLevels(NUM_ROWS,NUM_COLS)[NUM_LEVEL].length; i++) {
            int x = Levels.getLevels(NUM_ROWS,NUM_COLS)[NUM_LEVEL][i][0];
            int y = Levels.getLevels(NUM_ROWS,NUM_COLS)[NUM_LEVEL][i][1];

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

        if (buttonSolve.isPressed()) {
            showSolution();
        }

    }
}
