package it.daloma.sudoku.utils;

import it.daloma.sudoku.models.Board;
import it.daloma.sudoku.models.Cell;

public class Utils {

    public static int[] convertBiToMonodimensionalArray(int[][] board) {
        int [] monoBoard = new int[81];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                monoBoard[row*9 +col] = board[row][col];
            }
        }
        return monoBoard;
    }

    public static int[][] convertMonoToBidimensionalArray(int[] monoBoard) {
        int [][] board = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                board[row][col] = monoBoard[row*9 + col];
            }
        }
        return board;
    }

    public static int[][] convertBoardToMatrix(Board board) {
        int [][] matrix = new int[9][9];
        for (Cell cell: board.getCellList()) {
            int row = cell.getRow();
            int col = cell.getCol();
            int value = cell.getValue();
            matrix[row][col] = value;
        }
        return matrix;
    }

    public static boolean compare(Board board, int[][] matrixBoard){
        int [][] boardToMatrix = convertBoardToMatrix(board);
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (boardToMatrix[row][col] == matrixBoard[row][col]) continue;
                else {
                    System.out.println("ROW " + row + "COL "+ col + "VALUE FROM BOARD: "+ boardToMatrix[row][col] + " VALUE FROM SOLVED: "+ matrixBoard[row][col]);
                    return false;
                }
            }
        }
        return true;
    }

    public static int[] stringArrayToIntArray(String array) {

        if (array.equals("DEFAULT")) {
            return new int[81];
        }

        int[] monoArray = new int[81];
        array.replace("[", "");
        array.replace("]", "");
        String[] stringTokens = array.split(",");
        for (int i = 0; i < 81; i++) {
            monoArray[i] = Integer.parseInt(stringTokens[i]);
        }
        return monoArray;
    }


    public static String matrixBoardToString(int[] solvedBoard) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int index = 0; index < solvedBoard.length; index++) {
            stringBuilder.append(Integer.toString(solvedBoard[index]));
            if (index < solvedBoard.length - 1) break;
            stringBuilder.append(",");
        }
        stringBuilder.append("]");
        String resultStringArray = stringBuilder.toString();
        return resultStringArray;
    }
}
