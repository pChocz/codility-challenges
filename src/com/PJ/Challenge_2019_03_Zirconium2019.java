package com.PJ;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * DreamTeam
 * Divide developers into two teams to maximize their total contribution.
 *
 * ====================
 * === GOLDEN AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/certMUJGEN-RYJ49DJMYJ3GRD54/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/certMUJGEN-RYJ49DJMYJ3GRD54/
 *
 * </pre>
 */
class Challenge_2019_03_Zirconium2019 {

  @Test
  void test() {
    // corner cases
    assertEquals(1, solution(new int[]{1}, new int[]{3}, 1));
    assertEquals(3, solution(new int[]{1}, new int[]{3}, 0));
    assertEquals(15, solution(new int[]{7, 1, 4, 4}, new int[]{5, 3, 4, 3}, 0));
    assertEquals(16, solution(new int[]{7, 1, 4, 4}, new int[]{5, 3, 4, 3}, 4));

    // regular cases
    assertEquals(28, solution(new int[]{7, 1, 4, 2, 5, 8}, new int[]{5, 1, 4, 3, 2, 1}, 3));
    assertEquals(18, solution(new int[]{7, 1, 4, 4}, new int[]{5, 3, 4, 3}, 2));
    assertEquals(12, solution(new int[]{1, 2, 4, 3}, new int[]{3, 4, 6, 5}, 3));
    assertEquals(10, solution(new int[]{4, 2, 1}, new int[]{2, 5, 3}, 2));
    assertEquals(15, solution(new int[]{5, 5, 5}, new int[]{5, 5, 5}, 1));
  }

  @SuppressWarnings("UnnecessaryLocalVariable")
  private int solution(int[] A, int[] B, int F) {
    int numberOfAllDevelopers = A.length;
    int numberOfFrontendDevelopers = F;
    int numberOfBackendDevelopers = numberOfAllDevelopers - F;

    // corner cases evaluation
    if (numberOfBackendDevelopers == 0) {
      return Arrays.stream(A).sum();
    } else if (numberOfFrontendDevelopers == 0) {
      return Arrays.stream(B).sum();
    }

    // developers will be sorted based on their difference between
    // frontend contribution and backend contribution, or (in case
    // if they will be equal) based on their ID.
    Set<Developer> developers = new TreeSet<>((dev1, dev2) -> {
      if (dev1.difference > dev2.difference) {
        return 1;
      } else if (dev1.difference < dev2.difference) {
        return -1;
      } else if (dev1.id > dev2.id) {
        return 1;
      } else {
        return -1;
      }
    });

    for (int i = 0; i < numberOfAllDevelopers; i++) {
      developers.add(new Developer(i, A[i], B[i]));
    }

    Iterator<Developer> iterator = developers.iterator();
    int result = 0;
    int i = 0;

    while (iterator.hasNext()) {
      Developer developer = iterator.next();

      if (i++ < numberOfBackendDevelopers) {
        result += developer.backendContribution;

      } else {
        result += developer.frontendContribution;
      }
    }

    return result;
  }

  private static class Developer {
    int frontendContribution;
    int backendContribution;
    int difference;
    int id;

    Developer(int id, int frontendContribution, int backendContribution) {
      this.id = id;
      this.frontendContribution = frontendContribution;
      this.backendContribution = backendContribution;
      this.difference = frontendContribution - backendContribution;
    }
  }

}
