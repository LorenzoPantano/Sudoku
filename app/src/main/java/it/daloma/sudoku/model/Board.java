package it.daloma.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private static final int SIZE = 9;
    private ArrayList<Cell> cellList = new ArrayList<>();

    public Board(ArrayList<Cell> cellList) {
        this.cellList = cellList;
    }

    public ArrayList<Cell> getCellList() {
        return cellList;
    }

    public void setCellList(ArrayList<Cell> cellList) {
        this.cellList = cellList;
    }

    public void printBoard () {
        for (Cell cell: cellList) {
            System.out.println("Cell " + cell.getRow() + ":" + cell.getCol() + " VALUE: " + cell.getValue());
        }
    }
}
