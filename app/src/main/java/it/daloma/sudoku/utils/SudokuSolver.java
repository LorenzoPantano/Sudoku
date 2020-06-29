package it.daloma.sudoku.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.Cell;

public class SudokuSolver implements Response.Listener, Response.ErrorListener{

    private static final String TAG = "SUDOKU_SOLVER";
    ArrayList<Cell> generatedSolvedPuzzleCells = new ArrayList<>();
    private String urlSolved = "https://sugoku.herokuapp.com/solve";
    private RequestQueue requestQueue;
    private Context context;
    private Board generatedBoardFromGenerator;
    private int[][] matrixBoard;
    private String stringMatrixBoard = "";
    private HashMap<String, String> params = new HashMap<>();

    public SudokuSolver(Context context, Board board) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.context = context;
        this.generatedBoardFromGenerator = board;
        this.matrixBoard = convertBoardToMatrix(board);
        Log.d(TAG, "SudokuSolver: MATRIX BOARD: " + matrixBoard);
        Log.d(TAG, "SudokuSolver: STRING MATRIX: " + matrixToString(matrixBoard));
        params.put("data", matrixToString(matrixBoard));
    }

    private int[][] convertBoardToMatrix(Board board) {
        int [][] matrix = new int[9][9];
        for (Cell cell: board.getCellList()) {
            int row = cell.getRow();
            int col = cell.getCol();
            int value = cell.getValue();
            matrix[row][col] = value;
        }
        return matrix;
    }

    private String matrixToString(int [][] matrixBoard) {
        String stringMatrix;
        StringBuilder stringBuilder = new StringBuilder("{board:");
        stringBuilder.append("[");
        for (int row = 0; row < 9; row++) {
            stringBuilder.append("[");
            for (int col = 0; col < 9; col++) {
                if (col == 8 && row != 8) {
                    stringBuilder.append(Integer.toString(matrixBoard[row][col]));
                    stringBuilder.append("],");
                    continue;
                }
                else if (col == 8 && row == 8) {
                    stringBuilder.append(Integer.toString(matrixBoard[row][col]));
                    stringBuilder.append("]");
                    continue;
                }
                stringBuilder.append(Integer.toString(matrixBoard[row][col]));
                stringBuilder.append(",");

            }
        }
        stringBuilder.append("]}");
        Log.d(TAG, "matrixToString: " + stringBuilder);
        stringMatrix = stringBuilder.toString();
        return stringMatrix;
    }

    public void solveBoard() {
        System.out.println("Solving...");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlSolved, new JSONObject(params), this, this);
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Object response) {
        System.out.println("Response");

        Log.d(TAG, "onResponse: RESPONSE SOLVER: " + response.toString());
    }


}
