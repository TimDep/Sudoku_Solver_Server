package org.sudokusolverserver;

import java.util.ArrayList;

public class SudokuGrader {
    public boolean gradePuzzle(int[][] board, ArrayList<String> result) {
        ArrayList<Integer>[][] possibleValues = new ArrayList[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                possibleValues[i][j] = new ArrayList<>();
                if (board[i][j] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        possibleValues[i][j].add(num);
                    }
                }
            }
        }
        int basicCount = 0;
        int intermediateCount = 0;
        int advancedCount = 0;

        int changes = BasicTechniques.removeFixedNumbers(board, possibleValues);
        if (changes > 0) {
            System.out.println("Remove Fixed numbers Applied: " + changes);
            result.add("Remove Fixed numbers Applied: " + changes);
            SudokuMain.printBoard(board);
            result.add(SudokuMain.printSequence(board));
        }


        basicCount += applyBasicTechniquesUntilStalled(board, basicCount, possibleValues, result);
        if (isSolved(board)) {
            System.out.println("Difficulty is easy");
            return true;
        }

//            intermediateCount += applyIntermediateTechniquesUntilStalled(board, intermediateCount, possibleValues);
        if (isSolved(board)) {
            if (intermediateCount >= 2) {
                System.out.println("Difficulty is medium");
            } else {
                System.out.println("Difficulty is easy");
            }
            return true;
        }

//        advancedCount += applyAdvancedTechniquesUntilStalled(board, advancedCount, possibleValues);
        if (isSolved(board)) {
            if (advancedCount == 2) {
                System.out.println("Difficulty is hard");
            } else if (advancedCount >= 3) {
                System.out.println("Difficulty is expert");
            } else if (advancedCount == 0 && intermediateCount >= 2) {
                System.out.println("Difficulty is medium");
            } else {
                System.out.println("Difficulty is hard");
            }
            return true;
        }
        return false;
    }

    private int applyBasicTechniquesUntilStalled(int[][] board, int count, ArrayList<Integer>[][] possibleValues, ArrayList<String> result) {
        boolean nakedTriplesProgress;
        do {
            boolean hiddenPairsProgress;
            do {
                boolean nakedPairsProgress;
                do {
                    boolean hiddenProgress;
                    do {
                        boolean nakedProgress;
                        do {
                            int changes = BasicTechniques.applyNakedSingle(board, possibleValues);
                            if (changes > 0) {
                                System.out.println("Naked Single Applied: " + changes);
                                result.add("Naked Single Applied: " + changes);
                                SudokuMain.printBoard(board);
                                result.add(SudokuMain.printSequence(board));
                            }
                            count += changes;
                            nakedProgress = (changes > 0);
                        } while (nakedProgress);

                        int changes = BasicTechniques.applyHiddenSingle(board, possibleValues);
                        if (changes > 0) {
                            System.out.println("Hidden Single Applied: " + changes);
                            result.add("Hidden Single Applied: " + changes);
                            SudokuMain.printBoard(board);
                            result.add(SudokuMain.printSequence(board));
                        }
                        count += changes;
                        hiddenProgress = (changes > 0);

                    } while (hiddenProgress);

                    int changes = IntermediateTechniques.applyNakedPairs(board, possibleValues);
                    if (changes > 0) {
                        System.out.println("Naked Pair Applied: " + changes);
                        result.add("Naked Pair Applied: " + changes);
                        SudokuMain.printBoard(board);
                        result.add(SudokuMain.printSequence(board));
                    }
                    count += changes;
                    nakedPairsProgress = (changes > 0);
                } while (nakedPairsProgress);

                int changes = IntermediateTechniques.applyHiddenPairs(board, possibleValues);
                if (changes > 0) {
                    System.out.println("Hidden Pair Applied: " + changes);
                    result.add("Hidden Pair Applied: " + changes);
                    SudokuMain.printBoard(board);
                    result.add(SudokuMain.printSequence(board));
                }
                count += changes;
                hiddenPairsProgress = (changes > 0);
            } while (hiddenPairsProgress);

            int changes = HardTechniques.applyNakedTriples(board, possibleValues);
            if (changes > 0) {
                System.out.println("Naked Triples Applied: " + changes);
                result.add("Naked Triples Applied: " + changes);
                SudokuMain.printBoard(board);
                result.add(SudokuMain.printSequence(board));
            }
            count += changes;
            nakedTriplesProgress = (changes > 0);
        } while (nakedTriplesProgress);

        return count;
    }

    public boolean isSolved(int[][] board) {
        for (int[] row : board) {
            for (int element : row) {
                if (element == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
