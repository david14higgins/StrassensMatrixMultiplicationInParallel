import java.util.concurrent.RecursiveTask;

public class SMMRecursiveTask extends RecursiveTask<int[][]> {

    private final int[][] matrixA;
    private final int[][] matrixB;

    public SMMRecursiveTask(int[][] matrixA, int[][] matrixB, int forkingGranularity) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
    }

    @Override
    protected int[][] compute() {

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

            // Compute submatrices operands using ForkJoinPool
            SMMRecursiveTask subtask1 = new SMMRecursiveTask(add(A11, A22), add(B11, B22));
            subtask1.fork();
            SMMRecursiveTask subtask2 = new SMMRecursiveTask(add(A21, A22), B11);
            subtask2.fork();
            SMMRecursiveTask subtask3 = new SMMRecursiveTask(A11, sub(B12, B22));
            subtask3.fork();
            SMMRecursiveTask subtask4 = new SMMRecursiveTask(A22, sub(B21, B11));
            subtask4.fork();
            SMMRecursiveTask subtask5 = new SMMRecursiveTask(add(A11, A12), B22);
            subtask5.fork();
            SMMRecursiveTask subtask6 = new SMMRecursiveTask(sub(A21, A11), add(B11, B12));
            subtask6.fork();
            SMMRecursiveTask subtask7 = new SMMRecursiveTask(sub(A12, A22), add(B21, B22));
            subtask7.fork();

            int[][] M1 = subtask1.join();
            int[][] M2 = subtask2.join();
            int[][] M3 = subtask3.join();
            int[][] M4 = subtask4.join();
            int[][] M5 = subtask5.join();
            int[][] M6 = subtask6.join();
            int[][] M7 = subtask7.join();

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

// ----- Helper Methods -----

    // Function to sub two matrices
    public int[][] sub(int[][] matrixA, int[][] matrixB)
    {
        int n = matrixA.length;
        int[][] resultMatrix = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                resultMatrix[i][j] = matrixA[i][j] - matrixB[i][j];
        return resultMatrix;
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

