package com.PJ;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * PascalTriangle
 * Compute the number of "True" values in an OR-Pascal-triangle structure.
 *
 * ===================
 * ======  90%  ======
 * ===================
 *
 * solution report:
 * - https://app.codility.com/demo/results/training9CV2EY-H2W/
 *
 * </pre>
 */
public class Challenge_2018_03_Nickel2018 {

  private static int MAX_RESULT = 1_000_000_000;
  private static int NODES_FOR_MAX_RESULT = 44720;

  @Test
  void test() {
    // regular cases
    assertEquals(7, solution(new boolean[]{true, false, false, true}));
    assertEquals(11, solution(new boolean[]{true, false, false, true, false}));
    assertEquals(8, solution(new boolean[]{true, false, true, false}));

    // corner cases
    assertEquals(0, solution(new boolean[]{false, false, false, false}));
    assertEquals(10, solution(new boolean[]{true, true, true, true}));
  }

  @Test
  void test_huge_corner_cases() {
    boolean[] bigArray = new boolean[4_000];
    Arrays.fill(bigArray, true);
    assertEquals(8_002_000, solution(bigArray));

    boolean[] largeArray = new boolean[10_000];
    Arrays.fill(largeArray, true);
    assertEquals(50_005_000, solution(largeArray));

    boolean[] hugeArray = new boolean[50_000];
    Arrays.fill(hugeArray, true);
    assertEquals(MAX_RESULT, solution(hugeArray));

    boolean[] hugeArrayFalse = new boolean[50_000];
    Arrays.fill(hugeArrayFalse, false);
    assertEquals(0, solution(hugeArrayFalse));
  }

  private int solution(boolean[] P) {
    int result = 0;

    for (int i = 0; i < P.length; i++) {
      if (P[i]) {
        result++;
      }
    }
    result += calculateRow(P);

    return (result >= 0 && result < MAX_RESULT) ? result : MAX_RESULT;
  }


  private int calculateRow(boolean[] row) {
    int result = 0;

    boolean[] upperRow = new boolean[row.length - 1];

    for (int i = 0; i < row.length - 1; i++) {
      boolean product = row[i] || row[i + 1];
      upperRow[i] = product;
      if (product) {
        result++;
      }
    }

    if (result == 0) {
      return result;

    } else if (result == row.length - 1) {
      if (result > NODES_FOR_MAX_RESULT) {
        return MAX_RESULT;
      } else {
        return result + countAllNodesAbove(result);
      }

    } else {
      result += calculateRow(upperRow);

      if (result > MAX_RESULT) {
        return MAX_RESULT;
      }
    }

    return result;
  }

  private int countAllNodesAbove(int count) {
    if (count > NODES_FOR_MAX_RESULT) {
      return MAX_RESULT;
    } else {
      return (count)*(count - 1)/2;
    }
  }

}
