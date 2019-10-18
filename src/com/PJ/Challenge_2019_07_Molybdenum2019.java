package com.PJ;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * <pre>
 *
 * LeaderSliceInc
 * Given an array, find all its elements that can become a leader, after
 * increasing by 1 all of the numbers in some segment of a given length.
 *
 * ====================
 * === GOLDEN AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/certXNNMJC-MAG6MPC5ERJ46N6C/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/certXNNMJC-MAG6MPC5ERJ46N6C/
 *
 * </pre>
 */
class Challenge_2019_07_Molybdenum2019 {

  @Test
  void test() {
    assertArrayEquals(new int[]{}, solution(3, 3, new int[]{1, 2, 3}));
    assertArrayEquals(new int[]{2}, solution(1, 1, new int[]{1}));
    assertArrayEquals(new int[]{2, 3}, solution(3, 5, new int[]{2, 1, 3, 1, 2, 2, 3}));
    assertArrayEquals(new int[]{2, 3}, solution(4, 2, new int[]{1, 2, 2, 1, 2}));
    assertArrayEquals(new int[]{}, solution(3, 2, new int[]{1, 1, 1, 1, 1, 1}));
    assertArrayEquals(new int[]{1}, solution(3, 2, new int[]{1, 1, 1, 1, 1, 1, 1}));
    assertArrayEquals(new int[]{1}, solution(1, 2, new int[]{1, 1, 1, 1, 1, 1, 1}));
    assertArrayEquals(new int[]{2}, solution(1, 2, new int[]{1}));
    assertArrayEquals(new int[]{8}, solution(3, 10, new int[]{1, 6, 6, 7, 7, 8, 8, 8}));
    assertArrayEquals(new int[]{2}, solution(5, 5, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1}));
    assertArrayEquals(new int[]{10, 11}, solution(4, 20, new int[]{10, 10, 11, 10, 11, 11, 9, 11, 10}));
    assertArrayEquals(new int[]{4, 5}, solution(5, 10, new int[]{4, 2, 4, 4, 1, 4, 5, 3, 5, 5, 3}));
  }

  public int[] solution(final int K, final int M, final int[] A) {
    final int size = A.length;
    final int enough = size/2 + 1;

    int leader1 = -1;
    int leader2 = -1;

    final int[] occurrences = new int[M + 2];
    for (int i = 0; i < size; i++) {
      occurrences[A[i]]++;
    }

    // incrementing first segment of K elements
    for (int i = 0; i < K; i++) {
      occurrences[A[i]]--;
      occurrences[++A[i]]++;
    }

    // checking if a leader exists already
    for (int i = 0; i < M + 2; i++) {
      if (occurrences[i] >= enough) {
        leader1 = i;
        break;
      }
    }

    // traversing one by one until reaching the end of
    // the array or until two unique leaders are found
    for (int i = 0; i < size - K; i++) {
      occurrences[A[i]]--;
      occurrences[--A[i]]++;
      occurrences[A[i + K]]--;
      occurrences[++A[i + K]]++;

      final int candidateFirst = A[i];
      final int candidateLast = A[i + K];

      if (occurrences[candidateLast] >= enough) {
        if (leader1 == -1) {
          leader1 = candidateLast;
        } else if (candidateLast != leader1) {
          leader2 = candidateLast;
        }

        if (leader2 != -1) {
          break;
        }
      } else if (occurrences[candidateFirst] >= enough) {
        if (leader1 == -1) {
          leader1 = candidateFirst;
        } else if (candidateFirst != leader1) {
          leader2 = candidateFirst;
        }

        if (leader2 != -1) {
          break;
        }
      }
    }
    return buildResultArray(leader1, leader2);
  }

  private int[] buildResultArray(final int leader1, final int leader2) {
    if (leader1 == -1 && leader2 == -1) {
      return new int[0];
    } else if (leader2 == -1) {
      return new int[]{leader1};
    } else if (leader1 < leader2) {
      return new int[]{leader1, leader2};
    } else {
      return new int[]{leader2, leader1};
    }
  }

}
