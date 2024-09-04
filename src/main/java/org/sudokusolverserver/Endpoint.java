package org.sudokusolverserver;


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;

@Path("sudoku")
public class Endpoint {

    @Path("solve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<String> hello(int[][] board) {
        SudokuGrader grader = new SudokuGrader();
        ArrayList<String> result = new ArrayList<>();
        if (grader.gradePuzzle(board, result)) {
            System.out.println(SudokuMain.printSequence(board));
        }
        else{
            System.out.println("Not solvable for this engine.");
        }
        return result;
//        return "hello";
    }
}
