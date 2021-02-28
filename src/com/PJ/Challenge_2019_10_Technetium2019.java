package com.PJ;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * MaxPathFromTheLeftTopCorner
 * Find a maximal value path in a matrix, starting in the top-left
 * corner and ending in the bottom-right corner.
 *
 * ====================
 * === GOLDEN AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/certW5VH5S-WEGCZQ3W9PH7VJZR/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/certW5VH5S-WEGCZQ3W9PH7VJZR/
 *
 * </pre>
 */
class Challenge_2019_10_Technetium2019 {

  @Test
  void test() {
    assertEquals("997952", solution(new int[][]{{9, 9, 7}, {9, 7, 2}, {6, 9, 5}, {9, 1, 2}}));
    assertEquals("111", solution(new int[][]{{1, 1}, {0, 1}}));
    assertEquals("10", solution(new int[][]{{10}}));
  }

  private String solution(final int[][] A) {
    int numberOfRows = A.length;
    int numberOfColumns = A[0].length;

    StringBuilder[][] partialSums = new StringBuilder[numberOfRows][numberOfColumns];

    for (int row = 0; row < numberOfRows; row++) {
      for (int column = 0; column < numberOfColumns; column++) {

        int currentElement = A[row][column];

        if (row == 0 && column == 0) {
          // is first element
          partialSums[row][column] = new StringBuilder().append(currentElement);

        } else if (row == 0) {
          // is first row
          StringBuilder previousElement = new StringBuilder().append(partialSums[row][column - 1]);
          partialSums[row][column] = previousElement.append(currentElement);

        } else if (column == 0) {
          // is first column
          StringBuilder previousElement = new StringBuilder().append(partialSums[row - 1][column]);
          partialSums[row][column] = previousElement.append(currentElement);

        } else {
          // is somewhere in the middle

          if (isUpperElementBigger(partialSums[row - 1][column], partialSums[row][column - 1])) {
            // element from up is bigger
            StringBuilder previousSumElement = new StringBuilder().append(partialSums[row - 1][column]);
            partialSums[row][column] = previousSumElement.append(currentElement);


          } else {
            // element from left is bigger
            StringBuilder previousSumElement = new StringBuilder().append(partialSums[row][column - 1]);
            partialSums[row][column] = previousSumElement.append(currentElement);

          }
        }
      }
    }
    return partialSums[numberOfRows - 1][numberOfColumns - 1].toString();
  }

  private boolean isUpperElementBigger(StringBuilder upperElement, StringBuilder leftElement) {
    final int length = upperElement.length();
    for (int i = 0; i < length; i++) {
      final int numericValueUpper = Character.getNumericValue(upperElement.charAt(i));
      final int numericValueLeft = Character.getNumericValue(leftElement.charAt(i));

      if (numericValueUpper > numericValueLeft) {
        return true;
      } else if (numericValueUpper < numericValueLeft) {
        return false;
      }
    }
    // it means that are exactly the same
    return true;
  }

}
