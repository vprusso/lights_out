package com.example.captainhampton.lightsout;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Utils {

    Context context;

    public Utils(Context app_context) {
        context = app_context;
    }

    public void checkFirstRun() {

        // TODO : Comment this out:
        clearSharedPreferences();

        Boolean isFirstRun = context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE).getBoolean("IS_FIRST_RUN", true);

        if (isFirstRun) {
            Log.d("TAG", "First run");
            // If it is the first run, set the first run flag to false.
            context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE).edit().putBoolean("IS_FIRST_RUN", false).apply();

            initLevelSharedPreferences();
            initHintSharedPreferences();
            initSolutionSharedPreferences();

        } else {
            Log.d("TAG", "Not first run");
        }
    }

    public void clearSharedPreferences() {
        context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_FILE, 0).edit().clear().apply();
    }

    public void initLevelSharedPreferences() {
        // Levels start at 3x3 and go to 7x7
        for (int i = 3; i < 7; i++) {
            for (int j = 0; j < Levels.getLevels(i,i).length; j++) {
                String levelName = String.valueOf(i) + "-" + String.valueOf(i) + "-" + String.valueOf(j);
                context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE).edit().putString(levelName,"LOSE").apply();
                //Log.d("TAG", String.valueOf(i) + "-" + String.valueOf(i) + "-" + String.valueOf(j));
            }
        }
    }

    public void initHintSharedPreferences() {
        context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE).edit().putInt("NUM_HINTS", Constants.INIT_NUM_HINTS).apply();
    }

    public void initSolutionSharedPreferences() {
        context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE).edit().putInt("NUM_SOLUTIONS", Constants.INIT_NUM_SOLUTIONS).apply();
    }

    public String getLevelSharedPreferences(String sharedLevelPrefs) {
        return context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE).getString(sharedLevelPrefs, "");
    }

    public void saveUserLevelPreferences(String victoryType, String sharedLevelPrefs) {
        String previousVictoryType = getLevelSharedPreferences(sharedLevelPrefs);

        if (previousVictoryType.equals("LOSE") || previousVictoryType.equals("WIN")) {

            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(sharedLevelPrefs, victoryType);
            editor.apply();
        }
    }

    public int getHintSharedPreferences() {
        return context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE).getInt("NUM_HINTS", 0);
    }

    public int getSolutionSharedPreferences() {
        return context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE).getInt("NUM_SOLUTIONS", 0);
    }

    public int incrementHintSharedPreferences(int increment) {
        int num_hints = getHintSharedPreferences();
        num_hints += increment;
        saveHintSharedPreferences(num_hints);
        return num_hints;
    }

    public int incrementSolutionSharedPreferences(int increment) {
        int num_solutions = getSolutionSharedPreferences();
        num_solutions += increment;
        saveSolutionSharedPreferences(num_solutions);
        return num_solutions;
    }

    public int decrementSolutionSharedPreferences(int decrement) {
        int num_solutions = getSolutionSharedPreferences();
        num_solutions -= decrement;
        saveSolutionSharedPreferences(num_solutions);
        return num_solutions;
    }

    public int decrementHintSharedPreferences(int decrement) {
        int num_hints = getHintSharedPreferences();
        num_hints -= decrement;
        saveHintSharedPreferences(num_hints);
        return num_hints;
    }

    public void saveSolutionSharedPreferences(int solutions) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("NUM_SOLUTIONS", solutions);
        editor.apply();
    }

    public void saveHintSharedPreferences(int hints) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("NUM_HINTS", hints);
        editor.apply();
    }

}
