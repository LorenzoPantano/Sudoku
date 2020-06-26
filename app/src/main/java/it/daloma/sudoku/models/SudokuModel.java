package it.daloma.sudoku.models;

public class SudokuModel {

    private Board board;
    private boolean isNewGame;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public SudokuModel(Board board, boolean isNewGame) {
        this.board = board;
        this.isNewGame = isNewGame;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isNewGame() {
        return isNewGame;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public void setSelectedCol(int selectedCol) {
        this.selectedCol = selectedCol;
    }
}
