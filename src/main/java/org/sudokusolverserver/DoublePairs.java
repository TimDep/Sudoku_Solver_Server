package org.sudokusolverserver;

public class DoublePairs {
    private final int row;
    private final int column;

    public DoublePairs(int row, int column) {
        this.row = row;
        this.column = column;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Row: " + row + ", Column: " + column;
    }
}
