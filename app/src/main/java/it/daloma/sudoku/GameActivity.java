package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.Cell;
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
            int rowClicked = sudokuModel.getSelectedRow();
            int colClicked = sudokuModel.getSelectedCol();
            Cell selectedCell;
            final int index = rowClicked * (Globals.SIZE) + colClicked;

            switch (v.getId()) {
                case R.id.button1:
                    Log.w(TAG, "BUTTON 1 PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    else {
                        selectedCell.setValue(1);
                        Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                        sudokuBoardView.postInvalidate();
                        break;
                    }

                case R.id.button2:
                    Log.w(TAG, "BUTTON 2 PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    selectedCell.setValue(2);
                    Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                    sudokuBoardView.postInvalidate();
                    break;

                case R.id.button3:
                    Log.w(TAG, "BUTTON 3 PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    selectedCell.setValue(3);
                    Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                    sudokuBoardView.postInvalidate();
                    break;

                case R.id.button4:
                    Log.w(TAG, "BUTTON 4 PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    selectedCell.setValue(4);
                    Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                    sudokuBoardView.postInvalidate();
                    break;

                case R.id.button5:
                    Log.w(TAG, "BUTTON 5 PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    selectedCell.setValue(5);
                    Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                    sudokuBoardView.postInvalidate();
                    break;

                case R.id.button6:
                    Log.w(TAG, "BUTTON 6 PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    selectedCell.setValue(6);
                    Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                    sudokuBoardView.postInvalidate();
                    break;

                case R.id.button7:
                    Log.w(TAG, "BUTTON 7 PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    selectedCell.setValue(7);
                    Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                    sudokuBoardView.postInvalidate();
                    break;

                case R.id.button8:
                    Log.w(TAG, "BUTTON 8 PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    selectedCell.setValue(8);
                    Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                    sudokuBoardView.postInvalidate();
                    break;

                case R.id.button9:
                    Log.w(TAG, "BUTTON 9 PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    selectedCell.setValue(9);
                    Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                    sudokuBoardView.postInvalidate();
                    break;

                case R.id.buttonCancel:
                    Log.w(TAG, "BUTTON CANCEL PRESSED");
                    selectedCell = board.getCell(index);
                    if (selectedCell.isStartingCell()) {
                        Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                        break;
                    }
                    selectedCell.setValue(0);
                    Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                    sudokuBoardView.postInvalidate();
                    break;
            }

        }
    }

}
