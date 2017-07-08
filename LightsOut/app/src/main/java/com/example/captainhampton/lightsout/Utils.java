package com.example.captainhampton.lightsout;

import android.content.Context;
import android.util.Log;

public class Utils {

    Context context;

    public Utils(Context app_context) {
        context = app_context;
    }

    public void clearSharedPreferences() {
        context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_FILE, 0).edit().clear().apply();
    }

    public void checkFirstRun() {

        // TODO : Comment this out:
        clearSharedPreferences();

        Boolean isFirstRun = context.getSharedPreferences(Constants.SHARED_PREFS_FILE, context.MODE_PRIVATE).getBoolean("IS_FIRST_RUN", true);

        if (isFirstRun) {
            Log.d("TAG", "First run");
            // If it is the first run, set the first run flag to false.
            context.getSharedPreferences(Constants.SHARED_PREFS_FILE, context.MODE_PRIVATE).edit().putBoolean("IS_FIRST_RUN", false).apply();

            initLevelSharedPreferences();
            initHintSharedPreferences();

        } else {
            Log.d("TAG", "Not first run");
        }
    }

    public void initLevelSharedPreferences() {
        for (int i = 3; i < 7; i++) {
            for (int j = 0; j < Levels.getLevels(i,i).length; j++) {
                String levelName = String.valueOf(i) + "-" + String.valueOf(i) + "-" + String.valueOf(j);
                context.getSharedPreferences(Constants.SHARED_PREFS_FILE, context.MODE_PRIVATE).edit().putString(levelName,"LOSE").apply();
                //Log.d("TAG", String.valueOf(i) + "-" + String.valueOf(i) + "-" + String.valueOf(j));
            }
        }
    }

    public void initHintSharedPreferences() {
        context.getSharedPreferences(Constants.SHARED_PREFS_FILE, context.MODE_PRIVATE).edit().putInt("NUM_HINTS", Constants.INIT_NUM_HINTS).apply();
    }


}
