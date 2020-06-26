package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.SudokuModel;
import it.daloma.sudoku.view.SudokuBoardView;


public class GameActivity extends AppCompatActivity {

    //Controls Buttons
    Button[] buttons = new Button[10];
    private static final int[] BUTTON_IDS = {
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.buttonCancel
    };

    NumbersOnClickListener numbersOnClickListener;
    private Board board;
    private SudokuModel sudokuModel;
    private SudokuBoardView sudokuBoardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /*
        * NEW GAME
        *
        * Board Setup
        * */
        Intent gameIntent = getIntent();
        boolean isNewGame = gameIntent.getBooleanExtra("new game", false);
        if (isNewGame) {
            board = gameIntent.getParcelableExtra("Board");
            sudokuModel = new SudokuModel(board, isNewGame);
            sudokuModel.getBoard().printBoard();
            sudokuBoardView = findViewById(R.id.sudokuView);
            sudokuBoardView.setSudokuModel(sudokuModel);
        }

        //Controls Setup
        numbersOnClickListener = new NumbersOnClickListener();
        for (int i = 0; i < 10; i++) {
            buttons[i] = findViewById(BUTTON_IDS[i]);
            buttons[i].setOnClickListener(numbersOnClickListener);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) buttons[i].getLayoutParams();
            params.width = getResources().getDisplayMetrics().widthPixels / 7;
            params.height = params.width;
            buttons[i].setLayoutParams(params);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class NumbersOnClickListener implements View.OnClickListener {


        private static final String TAG = "NUMBERS_ONCLICK";

        public NumbersOnClickListener() {

        }

        @Override
        public void onClick(View v) {

            Log.w(TAG, "onClick: Clicked");

            switch (v.getId()) {
                case R.id.button1:
                    Log.w(TAG, "BUTTON 1 PRESSED");
                    break;

                case R.id.button2:
                    Log.w(TAG, "BUTTON 2 PRESSED");
                    break;

                case R.id.button3:
                    Log.w(TAG, "BUTTON 3 PRESSED");
                    break;

                case R.id.button4:
                    Log.w(TAG, "BUTTON 4 PRESSED");
                    break;

                case R.id.button5:
                    Log.w(TAG, "BUTTON 5 PRESSED");
                    break;

                case R.id.button6:
                    Log.w(TAG, "BUTTON 6 PRESSED");
                    break;

                case R.id.button7:
                    Log.w(TAG, "BUTTON 7 PRESSED");
                    break;

                case R.id.button8:
                    Log.w(TAG, "BUTTON 8 PRESSED");
                    break;

                case R.id.button9:
                    Log.w(TAG, "BUTTON 9 PRESSED");
                    break;

                case R.id.buttonCancel:
                    Log.w(TAG, "BUTTON CANCEL PRESSED");
                    break;
            }

        }
    }

}
