package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imgbtnInfo, imgbtnSettings, imgbtnMenu;
    private Button btnNewGame;
    public int[][] easyPuzzle = new int[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        imgbtnInfo = findViewById(R.id.imgbtnInfo);
        imgbtnMenu = findViewById(R.id.imgbtnMenu);
        imgbtnSettings = findViewById(R.id.imgbtnSettings);

        imgbtnInfo.setOnClickListener(new MainActivityButtonsListener());
        imgbtnSettings.setOnClickListener(new MainActivityButtonsListener());
        imgbtnMenu.setOnClickListener(new MainActivityButtonsListener());
        */

        btnNewGame = findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(this);

        //Sudoku Generator (Thread)
        System.out.println("Creating SudokuGenerator Class");
        final SudokuGenerator sudokuGenerator = new SudokuGenerator(this);

        Thread sudokuGeneratorThread = new Thread(new Runnable() {
            @Override
            public void run() {
               sudokuGenerator.puzzleGenerator(Globals.EASY);
               easyPuzzle = sudokuGenerator.getPuzzle();
            }
        });

        sudokuGeneratorThread.start();

    }

    public static void print2D(int mat[][])
    {
        for (int i = 0; i < mat.length; i++)
            for (int j = 0; j < mat[i].length; j++)
                System.out.print(mat[i][j] + " ");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNewGame) {
            SudokuPuzzle sudokuPuzzle = new SudokuPuzzle(Globals.EASY, easyPuzzle);
            int diff = sudokuPuzzle.getDifficulty();
            System.out.println(diff);
            print2D(sudokuPuzzle.getPuzzle());
            Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(gameIntent);
        }
    }
}


