package com.PJ;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * SheepAndSunshades
 * Given a set of points on a cartesian plane, find the minimum
 * distance between some pair of them to maximise another metric.
 *
 * ====================
 * === SILVER AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/cert8GGX7N-TDD3XHT4HXG58JSP/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/cert8GGX7N-TDD3XHT4HXG58JSP/
 *
 * </pre>
 */
class Challenge_2018_12_Rubidium2018 {

  private final DecimalFormat df = new DecimalFormat("#0.0");

  @Test
  void test_zero() {
    assertEquals(0, solution(new int[]{0, 1}, new int[]{0, 1}));
    assertEquals(0, solution(new int[]{1, 5, 2, 30, 1}, new int[]{1, 5, 10, 80, 2}));
    assertEquals(0, solution(new int[]{0, 0, 100, 2, 9}, new int[]{0, 1, 45, 3, 9}));
    assertEquals(0, solution(new int[]{0, 10, 20, 29, 30, 40, 50}, new int[]{1, 1, 1, 1, 1, 1, 1}));
    assertEquals(0, solution(new int[]{0, 10, 20, 28, 29, 30, 40, 50}, new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
  }

  @Test
  void test_regular() {
    assertEquals(1, solution(new int[]{1, 3, 2, 30}, new int[]{1, 3, 10, 80}));
    assertEquals(1, solution(new int[]{1, 4, 2, 30}, new int[]{1, 4, 10, 80}));
    assertEquals(1, solution(new int[]{1, 1, 1, 1, 3}, new int[]{1, 4, 7, 10, 3}));
    assertEquals(1, solution(new int[]{1, 1, 1, 1, 3, 5}, new int[]{1, 4, 7, 10, 3, 1}));
    assertEquals(2, solution(new int[]{1, 1, 8}, new int[]{1, 6, 0}));
    assertEquals(2, solution(new int[]{1, 8, 1}, new int[]{1, 0, 6}));
    assertEquals(2, solution(new int[]{1, 5, 2, 30, 10}, new int[]{1, 5, 10, 80, 10}));
    assertEquals(2, solution(new int[]{1, 1, 1, 1, 1, 1, 1, 1}, new int[]{1, 5, 9, 13, 17, 100, 21, 40}));
    assertEquals(2, solution(new int[]{4, 5, 0, 12, 9}, new int[]{12, 5, 0, 4, 17}));
    assertEquals(2, solution(new int[]{4, 5, 0, 15, 12}, new int[]{12, 5, 0, 20, 4}));
    assertEquals(2, solution(new int[]{5, 4, 0, 15, 12}, new int[]{5, 12, 0, 20, 4}));
    assertEquals(2, solution(new int[]{9, 4, 5, 0, 12, 9}, new int[]{9, 12, 5, 0, 4, 17}));
    assertEquals(4, solution(new int[]{1, 13, 3, 43, 21, 94, 19, 1}, new int[]{1, 51, 19, 13, 37, 100, 28, 40}));
  }

  /**
   * Verifies algorithm for large case - 10k of random points. It calculates the result based on
   * the divide and conquer algorithm as well as based on O(n^2) brute force algorithm and prints
   * time elapsed for both cases for comparison.
   * Assertion is based on comparison of a result from both given algorithms.
   *
   * @throws IOException when problem occurs when processing the file
   */
  @Test
  void test_performance_10k() throws IOException {
    String content = Files.readString(Path.of(System.getProperty("user.dir") + File.separator + "10k_points_unsorted_and_sorted.txt"));
    String[] contentArraySplit = content.split("\n");

    int[] unsortedX = Arrays.stream(contentArraySplit[0].split(",")).mapToInt(str -> Integer.parseInt(str.trim())).toArray();
    int[] unsortedY = Arrays.stream(contentArraySplit[1].split(",")).mapToInt(str -> Integer.parseInt(str.trim())).toArray();

    long startTimeDC = System.currentTimeMillis();
    int resultDC = solution(unsortedX, unsortedY);
    long elapsedTimeDC = System.currentTimeMillis() - startTimeDC;
    System.out.println("elapsed time DC = " + elapsedTimeDC + " ms\n");

    long startTimeBF = System.currentTimeMillis();
    int resultBF = solution(unsortedX, unsortedY, true);
    long elapsedTimeBF = System.currentTimeMillis() - startTimeBF;
    System.out.println("elapsed time BF = " + elapsedTimeBF + " ms\n");
    System.out.println("which means it's " + df.format(elapsedTimeBF/elapsedTimeDC) + " times faster with DC algorithm than with BF.\n");

    assertEquals(resultBF, resultDC);
  }

  private int solution(int[] X, int[] Y) {
    return solution(X, Y, false);
  }

  private int solution(int[] X, int[] Y, boolean bruteForceOnly) {
    int numberOfSheep = X.length;
    List<Sheep> sheepList = new ArrayList<>(numberOfSheep);

    for (int i = 0; i < numberOfSheep; i++) {
      sheepList.add(new Sheep(X[i], Y[i]));
    }
    sheepList.sort(Sheep.X_COMPARATOR);

    SunshadeBetweenSheep minimumDistance = minimumSpanFromList(sheepList, numberOfSheep, bruteForceOnly);
    if (minimumDistance == null) {
      System.out.println("result is 0");
      return 0;
    }

    System.out.println(minimumDistance);
    return minimumDistance.sunshadeSpan;
  }

  /**
   * Main method of the entire algorithm, that is called recursively based on the
   * divide and conquer algorithm (or brute force if specific parameter is passed).
   * Returns NULL if and only if the final result is "0".
   *
   * @param sheep          list of sheep to check
   * @param size           size of the given list
   * @param bruteForceOnly TRUE if we want to use brute force solution instead of divide and conquer
   * @return Sunshade between sheep as customized object
   */
  private SunshadeBetweenSheep minimumSpanFromList(List<Sheep> sheep, int size, boolean bruteForceOnly) {
    if (bruteForceOnly || size < 4) {
      return nSquareShortestDistance(sheep, size);
    }

    int xMedian = sheep.get(size/2).x;
    List<Sheep> sheepLeft = new ArrayList<>(sheep.subList(0, size/2));
    List<Sheep> sheepRight = new ArrayList<>(sheep.subList(size/2, size));

    SunshadeBetweenSheep minSpanLeft = minimumSpanFromList(sheepLeft, sheepLeft.size(), false);
    if (minSpanLeft == null) {
      return null;
    }

    SunshadeBetweenSheep minSpanRight = minimumSpanFromList(sheepRight, sheepRight.size(), false);
    if (minSpanRight == null) {
      return null;
    }

    SunshadeBetweenSheep minSpan = (minSpanLeft.sunshadeSpan < minSpanRight.sunshadeSpan) ? minSpanLeft : minSpanRight;
    double stripSpan = getDistanceValue(minSpan.sheep1, minSpan.sheep2);

    List<Sheep> sheepStrip = new ArrayList<>();

    for (int i = sheepLeft.size() - 1; i >= 0; i--) {
      if (sheepLeft.get(i).x < xMedian - stripSpan) {
        break;
      }
      sheepStrip.add(sheepLeft.get(i));
    }

    for (int i = 0; i <= sheepRight.size() - 1; i++) {
      if (sheepRight.get(i).x > xMedian + stripSpan) {
        break;
      }
      sheepStrip.add(sheepRight.get(i));
    }

    if (sheepStrip.isEmpty()) {
      return minSpan;
    }

    sheepStrip.sort(Sheep.Y_COMPARATOR);
    return getMinimumSpanIncludingStrip(sheepStrip, sheepStrip.size(), minSpan, stripSpan);
  }

  private SunshadeBetweenSheep getMinimumSpanIncludingStrip(List<Sheep> sheepStrip, int size, SunshadeBetweenSheep minDistance, double stripSpan) {
    int currentSunshadeSpan = minDistance.sunshadeSpan;
    int sheepId1 = -1;
    int sheepId2 = -1;

    for (int i = 0; i < size; i++) {
      for (int j = i + 1; (j < size) && (sheepStrip.get(j).y - sheepStrip.get(i).y < stripSpan); j++) {
        int currentSunshadeSpanStrip = getSunshadeSpanValue(sheepStrip.get(i), sheepStrip.get(j));

        if (currentSunshadeSpan == 0) {
          return null;

        } else if (currentSunshadeSpanStrip < currentSunshadeSpan) {
          sheepId1 = i;
          sheepId2 = j;
          currentSunshadeSpan = currentSunshadeSpanStrip;
        }
      }
    }

    if (sheepId1 != -1) {
      return new SunshadeBetweenSheep(sheepStrip.get(sheepId1), sheepStrip.get(sheepId2), currentSunshadeSpan);

    } else {
      return minDistance;
    }
  }


  /**
   * Brute force - if there are only 2 or 3 points in given list, there is no need to divide and conquer
   * and it's ok just to calculate all possible distances.
   */
  private SunshadeBetweenSheep nSquareShortestDistance(List<Sheep> sheep, int numberOfSheep) {
    int sheep1Id = -1;
    int sheep2Id = -1;
    int minSunshadeSpan = Integer.MAX_VALUE;

    for (int i = 0; i < numberOfSheep; i++) {
      for (int j = i + 1; j < numberOfSheep; j++) {
        int currentSunshadeSpan = getSunshadeSpanValue(sheep.get(i), sheep.get(j));

        if (currentSunshadeSpan == 0) {
          return null;

        } else if (currentSunshadeSpan < minSunshadeSpan) {
          sheep1Id = i;
          sheep2Id = j;
          minSunshadeSpan = currentSunshadeSpan;
        }
      }
    }
    return new SunshadeBetweenSheep(sheep.get(sheep1Id), sheep.get(sheep2Id), minSunshadeSpan);
  }

  private double getDistanceValue(Sheep sheep1, Sheep sheep2) {
    int deltaX = sheep1.x - sheep2.x;
    int deltaY = sheep1.y - sheep2.y;

    if (deltaX == 0) {
      return Math.abs(sheep1.y - sheep2.y);

    } else if (deltaY == 0) {
      return Math.abs(sheep1.x - sheep2.x);

    } else {
      return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    }
  }

  private int getSunshadeSpanValue(Sheep sheep1, Sheep sheep2) {
    int deltaX = Math.abs(sheep1.x - sheep2.x);
    int deltaY = Math.abs(sheep1.y - sheep2.y);
    return (deltaX > deltaY) ? deltaX/2 : deltaY/2;
  }

  /**
   * Sheep class with comparators implemented, both for x and y position.
   */
  static class Sheep implements Comparable<Sheep> {
    static final Comparator<Sheep> X_COMPARATOR = Comparator.comparingInt(o -> o.x);
    static final Comparator<Sheep> Y_COMPARATOR = Comparator.comparingInt(o -> o.y);
    int x;
    int y;

    Sheep(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public int compareTo(Sheep o) {
      return this.x - o.x;
    }

    @Override
    public String toString() {
      return "Sheep{" + x + ", " + y + '}';
    }
  }


  /**
   * Representation of the sunshade between two sheep.
   */
  class SunshadeBetweenSheep {
    Sheep sheep1;
    Sheep sheep2;
    int sunshadeSpan;

    SunshadeBetweenSheep(Sheep sheep1, Sheep sheep2, int sunshadeSpan) {
      this.sheep1 = sheep1;
      this.sheep2 = sheep2;
      this.sunshadeSpan = sunshadeSpan;
    }

    @Override
    public String toString() {
      return sheep1 + " <-> " + sheep2 + " =\t" + sunshadeSpan;
    }
  }

}
