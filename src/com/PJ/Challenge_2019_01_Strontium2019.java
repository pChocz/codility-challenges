package com.PJ;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * ConcatenatingOfWords
 * Concatenate the given words in such a way as to obtain a single word
 * with the longest possible substring composed of one particular letter.
 *
 * ====================
 * === GOLDEN AWARD ===
 * ====================
 *
 * solution report:
 * - https://app.codility.com/cert/view/certN2QEB7-EDK7U97DF47AF77P/details/
 *
 * certificate:
 * - https://app.codility.com/cert/view/certN2QEB7-EDK7U97DF47AF77P/
 *
 * </pre>
 */
class Challenge_2019_01_Strontium2019 {

  @Test
  @SuppressWarnings({"SpellCheckingInspection"})
  void test() {
    assertEquals(1, solution(new String[]{"xbx"}));
    assertEquals(3, solution(new String[]{"aaaxxbxx"}));
    assertEquals(1, solution(new String[]{"x"}));
    assertEquals(8, solution(new String[]{"xxxxxxxx"}));
    assertEquals(5, solution(new String[]{"xxxxxaaa"}));
    assertEquals(4, solution(new String[]{"xbxxv", "aaaxxbxx", "x"}));
    assertEquals(6, solution(new String[]{"aabb", "aaaa", "bbab"}));
    assertEquals(4, solution(new String[]{"xxbxx", "xbx", "x"}));
    assertEquals(4, solution(new String[]{"dd", "bb", "cc", "dd"}));
    assertEquals(11, solution(new String[]{"xbxx", "xxbxx", "xxxbxx", "xxxxbxx", "xxxxbxxxx", "xxxbxxxxxx", "xxbxxxxxxx"}));
    assertEquals(21, solution(new String[]{"csprfzcl", "aaaaaaa", "aaaaaaaaa", "mde", "aaaaabaaaaa", "eqghghvqjbey"}));
  }

  private int solution(String[] words) {
    int result = 0;
    int size = words.length;
    Map<Character, Occurrence> map = new HashMap<>();

    for (int i = 0; i < size; i++) {
      final String word = words[i];

      for (int pos = 0; pos < word.length(); pos++) {
        Side side = (pos == 0) ? Side.LEFT : Side.MID;
        char character = word.charAt(pos);
        int count = 1;

        while ((pos < word.length() - 1) && (word.charAt(pos) == word.charAt(pos + 1))) {
          pos++;
          count++;
        }

        if ((pos == word.length() - 1) && (side == Side.LEFT)) {
          side = Side.FULL;
        } else if ((pos == word.length() - 1) && (side != Side.LEFT)) {
          side = Side.RIGHT;
        }

        Substring substring = new Substring(i, side, count);

        if (!map.containsKey(character)) {
          map.put(character, new Occurrence(character));
        }
        map.get(character).addSubstring(substring);
      }
    }

    for (Map.Entry<Character, Occurrence> characterOccurrenceEntry : map.entrySet()) {
      Occurrence occurrence = characterOccurrenceEntry.getValue();
      int tempResult = occurrence.calculateResult();

      if (tempResult > result) {
        result = tempResult;
      }
    }

    return result;
  }

  private enum Side {
    LEFT, RIGHT, MID, FULL
  }

  private static class Occurrence {
    char character;
    int fullTotal = 0;
    int midLongest = 0;
    ArrayList<Substring> longestLefts = new ArrayList<>(2);
    ArrayList<Substring> longestRights = new ArrayList<>(2);

    Occurrence(char character) {
      this.character = character;
    }

    void addSubstring(Substring substring) {
      if (substring.side == Side.MID && substring.letterOccurrences > this.midLongest) {
        midLongest = substring.letterOccurrences;

      } else if (substring.side == Side.LEFT) {
        addSubstringToOneSidedList(substring, longestLefts);

      } else if (substring.side == Side.RIGHT) {
        addSubstringToOneSidedList(substring, longestRights);

      } else if (substring.side == Side.FULL) {
        fullTotal += substring.letterOccurrences;
      }
    }

    private void addSubstringToOneSidedList(Substring substring, ArrayList<Substring> oneSidedList) {
      if (oneSidedList.size() < 2) {
        oneSidedList.add(substring);

      } else {
        if (oneSidedList.get(0).letterOccurrences > oneSidedList.get(1).letterOccurrences) {
          if (substring.letterOccurrences > oneSidedList.get(1).letterOccurrences) {
            oneSidedList.remove(1);
            oneSidedList.add(substring);
          }

        } else {
          if (substring.letterOccurrences > oneSidedList.get(0).letterOccurrences) {
            oneSidedList.remove(0);
            oneSidedList.add(substring);
          }
        }
      }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    int calculateResult() {
      int bestLeftRightResult = 0;

      if (longestRights.isEmpty()) {
        for (Substring substring : longestLefts) {
          bestLeftRightResult = Math.max(substring.letterOccurrences, bestLeftRightResult);
        }
      }

      if (longestLefts.isEmpty()) {
        for (Substring substring : longestRights) {
          bestLeftRightResult = Math.max(substring.letterOccurrences, bestLeftRightResult);
        }
      }

      if ((longestLefts.size() == 1) && (longestRights.size() == 1)) {
        Substring currentSubstringLeft = longestLefts.get(0);
        Substring currentSubstringRight = longestRights.get(0);

        if (currentSubstringLeft.wordNumber == currentSubstringRight.wordNumber) {
          int currentMaxLeft = currentSubstringLeft.letterOccurrences;
          int currentMaxRight = currentSubstringRight.letterOccurrences;
          bestLeftRightResult = Math.max(currentMaxLeft, currentMaxRight);
        }
      }

      for (int i = 0; i < longestLefts.size(); i++) {
        for (int j = 0; j < longestRights.size(); j++) {
          Substring currentSubstringLeft = longestLefts.get(i);
          Substring currentSubstringRight = longestRights.get(j);

          if (currentSubstringLeft.wordNumber != currentSubstringRight.wordNumber) {
            int currentSum = currentSubstringLeft.letterOccurrences + currentSubstringRight.letterOccurrences;
            bestLeftRightResult = (currentSum > bestLeftRightResult) ? currentSum : bestLeftRightResult;
          }
        }
      }

      return Math.max(bestLeftRightResult + fullTotal, midLongest);
    }
  }

  private static class Substring {
    int wordNumber;
    Side side;
    int letterOccurrences;

    Substring(final int wordNumber, final Side side, final int letterOccurrences) {
      this.wordNumber = wordNumber;
      this.side = side;
      this.letterOccurrences = letterOccurrences;
    }
  }

}
