import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main (String[] args) {
//        int[][] matrix_A = { { 2, 4, 6, 8, 10, 12, 14, 16 },
//                { 1, 3, 5, 7, 9, 11, 13, 15 },
//                { 4, 6, 8, 10, 12, 14, 16, 2 },
//                { 3, 5, 7, 9, 11, 13, 15, 1 },
//                { 6, 8, 10, 12, 14, 16, 2, 4 },
//                { 5, 7, 9, 11, 13, 15, 1, 3 },
//                { 8, 10, 12, 14, 16, 2, 4, 6 },
//                { 7, 9, 11, 13, 15, 1, 3, 5 }};

        int[][] matrix_A = readMatrixFromFile("8x8_Matrix_A.txt");

        System.out.println("Array A =>");
        printMat(matrix_A);

//        int[][] matrix_B = { { 10, 12, 14, 16, 2, 4, 6, 8 },
//                { 9, 11, 13, 15, 1, 3, 5, 7 },
//                { 12, 14, 16, 2, 4, 6, 8, 10 },
//                { 11, 13, 15, 1, 3, 5, 7, 9 },
//                { 14, 16, 2, 4, 6, 8, 10, 12 },
//                { 13, 15, 1, 3, 5, 7, 9, 11 },
//                { 16, 2, 4, 6, 8, 10, 12, 14 },
//                { 15, 1, 3, 5, 7, 9, 11, 13 }};

        int[][] matrix_B = readMatrixFromFile("8x8_Matrix_B.txt");

        System.out.println("Array B =>");
        printMat(matrix_B);

        int[][] result_matrix =  multiply_matrix(matrix_A, matrix_B);

        System.out.println("Result Array =>");
        printMat(result_matrix);
    }
    private static int[][] multiply_matrix(int[][] matrix_A,int[][] matrix_B)
    {
        int col_1 = matrix_A[0].length;
        int row_1 = matrix_A.length;
        int col_2 = matrix_B[0].length;
        int row_2 = matrix_B.length;

        if (col_1 != row_2) {
            System.out.println("\nError: The number of columns in Matrix A  must be equal to the number of rows in Matrix B\n");
            int[][] temp = new int[1][1];
            temp[0][0]=0;
            return temp;
        }

        int[][] result_matrix = new int[row_1][col_2];
        initWithZeros(result_matrix,row_1,col_2);

        if (col_1 == 1){
            result_matrix[0][0] = matrix_A[0][0] * matrix_B[0][0];
        }else {
            int split_index = col_1 / 2;

            int[][] result_matrix_00 = new int[split_index][split_index];
            int[][] result_matrix_01 = new int[split_index][split_index];
            int[][] result_matrix_10 = new int[split_index][split_index];
            int[][] result_matrix_11 = new int[split_index][split_index];
            initWithZeros(result_matrix_00,split_index,split_index);
            initWithZeros(result_matrix_01,split_index,split_index);
            initWithZeros(result_matrix_10,split_index,split_index);
            initWithZeros(result_matrix_11,split_index,split_index);

            int[][] a00 = new int[split_index][split_index];
            int[][] a01 = new int[split_index][split_index];
            int[][] a10 = new int[split_index][split_index];
            int[][] a11 = new int[split_index][split_index];
            int[][] b00 = new int[split_index][split_index];
            int[][] b01 = new int[split_index][split_index];
            int[][] b10 = new int[split_index][split_index];
            int[][] b11 = new int[split_index][split_index];
            initWithZeros(a00,split_index,split_index);
            initWithZeros(a01,split_index,split_index);
            initWithZeros(a10,split_index,split_index);
            initWithZeros(a11,split_index,split_index);
            initWithZeros(b00,split_index,split_index);
            initWithZeros(b01,split_index,split_index);
            initWithZeros(b10,split_index,split_index);
            initWithZeros(b11,split_index,split_index);


            for (int i = 0; i < split_index; i++){
                for (int j = 0; j < split_index; j++) {
                    a00[i][j] = matrix_A[i][j];
                    a01[i][j] = matrix_A[i][j + split_index];
                    a10[i][j] = matrix_A[split_index + i][j];
                    a11[i][j] = matrix_A[i + split_index][j + split_index];
                    b00[i][j] = matrix_B[i][j];
                    b01[i][j] = matrix_B[i][j + split_index];
                    b10[i][j] = matrix_B[split_index + i][j];
                    b11[i][j] = matrix_B[i + split_index][j + split_index];
                }
            }

            add_matrix(multiply_matrix(a00, b00),multiply_matrix(a01, b10),result_matrix_00, split_index);
            add_matrix(multiply_matrix(a00, b01),multiply_matrix(a01, b11),result_matrix_01, split_index);
            add_matrix(multiply_matrix(a10, b00),multiply_matrix(a11, b10),result_matrix_10, split_index);
            add_matrix(multiply_matrix(a10, b01),multiply_matrix(a11, b11),result_matrix_11, split_index);

            for (int i = 0; i < split_index; i++){
                for (int j = 0; j < split_index; j++) {
                    result_matrix[i][j] = result_matrix_00[i][j];
                    result_matrix[i][j + split_index] = result_matrix_01[i][j];
                    result_matrix[split_index + i][j] = result_matrix_10[i][j];
                    result_matrix[i + split_index] [j + split_index] = result_matrix_11[i][j];
                }
            }
        }
        return result_matrix;
    }

    //Helper methods
    private static void printMat(int[][] a){
        for(int i=0; i<a.length;i++){
            for(int j=0;j<a[0].length;j++){
                System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void add_matrix(int[][] matrix_A,int[][] matrix_B,int[][] matrix_C, int split_index)
    {
        for (int i = 0; i < split_index; i++){
            for (int j = 0; j < split_index; j++){
                matrix_C[i][j] = matrix_A[i][j] + matrix_B[i][j];
            }
        }
    }

    private static void initWithZeros(int a[][], int r, int c){
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                a[i][j]=0;
            }
        }
    }

    private static int[][] readMatrixFromFile(String filename) {
        String filepath = "MatricesTextfiles/" + filename;

        List<int[]> matrixRows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;

            while((line = br.readLine()) != null){
                String[] rowStr = line.trim().split("\\s+");

                //Convert to integers
                int[] rowInt = new int[rowStr.length];
                for (int i = 0; i < rowStr.length; i++ ) {
                    rowInt[i] = Integer.parseInt(rowStr[i]);
                }

                //Add to matrix rows
                matrixRows.add(rowInt);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Convert the list of rows to a 2d array
        int[][] matrix = new int[matrixRows.size()][];
        matrix = matrixRows.toArray(matrix);

        return matrix;
    }




}