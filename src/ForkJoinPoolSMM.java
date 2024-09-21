import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolSMM {

    public int[][] execute(int[][] matrixA, int[][] matrixB) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        SMMRecursiveTask smmRecursiveTask = new SMMRecursiveTask(matrixA, matrixB);

        return forkJoinPool.invoke(smmRecursiveTask);
    }


}
