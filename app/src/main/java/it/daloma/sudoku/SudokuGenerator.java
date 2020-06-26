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
import android.os.Bundle;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.Cell;
import it.daloma.sudoku.utils.LoadingDialog;
import it.daloma.sudoku.utils.NewGameErrorDialog;

public class SudokuGenerator implements Response.ErrorListener, Response.Listener<JSONObject> {

    private String url = "https://sugoku.herokuapp.com/board?difficulty=";
    public int[][] puzzle = new int[9][9];
    ArrayList<Cell> generatedPuzzleCells = new ArrayList<>();
    private RequestQueue requestQueue;
    private Context context;
    LoadingDialog loadingDialog;
    NewGameErrorDialog newGameErrorDialog;

    public SudokuGenerator(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
        this.loadingDialog = new LoadingDialog((Activity) context);
        this.newGameErrorDialog = new NewGameErrorDialog((Activity) context);
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public void setDifficulty(int difficulty) {
        switch (difficulty){
            case Globals.EASY:
                url = url.concat("easy");
                break;
            case Globals.MEDIUM:
                url = url.concat("medium");
                break;
            case Globals.HARD:
                url = url.concat("hard");
                break;
            default:
                url = url.concat("random");
                break;
        }
    }

    public void puzzleGenerator (int difficulty) {
        loadingDialog.startLoadingDialog();
        setDifficulty(difficulty);
        System.out.println("Set Difficulty");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,this, this);
        requestQueue.add(request);
        System.out.println("Made request");
        url = "https://sugoku.herokuapp.com/board?difficulty=";
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
        //Check if not empty
        if (response.length() == 0){

        } //Error Handling

        try {
            JSONArray board = response.getJSONArray("board");
            System.out.println("Board " + board);
            for (int i = 0; i < board.length(); i++) {
                JSONArray box = board.getJSONArray(i);
                for (int j = 0; j < box.length(); j++) {
                    int k = box.getInt(j);
                    puzzle[i][j] = k;
                    Cell cell = new Cell(i, j, k);
                    generatedPuzzleCells.add(cell);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Board generatedBoard = new Board(generatedPuzzleCells);
        loadingDialog.dismissDialog();
        Intent gameIntent = new Intent(context, GameActivity.class);
        gameIntent.putExtra("Board", generatedBoard);
        gameIntent.putExtra("new game",true);
        context.startActivity(gameIntent);
    }
}
