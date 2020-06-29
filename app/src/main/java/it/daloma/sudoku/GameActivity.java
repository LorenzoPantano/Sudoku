package it.daloma.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.Cell;
import it.daloma.sudoku.models.SudokuModel;
import it.daloma.sudoku.views.PausableChronometer;
import it.daloma.sudoku.views.SudokuBoardView;


public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GAME_ACTIVITY";
    PausableChronometer chronometer;
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

    ImageButton imgbtnEdit, imgbtnHint, imgbtnUndo, imgbtnBackGame, imgbtnValidate;
    View emptyView1, emptyView2, emptyView3; //Per gestire click fuori dalla board

    NumbersOnClickListener numbersOnClickListener;
    ActionOnClickListener actionOnClickListener;
    private Board board;
    private SudokuModel sudokuModel;
    private SudokuBoardView sudokuBoardView;
    private int difficulty;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private TextView tvDifficultyGame;
    private String difficultyString = "Null";
    private boolean editing = false;
    private boolean endingGame = false;
    private boolean gameWon = false;

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
            difficulty = gameIntent.getIntExtra("difficulty", -1);
        } else {
            board = gameIntent.getParcelableExtra("Board");
            sudokuModel = new SudokuModel(board, isNewGame);
            sudokuModel.getBoard().printBoard();
            sudokuBoardView = findViewById(R.id.sudokuView);
            sudokuBoardView.setSudokuModel(sudokuModel);
            difficulty = gameIntent.getIntExtra("difficulty", -1);
        }

        //Difficulty TextView
        switch (difficulty) {
            case 0:
                //Shared Preferences
                sharedPreferences = getSharedPreferences("Game_EASY", MODE_PRIVATE);
                difficultyString = "Easy";
                break;
            case 1:
                difficultyString = "Medium";
                sharedPreferences = getSharedPreferences("Game_MEDIUM", MODE_PRIVATE);
                break;
            case 2:
                difficultyString = "Hard";
                sharedPreferences = getSharedPreferences("Game_HARD", MODE_PRIVATE);
                break;
        }
        tvDifficultyGame = findViewById(R.id.tvDifficultyGame);
        tvDifficultyGame.setText(difficultyString);

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

        actionOnClickListener = new ActionOnClickListener();
        imgbtnEdit = findViewById(R.id.imgbtnEdit);
        imgbtnHint = findViewById(R.id.imgbtnHint);
        imgbtnUndo = findViewById(R.id.imgbtnUndo);
        imgbtnBackGame = findViewById(R.id.imgbtnBackGame);
        imgbtnValidate = findViewById(R.id.imgbtnValidate);
        imgbtnEdit.setOnClickListener(actionOnClickListener);
        imgbtnUndo.setOnClickListener(actionOnClickListener);
        imgbtnHint.setOnClickListener(actionOnClickListener);
        imgbtnBackGame.setOnClickListener(actionOnClickListener);
        imgbtnValidate.setOnClickListener(actionOnClickListener);

        emptyView1 = findViewById(R.id.emptyView1);
        emptyView2 = findViewById(R.id.emptyView2);
        emptyView3 = findViewById(R.id.emptyView3);
        emptyView1.setOnClickListener(actionOnClickListener);
        emptyView2.setOnClickListener(actionOnClickListener);
        emptyView3.setOnClickListener(actionOnClickListener);

        //Chronometer setup
        chronometer = findViewById(R.id.chronometer);
        chronometer.start();

        //Chronometer.setBase() può essere usato per impostare un tempo di partenza
        //da salvare dall'ultima partita


    }

    @Override
    protected void onPause() {
        //Save board
        saveState(board);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        //Save board
        //saveState(board);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void saveState(Board board) {
        if (endingGame) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Games Played", sharedPreferences.getInt("Games Played", 0) + 1);
            if (gameWon) {
                editor.putInt("Games Won", sharedPreferences.getInt("Games Won", 0) + 1);
            }
            editor.putInt("difficulty", -1);
            editor.apply();
        } else {
            editor = sharedPreferences.edit();
            Gson gson = new Gson();
            editor.putString("saved_board", gson.toJson(board));
            editor.putInt("difficulty", difficulty);
            chronometer.stop();
            //TODO: Salva tempo
            editor.apply();
        }

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

            if (rowClicked == -1 || colClicked == -1) return;

            Cell selectedCell;
            final int index = rowClicked * (Globals.SIZE) + colClicked;

            switch (v.getId()) {
                case R.id.button1:
                    Log.w(TAG, "BUTTON 1 PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 1);
                    break;

                case R.id.button2:
                    Log.w(TAG, "BUTTON 2 PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 2);
                    break;

                case R.id.button3:
                    Log.w(TAG, "BUTTON 3 PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 3);
                    break;

                case R.id.button4:
                    Log.w(TAG, "BUTTON 4 PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 4);
                    break;

                case R.id.button5:
                    Log.w(TAG, "BUTTON 5 PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 5);
                    break;

                case R.id.button6:
                    Log.w(TAG, "BUTTON 6 PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 6);
                    break;

                case R.id.button7:
                    Log.w(TAG, "BUTTON 7 PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 7);
                    break;

                case R.id.button8:
                    Log.w(TAG, "BUTTON 8 PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 8);
                    break;

                case R.id.button9:
                    Log.w(TAG, "BUTTON 9 PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 9);
                    break;

                case R.id.buttonCancel:
                    Log.w(TAG, "BUTTON CANCEL PRESSED");
                    selectedCell = board.getCell(index);
                    handleInput(rowClicked, colClicked, selectedCell, 0);
                    break;
            }

        }

        private void handleInput(int rowClicked, int colClicked, Cell selectedCell, int input) {
            if (!editing) {
                insertValueInCell(selectedCell, input);
                return;
            } else {
                addAnnotation(rowClicked, colClicked, selectedCell, input);
                return;
            }
        }

        private void addAnnotation(int rowClicked, int colClicked, Cell selectedCell, int value) {
            if (selectedCell.isStartingCell()) {
                Log.d(TAG, "addAnnotation: CAN'T EDIT STARTING CELL");
                return;
            }
            selectedCell.addAnnotation(value);
            selectedCell.setValue(0);
            Log.d(TAG, "onClick: ADDEDD ANNOTATION AT CELL: " + rowClicked + colClicked + " VALUE: " + selectedCell.getAnnotationIndex(value));
            sudokuBoardView.postInvalidate();
            return;
        }

        private void insertValueInCell(Cell selectedCell, int value) {
            if (selectedCell.isStartingCell()) {
                Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
                return;
            }
            else {
                selectedCell.setValue(value);
                selectedCell.clearAnnotations();
                Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                sudokuBoardView.postInvalidate();
                return;
            }
        }
    }

    private class ActionOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.imgbtnEdit:
                    Log.d(TAG, "onClick: EDITING");
                    if (!editing) {
                        imgbtnEdit.setBackground(getDrawable(R.drawable.action_buttons_pressed));
                        imgbtnEdit.setImageTintList(ColorStateList.valueOf(getColor(R.color.background_white)));
                        editing = true;
                        break;
                    } else {
                        imgbtnEdit.setBackground(getDrawable(R.drawable.action_buttons_non_pressed));
                        imgbtnEdit.setImageTintList(ColorStateList.valueOf(getColor(R.color.pantone_classic_blue)));
                        editing = false;
                        break;
                    }

                case R.id.imgbtnBackGame:
                    onBackPressed();
                    break;


                case R.id.imgbtnHint:
                    int selectedRow = sudokuModel.getSelectedRow();
                    int selectedCol = sudokuModel.getSelectedCol();

                    if (selectedRow == -1 || selectedCol == -1) {
                        Toast.makeText(GameActivity.this, "Choose a cell", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //TODO: Implement hint

                    break;

                case R.id.imgbtnValidate:
                    endGame();
                    break;

                case R.id.emptyView1:

                case R.id.emptyView3:

                case R.id.emptyView2:
                    handleOutsideClick();
                    break;

            }

        }

        public void handleOutsideClick() {
            sudokuModel.setSelectedCol(-1);
            sudokuModel.setSelectedRow(-1);
            sudokuBoardView.postInvalidate();
        }
    }

    private void endGame() {
        endingGame = true;
        onBackPressed();
    }
}
