package com.PJ;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * DividingTheKingdom
 * In how many ways can you split a kingdom into two parts,
 * so that the parts contain equal number of gold mines?
 *
 * ====================
 * === SILVER AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/certW8V56S-84QRAUSMNUY8FPYF/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/certW8V56S-84QRAUSMNUY8FPYF/
 *
 * </pre>
 */
@SuppressWarnings("ALL")
public class Challenge_2018_08_DigitalGold {

  @Test
  void test() {
    assertEquals(1, solution(1, 2, new int[]{0, 0}, new int[]{0, 1}));
    assertEquals(2, solution(2, 2, new int[]{0, 1}, new int[]{0, 1}));
    assertEquals(3, solution(5, 5, new int[]{0, 4, 2, 0}, new int[]{0, 0, 1, 4}));
  }

  private int solution(int N, int M, int[] X, int[] Y) {
    int result = 0;
    result += getSolutions(N, X);
    result += getSolutions(M, Y);

    return result;
  }

  private int getSolutions(int dimension, int[] coords) {
    int tempResult = 0;

    for (int i = 0; i < dimension - 1; i++) {
      int minesOneSide = 0;
      int minesOtherSide = 0;
      for (int row : coords) {
        if (row <= i) {
          minesOneSide++;
        } else {
          minesOtherSide++;
        }
      }
      if (minesOtherSide == minesOneSide) {
        tempResult++;
      }
    }
    return tempResult;
  }

}
