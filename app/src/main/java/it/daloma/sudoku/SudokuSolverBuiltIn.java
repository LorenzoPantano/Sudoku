package it.daloma.sudoku;

import java.util.stream.IntStream;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.Cell;

public class SudokuSolverBuiltIn {

    private static final int BOARD_START_INDEX = 0;
    private static final int BOARD_SIZE = 9;
    private static final int NO_VALUE = 0;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 9;
    private static final int SUBSECTION_SIZE = 3;
    private int[][] matrixBoard;

    public SudokuSolverBuiltIn(Board board) {
        this.matrixBoard = convertBoardToMatrix(board);
    }

    public void setMatrixBoard(int[][] matrixBoard) {
        this.matrixBoard = matrixBoard;
    }

    public int[][] getMatrixBoard() {
        return matrixBoard;
    }

    public boolean solve(int [][] board) {

        //TODO: Resolve stackOverflowERROR

        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (matrixBoard[row][column] == NO_VALUE) {
                    for (int k = MIN_VALUE; k <= MAX_VALUE; k++) {
                        matrixBoard[row][column] = k;
                        if (isValid(matrixBoard, row, column) && solve(matrixBoard)) {
                            return true;
                        }
                        matrixBoard[row][column] = NO_VALUE;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int column) {
        return (rowConstraint(board, row) && columnConstraint(board, column) && subsectionConstraint(board, row, column));
    }

    private boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        for (int i = BOARD_START_INDEX; i < BOARD_SIZE; i++) {
            if (checkConstraint(board, row, constraint, i)) continue;
            else return false;
        }
        return true;
    }

    private boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        for (int i = BOARD_START_INDEX; i < BOARD_SIZE; i++) {
            if (checkConstraint(board, column, constraint, i)) continue;
            else return false;
        }
        return true;
    }

    private boolean subsectionConstraint(int[][] board, int row, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        int subsectionRowStart = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;

        int subsectionColumnStart = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
                if (!checkConstraint(board, r, constraint, c)) return false;
            }
        }
        return true;
    }

    private boolean checkConstraint(
            int[][] board,
            int row,
            boolean[] constraint,
            int column) {
        if (board[row][column] != NO_VALUE) {
            if (!constraint[board[row][column] - 1]) {
                constraint[board[row][column] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }

    private int[][] convertBoardToMatrix(Board board) {
        int [][] matrix = new int[9][9];
        for (Cell cell: board.getCellList()) {
            int row = cell.getRow();
            int col = cell.getCol();
            int value = cell.getValue();
            matrix[row][col] = value;
        }
        return matrix;
    }

    public void printBoard(int[][] board) {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                System.out.print(board[row][column] + " ");
            }
            System.out.println();
        }
    }
}
