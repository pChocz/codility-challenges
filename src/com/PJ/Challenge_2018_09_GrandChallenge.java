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
 * </pre>
 */
@SuppressWarnings("ALL")
public class Challenge_2018_09_GrandChallenge {

  @Test
  void test() {
    assertEquals(0, solution("aaaaaaa"));
    assertEquals(2, solution("abc"));
    assertEquals(4, solution("cabbacc"));
    assertEquals(6, solution("abababa"));
  }

  private int solution(String S) {
    int result = 0;

    for (int i = 0; i < S.length(); i++) {
      for (int j = i + 1; j < S.length(); j = j + 2) {
        String substring = S.substring(i, j + 1);

        if (checkIfStringIsBalanced(substring) && substring.length() > result) {
          result = substring.length();
        }
      }
    }
    return result;
  }

  private boolean checkIfStringIsBalanced(String S) {
    Map<Character, Integer> map = new HashMap<>();
    List<Character> list = new ArrayList<>();

    for (int i = 0; i < S.length(); i++) {
      Character character = S.charAt(i);

      if (!map.keySet().contains(character)) {
        map.put(character, 1);
      } else {
        int count = map.containsKey(character) ? map.get(character) : 0;
        map.put(character, count + 1);
      }
    }

    if (map.size() == 2 && map.values().toArray()[0] == map.values().toArray()[1]) {
      return true;
    }
    return false;
  }

}
