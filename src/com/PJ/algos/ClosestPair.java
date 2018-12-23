package com.PJ.algos;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * Implementation of the divide and conquer algorithm to calculate
 * the distance between the closest pair of points.
 *
 * It's complexity is of O( n*log(n) )
 *
 * Following assumptions are made:
 *  - both coordinates of all points must be of double type
 *
 * </pre>
 */

@SuppressWarnings({"LocalCanBeFinal", "Convert2streamapi", "MagicNumber"})
class ClosestPair {

  private static final double DELTA = 0.001;

  @Test
  void test_small() {
    assertEquals(1, solution(new int[]{0, 10, 20, 29, 30, 40, 50}, new int[]{1, 1, 1, 1, 1, 1, 1}, false));
    assertEquals(Math.sqrt(2), solution(new int[]{0, 0, 10, 10, 1}, new int[]{0, 10, 0, 10, 1}, false), DELTA);
    assertEquals(Math.sqrt(2), solution(new int[]{0, 1}, new int[]{0, 1}, false), DELTA);
    assertEquals(10, solution(new int[]{0, 0, 10, 10}, new int[]{0, 10, 0, 10}, false), DELTA);
  }

  /**
   * Compares result from applying brute force algorithm as well as divide & conquer for 10k of points.
   * Points' coordinates are read from the external file
   *
   * @throws IOException when there are problems when reading from the file
   */
  @Test
  void test_comparison_DC_BF_10k() throws IOException {
    String content = Files.readString(Path.of(System.getProperty("user.dir") + File.separator + "10k_points_unsorted_and_sorted.txt"));
    String[] contentArraySplit = content.split("\n");

    int[] unsortedX = Arrays.stream(contentArraySplit[0].split(",")).mapToInt(str -> Integer.parseInt(str.trim())).toArray();
    int[] unsortedY = Arrays.stream(contentArraySplit[1].split(",")).mapToInt(str -> Integer.parseInt(str.trim())).toArray();

    long startTimeBF = System.currentTimeMillis();
    double bruteForceResult = solution(unsortedX, unsortedY, true);
    long elapsedTimeBF = System.currentTimeMillis() - startTimeBF;
    System.out.println("Result = " + bruteForceResult + " elapsed time BF = " + elapsedTimeBF + " ms\n");

    long startTimeDC = System.currentTimeMillis();
    double divideAndConquerResult = solution(unsortedX, unsortedY, false);
    long elapsedTimeDC = System.currentTimeMillis() - startTimeDC;
    System.out.println("Result = " + divideAndConquerResult + " elapsed time DC = " + elapsedTimeDC + " ms\n");

    assertEquals(divideAndConquerResult, bruteForceResult, DELTA);
  }


  private double solution(int[] X, int[] Y, boolean useBruteForce) {
    int numberOfPoints = X.length;

    List<Point> pointsList = new ArrayList<>(numberOfPoints);
    for (int i = 0; i < numberOfPoints; i++) {
      Point point = new Point(X[i], Y[i]);
      pointsList.add(point);
    }
    pointsList.sort(Point.X_COMPARATOR);

    DistanceBetweenPoints minimumDistance = minimumDistance(pointsList, numberOfPoints, useBruteForce);
    System.out.println("Closest pair: " + minimumDistance);
    return minimumDistance.distance;
  }

  private DistanceBetweenPoints minimumDistance(List<Point> points, int size, boolean useBruteForce) {
    if (useBruteForce || size < 4) {
      return nSquareShortestDistance(points, size);
    }

    double xMedian = points.get(size/2).x;
    List<Point> pointsLeft = new ArrayList<>(points.subList(0, size/2));
    List<Point> pointsRight = new ArrayList<>(points.subList(size/2, size));

    DistanceBetweenPoints minDistanceLeft = minimumDistance(pointsLeft, pointsLeft.size(), false);
    DistanceBetweenPoints minDistanceRight = minimumDistance(pointsRight, pointsRight.size(), false);
    DistanceBetweenPoints minDistance = (minDistanceLeft.distance < minDistanceRight.distance) ? minDistanceLeft : minDistanceRight;

    double stripWidth = minDistance.distance;
    List<Point> pointsStrip = new ArrayList<>();

    for (int i = pointsLeft.size() - 1; i >= 0; i--) {
      if (pointsLeft.get(i).x < xMedian - stripWidth) {
        break;
      }
      pointsStrip.add(pointsLeft.get(i));
    }

    for (int i = 0; i <= pointsRight.size() - 1; i++) {
      if (pointsRight.get(i).x > xMedian + stripWidth) {
        break;
      }
      pointsStrip.add(pointsRight.get(i));
    }

    pointsStrip.sort(Point.Y_COMPARATOR);
    return getMinimumDistanceIncludingStrip(pointsStrip, pointsStrip.size(), minDistance, stripWidth);
  }

  private DistanceBetweenPoints getMinimumDistanceIncludingStrip(List<Point> pointsStrip, int size, DistanceBetweenPoints minDistance, double stripSpan) {
    DistanceBetweenPoints currentMinDistance = minDistance;
    for (int i = 0; i < size; i++) {
      for (int j = i + 1; (j < size) && (pointsStrip.get(j).y - pointsStrip.get(i).y < stripSpan); j++) {
        double currentDistance = getDistanceValue(pointsStrip.get(i), pointsStrip.get(j));
        if (currentDistance < currentMinDistance.distance) {
          currentMinDistance = new DistanceBetweenPoints(pointsStrip.get(i), pointsStrip.get(j), currentDistance);
        }
      }
    }
    return currentMinDistance;
  }


  /**
   * Brute force - if there are only 2 or 3 points in given list, there is no need to divide and conquer
   * and it's ok just to calculate all possible distances to choose the shortest.
   */
  private DistanceBetweenPoints nSquareShortestDistance(List<Point> points, int numberOfPoints) {
    DistanceBetweenPoints minDistance = new DistanceBetweenPoints(Double.MAX_VALUE);

    for (int i = 0; i < numberOfPoints; i++) {
      for (int j = i + 1; j < numberOfPoints; j++) {
        double currentDistance = getDistanceValue(points.get(i), points.get(j));
        if (currentDistance < minDistance.distance) {
          minDistance = new DistanceBetweenPoints(points.get(i), points.get(j), currentDistance);
        }
      }
    }
    return minDistance;
  }

  private double getDistanceValue(Point point1, Point point2) {
    double deltaX = point1.x - point2.x;
    double deltaY = point1.y - point2.y;

    if (deltaX == 0) {
      return Math.abs(point1.y - point2.y);

    } else if (deltaY == 0) {
      return Math.abs(point1.x - point2.x);

    } else {
      return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    }
  }

  /**
   * Point class with comparators implemented, both for x and y position.
   */
  static class Point implements Comparable<Point> {
    static final Comparator<Point> X_COMPARATOR = Comparator.comparingDouble(o -> o.x);
    static final Comparator<Point> Y_COMPARATOR = Comparator.comparingDouble(o -> o.y);
    double x;
    double y;

    Point(double x, double y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public int compareTo(Point o) {
      return (int) (this.x - o.x);
    }

    @Override
    public String toString() {
      return "(" + x + "," + y + ")";
    }
  }


  /**
   * Representation of distance between two points
   */
  class DistanceBetweenPoints {
    Point point1;
    Point point2;
    double distance;

    DistanceBetweenPoints(double distance) {
      this.distance = distance;
    }

    DistanceBetweenPoints(Point point1, Point point2, double distance) {
      this.point1 = point1;
      this.point2 = point2;
      this.distance = distance;
    }

    @Override
    public String toString() {
      return point1 + " <-> " + point2 + " =\t" + distance;
    }
  }

}
