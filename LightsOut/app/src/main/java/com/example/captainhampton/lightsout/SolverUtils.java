package com.example.captainhampton.lightsout;

import org.ejml.simple.SimpleMatrix;

public class SolverUtils {

    public static SimpleMatrix getAdjacencyMatrix(int num_rows, int num_cols, int num_level) {

        SimpleMatrix M = new SimpleMatrix(num_rows*num_cols, num_rows*num_cols);
        Solver solver = new Solver(num_rows, num_cols, num_level);

        int row_count = 0;
        for (int i = 0; i < num_rows; i++) {
            for (int j = 0; j < num_cols; j++) {
                double[] vec = solver.getAdjacentPositions(i,j);
                M.setRow(row_count,0,vec);
                row_count++;
            }
        }
        return M;
    }
}
