package com.PJ;

/**
 *
 * BalancedPassword
 * Given a string S, find the length of the longest balanced substring of S.
 *
 * ====================
 * === SILVER AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/certDJSNN2-57YZ57ZAWY6G9YFG/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/certDJSNN2-57YZ57ZAWY6G9YFG/
 *
 */
@SuppressWarnings("ALL")
public class Challenge_2018_09_GrandChallenge {

    public static void main(String[] args) {
        System.out.println(solution("cabbacc"));
        System.out.println(solution("abababa"));
        System.out.println(solution("aaaaaaa"));
        System.out.println(solution("abc"));
    }

    private static int solution(String S) {
        int result = 0;

        for (int i=0; i<S.length(); i++) {
            for (int j=i+1; j<S.length(); j=j+2) {
                String substring = S.substring(i,j+1);

                System.out.println(substring);
            }
        }
        return result;
    }

    private static boolean checkIfStringIsBalanced(String S) {
        char first = 'A';
        char second = 'A';
        boolean firstAssigned = false;
        boolean secondAssigned = false;
        int occurFirst = 0;
        int occurSecond = 0;

        for (int i=0; i<S.length(); i++) {

            if (firstAssigned == false) {
                first = S.charAt(i);
                firstAssigned = true;
                occurFirst++;

            } else if (firstAssigned && S.charAt(i) == first) {
                occurFirst++;

            } else if (secondAssigned == false) {
                second = S.charAt(i);
                secondAssigned = true;
                occurSecond++;

            } else if (secondAssigned && S.charAt(i) == second) {
                occurSecond++;

            } else {
                return false;
            }
        }

        if (occurFirst == occurSecond) {
            return true;
        } else {
            return false;
        }
    }

}
