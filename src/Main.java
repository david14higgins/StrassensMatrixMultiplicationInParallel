import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {
    public static void main (String[] args) {
//        int[][] matrixA = readMatrixFromFile("8x8_Matrix_A.txt");
//
//        System.out.println("Array A =>");
//        printMatrix(matrixA);
//
//        int[][] matrixB = readMatrixFromFile("8x8_Matrix_B.txt");
//
//        System.out.println("Array B =>");
//        printMatrix(matrixB);
//
//        long sequentialStartTime = System.nanoTime();
//        SequentialSMM sequentialSMM = new SequentialSMM();
//        int[][] resultMatrix1 = sequentialSMM.multiply(matrixA, matrixB);
//        long sequentialEndTime = System.nanoTime();
//        long sequentialExecutionTime = sequentialEndTime - sequentialStartTime;
//
//        long forkStartTime = System.nanoTime();
//        ForkJoinPoolSMM forkJoinPoolSMM = new ForkJoinPoolSMM();
//        int[][] resultMatrix2 = forkJoinPoolSMM.execute(matrixA, matrixB);
//        long forkEndTime = System.nanoTime();
//        long forkExecutionTime = forkEndTime - forkStartTime;
//
//        System.out.println("Result Array =>");
//        printMatrix(resultMatrix1);
//        System.out.println();
//
//        if (forkExecutionTime > sequentialExecutionTime) {
//            double multiplier = (double) forkExecutionTime / sequentialExecutionTime;
//            System.out.println("Sequential time was " + String.format("%.2f", multiplier) + "x faster");
//        } else {
//            double multiplier = (double) sequentialExecutionTime / forkExecutionTime;
//            System.out.println("ForkJoinPool time was " + String.format("%.2f", multiplier) + "x faster");
//        }
        int[][] generatedMatrixA = generateRandomMatrix(64, 0, 100);
        int[][] generatedMatrixB = generateRandomMatrix(64, 0, 100);

        long sequentialStartTime = System.nanoTime();
        SequentialSMM sequentialSMM = new SequentialSMM();
        int[][] resultMatrix1 = sequentialSMM.multiply(generatedMatrixA, generatedMatrixB);
        long sequentialEndTime = System.nanoTime();
        long sequentialExecutionTime = sequentialEndTime - sequentialStartTime;

        long forkStartTime = System.nanoTime();
        ForkJoinPoolSMM forkJoinPoolSMM = new ForkJoinPoolSMM();
        int[][] resultMatrix2 = forkJoinPoolSMM.execute(generatedMatrixA, generatedMatrixB);
        long forkEndTime = System.nanoTime();
        long forkExecutionTime = forkEndTime - forkStartTime;

        printMatrix(resultMatrix2);

        outputComparison(sequentialExecutionTime, forkExecutionTime);
    }

    // Helper methods
    private static void outputComparison(long sequentialExecutionTime, long forkExecutionTime) {
        if (forkExecutionTime > sequentialExecutionTime) {
            double multiplier = (double) forkExecutionTime / sequentialExecutionTime;
            System.out.println("Sequential time was " + String.format("%.2f", multiplier) + "x faster");
        } else {
            double multiplier = (double) sequentialExecutionTime / forkExecutionTime;
            System.out.println("ForkJoinPool time was " + String.format("%.2f", multiplier) + "x faster");
        }
    }

    // IO methods
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

    // Generates a random matrix where each element is a random value between the upper and lower bound
    private static int[][] generateRandomMatrix(int dimension, int lowerBound, int upperBound) {
        int[][] generatedMatrix = new int[dimension][dimension];
        Random random = new Random();

        for(int i = 0; i < dimension; i++) {
            for(int j = 0; j < dimension; j++) {
                generatedMatrix[i][j] = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
            }
        }

        return generatedMatrix;
    }
}