package it.daloma.sudoku;

/* SUDOKU GENERATOR
Singleton perché basta una istanza di questa classe per tutta l'app.
Viene chiamata ogni volta che parte una nuova partita.
* */


public class SudokuGenerator {

    private static SudokuGenerator instace;

    private SudokuGenerator() {

    }

    public static SudokuGenerator getInstace() {
        if (instace == null) instace = new SudokuGenerator();
        return instace;
    }

    public int [][] generateInitalNumbers() {
        int [][] sudoku = new int[9][9];
        return sudoku;
    }
}
