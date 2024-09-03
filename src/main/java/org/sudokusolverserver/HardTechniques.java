package org.sudokusolverserver;

import java.util.ArrayList;
import java.util.HashSet;

public class HardTechniques {
    public static int applyNakedTriples(int[][] board, ArrayList[][] possibleValues) {
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    if (possibleValues[row][col].size() == 2 || possibleValues[row][col].size() == 3) {
                        count = searchNakedTriples(board, row, col, count, possibleValues);
                    }
                }
            }
        }
        return count;
    }

    private static int searchNakedTriples(int[][] board, int row, int col, int count, ArrayList[][] possibleValues) {
        // NOTE examples : 3 / 3 / 3
        // NOTE examples : 3 / 3 / 2
        // NOTE examples : 3 / 2 / 2

        // NOTE NOT YET examples : 2 / 2 / 2  ,  12 / 23 / 13

        int teller = 0;
        ArrayList<DoublePairs> pairs = new ArrayList<>();
        pairs.add(new DoublePairs(row, col));
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == 0 && i != col) {
                if (possibleValues[row][i].size() >= 2 && possibleValues[row][i].size() <= 3 && possibleValues[row][col].containsAll(possibleValues[row][i])) {
                    teller++;
                    pairs.add(new DoublePairs(row, i));
                }

            }
        }
        if (teller == 2 || pairs.size() == 3) {
            count = removeFromOtherCells(pairs, board, count, possibleValues);
        }
        teller = 0;
        resetEverything(pairs, row, col);


        for (int i = 0; i < 9; i++) {
            if (board[i][col] == 0 && i != row) {
                if (possibleValues[i][col].size() >= 2 && possibleValues[i][col].size() <= 3 && possibleValues[row][col].containsAll(possibleValues[i][col])) {
                    teller++;
                    pairs.add(new DoublePairs(i, col));
                }
            }
        }
        if (teller == 2 || pairs.size() == 3) {
            count = removeFromOtherCells(pairs, board, count, possibleValues);
        }
        teller = 0;
        resetEverything(pairs, row, col);

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boolean notFirstCell = startRow + i != row || startCol + j != col;
                if (board[startRow + i][startCol + j] == 0 && notFirstCell) {
                    if (pairs.size() == 2 && (startRow + i != pairs.get(1).getRow() || startCol + j != pairs.get(1).getColumn())) {
                        if (possibleValues[startRow + i][startCol + j].size() >= 2 && possibleValues[startRow + i][startCol + j].size() <= 3 && possibleValues[row][col].containsAll(possibleValues[startRow + i][startCol + j])) {
                            teller++;
                            pairs.add(new DoublePairs(startRow + i, startCol + j));
                        }
                    }

                }
            }
        }
        if (teller == 2 || pairs.size() == 3) {
            count = removeFromOtherCells(pairs, board, count, possibleValues);
        }

        return count;
    }

    public static int removeFromOtherCells(ArrayList<DoublePairs> pairs, int[][] board, int count, ArrayList<Integer>[][] possibleValues) {
        DoublePairs firstPair = pairs.get(0); //row and column
        DoublePairs secondPair = pairs.get(1);// row and column
        DoublePairs thirdPair = pairs.get(2);

        HashSet<Integer> set = new HashSet<>();
        for (DoublePairs pair : pairs) {
            set.addAll(possibleValues[pair.getRow()][pair.getColumn()]);
        }
        ArrayList<Integer> numbers = new ArrayList<>(set);

        if (firstPair.getRow() == secondPair.getRow() && firstPair.getRow() == thirdPair.getRow() && secondPair.getRow() == thirdPair.getRow()) {
            int row = firstPair.getRow();
            for (int i = 0; i < 9; i++) {
                if (board[row][i] == 0 && possibleValues[row][i].contains(numbers.get(0)) && i != firstPair.getColumn() && i != secondPair.getColumn() && i != thirdPair.getColumn()) {
                    possibleValues[row][i].remove(numbers.get(0));
                    count++;
                }
                if (board[row][i] == 0 && possibleValues[row][i].contains(numbers.get(1)) && i != firstPair.getColumn() && i != secondPair.getColumn() && i != thirdPair.getColumn()) {
                    possibleValues[row][i].remove(numbers.get(1));
                    count++;
                }
                if (board[row][i] == 0 && possibleValues[row][i].contains(numbers.get(2)) && i != firstPair.getColumn() && i != secondPair.getColumn() && i != thirdPair.getColumn()) {
                    possibleValues[row][i].remove(numbers.get(2));
                    count++;
                }
            }
        } else if (firstPair.getColumn() == secondPair.getColumn() && firstPair.getColumn() == thirdPair.getColumn() && secondPair.getColumn() == thirdPair.getColumn()) {
            int col = firstPair.getColumn();
            for (int i = 0; i < 9; i++) {
                if (board[i][col] == 0 && possibleValues[i][col].contains(numbers.get(0)) && i != firstPair.getRow() && i != secondPair.getRow() && i != thirdPair.getRow()) {
                    possibleValues[i][col].remove(numbers.get(0));
                    count++;
                }
                if (board[i][col] == 0 && possibleValues[i][col].contains(numbers.get(1)) && i != firstPair.getRow() && i != secondPair.getRow() && i != thirdPair.getRow()) {
                    possibleValues[i][col].remove(numbers.get(1));
                    count++;
                }
                if (board[i][col] == 0 && possibleValues[i][col].contains(numbers.get(2)) && i != firstPair.getRow() && i != secondPair.getRow() && i != thirdPair.getRow()) {
                    possibleValues[i][col].remove(numbers.get(2));
                    count++;
                }
            }
        }
        int rowFirst = firstPair.getRow();
        int colFirst = firstPair.getColumn();
        int rowSecond = secondPair.getRow();
        int colSecond = secondPair.getColumn();
        int rowThird = thirdPair.getRow();
        int colThird = thirdPair.getColumn();
        int startRow = rowFirst - rowFirst % 3;
        int startCol = colFirst - colFirst % 3;
        int startRowSec = rowSecond - rowSecond % 3;
        int startColSec = colSecond - colSecond % 3;
        int startRowThird = rowThird - rowThird % 3;
        int startColThird = colThird - colThird % 3;
        if (startRow == startRowSec && startRowSec == startRowThird && startCol == startColSec && startColSec == startColThird) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    boolean notFirstCell = startRow + i != rowFirst || startCol + j != colFirst;
                    boolean notSecondCell = startRow + i != rowSecond || startCol + j != colSecond;
                    boolean notThirdCell = startRow + i != rowThird || startCol + j != colThird;
                    if (board[startRow + i][startCol + j] == 0 && possibleValues[startRow + i][startCol + j].contains(numbers.get(0)) && notFirstCell && notSecondCell && notThirdCell) {
                        possibleValues[startRow + i][startCol + j].remove(numbers.get(0));
                        count++;
                    }
                    if (board[startRow + i][startCol + j] == 0 && possibleValues[startRow + i][startCol + j].contains(numbers.get(1)) && notFirstCell && notSecondCell && notThirdCell) {
                        possibleValues[startRow + i][startCol + j].remove(numbers.get(1));
                        count++;
                    }
                    if (board[startRow + i][startCol + j] == 0 && possibleValues[startRow + i][startCol + j].contains(numbers.get(2)) && notFirstCell && notSecondCell && notThirdCell) {
                        possibleValues[startRow + i][startCol + j].remove(numbers.get(2));
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void resetEverything(ArrayList<DoublePairs> pairs, int row, int col) {
        pairs.clear();
        pairs.add(new DoublePairs(row, col));
    }

    public static int applyHiddenTriples(int[][] board, ArrayList[][] possibleValues) {
        // NOTE examples : 3 / 3 / 3
        // NOTE examples : 3 / 3 / 2
        // NOTE examples : 3 / 2 / 2

        // NOTE NOT YET examples : 2 / 2 / 2  ,  12 / 23 / 13

        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    count = checkIfThereIsAnHiddenTriple(board, row, col, count, possibleValues);
                }
            }
        }
        return count;
    }

    private static int checkIfThereIsAnHiddenTriple(int[][] board, int row, int col, int count, ArrayList<Integer>[][] possibleValues) {
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
        if (totalNumbers == 3) {
            pairs.clear();
        } else {
            for (int i = 0; i < 9; i++) {
                if (listCandidates[8][i] == 1) { // TODO MAG OOK 2 ZIJN
                    list.add(i + 1);
                }
            }
            if (list.size() == 3) { //TODO dit is het kijken of inzelfde paar zit ofs, wordt moeilijk dit normaal
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
        DoublePairs thirdPair = pairs.get(2); //Row and column
        int rowFirst = firstPair.getRow();
        int colFirst = firstPair.getColumn();
        int rowSecond = secondPair.getRow();
        int colSecond = secondPair.getColumn();
        int rowThird = thirdPair.getRow();
        int colThird = thirdPair.getColumn();
        possibleValues[rowFirst][colFirst].clear();
        possibleValues[rowFirst][colFirst].addAll(numbers);
        possibleValues[rowSecond][colSecond].clear();
        possibleValues[rowSecond][colSecond].addAll(numbers);
        possibleValues[rowThird][colThird].clear();
        possibleValues[rowThird][colThird].addAll(numbers);
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
