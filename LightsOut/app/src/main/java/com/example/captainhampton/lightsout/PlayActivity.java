package com.example.captainhampton.lightsout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Locale;
import java.util.Random;

public class PlayActivity extends AppCompatActivity implements OnClickListener {

    static int NUM_ROWS, NUM_COLS, NUM_LEVEL;

    Button buttonHint, buttonReset, buttonSolve;
    Button[][] lights;
    TextView textViewNumMoves, textViewLevelTitle;
    TableLayout tableLayoutBoard;
    AlertDialog.Builder alertDialogBuilder;

    boolean[][] lightStates;
    boolean showSolutionFlag;
    int numMoves, minNumMoves, totalLevels, numHints, numSolutions;
    String sharedLevelPrefs;

    private Solver solver;
    private Utils utils;

    private InterstitialAd mInterstitial;
    private AdRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level);

        NUM_ROWS = getIntent().getIntExtra("NUM_ROWS", 0);
        NUM_COLS = getIntent().getIntExtra("NUM_COLS", 0);
        NUM_LEVEL = getIntent().getIntExtra("NUM_LEVEL", 0);

        lights = new Button[NUM_ROWS][NUM_COLS];
        lightStates = new boolean[NUM_ROWS][NUM_COLS];
        totalLevels = Levels.getLevels(NUM_ROWS, NUM_COLS).length;

        showSolutionFlag = false;

        sharedLevelPrefs = String.valueOf(NUM_ROWS) + "-" + String.valueOf(NUM_COLS) + "-" + String.valueOf(NUM_LEVEL);

        solver = new Solver(NUM_ROWS, NUM_COLS, NUM_LEVEL);
        utils = new Utils(this);

        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        // Set an AdListener.
        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });

        //request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        request = new AdRequest.Builder().build();
        mInterstitial.loadAd(request);

        setupVariables();
        initBoard();
        setupBoard();

        minNumMoves = findMinimumNumberOfMoves();

    }

    private void setupVariables() {

        numHints = utils.getHintSharedPreferences();
        numSolutions = utils.getSolutionSharedPreferences();

        tableLayoutBoard = (TableLayout)findViewById(R.id.tableLayoutBoard);

        buttonReset = (Button)findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(this);

        buttonHint = (Button)findViewById(R.id.buttonHint);
        buttonHint.setOnClickListener(this);
        buttonHint.setText("Hints(" + String.valueOf(numHints) + ")");

        buttonSolve = (Button)findViewById(R.id.buttonSolve);
        buttonSolve.setOnClickListener(this);
        buttonSolve.setText("Solve(" + String.valueOf(numSolutions) + ")");

        textViewNumMoves = (TextView)findViewById(R.id.textViewNumMoves);
        textViewLevelTitle = (TextView)findViewById(R.id.textViewLevelTitle);
        textViewLevelTitle.setText(String.valueOf(NUM_LEVEL));
    }

    private void setupBoard() {

        clearBoard();

        for (int i = 0; i < Levels.getLevels(NUM_ROWS,NUM_COLS)[NUM_LEVEL].length; i++) {
            int x = Levels.getLevels(NUM_ROWS,NUM_COLS)[NUM_LEVEL][i][0];
            int y = Levels.getLevels(NUM_ROWS,NUM_COLS)[NUM_LEVEL][i][1];

            activateButton(x, y);
        }
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
                Button boardButton = new Button(this);
                boardButton.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));
                boardButton.setBackgroundResource(R.drawable.gradient_background);
                // make text not clip on small buttons
                boardButton.setPadding(0, 0, 0, 0);
                boardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Button button = lights[x][y];

                        if (button.isPressed()) {
                            pressedLights(x, y);
                            numMoves++;
                            textViewNumMoves.setText(String.format(Locale.US, "%d", numMoves));
                        }

                        if (checkVictory()) {
                            // Victory dance
                            displayVictoryDialogBox();

                        }

                    }
                });

                tableRowBoard.addView(boardButton);
                lights[x][y] = boardButton;
            }
        }
    }

    private int findMinimumNumberOfMoves() {
        boolean[][] solution = solver.calculateWinningConfig(lightStates);

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

    private void resetNumMoves() {
        numMoves = 0;
        textViewNumMoves.setText(String.format(Locale.US, "%d", numMoves));
    }

    private void activateButton(int x, int y) {
        lightStates[x][y] = Boolean.TRUE;
        lights[x][y].setBackgroundResource(R.drawable.light_on);
    }

    private void deactivateButton(int x, int y) {
        lightStates[x][y] = Boolean.FALSE;
        lights[x][y].setBackgroundResource(R.drawable.light_off);
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
        return ( lightStates[x][y] == Boolean.TRUE );
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

        if (showSolutionFlag)
            showSolution();
    }

    private void clearBoard() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                deactivateButton(i, j);
            }
        }
        resetNumMoves();
        showSolutionFlag = false;
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

    private void showSolution() {
        boolean[][] solution = solver.calculateWinningConfig(lightStates);
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (solution[i][j] == Boolean.TRUE) {
                    lights[i][j].setBackgroundResource(R.drawable.light_hint);
                }
            }
        }
    }

    private void showHint() {
        boolean[][] solution = solver.calculateWinningConfig(lightStates);

        overLoop:
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_COLS; j++) {
                    if (solution[i][j] == Boolean.TRUE) {
                        lights[i][j].setBackgroundResource(R.drawable.light_hint);
                        break overLoop;
                    }
                }
            }
    }

    @Override
    public void onBackPressed() {
        Intent levelDimSelectIntent = new Intent(PlayActivity.this, LevelDimSelect.class);
        startActivity(levelDimSelectIntent);
        finish();
    }

    public void goToNextLevel() {
        if (NUM_LEVEL < totalLevels - 1) {
            Intent playLevelIntent = new Intent(PlayActivity.this, PlayActivity.class);
            playLevelIntent.putExtra("NUM_ROWS", NUM_ROWS);
            playLevelIntent.putExtra("NUM_COLS", NUM_COLS);
            playLevelIntent.putExtra("NUM_LEVEL", NUM_LEVEL + 1);
            startActivity(playLevelIntent);
        } else {
            Intent levelDimSelectIntent = new Intent(PlayActivity.this, LevelDimSelect.class);
            startActivity(levelDimSelectIntent);
            finish();
        }
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

        String victoryMessage = "You just beat level " + NUM_LEVEL + " from the " +
                NUM_ROWS + "x" + NUM_COLS + " set of levels. \n\n" + randomMessage;

        if (numMoves == minNumMoves) {
            victoryTitle = "Perfect!";
            victoryType = "PERFECT";
            victoryIcon = android.R.drawable.btn_star;
            // Make sure that you can't just beat the same level over and over to boost hints.
            if (utils.getLevelSharedPreferences(sharedLevelPrefs).equals("WIN") || utils.getLevelSharedPreferences(sharedLevelPrefs).equals("LOSE")) {
                numHints = utils.incrementHintSharedPreferences(Constants.PERFECT_HINT_INCREMENT);
                numSolutions = utils.incrementSolutionSharedPreferences(Constants.PERFECT_SOLUTION_INCREMENT);
            }
        } else {
            victoryTitle = "Great job!";
            victoryType = "WIN";
            victoryIcon = android.R.drawable.btn_star;
        }
        utils.saveUserLevelPreferences(victoryType, sharedLevelPrefs);

        alertDialogBuilder.setTitle(victoryTitle)
                .setMessage(victoryMessage)
                .setPositiveButton("Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent levelDimSelectIntent = new Intent(PlayActivity.this, LevelDimSelect.class);
                        startActivity(levelDimSelectIntent);
                        finish();

                    }
                })
                .setNegativeButton("Next Level", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       if (mInterstitial.isLoaded()) {
                           mInterstitial.show();
                           // Once ad closes, it proceeds to next level.
                       }
                    }
                })
                .setIcon(victoryIcon)
                .show();
    }

    @Override
    public void onClick(View v) {

        if (buttonReset.isPressed()) {
            setupBoard();
        }

        if (buttonHint.isPressed()) {
            if (numHints > 0) {
                showHint();
                numHints = utils.decrementHintSharedPreferences(1);
                buttonHint.setText("Hints(" + String.valueOf(numHints) + ")");
            } else {
                Toast.makeText(getApplicationContext(), "No more hints!", Toast.LENGTH_SHORT).show();
            }
        }

        if (buttonSolve.isPressed()) {
            if (numSolutions > 0) {
                showSolutionFlag = true;
                showSolution();
                numSolutions = utils.decrementSolutionSharedPreferences(1);
                buttonSolve.setText("Solve(" + String.valueOf(numSolutions) + ")");
            } else {
                Toast.makeText(getApplicationContext(), "No more solutions!", Toast.LENGTH_SHORT).show();

            }
        }

    }
}
