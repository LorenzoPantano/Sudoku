package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ViewSwitcher;

import it.daloma.sudoku.utils.LoadingDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";
    private ImageButton imgbtnInfo, imgbtnSettings, imgbtnStats, imgbtnArrowLeft, imgbtnArrowRight;
    private Button btnNewGame, btnResume;
    private TextSwitcher textSwitcherDifficulty;
    private static final String[] difficulties = {"Easy", "Medium", "Hard"};
    public int[][] puzzle = new int[9][9];
    private int selectedDifficulty = 0;  //Potrebbe essere preso da SharedPreferences salvando l'ultima partita
    private SudokuGenerator sudokuGenerator;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        sudokuGenerator = new SudokuGenerator(this);
        loadingDialog = new LoadingDialog(MainActivity.this);
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

                case R.id.imgbtnArrowLeft:
                    selectedDifficulty  = (selectedDifficulty - 1) % 3;
                    if (selectedDifficulty == -1) selectedDifficulty = 2;
                    textSwitcherDifficulty.setText(difficulties[selectedDifficulty]);
                    Log.d(TAG, "SELECTED DIFFICULTY: " + selectedDifficulty);
                    break;

                case R.id.imgbtnArrowRight:
                    selectedDifficulty  = (selectedDifficulty + 1) % 3;
                    textSwitcherDifficulty.setText(difficulties[selectedDifficulty]);
                    Log.d(TAG, "SELECTED DIFFICULTY: " + selectedDifficulty);
                    break;

                case R.id.imgbtnStats:
                    Intent statsIntent = new Intent(MainActivity.this, StatsActivity.class);
                    startActivity(statsIntent);
            }
        }

    }

}

