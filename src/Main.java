import matrixPathFinder.MatrixPathFinder;

public class Main {
    public static void main(String[] args) {
        long start = System.nanoTime();

        MatrixPathFinder.matrixPathFinderExecution();

        long end = System.nanoTime();
        long timeElapsed = end - start;

        System.out.println("\nTotal time elapsed: " + timeElapsed / 1000000 + "ms");
    }
}