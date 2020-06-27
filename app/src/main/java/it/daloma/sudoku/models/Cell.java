package it.daloma.sudoku.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Cell implements Parcelable {

    private int row;
    private int col;
    private int value;
    private int isStartingCell;

    public Cell(int row, int col, int value, int isStartingCell) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.isStartingCell = isStartingCell;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isStartingCell() {
        if (isStartingCell == 1) return true;
        else return false;
    }

    protected Cell(Parcel in) {
        row = in.readInt();
        col = in.readInt();
        value = in.readInt();
        isStartingCell = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(row);
        dest.writeInt(col);
        dest.writeInt(value);
        dest.writeInt(isStartingCell);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cell> CREATOR = new Parcelable.Creator<Cell>() {
        @Override
        public Cell createFromParcel(Parcel in) {
            return new Cell(in);
        }

        @Override
        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };
}
