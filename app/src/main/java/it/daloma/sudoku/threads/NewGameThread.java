package it.daloma.sudoku.threads;

import it.daloma.sudoku.SudokuGenerator;

public class NewGameThread extends Thread {

    private int selectedDifficulty;
    private SudokuGenerator sudokuGenerator;

    public NewGameThread(int selectedDifficulty, SudokuGenerator sudokuGenerator) {
        this.selectedDifficulty = selectedDifficulty;
        this.sudokuGenerator = sudokuGenerator;
    }

    @Override
    public void run() {
        sudokuGenerator.puzzleGenerator(selectedDifficulty);
        super.run();
    }
}
