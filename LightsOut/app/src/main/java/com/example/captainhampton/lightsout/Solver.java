package com.example.captainhampton.lightsout;

import org.ejml.simple.SimpleMatrix;

public class Solver {

    int NUM_ROWS, NUM_COLS;
    public Solver(int numRows, int numCols){
        NUM_COLS = numCols;
        NUM_ROWS = numRows;
    }
    PlayActivity playActivity = new PlayActivity();

    public static int findFirstIdx(SimpleMatrix C) {
        int idx = -1;
        for (int n = 0; n < C.numRows(); n++) {
            for (int m = 0; m < C.numCols(); m++) {
                if (C.get(n, m) == 1) {
                    idx = C.getIndex(n, m);
                    return idx;
                }
            }
        }
        return idx;
    }

    public static SimpleMatrix getRow(SimpleMatrix M, int row_num, int col_offset) {
        SimpleMatrix row = M.extractMatrix(row_num,row_num+1,col_offset,M.numCols());
        return row;
    }

    public static SimpleMatrix getCol(SimpleMatrix M, int col_num, int row_offset) {
        SimpleMatrix col = M.extractMatrix(row_offset,M.numRows(),col_num,col_num+1);
        return col;
    }

    public static SimpleMatrix setRow(SimpleMatrix M, SimpleMatrix row, int row_num, int from, int to) {

        double val;
        for (int j = from; j < to; j++ ) {
            val = row.get(0,j);
            M.set(row_num,j,val);
        }

        return M;
    }

    public static SimpleMatrix logicalSymmetricDifference(SimpleMatrix M, SimpleMatrix flip) {

        SimpleMatrix P = new SimpleMatrix(new double[M.numRows()][M.numCols()]);

        for (int i = 0; i < M.numRows(); i++) {
            for (int j = 0; j < M.numCols(); j++) {
                double a = M.get(i,j);
                double b = flip.get(i, j);
                double c = Double.longBitsToDouble(
                        Double.doubleToRawLongBits(a) ^ Double.doubleToRawLongBits(b));
                P.set(i,j,c);
            }
        }
        return P;
    }

    public static SimpleMatrix g2rref(SimpleMatrix A) {
        // Function that computes the reduced row echeleon form of a matrix over the field GF(2).
        int i = 0;
        int j = 0;

        while ( (i < A.numRows()) && (j < A.numCols()) ) {
            SimpleMatrix C = A.extractMatrix(i,A.numRows(),j,j+1);

            // Find value and index of largest element in the remainder of column j.
            int k = findFirstIdx(C) + i;

            // Swap i-th and k-th rows.
            if (k >= 0) {
                SimpleMatrix row_i = getRow(A,i,0);
                SimpleMatrix row_k = getRow(A,k,0);

                A = setRow(A,row_i,k,0,row_i.numCols());
                A = setRow(A,row_k,i,0,row_k.numCols());

            }

            // Save the right hand side of the pivot row
            SimpleMatrix aijn = getRow(A,i,j);

            // Column we're looking at
            SimpleMatrix col = getCol(A,j,0);

            // Never XOR the pivot row against itself
            col.set(i,0);

            // This builds an matrix of bits to flip
            SimpleMatrix flip = col.mult(aijn);

            // XOR the right hand side of the pivot row with all the other rows
            SimpleMatrix A_sub = A.extractMatrix(0,A.numRows(),j,A.numCols());

            SimpleMatrix A_sub_xor = logicalSymmetricDifference(A_sub,flip);

            for (int n = 0; n < A.numRows(); n++) {
                int col_count = 0;
                for (int m = j; m < A.numCols(); m++) {
                    double xor_val = A_sub_xor.get(n,col_count);
                    A.set(n,m,xor_val);
                    col_count++;
                }
            }
            i++;
            j++;
        }

        return A;
    }

    public boolean isConfigSolvable(boolean[][] light_states) {

        // 5x5 configuration is solvable if the vector of lights are orthogonal to these two
        // vectors n1 and n2 that correspond to vectors in the null space of the adjacency matrix
        // of the game board. (https://www.math.ksu.edu/math551/math551a.f06/lights_out.pdf)
        SimpleMatrix n1 = new SimpleMatrix(new double[][]
                {{0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0}});
        n1 = n1.transpose();
        SimpleMatrix n2 = new SimpleMatrix(new double[][]
                {{1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1}});
        n2 = n2.transpose();

        SimpleMatrix light_vector = calculateLightVector(light_states);
        SimpleMatrix vec1 = light_vector.mult(n1);
        SimpleMatrix vec2 = light_vector.mult(n2);

        double val1 = vec1.get(0, 0);
        double val2 = vec2.get(0, 0);

        // If inner product of both vectors n1 and n2 from the null space are orthogonal (mod 2)
        // then the configuration is solvable.
        return (val1 % 2 == 0 && val2 % 2 == 0);
    }

    public boolean[][] calculateWinningConfig(boolean[][] light_states) {
        // Winning configuration boils down to Ax = b
        // Refer to https://www.math.ksu.edu/math551/math551a.f06/lights_out.pdf
        boolean[][] solution = new boolean[NUM_ROWS][NUM_COLS];


        SimpleMatrix A = SolverUtils.getAdjacencyMatrix(NUM_ROWS, NUM_COLS);
        SimpleMatrix b;
        b = calculateLightVector(light_states);
        A = A.combine(0, A.numCols(), b.transpose());
        A = g2rref(A);

        SimpleMatrix x = getCol(A, A.numCols() - 1, 0);

        int x_size = 0;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if ( x.get(x_size) == 1.0 )
                    solution[i][j] = Boolean.TRUE;
                else
                    solution[i][j] = Boolean.FALSE;
                x_size++;
            }
        }

        return solution;
    }

    public SimpleMatrix calculateLightVector(boolean[][] light_states) {
        SimpleMatrix vec = new SimpleMatrix(1,NUM_ROWS*NUM_COLS);

        int vec_size = 0;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (light_states[i][j] == Boolean.TRUE)
                    vec.set(0,vec_size,1);
                else
                    vec.set(0, vec_size,0);

                vec_size++;
            }
        }

        return vec;
    }

    public double[] getAdjacentPositions(int x, int y) {
        double[] adj_pos_vec = new double[NUM_ROWS * NUM_COLS];

        int top = x - 1;
        int bot = x + 1;
        int left = y - 1;
        int right = y + 1;

        adj_pos_vec[x * NUM_ROWS + y] = 1;

        if ( !playActivity.isLightOutOfBounds(top, y) )
            adj_pos_vec[top * NUM_ROWS + y] = 1;

        if ( !playActivity.isLightOutOfBounds(bot, y) )
            adj_pos_vec[bot * NUM_ROWS + y] = 1;

        if ( !playActivity.isLightOutOfBounds(x, left) )
            adj_pos_vec[x * NUM_ROWS + left] = 1;

        if ( !playActivity.isLightOutOfBounds(x, right) )
            adj_pos_vec[x * NUM_ROWS + right] = 1;

        return adj_pos_vec;
    }

}
