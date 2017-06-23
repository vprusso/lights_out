package com.example.captainhampton.lightsout;

import org.ejml.simple.SimpleMatrix;

public class SolverUtils {

    public static SimpleMatrix getAdjacencyMatrix(int numRows, int numCols) {

        SimpleMatrix M = new SimpleMatrix(numRows*numCols, numRows*numCols);
        Solver solver = new Solver(numRows, numCols);

        int row_count = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                double[] vec = solver.getAdjacentPositions(i,j);
                M.setRow(row_count,0,vec);
                row_count++;
            }
        }
        return M;
    }
}
