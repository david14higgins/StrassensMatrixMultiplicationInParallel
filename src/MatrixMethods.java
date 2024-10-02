import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixMethods {
    // Generates a random matrix where each element is a random value between the upper and lower bound
    public static int[][] generateRandomMatrix(int dimension, int lowerBound, int upperBound) {
        int[][] generatedMatrix = new int[dimension][dimension];
        Random random = new Random();

        for(int i = 0; i < dimension; i++) {
            for(int j = 0; j < dimension; j++) {
                generatedMatrix[i][j] = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
            }
        }
        return generatedMatrix;
    }

    public static void printMatrix(int[][] matrix){
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[0].length; j++){
                System.out.print(matrix[i][j]+ " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int[][] readMatrixFromFile(String filename) {
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

    public static void writeMatrixToFile(int[][] matrix, String filename) {
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
}
