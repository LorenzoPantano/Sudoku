package it.daloma.sudoku.models;

/*
* SUDOKU MODEL
*
* Fa da tramite tra Game Activity e SudokuBoardView per determinare quali celle siano state selezionate.
*
* */

public class SudokuModel {

    private Board board;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public SudokuModel(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
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
