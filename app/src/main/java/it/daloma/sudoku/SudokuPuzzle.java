package it.daloma.sudoku;

import java.util.ArrayList;

public class SudokuPuzzle {

    public boolean isCompleted;
    public boolean isSolved;
    public String completingTime;
    public int difficulty;
    public int[][] puzzle;

    public SudokuPuzzle(int difficulty, int[][] puzzle) {
        this.difficulty = difficulty;
        this.puzzle = puzzle;
        this.isCompleted = false;
        this.isSolved = false;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public String getCompletingTime() {
        return completingTime;
    }

    public void setCompletingTime(String completingTime) {
        this.completingTime = completingTime;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }
}
