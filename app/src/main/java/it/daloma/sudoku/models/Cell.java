package it.daloma.sudoku.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Cell implements Parcelable {

    private int row;
    private int col;
    private int value;

    public Cell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    protected Cell(Parcel in) {
        row = in.readInt();
        col = in.readInt();
        value = in.readInt();
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
