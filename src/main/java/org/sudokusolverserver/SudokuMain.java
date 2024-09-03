package org.sudokusolverserver;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sudoku")
public class SudokuMain {

    @POST
    @Path("/solve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response solveSudoku(int[][] board) {
        SudokuGrader grader = new SudokuGrader();
        if (grader.gradePuzzle(board)) {
            printSequence(board);
        }
        else{
            System.out.println("Not solvable for this engine.");
        }
        return Response.ok(board).build();
    }

    public static void printBoard(int[][] board){
        for (int[] rij : board) {
            for (int num : rij) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printSequence(int[][] board) {
        for (int[] row : board) {
            for (int j : row) {
                System.out.print(j);
            }
        }
        System.out.println();
    }
}
