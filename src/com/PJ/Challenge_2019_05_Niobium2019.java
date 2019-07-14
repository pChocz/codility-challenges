package com.PJ;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * FlippingMatrix
 * A matrix of binary values is given. We can flip the values in selected columns.
 * What is the maximum number of rows that we can obtain that contain all the same values?
 *
 * ====================
 * === SILVER AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/certAZ4ETN-2AN4SBTDPV3WE3ZC/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/certAZ4ETN-2AN4SBTDPV3WE3ZC/
 *
 * </pre>
 */
class Challenge_2019_05_Niobium2019 {

  @Test
  void test() {
    assertEquals(3, solution(new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}));
    assertEquals(3, solution(new int[][]{{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}}));
    assertEquals(2, solution(new int[][]{{0, 0, 0, 0}, {0, 1, 0, 0}, {1, 0, 1, 1}}));
    assertEquals(4, solution(new int[][]{{0, 1, 0, 1}, {1, 0, 1, 0}, {0, 1, 0, 1}, {1, 0, 1, 0}}));
  }

  public int solution(int[][] A) {
    int result = 0;
    int numberOfColumns = A[0].length;
    int numberOfRows = A.length;
    Map<Row, Integer> rows = new HashMap<>();

    for (int i = 0; i < numberOfRows; i++) {
      Row row = new Row(numberOfColumns);

      for (int j = 0; j < numberOfColumns; j++) {
        boolean currentItem = (A[i][j] == 1);
        row.items.add(currentItem);
      }

      int count = rows.getOrDefault(row, 0);
      rows.put(row, count + 1);
      if (count + 1 > result) {
        result = count + 1;
      }
    }
    return result;
  }

  class Row {
    List<Boolean> items;

    Row(int size) {
      this.items = new ArrayList<>(size);
    }

    @Override
    public boolean equals(Object obj) {
      Row row = (Row) obj;

      int equalses = 0;
      int nonEqualses = 0;

      for (int i = 0; i < this.items.size(); i++) {
        if (row.items.get(i) == this.items.get(i)) {
          equalses++;
        } else {
          nonEqualses++;
        }
        if (equalses > 0 && nonEqualses > 0) {
          return false;
        }
      }
      return true;
    }

    @Override
    public int hashCode() {
      return this.items.size();
    }
  }

}