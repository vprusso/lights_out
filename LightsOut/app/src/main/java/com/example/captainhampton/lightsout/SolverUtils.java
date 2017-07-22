package com.example.captainhampton.lightsout;

import org.ejml.simple.SimpleMatrix;

public class SolverUtils {

    public static SimpleMatrix getAdjacencyMatrix(int numRows, int numCols, int numLevel) {

        SimpleMatrix M = new SimpleMatrix(numRows*numCols, numRows*numCols);
        Solver solver = new Solver(numRows, numCols, numLevel);

        int rowCount = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                double[] vec = solver.getAdjacentPositions(i,j);
                M.setRow(rowCount,0,vec);
                rowCount++;
            }
        }
        return M;
    }
}
