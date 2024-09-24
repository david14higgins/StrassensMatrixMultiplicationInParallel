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

            int[][] M1 = multiply(add(A11, A22), add(B11, B22));
            int[][] M2 = multiply(add(A21, A22), B11);
            int[][] M3 = multiply(A11, sub(B12, B22));
            int[][] M4 = multiply(A22, sub(B21, B11));
            int[][] M5 = multiply(add(A11, A12), B22);
            int[][] M6 = multiply(sub(A21, A11), add(B11, B12));
            int[][] M7 = multiply(sub(A12, A22), add(B21, B22));

            int [][] C11 = add(sub(add(M1, M4), M5), M7);
            int [][] C12 = add(M3, M5);
            int [][] C21 = add(M2, M4);
            int [][] C22 = add(sub(add(M1, M3), M2), M6);

            // join 4 halves into one result matrix
            join(C11, resultMatrix, 0 , 0);
            join(C12, resultMatrix, 0 , n/2);
            join(C21, resultMatrix, n/2, 0);
            join(C22, resultMatrix, n/2, n/2);
        }
        return resultMatrix;
    }


    // ----- Helper Methods -----

    // Function to sub two matrices
    public int[][] sub(int[][] matrixA, int[][] matrixB)
    {
        int n = matrixA.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = matrixA[i][j] - matrixB[i][j];
        return C;
    }
    // Function to add two matrices
    public int[][] add(int[][] matrixA, int[][] matrixB)
    {
        int n = matrixA.length;
        int[][] resultMatrix = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                resultMatrix[i][j] = matrixA[i][j] + matrixB[i][j];
        return resultMatrix;
    }

    // Method to split parent matrix into child matrices
    public void split(int[][] parentMatrix, int[][] childMatrix, int startParentRowIndex, int startParentColumnIndex)
    {
        for(int childRowIndex = 0, parentRowIndex = startParentRowIndex; childRowIndex < childMatrix.length; childRowIndex++, parentRowIndex++)
            for(int childColumnIndex = 0, parentColumnIndex = startParentColumnIndex; childColumnIndex < childMatrix.length; childColumnIndex++, parentColumnIndex++)
                childMatrix[childRowIndex][childColumnIndex] = parentMatrix[parentRowIndex][parentColumnIndex];
    }

    // Function to join child matrices into parent matrix
    public void join(int[][] childMatrix, int[][] parentMatrix, int startParentRowIndex, int startParentColumnIndex)
    {
        for(int childRowIndex = 0, parentRowIndex = startParentRowIndex; childRowIndex < childMatrix.length; childRowIndex++, parentRowIndex++)
            for(int childColumnIndex = 0, parentColumnIndex = startParentColumnIndex; childColumnIndex < childMatrix.length; childColumnIndex++, parentColumnIndex++)
                parentMatrix[parentRowIndex][parentColumnIndex] = childMatrix[childRowIndex][childColumnIndex];
    }
}
