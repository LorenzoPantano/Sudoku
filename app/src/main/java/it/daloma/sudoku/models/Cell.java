package it.daloma.sudoku.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Cell implements Parcelable {

    private int row;
    private int col;
    private int value;
    private int isStartingCell;
    private ArrayList<Integer> annotations;

    public Cell(int row, int col, int value, int isStartingCell) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.isStartingCell = isStartingCell;
        this.annotations = new ArrayList<>();
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

    public void addAnnotation(int value) {
        if (value == 0) {
            //CANCEL BUTTON
            this.annotations.clear();
            return;
        }
        if (this.annotations.contains(value)) return;
        else this.annotations.add(value);
    }

    public int getAnnotationIndex(int value) {
        if (this.annotations.contains(value)) {
            return this.annotations.indexOf(value);
        } else return -1;
    }

    public ArrayList<Integer> getAnnotations() {
        return this.annotations;
    }

    public void clearAnnotations(){
        this.annotations.clear();
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
        if (in.readByte() == 0x01) {
            annotations = new ArrayList<Integer>();
            in.readList(annotations, Integer.class.getClassLoader());
        } else {
            annotations = null;
        }
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
        if (annotations == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(annotations);
        }
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

