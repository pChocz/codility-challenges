package com.PJ;

import java.util.*;

/**
 *
 * Buckets
 * Given N buckets and M colored balls to put in them, find the earliest
 * moment when some bucket contains Q balls of the same color.
 *
 * ====================
 * === GOLDEN AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/certYNN974-82D39BX25J9Q7Q84/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/certYNN974-82D39BX25J9Q7Q84/
 *
 */
@SuppressWarnings("ALL")
public class Challenge_2018_10_Bromum2018 {

    public static void main(String[] args) {

        System.out.println(solution(3,2,
                new int[]{1, 2, 0, 1, 1, 0, 0, 1},
                new int[]{0, 3, 0, 2, 0, 3, 0, 0}));

        System.out.println(solution(2,2,
                new int[]{0,1},
                new int[]{5,5}));

        System.out.println(solution(2,2,
                new int[]{0,1,0,1,0,1},
                new int[]{1,3,0,0,3,3}));

        System.out.println(solution(1, 1,
                new int[]{0},
                new int[]{0}));
    }

    private static int solution(int N, int Q, int[] B, int[] C) {
        if (Q == 1) {
            return 0;
        }

        Map<Integer, Map<Integer, Integer>> mapOfMaps = new HashMap();

        for (int i = 0; i < B.length; i++) {
            if (mapOfMaps.get(B[i]) == null) {
                mapOfMaps.put(B[i], new HashMap<>());
            }

            Map<Integer, Integer> currentMap = mapOfMaps.get(B[i]);

            if (!currentMap.keySet().contains(C[i])) {
                currentMap.put(C[i], 1);

            } else {
                int count = currentMap.get(C[i]);
                if (++count == Q) {
                    return i;
                }
                currentMap.put(C[i], count);
            }
        }
        return -1;
    }

    private static boolean arrayContainsValue(int[] B, int i) {
        for (final int e : B)
            if (e == i) {
                return true;
            }
        return false;
    }

}
