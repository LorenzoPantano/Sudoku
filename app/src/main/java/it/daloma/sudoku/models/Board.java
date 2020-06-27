package it.daloma.sudoku.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Board implements Parcelable {

    private static final int SIZE = 9;
    private ArrayList<Cell> cellList;

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
            System.out.println("Cell " + cell.getRow() + ":" + cell.getCol() + " VALUE: " + cell.getValue() + " STARTING CELL: " + cell.isStartingCell());
        }
    }

    protected Board(Parcel in) {
        if (in.readByte() == 0x01) {
            cellList = new ArrayList<Cell>();
            in.readList(cellList, Cell.class.getClassLoader());
        } else {
            cellList = null;
        }
    }

    public Cell getCell(int index) {
        return cellList.get(index);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (cellList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cellList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel in) {
            return new Board(in);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };
}
