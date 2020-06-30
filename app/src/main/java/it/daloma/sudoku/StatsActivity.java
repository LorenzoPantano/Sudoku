package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class StatsActivity extends AppCompatActivity {

    private static final String TAG = "STATS_ACTIVITY";
    private Button[] diffButtons = new Button[3];
    private ImageButton imgbtnBackStats;
    private static final int[] BUTTONS_ID = {
            R.id.btnEasy,
            R.id.btnMedium,
            R.id.btnHard
    };
    private int selectedButton = 0;
    StatsActivityClickListener statsActivityClickListener = new StatsActivityClickListener();
    SharedPreferences sharedPreferences;

    TextView tvGamesPlayedNumber, tvGamesWonNumber, tvWinRateNumber, tvBestTimeNumber, tvAverageTimeNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //Buttons
        for (int i = 0; i < 3; i++) {
            diffButtons[i] = findViewById(BUTTONS_ID[i]);
            diffButtons[i].setOnClickListener(statsActivityClickListener);
        }

        selectDifficulty();

        tvGamesPlayedNumber = findViewById(R.id.tvGamesPlayedNumber);
        tvGamesWonNumber = findViewById(R.id.tvGamesWonNumber);
        tvWinRateNumber = findViewById(R.id.tvWinRateNumber);
        tvBestTimeNumber = findViewById(R.id.tvBestTimeNumber);
        tvAverageTimeNumber = findViewById(R.id.tvAverageTimeNumber);

        imgbtnBackStats = findViewById(R.id.imgbtnBackStats);
        imgbtnBackStats.setOnClickListener(statsActivityClickListener);

        updateTextViews();

    }

    private void updateTextViews() {
        tvGamesPlayedNumber.setText(Integer.toString(sharedPreferences.getInt("Games Played", 0)));
        tvBestTimeNumber.setText(calculcateBestTime());
        tvAverageTimeNumber.setText(calculcateAverageTime());
    }

    private String calculcateAverageTime() {
        int gameNumber = sharedPreferences.getInt("Games Played", 0);
        ArrayList<Long> timesForDifficulty = new ArrayList<>();
        for (int game = gameNumber; game > 0 ; game--) {
            Log.d(TAG, "calculcateBestTime: GAME: " + game + " TIME MS: " + sharedPreferences.getLong("Game Time " + game, 0));
            timesForDifficulty.add(sharedPreferences.getLong("Game Time " + game, 0));
        }
        if (gameNumber == 0) {
            return "No games played";
        } else {
            long totalSumTimes = 0;
            for (long time: timesForDifficulty) {
                totalSumTimes = totalSumTimes + time;
            }
            long averageTime = totalSumTimes/timesForDifficulty.size();
            long minutes = (averageTime / 1000) / 60;
            long seconds = (averageTime / 1000) % 60;
            String averageTimeString = "";
            return averageTimeString + minutes + ":" + seconds;
        }
    }

    private String calculcateBestTime() {
        int gameNumber = sharedPreferences.getInt("Games Played", 0);
        ArrayList<Long> timesForDifficulty = new ArrayList<>();
        for (int game = gameNumber; game > 0 ; game--) {
            Log.d(TAG, "calculcateBestTime: GAME: " + game + " TIME MS: " + sharedPreferences.getLong("Game Time " + game, 0));
            timesForDifficulty.add(sharedPreferences.getLong("Game Time " + game, 0));
        }
        if (gameNumber == 0) {
            return "No games played";
        } else {
            Collections.sort(timesForDifficulty);
            long bestTime = timesForDifficulty.get(0);
            long minutes = (bestTime / 1000) / 60;
            long seconds = (bestTime / 1000) % 60;
            String bestTimeString = "";
            return bestTimeString + minutes + ":" + seconds;
        }
    }

    private void selectDifficulty() {
        switch (selectedButton) {
            case 0:
                //Shared Preferences
                sharedPreferences = getSharedPreferences("Game_EASY", MODE_PRIVATE);
                break;
            case 1:
                sharedPreferences = getSharedPreferences("Game_MEDIUM", MODE_PRIVATE);
                break;
            case 2:
                sharedPreferences = getSharedPreferences("Game_HARD", MODE_PRIVATE);
                break;
        }
    }

    private class StatsActivityClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnEasy:
                    if (selectedButton == 0) break;
                    else {
                        selectedButton = 0;
                        diffButtons[0].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_white));
                        diffButtons[0].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.pantone_classic_blue)));

                        for (int i = 1; i < 3; i++) {
                            diffButtons[i].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_black));
                            diffButtons[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.background_white)));
                        }
                        selectDifficulty();
                        updateTextViews();
                        break;
                    }

                case R.id.btnMedium:
                    if (selectedButton == 1) break;
                    else {
                        selectedButton = 1;
                        diffButtons[1].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_white));
                        diffButtons[1].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.pantone_classic_blue)));

                        for (int i = 0; i < 3; i++) {
                            if (i == 1) continue;
                            diffButtons[i].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_black));
                            diffButtons[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.background_white)));
                        }
                        selectDifficulty();
                        updateTextViews();
                        break;
                    }

                case R.id.btnHard:
                    if (selectedButton == 2) break;
                    else {
                        selectedButton = 2;
                        diffButtons[2].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_white));
                        diffButtons[2].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.pantone_classic_blue)));

                        for (int i = 0; i < 2; i++) {
                            diffButtons[i].setTextColor(ContextCompat.getColor(StatsActivity.this, R.color.background_black));
                            diffButtons[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(StatsActivity.this, R.color.background_white)));
                        }
                        selectDifficulty();
                        updateTextViews();
                        break;
                    }

                case R.id.imgbtnBackStats:
                    onBackPressed();
                    break;

            }
        }
    }
}
