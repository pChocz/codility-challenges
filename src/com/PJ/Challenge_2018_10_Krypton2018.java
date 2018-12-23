package com.PJ;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * MinTrailingZeros
 * Find a path in given matrix, such that the product of all
 * the numbers on the path has the minimal number of trailing zeros.
 *
 * ====================
 * === GOLDEN AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/certFZ2AYK-9VYWANC47QQ3GPHZ/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/certFZ2AYK-9VYWANC47QQ3GPHZ/
 *
 * </pre>
 */
class Challenge_2018_10_Krypton2018 {

  @Test
  void test() {
    assertEquals(0, solution(new int[][]{{1, 1}, {0, 1}}));
    assertEquals(1, solution(new int[][]{{10}}));
    assertEquals(1, solution(new int[][]{{1, 2, 0}, {10, 5, 3}, {5, 1, 2}}));
    assertEquals(1, solution(new int[][]{{2, 10, 1, 3}, {10, 5, 4, 5}, {2, 10, 2, 1}, {25, 2, 5, 1}}));
    assertEquals(1, solution(new int[][]{{10, 10, 10}, {10, 0, 10}, {10, 10, 10}}));
    assertEquals(2, solution(new int[][]{{10, 1, 10, 1}, {1, 1, 1, 10}, {10, 1, 10, 1}, {1, 10, 1, 1}}));
  }

  private int solution(int[][] A) {
    int size = A.length;
    boolean containsZero = false;

    int[][][] divisionMatrix = new int[size][size][2];
    int[][][] divisionMatrixSum = new int[size][size][2];

    if (A[0][0] == 0 || A[size - 1][size - 1] == 0) {
      return 1;
    }

    for (int row = 0; row < size; row++) {
      for (int column = 0; column < size; column++) {

        if (A[row][column] == 0) {
          divisionMatrix[row][column][0] = -1;
          divisionMatrix[row][column][1] = -1;
          containsZero = true;
        } else {
          divisionMatrix[row][column] = dividableByTwoAndFive(A[row][column]);
        }

        int[] currentElement = divisionMatrix[row][column];

        if (currentElement[0] == -1) {
          divisionMatrixSum[row][column][0] = -1;
          divisionMatrixSum[row][column][1] = -1;
          continue;
        }

        if (row == 0 & column == 0) {
          divisionMatrixSum[row][column] = divisionMatrix[row][column];

        } else if (row == 0) {
          if (divisionMatrixSum[row][column - 1][0] == -1) {
            divisionMatrixSum[row][column][0] = -1;
            divisionMatrixSum[row][column][1] = -1;

          } else {
            divisionMatrixSum[row][column][0] = divisionMatrixSum[row][column - 1][0] + currentElement[0];
            divisionMatrixSum[row][column][1] = divisionMatrixSum[row][column - 1][1] + currentElement[1];
          }

        } else if (column == 0) {
          if (divisionMatrixSum[row - 1][column][0] == -1) {
            divisionMatrixSum[row][column][0] = -1;
            divisionMatrixSum[row][column][1] = -1;

          } else {
            divisionMatrixSum[row][column][0] = divisionMatrixSum[row - 1][column][0] + currentElement[0];
            divisionMatrixSum[row][column][1] = divisionMatrixSum[row - 1][column][1] + currentElement[1];
          }

        } else {

          int[] rightCandidate = divisionMatrixSum[row][column - 1];
          int[] downCandidate = divisionMatrixSum[row - 1][column];

          if (rightCandidate[0] == -1 && downCandidate[0] != -1) {
            divisionMatrixSum[row][column][0] = downCandidate[0] + currentElement[0];
            divisionMatrixSum[row][column][1] = downCandidate[1] + currentElement[1];
            continue;

          } else if (rightCandidate[0] != -1 && downCandidate[0] == -1) {
            divisionMatrixSum[row][column][0] = rightCandidate[0] + currentElement[0];
            divisionMatrixSum[row][column][1] = rightCandidate[1] + currentElement[1];
            continue;
          }


          int rightCandidateResult = Math.min(rightCandidate[0] + currentElement[0], rightCandidate[1] + currentElement[1]);

          int downCandidateResult = Math.min(downCandidate[0] + currentElement[0], downCandidate[1] + currentElement[1]);

          if (rightCandidateResult < downCandidateResult) {
            divisionMatrixSum[row][column][0] = rightCandidate[0] + currentElement[0];
            divisionMatrixSum[row][column][1] = rightCandidate[1] + currentElement[1];

          } else {
            divisionMatrixSum[row][column][0] = downCandidate[0] + currentElement[0];
            divisionMatrixSum[row][column][1] = downCandidate[1] + currentElement[1];
          }
        }
      }
    }

    int result = Math.min(divisionMatrixSum[size - 1][size - 1][0], divisionMatrixSum[size - 1][size - 1][1]);

    if (result > 0 && containsZero) {
      return 1;
    }

    return result;
  }


  private int[] dividableByTwoAndFive(int value) {
    int[] result = new int[2];

    if (value == 0) {
      return result;
    }

    while (true) {
      if (value%2 != 0 && value%5 != 0) {
        return result;

      } else if (value%2 == 0) {
        result[0]++;
        value /= 2;

      } else if (value%5 == 0) {
        result[1]++;
        value /= 5;

      }
    }
  }

}
