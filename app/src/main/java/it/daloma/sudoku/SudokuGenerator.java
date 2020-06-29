package it.daloma.sudoku;

/* SUDOKU GENERATOR
Viene chiamata ogni volta che parte una nuova partita. Al click del bottone
parte un thread che prende dall'API un puzzle della difficoltà scelta.

API:  https://github.com/berto/sugoku
Board - returns a puzzle board
https://sugoku.herokuapp.com/board
Arguments - Difficulty:
easy
medium
hard
random
Example: https://sugoku.herokuapp.com/board?difficulty=easy

* */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.Cell;
import it.daloma.sudoku.utils.LoadingDialog;
import it.daloma.sudoku.utils.NewGameErrorDialog;
import it.daloma.sudoku.utils.SudokuSolver;

public class SudokuGenerator implements Response.ErrorListener, Response.Listener<JSONObject> {

    private static final String TAG = "SUDOKU_GENERATOR";
    private String urlGenerate = "https://sugoku.herokuapp.com/board?difficulty=";
    ArrayList<Cell> generatedPuzzleCells = new ArrayList<>();
    private RequestQueue requestQueue;
    private Context context;
    LoadingDialog loadingDialog;
    NewGameErrorDialog newGameErrorDialog;
    private int difficultyForIntent = -1;

    public SudokuGenerator(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
        this.loadingDialog = new LoadingDialog((Activity) context);
        this.newGameErrorDialog = new NewGameErrorDialog((Activity) context);
    }


    private void setDifficulty(int difficulty) {
        switch (difficulty){
            case Globals.EASY:
                urlGenerate = urlGenerate.concat("easy");
                difficultyForIntent = Globals.EASY;
                break;
            case Globals.MEDIUM:
                urlGenerate = urlGenerate.concat("medium");
                difficultyForIntent = Globals.MEDIUM;
                break;
            case Globals.HARD:
                urlGenerate = urlGenerate.concat("hard");
                difficultyForIntent = Globals.HARD;
                break;
            default:
                urlGenerate = urlGenerate.concat("random");
                break;
        }
    }

    public void puzzleGenerator (int difficulty) {
        loadingDialog.startLoadingDialog();
        setDifficulty(difficulty);
        System.out.println("Set Difficulty");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlGenerate,null,this, this);
        requestQueue.add(request);
        System.out.println("Made request");
        urlGenerate = "https://sugoku.herokuapp.com/board?difficulty=";
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("ERROR Response");
        newGameErrorDialog.startErrorDialog();
        loadingDialog.dismissDialog();
    }


    @Override
    public void onResponse(JSONObject response) {

        System.out.println("Response");

        try {
            JSONArray board = response.getJSONArray("board");
            System.out.println("Board " + board);
            generatedPuzzleCells = new ArrayList<>();
            for (int i = 0; i < board.length(); i++) {
                JSONArray box = board.getJSONArray(i);
                for (int j = 0; j < box.length(); j++) {
                    int k = box.getInt(j);
                    Cell cell;
                    if (k == 0) {
                        cell = new Cell(i, j, k, 0);
                    } else {
                        cell = new Cell(i, j, k, 1);
                    }
                    Log.d(TAG, "onResponse: GENERATED CELL: " + i +" "+j+" : "+k+" "+cell.isStartingCell());
                    generatedPuzzleCells.add(cell);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Board generatedBoard = new Board(generatedPuzzleCells);
        generatedBoard.printBoard();
        loadingDialog.dismissDialog();
        Intent gameIntent = new Intent(context, GameActivity.class);
        gameIntent.putExtra("Board", generatedBoard);
        gameIntent.putExtra("new game",true);
        gameIntent.putExtra("difficulty", difficultyForIntent);
        context.startActivity(gameIntent);
    }
}
