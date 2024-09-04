package org.sudokusolverserver;

public class SudokuMain {
    public static void printBoard(int[][] board){
        for (int[] rij : board) {
            for (int num : rij) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    static String printSequence(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int j : row) {
                sb.append(j);
            }
        }
        return sb.toString();
    }
}
