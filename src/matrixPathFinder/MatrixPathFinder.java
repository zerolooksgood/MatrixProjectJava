package matrixPathFinder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class MatrixPathFinder {

    public static void matrixPathFinderExecution() {
        int[][] originalMatrix = matrixGenerator(null, null, null, null);

        int[][] shortestPathMatrix = calculateMatrix(originalMatrix, true);
        List<Integer[]> shortestPath = findPath(shortestPathMatrix, true);

        int[][] longestPathMatrix = calculateMatrix(originalMatrix, false);
        List<Integer[]> longestPath = findPath(longestPathMatrix, false);

        displayMatrix(originalMatrix, null);
        displayMatrix(originalMatrix, shortestPath);
        displayMatrix(originalMatrix, longestPath);
    }

    private static int[][] matrixGenerator(Integer height, Integer width, Integer minValue, Integer maxValue) {
        height = height == null ? (int) (Math.random() * 10 + 2) : height;
        width = width == null ? (int) (Math.random() * 10 + 2) : width;
        minValue = minValue == null ? 1 : minValue;
        maxValue = maxValue == null ? 100 : maxValue;

        if (height < 2 || width < 2) { throw new InvalidParameterException("The height and width of the matrix have to be at least 2"); }
        if (minValue >= maxValue) { throw new InvalidParameterException("The minimum value has to be lower than the maximum value"); }

        int[][] matrix = new int[height][width];

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = (int) (Math.random() * (maxValue - minValue)) + minValue;
            }
        }

        return matrix;
    }

    private static void displayMatrix(int[][] matrix, List<Integer[]> path) {
        validateMatrix(matrix);

        int maxNumLength = 0;
        for (int[] row : matrix) {
            for (int num : row) {
                maxNumLength = Math.max(maxNumLength, String.valueOf(num).length());
            }
        }

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                StringBuilder formattedText = new StringBuilder(String.valueOf(matrix[row][col]));
                while (formattedText.length() < maxNumLength) {
                    formattedText.insert(0, " ");
                }

                if (pathContainsPos(path, new int[] {row, col})) {
                    System.out.printf("\u001B[32m[%s]\u001B[0m", formattedText);
                } else {
                    System.out.printf("[%s]", formattedText);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean pathContainsPos(List<Integer[]> path, int[] pos) {
        if (path == null) { return false; }
        validatePath(path);
        if (pos.length != 2) { throw new InvalidParameterException("Provided position is not two dimensional"); }

        for (Integer[] pathPos : path) {
            if (pathPos[0] == pos[0] && pathPos[1] == pos[1]) { return true; }
        }
        return false;
    }

    private static int[][] calculateMatrix(int[][] matrix, boolean shortestPath) {
        validateMatrix(matrix);

        int[][] outMatrix = new int[matrix.length][matrix[0].length];
        for (int row = 0; row < outMatrix.length; row++) {
            System.arraycopy(matrix[row], 0, outMatrix[row], 0, outMatrix[row].length);
        }

        for (int row = 1; row < outMatrix.length; row++) {
            outMatrix[row][0] += outMatrix[row - 1][0];
        }

        for (int col = 1; col < outMatrix[0].length; col++) {
            outMatrix[0][col] += outMatrix[0][col - 1];
        }

        for (int row = 1; row < outMatrix.length; row++) {
            for (int col = 1; col < outMatrix[row].length; col++) {
                int numAbove = outMatrix[row - 1][col];
                int numLeft = outMatrix[row][col - 1];

                outMatrix[row][col] += shortestPath ? Math.min(numAbove, numLeft) : Math.max(numAbove, numLeft);
            }
        }

        return outMatrix;
    }

    private static List<Integer[]> findPath(int[][] matrix, boolean shortestPath) {
        validateMatrix(matrix);

        List<Integer[]> path = new ArrayList<>();

        int row = matrix.length - 1;
        int col = matrix[0].length - 1;

        while (row != 0 || col != 0) {
            path.add(new Integer[] {row, col});

            if (row == 0) {
                col --;
            } else if (col == 0) {
                row--;
            } else if ((matrix[row][col - 1] <= matrix[row -1][col]) == shortestPath) {
                col--;
            } else {
                row--;
            }
        }

        path.add(new Integer[] {0, 0});

        return path.reversed();
    }

    private static void validateMatrix(int[][] matrix) {
        int referenceSize = matrix[0].length;
        for (int row = 0; row < matrix.length; row++) {
            if (matrix[row].length != referenceSize) {
                throw new InvalidParameterException(String.format("Row number [%d] in the matrix deviates from the reference length of [%d] by being [%d] long", row, referenceSize, matrix[row].length));
            }
        }

        if (matrix.length < 2) {
            throw new InvalidParameterException("The height of the matrix was smaller than 2");
        }
        for (int row = 0; row < matrix.length; row++) {
            if (matrix[row].length < 2) {
                throw new InvalidParameterException(String.format("The width of row [%d] in the matrix was smaller than 2", row));
            }
        }
    }

    private static void validatePath(List<Integer[]> path) {
        for (Integer[] pos : path) {
            if (pos.length != 2) {
                throw new InvalidParameterException(String.format("Position with index [%d] in the path is not a two dimensional position", path.indexOf(pos)));
            }
        }

        for (Integer[] pos : path) {
            if (pos[0] < 0 || pos[1] < 0) {
                throw new InvalidParameterException(String.format("Position with index [%d] in the path contains negative coordinates", path.indexOf(pos)));
            }
        }
    }
}
