package it.daloma.sudoku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.Cell;
import it.daloma.sudoku.models.SudokuModel;
import it.daloma.sudoku.utils.Utils;
import it.daloma.sudoku.views.PausableChronometer;
import it.daloma.sudoku.views.SudokuBoardView;


public class GameActivity extends AppCompatActivity {

    //Logcat
    private static final String TAG = "GAME_ACTIVITY";


    //Controls Buttons
    Button[] buttons = new Button[10];
    ImageButton imgbtnEdit, imgbtnHint, imgbtnBackGame, imgbtnValidate;
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

    //Views
    PausableChronometer chronometer;
    View emptyView1, emptyView2, emptyView3; //Per gestire click fuori dalla board
    private TextView tvDifficultyGame;

    //Click listeners
    NumbersOnClickListener numbersOnClickListener;
    ActionOnClickListener actionOnClickListener;

    //Shared preferences
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesSettings;
    SharedPreferences.Editor editor;

    //Attributes
    private Board board;
    private int[] monoSolvedBoard;
    private int[][] solvedBoard;
    private SudokuModel sudokuModel;
    private SudokuBoardView sudokuBoardView;
    private int difficulty;
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
        *
        * La board (generata dal generator e non) viene passata all'activity tramite
        * Intent. Nella onCreate() della activity infatti viene instanziato un Model
        * usando la board passata, si prende poi la difficoltà (anch'essa tramite intent)
        * e la soluzione del puzzle. Quest'ultima sottoforma di array monodimensionale
        * (che si può passare più facilmente tramite intent) che poi verrà riconvertito a
        * matrice 9X9.
        * */
        Intent gameIntent = getIntent();
        Gson gson = new Gson();
        boolean isNewGame = gameIntent.getBooleanExtra("new game", false);
        board = gameIntent.getParcelableExtra("Board");
        sudokuModel = new SudokuModel(board);
        sudokuBoardView = findViewById(R.id.sudokuView);
        sudokuBoardView.setSudokuModel(sudokuModel);
        difficulty = gameIntent.getIntExtra("difficulty", -1);
        monoSolvedBoard = gameIntent.getIntArrayExtra("Solved board");
        solvedBoard = Utils.convertMonoToBidimensionalArray(monoSolvedBoard);

        /*
        * DIFFICULTY
        *
        * In base alla difficoltà raccolta nell'Intent, si sceglie quale delle
        * shared preferences usare. Ce ne sono tre in
        * modo da poter salvare i dati di massimo una partita per ogni difficoltà.
        * */
        switch (difficulty) {
            case 0:
                sharedPreferences = getSharedPreferences("Game_EASY", MODE_PRIVATE);
                difficultyString = getResources().getString(R.string.difficulty_Easy);
                break;
            case 1:
                difficultyString = getResources().getString(R.string.difficulty_Medium);
                sharedPreferences = getSharedPreferences("Game_MEDIUM", MODE_PRIVATE);
                break;
            case 2:
                difficultyString = getResources().getString(R.string.difficulty_Hard);
                sharedPreferences = getSharedPreferences("Game_HARD", MODE_PRIVATE);
                break;
        }
        tvDifficultyGame = findViewById(R.id.tvDifficultyGame);
        tvDifficultyGame.setText(difficultyString);

        /*
        * CONTROLS SETUP
        *
        * Per ogni bottone di controllo (numeri e cancel), viene settata la dimensione
        * da occupare nello schermo proporzionale alla larghezza di quest'ultimo.
        * (l'app non si può usare in modalità landscape).
        * NumbersOnClickListener è il listener dedicato a questi bottoni.
        * */
        numbersOnClickListener = new NumbersOnClickListener();
        for (int i = 0; i < 10; i++) {
            buttons[i] = findViewById(BUTTON_IDS[i]);
            buttons[i].setOnClickListener(numbersOnClickListener);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) buttons[i].getLayoutParams();
            params.width = getResources().getDisplayMetrics().widthPixels / 7;
            params.height = params.width;
            buttons[i].setLayoutParams(params);
        }

        /*
        * ACTION BUTTONS
        *
        * Sono i bottoni di "azione" quali: Appunti, Suggerimento e Concludi Partita.
        * Si sono usate anche alcune view "Vuote" per gestire i click fuori dalla board,
        * in modo da poter deselezionare le righe e colonne evidenziate in precedenza.
        * ActionOnClickListener è il listener dedicato a questi bottoni.
        * */
        actionOnClickListener = new ActionOnClickListener();
        imgbtnEdit = findViewById(R.id.imgbtnEdit);
        imgbtnHint = findViewById(R.id.imgbtnHint);
        imgbtnBackGame = findViewById(R.id.imgbtnBackGame);
        imgbtnValidate = findViewById(R.id.imgbtnValidate);
        imgbtnEdit.setOnClickListener(actionOnClickListener);
        imgbtnHint.setOnClickListener(actionOnClickListener);
        imgbtnBackGame.setOnClickListener(actionOnClickListener);
        imgbtnValidate.setOnClickListener(actionOnClickListener);
        emptyView1 = findViewById(R.id.emptyView1);
        emptyView2 = findViewById(R.id.emptyView2);
        emptyView3 = findViewById(R.id.emptyView3);
        emptyView1.setOnClickListener(actionOnClickListener);
        emptyView2.setOnClickListener(actionOnClickListener);
        emptyView3.setOnClickListener(actionOnClickListener);

        /*
        * Chronometer
        *
        * E' stata usata una versione estesa del cronometro classico di android in modo
        * da poter salvare il tempo di una partita e riprenderlo al momento di una Resume.
        *
        * https://gist.github.com/luongvo/9017efc64dc73b0d1e2aba83da641c0d
        *
        * Se si tratta di una partita ripresa (e non nuova) il tempo viene preso in forma di ms
        * dalle shared preferences della difficoltà in questione. Altrimenti il cronometro parte da 0.
        * Inoltre può essere scelto di non mostrarlo nelle impostazioni dell'app.
        *
        * */
        chronometer = findViewById(R.id.chronometer);
        if (!isNewGame) chronometer.setCurrentTime(sharedPreferences.getLong("saved_time", 0));
        chronometer.start();
        sharedPreferencesSettings = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferencesSettings.getBoolean("showElapsedTime", true)) {
            chronometer.setVisibility(View.INVISIBLE);
        }

        //Restore solved board



    }

    @Override
    protected void onPause() {
        //Save state
        saveState(board);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        //Viene eseguito onPause();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void saveState(Board board) {
        Gson gson = new Gson();

        /*
        * END GAME
        *
        * Se il gioco sta finendo (cioè si è premuto il pulsante di validazione), si
        * distingue se si è vinto la partita o si è perso, facendo una comparazione nella funzione endGame()
        * In entrambi i casi si aumentano le stats per le partite giocate (+1).
        * Se si è vinto si aumentano anche quelle per le partite vinte (+1) e si mette anche il tempo di questa partita,
        * differenziandoli nel nome "Game Time $numeropartitavinta".
        * Se si è perso invece viene salvato solo il tempo ma con un nome diverso in modo che non interferisca con i tempi delle
        * partite vinte, che sono quelli che vogliono essere mostrati nelle stats.
        *
        * */

        if (endingGame) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Games Played", sharedPreferences.getInt("Games Played", 0) + 1);
            if (gameWon) {
                editor.putInt("Games Won", sharedPreferences.getInt("Games Won", 0) + 1);
                editor.putLong("Game Time Win" + (sharedPreferences.getInt("Games Won", 0) + 1), (SystemClock.elapsedRealtime() - chronometer.getBase()));
                Log.d(TAG, "saveState: GAME TIME MS: " + (SystemClock.elapsedRealtime() - chronometer.getBase()));
                chronometer.stop();
            } else {
                editor.putLong("Game Time Loss" + (sharedPreferences.getInt("Games Played", 0) + 1), (SystemClock.elapsedRealtime() - chronometer.getBase()));
                Log.d(TAG, "saveState: GAME TIME MS: " + (SystemClock.elapsedRealtime() - chronometer.getBase()));
                chronometer.stop();
            }
            editor.putInt("difficulty", -1);
            editor.apply();
        } else {
            editor = sharedPreferences.edit();
            editor.putString("saved_board", gson.toJson(board));
            editor.putInt("difficulty", difficulty);
            Log.d(TAG, "saveState: SAVING CURRENT TIME MS: " + (SystemClock.elapsedRealtime() - chronometer.getBase()));
            editor.putLong("saved_time", (SystemClock.elapsedRealtime() - chronometer.getBase()));
            chronometer.stop();
            editor.apply();
        }

    }


    private class NumbersOnClickListener implements View.OnClickListener {


        private static final String TAG = "NUMBERS_ONCLICK";

        NumbersOnClickListener() {

        }

        @Override
        public void onClick(View v) {

            Log.w(TAG, "onClick: Clicked");

            //Riga e colonna selezionate vengono dal model,che viene aggiornato in base ai click nella view

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
            } else {
                addAnnotation(rowClicked, colClicked, selectedCell, input);
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
        }

        void insertValueInCell(Cell selectedCell, int value) {
            if (selectedCell.isStartingCell()) {
                Log.d(TAG, "onClick: CAN'T EDIT STARTING CELL");
            }
            else {
                selectedCell.setValue(value);
                selectedCell.clearAnnotations();
                Log.d(TAG, "onClick: NEW VALUE : " + selectedCell.getValue());
                sudokuBoardView.postInvalidate();
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
                        //UI
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

                    //Il valore effettivo viene preso dalla soluzione del puzzle e viene inserito nella cella selezionata.

                    int suggestedValue = solvedBoard[selectedRow][selectedCol];
                    Cell selectedCell = board.getCell(selectedRow * (Globals.SIZE) + selectedCol);
                    numbersOnClickListener.insertValueInCell(selectedCell, suggestedValue);
                    break;

                case R.id.imgbtnValidate:
                    //Confirmation Dialog
                    new AlertDialog.Builder(GameActivity.this)
                            .setTitle(getResources().getString(R.string.are_you_sure))
                            .setMessage(getResources().getString(R.string.validate))
                            .setIcon(R.drawable.baseline_info_black_24)
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> endGame())
                            .setNegativeButton(android.R.string.no, null).show();
                    break;

                case R.id.emptyView1:

                case R.id.emptyView3:

                case R.id.emptyView2:
                    handleOutsideClick();
                    break;

            }

        }


        //Per le empty view
        void handleOutsideClick() {
            sudokuModel.setSelectedCol(-1); //Riga o colonna -1 significa nessuna selezionata
            sudokuModel.setSelectedRow(-1);
            sudokuBoardView.postInvalidate();
        }
    }

    private void endGame() {
        endingGame = true;
        if (Utils.compare(board, solvedBoard)) {
            //GAME VINTO
            Log.d(TAG, "endGame: GAME WON");
            Toast.makeText(GameActivity.this, getResources().getString(R.string.game_won), Toast.LENGTH_LONG).show();
            gameWon = true;
        } else {
            Log.d(TAG, "endGame: GAME LOSS");
            Toast.makeText(GameActivity.this, getResources().getString(R.string.game_loss), Toast.LENGTH_LONG).show();
            gameWon = false;
        }
        onBackPressed();
    }
}
