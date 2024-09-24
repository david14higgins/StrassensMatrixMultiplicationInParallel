public class SequentialSMM {
    public int[][] multiply(int[][] matrixA, int[][] matrixB)
    {
        int n = matrixA.length;
        int[][] resultMatrix = new int[n][n];

        // Base Case
        if (n == 1)
            resultMatrix[0][0] = matrixA[0][0] * matrixB[0][0];
        // General Case, apply recursion
        else
        {
            int[][] A11 = new int[n/2][n/2];
            int[][] A12 = new int[n/2][n/2];
            int[][] A21 = new int[n/2][n/2];
            int[][] A22 = new int[n/2][n/2];
            int[][] B11 = new int[n/2][n/2];
            int[][] B12 = new int[n/2][n/2];
            int[][] B21 = new int[n/2][n/2];
            int[][] B22 = new int[n/2][n/2];

            // Dividing matrix A
            split(matrixA, A11, 0 , 0);
            split(matrixA, A12, 0 , n/2);
            split(matrixA, A21, n/2, 0);
            split(matrixA, A22, n/2, n/2);

            // Dividing matrix B
            split(matrixB, B11, 0 , 0);
            split(matrixB, B12, 0 , n/2);
            split(matrixB, B21, n/2, 0);
            split(matrixB, B22, n/2, n/2);

            // Compute submatrices operands
            int[][] M1 = multiply(add(A11, A22), add(B11, B22));
            int[][] M2 = multiply(add(A21, A22), B11);
            int[][] M3 = multiply(A11, sub(B12, B22));
            int[][] M4 = multiply(A22, sub(B21, B11));
            int[][] M5 = multiply(add(A11, A12), B22);
            int[][] M6 = multiply(sub(A21, A11), add(B11, B12));
            int[][] M7 = multiply(sub(A12, A22), add(B21, B22));

            // Compute submatrices
            int [][] C11 = add(sub(add(M1, M4), M5), M7);
            int [][] C12 = add(M3, M5);
            int [][] C21 = add(M2, M4);
            int [][] C22 = add(sub(add(M1, M3), M2), M6);

            // join 4 submatrices into result matrix
            join(C11, resultMatrix, 0 , 0);
            join(C12, resultMatrix, 0 , n/2);
            join(C21, resultMatrix, n/2, 0);
            join(C22, resultMatrix, n/2, n/2);
        }
        return resultMatrix;
    }

    private int[][] naiveMultiply(int[][] matrixA, int[][] matrixB) {
        // Check if matrices are compatible for multiplication
        if (matrixA[0].length != matrixB.length) {
            throw new IllegalArgumentException("Matrix A's columns must match Matrix B's rows for multiplication.");
        }

        int[][] resultMatrix = new int[matrixA.length][matrixB[0].length];

        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                //Intialise cell of result matrix
                resultMatrix[i][j] = 0;
                for (int k = 0; k < matrixA[0].length; k++) {
                    resultMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }

        return resultMatrix;
    }
    

    // ----- Helper Methods -----

    // Function to sub two matrices
    private int[][] sub(int[][] matrixA, int[][] matrixB)
    {
        int n = matrixA.length;
        int[][] resultMatrix = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                resultMatrix[i][j] = matrixA[i][j] - matrixB[i][j];
        return resultMatrix;
    }
    // Function to add two matrices
    private int[][] add(int[][] matrixA, int[][] matrixB)
    {
        int n = matrixA.length;
        int[][] resultMatrix = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                resultMatrix[i][j] = matrixA[i][j] + matrixB[i][j];
        return resultMatrix;
    }

    // Method to split parent matrix into child matrices
    private void split(int[][] parentMatrix, int[][] childMatrix, int startParentRowIndex, int startParentColumnIndex)
    {
        for(int childRowIndex = 0, parentRowIndex = startParentRowIndex; childRowIndex < childMatrix.length; childRowIndex++, parentRowIndex++)
            for(int childColumnIndex = 0, parentColumnIndex = startParentColumnIndex; childColumnIndex < childMatrix.length; childColumnIndex++, parentColumnIndex++)
                childMatrix[childRowIndex][childColumnIndex] = parentMatrix[parentRowIndex][parentColumnIndex];
    }

    // Function to join child matrices into parent matrix
    private void join(int[][] childMatrix, int[][] parentMatrix, int startParentRowIndex, int startParentColumnIndex)
    {
        for(int childRowIndex = 0, parentRowIndex = startParentRowIndex; childRowIndex < childMatrix.length; childRowIndex++, parentRowIndex++)
            for(int childColumnIndex = 0, parentColumnIndex = startParentColumnIndex; childColumnIndex < childMatrix.length; childColumnIndex++, parentColumnIndex++)
                parentMatrix[parentRowIndex][parentColumnIndex] = childMatrix[childRowIndex][childColumnIndex];
    }
}
