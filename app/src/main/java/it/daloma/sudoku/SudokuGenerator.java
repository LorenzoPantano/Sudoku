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

import android.content.Context;

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

import java.util.ArrayList;

public class SudokuGenerator implements Response.ErrorListener, Response.Listener<JSONObject> {

    private String url = "https://sugoku.herokuapp.com/board?difficulty=";
    public int[][] puzzle = new int[9][9];
    private RequestQueue requestQueue;

    public SudokuGenerator(Context context) {
        requestQueue = Volley.newRequestQueue(context);
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
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
