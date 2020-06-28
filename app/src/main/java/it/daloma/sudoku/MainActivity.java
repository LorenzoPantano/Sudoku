package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;

import java.time.LocalTime;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.utils.LoadingDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";
    private ImageButton imgbtnInfo, imgbtnSettings, imgbtnStats, imgbtnArrowLeft, imgbtnArrowRight;
    private Button btnNewGame, btnResume;
    private TextSwitcher textSwitcherDifficulty;
    private static final String[] difficulties = {"Easy", "Medium", "Hard"};
    private int selectedDifficulty = 0;  //Potrebbe essere preso da SharedPreferences salvando l'ultima partita
    private SudokuGenerator sudokuGenerator;
    private LoadingDialog loadingDialog;
    SharedPreferences sharedPreferencesEasy;
    SharedPreferences sharedPreferencesMedium;
    SharedPreferences sharedPreferencesHard;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load Settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        //Load shared prefs
        sharedPreferencesEasy = getSharedPreferences("Game_EASY", MODE_PRIVATE);
        sharedPreferencesMedium = getSharedPreferences("Game_MEDIUM", MODE_PRIVATE);
        sharedPreferencesHard = getSharedPreferences("Game_HARD", MODE_PRIVATE);
        selectSharedPrefs();

        //Buttons
        MainActivityButtonsListener mainActivityButtonsListener = new MainActivityButtonsListener();

        //Icons Buttons
        imgbtnInfo = findViewById(R.id.imgbtnInfo);
        imgbtnStats = findViewById(R.id.imgbtnStats);
        imgbtnSettings = findViewById(R.id.imgbtnSettings);
        imgbtnInfo.setOnClickListener(mainActivityButtonsListener);
        imgbtnSettings.setOnClickListener(mainActivityButtonsListener);
        imgbtnStats.setOnClickListener(mainActivityButtonsListener);

        //Arrows
        imgbtnArrowLeft = findViewById(R.id.imgbtnArrowLeft);
        imgbtnArrowRight = findViewById(R.id.imgbtnArrowRight);
        imgbtnArrowRight.setOnClickListener(mainActivityButtonsListener);
        imgbtnArrowLeft.setOnClickListener(mainActivityButtonsListener);

        //Game Buttons
        btnNewGame = findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(mainActivityButtonsListener);
        btnResume = findViewById(R.id.btnResume);
        btnResume.setOnClickListener(mainActivityButtonsListener);

        if (sharedPreferences.getInt("difficulty", -1) == -1) {
            makeInvisibleButton(btnResume);
        } else {
            makeVisibleButton(btnResume);
        }

        //Difficulty
        textSwitcherDifficulty = findViewById(R.id.textSwitcherDifficulty);
        textSwitcherDifficulty.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tvDifficulty = new TextView(MainActivity.this);
                tvDifficulty.setTextSize(18);
                tvDifficulty.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pantone_classic_blue));
                tvDifficulty.setGravity(Gravity.CENTER);
                Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.raleway_semibold);
                tvDifficulty.setTypeface(typeface);
                return tvDifficulty;
            }
        });
        textSwitcherDifficulty.setCurrentText(difficulties[selectedDifficulty]);
        Animation inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        inAnimation.setDuration(200);
        outAnimation.setDuration(200);
        textSwitcherDifficulty.setInAnimation(inAnimation);
        textSwitcherDifficulty.setOutAnimation(outAnimation);

        //SudokuGenerator
        loadingDialog = new LoadingDialog(MainActivity.this);
        sudokuGenerator = new SudokuGenerator(MainActivity.this);
    }

    private void selectSharedPrefs() {
        switch (selectedDifficulty) {
            case 0:
                sharedPreferences = sharedPreferencesEasy;
                break;
            case 1:
                sharedPreferences = sharedPreferencesMedium;
                break;
            case 2:
                sharedPreferences = sharedPreferencesHard;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences.getInt("difficulty", -1) == -1) {
            makeInvisibleButton(btnResume);
        } else {
            makeVisibleButton(btnResume);
        }
    }

    private void makeInvisibleButton(Button button) {
        button.setVisibility(View.INVISIBLE);
        button.setClickable(false);
    }

    private void makeVisibleButton(Button button) {
        button.setVisibility(View.VISIBLE);
        button.setClickable(true);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private class MainActivityButtonsListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnNewGame:
                    sudokuGenerator.puzzleGenerator(selectedDifficulty);
                    break;

                case R.id.btnResume:
                    Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                    Gson gson = new Gson();
                    String boardFromSharedPrefs = sharedPreferences.getString("saved_board", "DEFAULT");
                    Log.d(TAG, "onClick: FROM SHARED PREFS " + boardFromSharedPrefs);
                    Board board = gson.fromJson(boardFromSharedPrefs, Board.class);
                    gameIntent.putExtra("Board", board);
                    gameIntent.putExtra("new_game", false);
                    gameIntent.putExtra("difficulty", selectedDifficulty);
                    startActivity(gameIntent);
                    break;

                case R.id.imgbtnArrowLeft:
                    selectedDifficulty  = (selectedDifficulty - 1) % 3;
                    if (selectedDifficulty == -1) selectedDifficulty = 2;
                    textSwitcherDifficulty.setText(difficulties[selectedDifficulty]);
                    selectSharedPrefs();
                    if (sharedPreferences.getInt("difficulty", -1) == -1) {
                        makeInvisibleButton(btnResume);
                    } else {
                        makeVisibleButton(btnResume);
                    }
                    Log.d(TAG, "SELECTED DIFFICULTY: " + selectedDifficulty);
                    break;

                case R.id.imgbtnArrowRight:
                    selectedDifficulty  = (selectedDifficulty + 1) % 3;
                    textSwitcherDifficulty.setText(difficulties[selectedDifficulty]);
                    selectSharedPrefs();
                    if (sharedPreferences.getInt("difficulty", -1) == -1) {
                        makeInvisibleButton(btnResume);
                    } else {
                        makeVisibleButton(btnResume);
                    }
                    Log.d(TAG, "SELECTED DIFFICULTY: " + selectedDifficulty);
                    break;

                case R.id.imgbtnStats:
                    Intent statsIntent = new Intent(MainActivity.this, StatsActivity.class);
                    startActivity(statsIntent);
                    break;

                case R.id.imgbtnSettings:
                    Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(settingsIntent);
                    break;
            }
        }

    }

}

