package org.sudokusolverserver;

import java.util.ArrayList;

public class IntermediateTechniques {
    public static int applyNakedPairs(int[][] board, ArrayList[][] possibleValues) {
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    if (possibleValues[row][col].size() == 2) {
                        count = searchNakedPairs(board, row, col, count, possibleValues);
                    }
                }
            }
        }
        return count;
    }

    public static int searchNakedPairs(int[][] board, int row, int col, int count, ArrayList[][] possibleValues) { //TODO Klopt niet helemaal omdat er ook meerdere naked pairs kunnen zijn dan 2,
        //TODO vb 2 in kolom naar beneden maar dan nog 1 in kolom naar links bv, zijn er ook 2 maar op een andere lijn, 3 in totaal. Weet niet zeker
        int teller = 0;
        ArrayList<DoublePairs> pairs = new ArrayList<>();
        pairs.add(new DoublePairs(row, col));
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == 0 && i != col) {
                if (possibleValues[row][i].size() == 2 && possibleValues[row][i].equals(possibleValues[row][col])) {
                    teller++;
                    pairs.add(new DoublePairs(row, i));
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            if (board[i][col] == 0 && i != row) {
                if (possibleValues[i][col].size() == 2 && possibleValues[i][col].equals(possibleValues[row][col])) {
                    teller++;
                    pairs.add(new DoublePairs(i, col));
                }
            }
        }
        if (pairs.size() < 2) {
            int startRow = row - row % 3;
            int startCol = col - col % 3;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[startRow + i][startCol + j] == 0 && (startRow + i != row || startCol + j != col)) {
                        if (possibleValues[startRow + i][startCol + j].size() == 2 && possibleValues[startRow + i][startCol + j].equals(possibleValues[row][col])) {
                            teller++;
                            pairs.add(new DoublePairs(startRow + i, startCol + j));
                        }
                    }
                }
            }
        }
        if (teller == 1 || pairs.size() == 2) {
            count = removeFromOtherCells(pairs, board, count, possibleValues);
        }

        return count;
    }

    public static int removeFromOtherCells(ArrayList<DoublePairs> pairs, int[][] board, int count, ArrayList[][] possibleValues) {
        DoublePairs firstPair = pairs.get(0); //row and column
        DoublePairs secondPair = pairs.get(1);// row and column
        ArrayList numbers = possibleValues[firstPair.getRow()][firstPair.getColumn()];
        if (firstPair.getRow() == secondPair.getRow()) {
            int row = firstPair.getRow();
            for (int i = 0; i < 9; i++) {
                if (board[row][i] == 0 && possibleValues[row][i].contains(numbers.get(0)) && i != firstPair.getColumn() && i != secondPair.getColumn()) {
                    possibleValues[row][i].remove(numbers.get(0));
                    count++;
                }
                if (board[row][i] == 0 && possibleValues[row][i].contains(numbers.get(1)) && i != firstPair.getColumn() && i != secondPair.getColumn()) {
                    possibleValues[row][i].remove(numbers.get(1));
                    count++;
                }
            }
        } else if (firstPair.getColumn() == secondPair.getColumn()) {
            int col = firstPair.getColumn();
            for (int i = 0; i < 9; i++) {
                if (board[i][col] == 0 && possibleValues[i][col].contains(numbers.get(0)) && i != firstPair.getRow() && i != secondPair.getRow()) {
                    possibleValues[i][col].remove(numbers.get(0));
                    count++;
                }
                if (board[i][col] == 0 && possibleValues[i][col].contains(numbers.get(1)) && i != firstPair.getRow() && i != secondPair.getRow()) {
                    possibleValues[i][col].remove(numbers.get(1));
                    count++;
                }
            }
        }
        int rowFirst = firstPair.getRow();
        int colFirst = firstPair.getColumn();
        int rowSecond = secondPair.getRow();
        int colSecond = secondPair.getColumn();
        int startRow = rowFirst - rowFirst % 3;
        int startCol = colFirst - colFirst % 3;
        int startRowSec = rowSecond - rowSecond % 3;
        int startColSec = colSecond - colSecond % 3;
        if (startRow == startRowSec && startCol == startColSec) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    boolean notFirstCell = startRow + i != rowFirst || startCol + j != colFirst;
                    boolean notSecondCell = startRow + i != rowSecond || startCol + j != colSecond;
                    if (board[startRow + i][startCol + j] == 0 && possibleValues[startRow + i][startCol + j].contains(numbers.get(0)) && notFirstCell && notSecondCell) {
                        possibleValues[startRow + i][startCol + j].remove(numbers.get(0));
                        count++;
                    }
                    if (board[startRow + i][startCol + j] == 0 && possibleValues[startRow + i][startCol + j].contains(numbers.get(1)) && notFirstCell && notSecondCell) {
                        possibleValues[startRow + i][startCol + j].remove(numbers.get(1));
                        count++;
                    }
                }
            }


        }
        return count;
    }

    public static int applyHiddenPairs(int[][] board, ArrayList[][] possibleValues) {
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    count = checkIfThereIsAHiddenPair(board, row, col, count, possibleValues);
                }
            }
        }
        return count;
    }

    public static int checkIfThereIsAHiddenPair(int[][] board, int row, int col, int count, ArrayList<Integer>[][] possibleValues) {
        int[][] listCandidates = new int[9][11];
        ArrayList<Integer> cellCandidates = new ArrayList<>(possibleValues[row][col]);
        ArrayList<DoublePairs> pairs = new ArrayList<>();
        pairs.add(new DoublePairs(row, col));
        int teller = 0;
        boolean empty = true;
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == 0 && i != row) {
                for (int j = 0; j < possibleValues[i][col].size(); j++) {
                    if (cellCandidates.contains(possibleValues[i][col].get(j))) {
                        listCandidates[teller][possibleValues[i][col].get(j) - 1] = possibleValues[i][col].get(j);
                        empty = false;
                    }
                }
                if (!empty) {
                    listCandidates[teller][9] = i;
                    listCandidates[teller][10] = col;
                    teller++;
                }

            }

        }

        teller = 0;
        empty = true;
        ArrayList<Integer> numbers = checkHowManyNumbers(listCandidates, pairs);
        if (pairs.size() == 2 && numbers.size() == 2) {
            count = removeOtherNumbersFromCell(pairs, numbers, count, possibleValues);
            count = removeFromOtherCells(pairs, board, count, possibleValues);
        }
        clearMatrixAndPairs(listCandidates, pairs, row, col);

        for (int i = 0; i < 9; i++) {
            if (board[row][i] == 0 && i != col) {
                for (int j = 0; j < possibleValues[row][i].size(); j++) {
                    if (cellCandidates.contains(possibleValues[row][i].get(j))) {
                        listCandidates[teller][possibleValues[row][i].get(j) - 1] = possibleValues[row][i].get(j);
                        empty = false;
                    }
                }
                if (!empty) {
                    listCandidates[teller][9] = row;
                    listCandidates[teller][10] = i;
                    teller++;
                }
            }

        }

        teller = 0;
        empty = true;
        numbers = checkHowManyNumbers(listCandidates, pairs);
        if (pairs.size() == 2 && numbers.size() == 2) {
            count = removeOtherNumbersFromCell(pairs, numbers, count, possibleValues);
            count = removeFromOtherCells(pairs, board, count, possibleValues);
        }
        clearMatrixAndPairs(listCandidates, pairs, row, col);

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == 0 && (startRow + i != row || startCol + j != col)) {
                    for (int k = 0; k < possibleValues[startRow + i][startCol + j].size(); k++) {
                        if (cellCandidates.contains(possibleValues[startRow + i][startCol + j].get(k))) {
                            listCandidates[teller][possibleValues[startRow + i][startCol + j].get(k) - 1] = possibleValues[startRow + i][startCol + j].get(k);
                            empty = false;
                        }
                    }
                    if (!empty) {
                        listCandidates[teller][9] = startRow + i;
                        listCandidates[teller][10] = startCol + j;
                        teller++;
                    }
                }
            }
        }
        numbers = checkHowManyNumbers(listCandidates, pairs);
        if (pairs.size() == 2 && numbers.size() == 2) {
            count = removeOtherNumbersFromCell(pairs, numbers, count, possibleValues);
            count = removeFromOtherCells(pairs, board, count, possibleValues);
        }

//        for (int[] rij : board) {
//            for (int num : rij) {
//                System.out.print(num + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();

        return count;
    }

    public static ArrayList<Integer> checkHowManyNumbers(int[][] listCandidates, ArrayList<DoublePairs> pairs) {
        int teller = 0;
        int totalNumbers = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                if (listCandidates[j][i] != 0) {
                    teller++;
                    totalNumbers++;
                }
            }
            listCandidates[8][i] = teller;
            teller = 0;
        }
        if (totalNumbers == 2) {
            pairs.clear();
        } else {
            for (int i = 0; i < 9; i++) {
                if (listCandidates[8][i] == 1) {
                    list.add(i + 1);
                }
            }
            if (list.size() == 2) {
                for (int i = 0; i < 8; i++) {
                    if (listCandidates[i][list.get(0) - 1] == list.get(0)) {
                        if (listCandidates[i][list.get(1) - 1] == list.get(1)) {
                            pairs.add(new DoublePairs(listCandidates[i][9], listCandidates[i][10]));
                        }
                    }
                }
            }
        }

        return list;
    }

    private static int removeOtherNumbersFromCell(ArrayList<DoublePairs> pairs, ArrayList<Integer> numbers, int count, ArrayList<Integer>[][] possibleValues) {
        DoublePairs firstPair = pairs.get(0); //row and column
        DoublePairs secondPair = pairs.get(1);// row and column
        int rowFirst = firstPair.getRow();
        int colFirst = firstPair.getColumn();
        int rowSecond = secondPair.getRow();
        int colSecond = secondPair.getColumn();
        possibleValues[rowFirst][colFirst].clear();
        possibleValues[rowFirst][colFirst].addAll(numbers);
        possibleValues[rowSecond][colSecond].clear();
        possibleValues[rowSecond][colSecond].addAll(numbers);
        count++;
        return count;
    }

    private static void clearMatrixAndPairs(int[][] matrix, ArrayList<DoublePairs> pairs, int row, int col) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 0;
            }
        }
        pairs.clear();
        pairs.add(new DoublePairs(row, col));
    }
}
