import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {
    public static void main (String[] args) {

        int currentDimension = 2048;
        int granularity = 4;

        SequentialSMM sequentialSMM = new SequentialSMM(granularity);

        long[] executionTimes = new long[10];

        int numPerDimensions = 10;
        for (int i = 0; i < numPerDimensions; i++) {
            int[][] matrixA = readMatrixFromFile(currentDimension + "x" + currentDimension + "_matrixA_" + (i + 1) + ".txt");
            int[][] matrixB = readMatrixFromFile(currentDimension + "x" + currentDimension + "_matrixB_" + (i + 1) + ".txt");

            long startTime = System.nanoTime();
            int[][] resultMatrix = sequentialSMM.multiply(matrixA, matrixB);
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            System.out.println("Execution done");
            executionTimes[i] = executionTime;
        }

        long totalExecutionTime = 0;
        for (long executionTime : executionTimes) {
            System.out.println("Execution Time: " + executionTime);
            totalExecutionTime += executionTime;
        }
        long averageExecutionTime = totalExecutionTime / 10;

        System.out.println("Average Execution Time: " + averageExecutionTime);

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

    private static void writeMatrixToFile(int[][] matrix, String filename) {
        String filepath = "MatricesTextfiles/" + filename;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (int[] row : matrix) {
                for (int i = 0; i < row.length; i++) {
                    writer.write(Integer.toString(row[i]));
                    if (i < row.length - 1) {
                        writer.write(" "); // Add space between columns
                    }
                }
                writer.newLine(); // Move to the next line for the next row
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing the matrix to the file.");
            throw new RuntimeException(e);
        }
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

    private static void createMatrixTestSuite(int minDimension, int maxDimension, int numPerDimension) {
        int currentDimension = minDimension;
        while (currentDimension <= maxDimension) {
            for(int i = 0; i < numPerDimension; i++) {
                int[][] matrixA = generateRandomMatrix(currentDimension, 0, 100);
                String matrixAFileName = currentDimension + "x" + currentDimension + "_matrixA_" + (i + 1) + ".txt";
                writeMatrixToFile(matrixA, matrixAFileName);

                int[][] matrixB = generateRandomMatrix(currentDimension, 0, 100);
                String matrixBFileName = currentDimension + "x" + currentDimension + "_matrixB_" + (i + 1) + ".txt";
                writeMatrixToFile(matrixB, matrixBFileName);
            }
            currentDimension *= 2;
        }
    }
}