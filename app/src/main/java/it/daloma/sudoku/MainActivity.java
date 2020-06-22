package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";
    private ImageButton imgbtnInfo, imgbtnSettings, imgbtnMenu, imgbtnArrowLeft, imgbtnArrowRight;
    private Button btnNewGame, btnResume;
    private TextView tvDifficulty;
    private TextSwitcher textSwitcherDifficulty;
    private static final String[] difficulties = {"Easy", "Medium", "Hard"};
    public int[][] easyPuzzle = new int[9][9];
    private int selectedDifficulty = 0;  //Potrebbe essere preso da SharedPreferences salvando l'ultima partita

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buttons
        MainActivityButtonsListener mainActivityButtonsListener = new MainActivityButtonsListener();

        //Icons Buttons
        imgbtnInfo = findViewById(R.id.imgbtnInfo);
        imgbtnMenu = findViewById(R.id.imgbtnMenu);
        imgbtnSettings = findViewById(R.id.imgbtnSettings);
        imgbtnInfo.setOnClickListener(mainActivityButtonsListener);
        imgbtnSettings.setOnClickListener(mainActivityButtonsListener);
        imgbtnMenu.setOnClickListener(mainActivityButtonsListener);

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
        tvDifficulty = findViewById(R.id.tvDifficultyGame);
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

        //Sudoku Generator (Thread)
        final SudokuGenerator sudokuGenerator = new SudokuGenerator(this);

        Thread sudokuGeneratorThread = new Thread(new Runnable() {
            @Override
            public void run() {
               sudokuGenerator.puzzleGenerator(selectedDifficulty);
               easyPuzzle = sudokuGenerator.getPuzzle();
            }
        });

    }

    public static void print2D(int[][] mat)
    {
        for (int[] ints : mat)
            for (int j = 0; j < ints.length; j++)
                System.out.print(ints[j] + " ");
    }

    private class MainActivityButtonsListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnNewGame:
                    SudokuPuzzle sudokuPuzzle = new SudokuPuzzle(Globals.EASY, easyPuzzle);
                    int diff = sudokuPuzzle.getDifficulty();
                    System.out.println(diff);
                    print2D(sudokuPuzzle.getPuzzle());
                    Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(gameIntent);
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
            }
        }
    }

}

