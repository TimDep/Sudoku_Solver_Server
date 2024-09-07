package org.sudokusolverserver;


import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("sudoku")
public class Endpoint {

    @Path("solve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String solveSudoku(String boardJson) {
        Gson gson = new Gson();
        int[][] board;

        try {
            // Deserialize JSON to int[][]
            board = gson.fromJson(boardJson, int[][].class);
            System.out.println("Received board: " + Arrays.deepToString(board));
        } catch (JsonSyntaxException e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
            return "{\"error\":\"Invalid JSON format\"}";
        }

        SudokuGrader grader = new SudokuGrader();
        ArrayList<String> steps = new ArrayList<>();
        String solution = "";

        if (grader.gradePuzzle(board, steps)) {
            solution = SudokuMain.printSequence(board);
            System.out.println(SudokuMain.printSequence(board));
        } else {
            System.out.println("Not solvable for this engine.");
            solution = "Not solvable for this engine.";
        }

        SolutionContainer oplossing = new SolutionContainer(steps, solution);
        return gson.toJson(oplossing);
    }
}

class SolutionContainer {
    private List<String> steps;
    private String solution;

    public SolutionContainer(List<String> steps, String solution) {
        this.steps = steps;
        this.solution = solution;
    }
}
