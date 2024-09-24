import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main (String[] args) {
        int[][] matrixA = readMatrixFromFile("8x8_Matrix_A.txt");

        System.out.println("Array A =>");
        printMatrix(matrixA);

        int[][] matrixB = readMatrixFromFile("8x8_Matrix_B.txt");

        System.out.println("Array B =>");
        printMatrix(matrixB);

        long sequentialStartTime = System.nanoTime();
        SequentialSMM sequentialSMM = new SequentialSMM();
        int[][] resultMatrix1 = sequentialSMM.multiply(matrixA, matrixB);
        long sequentialEndTime = System.nanoTime();
        long sequentialExecutionTime = sequentialEndTime - sequentialStartTime;

        long forkStartTime = System.nanoTime();
        ForkJoinPoolSMM forkJoinPoolSMM = new ForkJoinPoolSMM();
        int[][] resultMatrix2 = forkJoinPoolSMM.execute(matrixA, matrixB);
        long forkEndTime = System.nanoTime();
        long forkExecutionTime = forkEndTime - forkStartTime;

        System.out.println("Result Array =>");
        printMatrix(resultMatrix1);
        System.out.println();

        if (forkExecutionTime > sequentialExecutionTime) {
            double multiplier = (double) forkExecutionTime / sequentialExecutionTime;
            System.out.println("Sequential time was " + String.format("%.2f", multiplier) + "x faster");
        } else {
            double multiplier = (double) sequentialExecutionTime / forkExecutionTime;
            System.out.println("ForkJoinPool time was " + String.format("%.2f", multiplier) + "x faster");
        }


    }

    //IO methods
    private static void printMatrix(int[][] matrix){
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[0].length; j++){
                System.out.print(matrix[i][j]+ " ");
            }
            System.out.println();
        }
        System.out.println();
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