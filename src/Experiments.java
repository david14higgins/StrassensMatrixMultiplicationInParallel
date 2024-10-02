public class Experiments {
    public static void SequentialExperiment() {
        //Vary these values
        int currentDimension = 512;
        int granularity = 8;

        SequentialSMM sequentialSMM = new SequentialSMM(granularity);

        long[] executionTimes = new long[10];

        int numPerDimensions = 10;
        for (int i = 0; i < numPerDimensions; i++) {
            int[][] matrixA = MatrixMethods.readMatrixFromFile(currentDimension + "x" + currentDimension + "_matrixA_" + (i + 1) + ".txt");
            int[][] matrixB = MatrixMethods.readMatrixFromFile(currentDimension + "x" + currentDimension + "_matrixB_" + (i + 1) + ".txt");

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

    private static void createMatrixTestSuite(int minDimension, int maxDimension, int numPerDimension) {
        int currentDimension = minDimension;
        while (currentDimension <= maxDimension) {
            for(int i = 0; i < numPerDimension; i++) {
                int[][] matrixA = MatrixMethods.generateRandomMatrix(currentDimension, 0, 100);
                String matrixAFileName = currentDimension + "x" + currentDimension + "_matrixA_" + (i + 1) + ".txt";
                MatrixMethods.writeMatrixToFile(matrixA, matrixAFileName);

                int[][] matrixB = MatrixMethods.generateRandomMatrix(currentDimension, 0, 100);
                String matrixBFileName = currentDimension + "x" + currentDimension + "_matrixB_" + (i + 1) + ".txt";
                MatrixMethods.writeMatrixToFile(matrixB, matrixBFileName);
            }
            currentDimension *= 2;
        }
    }

    private static void outputComparison(long sequentialExecutionTime, long forkExecutionTime) {
        if (forkExecutionTime > sequentialExecutionTime) {
            double multiplier = (double) forkExecutionTime / sequentialExecutionTime;
            System.out.println("Sequential time was " + String.format("%.2f", multiplier) + "x faster");
        } else {
            double multiplier = (double) sequentialExecutionTime / forkExecutionTime;
            System.out.println("ForkJoinPool time was " + String.format("%.2f", multiplier) + "x faster");
        }
    }
}
