package it.daloma.sudoku.model;

import androidx.lifecycle.MutableLiveData;

public class SudokuModel {

    public MutableLiveData<int[]> selectedCellLive = new MutableLiveData<>();

    private int selectedRow = -1;
    private int selectedCol = -1;

    public SudokuModel() {
        selectedCellLive.postValue(new int[]{selectedRow, selectedCol});
    }

    public void updateSelectedCell (int selectedCol, int selectedRow) {
        selectedCellLive.postValue(new int[]{selectedRow, selectedCol});
    }
}
