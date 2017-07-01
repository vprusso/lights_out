package com.example.captainhampton.lightsout;

import android.content.Context;
import android.content.DialogInterface;
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
    int num_moves, min_num_moves, total_levels;
    long level_time;
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
        total_levels = Levels.getLevels(NUM_ROWS, NUM_COLS).length;

        sharedLevelPrefs = String.valueOf(NUM_ROWS) + "-" + String.valueOf(NUM_COLS) + "-" + String.valueOf(NUM_LEVEL);

        solver = new Solver(NUM_ROWS, NUM_COLS, NUM_LEVEL);
        setupVariables();
        initBoard();
        setupBoard();

    }

    private void loadSharedPreferences() {

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

    private void setupBoard() {

        clearBoard();
        clearSolution();

        for (int i = 0; i < Levels.getLevels(NUM_ROWS,NUM_COLS)[NUM_LEVEL].length; i++) {
            int x = Levels.getLevels(NUM_ROWS,NUM_COLS)[NUM_LEVEL][i][0];
            int y = Levels.getLevels(NUM_ROWS,NUM_COLS)[NUM_LEVEL][i][1];

            activateButton(x, y);
        }
        min_num_moves = findMinimumNumberOfMoves();
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
                            // Victory dance
                            displayVictoryDialogBox();

                        }

                    }
                });

                tableRowBoard.addView(button);
                lights[x][y] = button;
            }
        }
    }

    private int findMinimumNumberOfMoves() {
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
        int victoryIcon, randomMessageInteger = rand.nextInt(3) + 1;
        String victoryTitle, randomMessage, victoryType;

        if (randomMessageInteger == 1) {
            randomMessage = "Onto the next level?";
        } else if (randomMessageInteger == 2) {
            randomMessage = "Thirsty for more?";
        } else if (randomMessageInteger == 3) {
            randomMessage = "Onward to glory?";
        } else {
            randomMessage = "";
        }

        String victoryStatsMessage = "";

        String victoryMessage = "You just beat level " + NUM_LEVEL + " from the " +
                NUM_ROWS + "x" + NUM_COLS + " set of levels. \n\n" + randomMessage;

        if (num_moves == min_num_moves) {
            victoryTitle = "Perfect!";
            victoryType = "PERFECT";
            victoryIcon = android.R.drawable.btn_star;
        } else {
            victoryTitle = "Great job!";
            victoryType = "WIN";
            victoryIcon = android.R.drawable.btn_star;
        }

        alertDialogBuilder.setTitle(victoryTitle)
                .setMessage(victoryMessage)
                .setPositiveButton("Next Level", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
//                        if (NUM_LEVEL < total_levels-1) {
//                            NUM_LEVEL++;
//                            setLevel(NUM_LEVEL);
//                            setupBoard();
//                        } else {
//                            finish();
//                        }
                    }
                })
                .setNegativeButton("Play Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setLevel(NUM_LEVEL);
                        setupBoard();
                    }
                })
                .setIcon(victoryIcon)
                .show();
        saveUserLevelPreferences(victoryType);
    }

    private void saveUserLevelPreferences(String victoryType) {
        // TODO: Check to make sure you're not replacing a "WIN" victory with a "PERFECT" one.
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedLevelPrefs, victoryType);
        editor.apply();
        //getLevelSharedPreferences();
    }

    private void getLevelSharedPreferences() {
        for (int i = 3; i < 7; i++) {
            for (int j = 0; j < Levels.getLevels(i,i).length; j++) {
                String v = String.valueOf(i) + "-" + String.valueOf(i) + "-" + String.valueOf(j);
                String t = getSharedPreferences(Constants.SHARED_PREFS_FILE, MODE_PRIVATE).getString(v,"");
                Log.d("TAG", t);
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

    private void showHint() {
        boolean[][] solution = solver.calculateWinningConfig(light_states);

        overLoop:
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_COLS; j++) {
                    if (solution[i][j] == Boolean.TRUE) {
                        lights[i][j].setTextColor(Color.YELLOW);
                        break overLoop;
                    }
                }
            }
    }

    @Override
    public void onClick(View v) {

        if (buttonReset.isPressed()) {
            setupBoard();
        }

        if (buttonHint.isPressed()) {
            showHint();
        }

        if (buttonSolve.isPressed()) {
            showSolution();
        }

    }
}
