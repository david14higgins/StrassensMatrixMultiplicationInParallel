public class SequentialSMM {

    public int[][] multiplyMatrix(int[][] matrixA, int[][] matrixB) {
        int col1 = matrixA[0].length;
        int row1 = matrixA.length;
        int col2 = matrixB[0].length;
        int row2 = matrixB.length;

        if (col1 != row2) {
            System.out.println("\nError: The number of columns in Matrix A  must be equal to the number of rows in Matrix B\n");
            int[][] temp = new int[1][1];
            temp[0][0]=0;
            return temp;
        }

        int[][] resultMatrix = new int[row1][col2];

        // ----- BASE CASE ----- to be modified when modifying granularity 
        if (col1 == 1){
            resultMatrix[0][0] = matrixA[0][0] * matrixB[0][0];
        }else {
            int splitIndex = col1 / 2;

            int[][] resultMatrix_00 = new int[splitIndex][splitIndex];
            int[][] resultMatrix_01 = new int[splitIndex][splitIndex];
            int[][] resultMatrix_10 = new int[splitIndex][splitIndex];
            int[][] resultMatrix_11 = new int[splitIndex][splitIndex];

            int[][] a00 = new int[splitIndex][splitIndex];
            int[][] a01 = new int[splitIndex][splitIndex];
            int[][] a10 = new int[splitIndex][splitIndex];
            int[][] a11 = new int[splitIndex][splitIndex];
            int[][] b00 = new int[splitIndex][splitIndex];
            int[][] b01 = new int[splitIndex][splitIndex];
            int[][] b10 = new int[splitIndex][splitIndex];
            int[][] b11 = new int[splitIndex][splitIndex];

            for (int i = 0; i < splitIndex; i++){
                for (int j = 0; j < splitIndex; j++) {
                    a00[i][j] = matrixA[i][j];
                    a01[i][j] = matrixA[i][j + splitIndex];
                    a10[i][j] = matrixA[splitIndex + i][j];
                    a11[i][j] = matrixA[i + splitIndex][j + splitIndex];
                    b00[i][j] = matrixB[i][j];
                    b01[i][j] = matrixB[i][j + splitIndex];
                    b10[i][j] = matrixB[splitIndex + i][j];
                    b11[i][j] = matrixB[i + splitIndex][j + splitIndex];
                }
            }

            addMatrix(multiplyMatrix(a00, b00), multiplyMatrix(a01, b10),resultMatrix_00, splitIndex);
            addMatrix(multiplyMatrix(a00, b01), multiplyMatrix(a01, b11),resultMatrix_01, splitIndex);
            addMatrix(multiplyMatrix(a10, b00), multiplyMatrix(a11, b10),resultMatrix_10, splitIndex);
            addMatrix(multiplyMatrix(a10, b01), multiplyMatrix(a11, b11),resultMatrix_11, splitIndex);

            for (int i = 0; i < splitIndex; i++){
                for (int j = 0; j < splitIndex; j++) {
                    resultMatrix[i][j] = resultMatrix_00[i][j];
                    resultMatrix[i][j + splitIndex] = resultMatrix_01[i][j];
                    resultMatrix[splitIndex + i][j] = resultMatrix_10[i][j];
                    resultMatrix[i + splitIndex] [j + splitIndex] = resultMatrix_11[i][j];
                }
            }
        }
        return resultMatrix;
    }

    //Helper Methods
    private static void addMatrix(int[][] matrixA,int[][] matrixB,int[][] resultMatrix, int splitIndex)
    {
        for (int i = 0; i < splitIndex; i++){
            for (int j = 0; j < splitIndex; j++){
                resultMatrix[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
    }
}
