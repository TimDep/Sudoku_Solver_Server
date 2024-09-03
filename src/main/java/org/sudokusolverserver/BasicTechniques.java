package org.sudokusolverserver;

import java.util.ArrayList;

public class BasicTechniques {
    public static int applyNakedSingle(int[][] board, ArrayList<Integer>[][] possibleValues) {
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) { // Empty cell
                    if (possibleValues[row][col].size() == 1) {
                        board[row][col] = possibleValues[row][col].get(0);
                        possibleValues[row][col].clear();
                        removeFromTheOtherCells(possibleValues, row, col, board[row][col]);
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static int removeFixedNumbers(int[][] board, ArrayList<Integer>[][] possibleValues) {
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int i = 0; i < 9; i++) {
                        if (board[row][i] != 0) {
                            if (possibleValues[row][col].contains(board[row][i])) {
                                possibleValues[row][col].remove((Integer) board[row][i]);
                                count++;
                            }
                        }
                    }
                    for (int i = 0; i < 9; i++) {
                        if (board[i][col] != 0) {
                            if (possibleValues[row][col].contains(board[i][col])) {
                                possibleValues[row][col].remove((Integer) board[i][col]);
                                count++;
                            }
                        }
                    }
                    int startRow = row - row % 3;
                    int startCol = col - col % 3;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (board[startRow + i][startCol + j] != 0) {
                                if (possibleValues[row][col].contains(board[startRow + i][startCol + j])) {
                                    possibleValues[row][col].remove((Integer) board[startRow + i][startCol + j]);
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    public static int applyHiddenSingle(int[][] board, ArrayList<Integer>[][] possibleValues) {
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) { //empty
                    count = checkIfThereIsOnlyOne(board, row, col, count, possibleValues);
                }
            }
        }
        return count;
    }

    public static int checkIfThereIsOnlyOne(int[][] board, int row, int col, int count, ArrayList<Integer>[][] possibleValues) {
        ArrayList<Integer> mogelijks = new ArrayList<>(possibleValues[row][col]);
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == 0 && i != row && !mogelijks.isEmpty()) {
                mogelijks.removeAll(possibleValues[i][col]);
            }

        }
        count = checkIfListIsOne(mogelijks, row, col, count, board, possibleValues);

        mogelijks = new ArrayList<>(possibleValues[row][col]);


        for (int i = 0; i < 9; i++) {
            if (board[row][i] == 0 && i != col && !mogelijks.isEmpty()) {
                mogelijks.removeAll(possibleValues[row][i]);
            }
        }


        count = checkIfListIsOne(mogelijks, row, col, count, board, possibleValues);

        mogelijks = new ArrayList<>(possibleValues[row][col]);

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == 0 && (startRow + i != row || startCol + j != col) && !mogelijks.isEmpty()) {
                    mogelijks.removeAll(possibleValues[startRow + i][startCol + j]);
                }
            }

        }
        count = checkIfListIsOne(mogelijks, row, col, count, board, possibleValues);

//        for (int[] rij : board) {
//            for (int num : rij) {
//                System.out.print(num + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();

        return count;
    }

    private static int checkIfListIsOne(ArrayList<Integer> mogelijks, int row, int col, int count, int[][] board, ArrayList<Integer>[][] possibleValues) {
        if (mogelijks.size() == 1) {
            board[row][col] = mogelijks.get(0);
            possibleValues[row][col].clear();
            removeFromTheOtherCells(possibleValues, row, col, mogelijks.get(0));
            count++;
        }
        return count;
    }

    private static void removeFromTheOtherCells(ArrayList<Integer>[][] possibleValues, int row, int col, int mogelijk) {
        for (int i = 0; i < 9; i++) {
            if (possibleValues[row][i].contains(mogelijk)) {
                possibleValues[row][i].remove((Integer) mogelijk);
            }
        }
        for (int i = 0; i < 9; i++) {
            if (possibleValues[i][col].contains(mogelijk)) {
                possibleValues[i][col].remove((Integer) mogelijk);
            }
        }
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (possibleValues[startRow + i][startCol + j].contains(mogelijk)) {
                    possibleValues[startRow + i][startCol + j].remove((Integer) mogelijk);
                }
            }
        }
    }
}
