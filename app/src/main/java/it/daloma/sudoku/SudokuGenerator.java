package it.daloma.sudoku;

/* SUDOKU GENERATOR
Singleton perché basta una istanza di questa classe per tutta l'app.
Viene chiamata ogni volta che parte una nuova partita. Al click del bottone
parte un thread che prende dall'API un puzzle della difficoltà scelta.

API:  https://github.com/berto/sugoku
Board - returns a puzzle board
https://sugoku.herokuapp.com/board
Arguments - Difficulty:
easy
medium
hard
random
Example: https://sugoku.herokuapp.com/board?difficulty=easy

* */


public class SudokuGenerator {

    private static SudokuGenerator instace;

    private SudokuGenerator() {

    }

    public static SudokuGenerator getInstace() {
        if (instace == null) instace = new SudokuGenerator();
        return instace;
    }



}
