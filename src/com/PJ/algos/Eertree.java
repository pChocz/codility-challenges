package com.PJ.algos;


import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Eertree algorithm implementation, based on:
 *
 * <pre>
 *    EERTREE: An Efficient Data Structure for
 *       Processing Palindromes in Strings
 *
 *    by: Mikhail Rubinchik and Arseny M. Shur
 *
 *      https://arxiv.org/pdf/1506.04862.pdf
 *
 *    ----------------------------------------
 *
 * Computes all unique palindromes that are substrings
 * of a given string and returns them as a list.
 *
 *    ----------------------------------------
 *
 * EXAMPLES:
 * input: "eertree"
 * output: [ee, rtr, ertre, eertree]
 *
 * input: "ab"
 * output: []
 *
 * input: "abba"
 * output: [bb, abba]
 *
 * input: "opmrowlworm"
 * output: [wlw, owlwo, rowlwor, mrowlworm]
 *
 * </pre>
 */
@SuppressWarnings({"LocalCanBeFinal", "JavaDoc", "NonFinalUtilityClass", "FieldCanBeLocal", "SpellCheckingInspection"})
public class Eertree {

  private static String EMPTY_STRING = "";
  private static int IMAGINARY_NODE_ID = -1;
  private static int NULL_NODE_ID = 0;

  public static void main(String[] args) {
    System.out.println(solution("eertree"));
    System.out.println(solution("ab"));
    System.out.println(solution("abba"));
    System.out.println(solution("mrowlworm"));
  }

  private static List<String> solution(String string) {
    int stringLength = string.length();
    Map<Integer, Node> allNodes = preinitializeNodesMap();

    List<String> allPalindromes = new ArrayList<>();
    Node currentNode = allNodes.get(NULL_NODE_ID);
    Node traversedNode;

    for (int i = 0; i < stringLength; i++) {
      String currentLetter = string.substring(i, i + 1);
      String partOfStringUntilNow = string.substring(0, i + 1);

      traversedNode = currentNode;

      while (true) {
        String palindromeCandidateForCurrentNode = setProperCandidate(traversedNode, currentLetter);

        if (partOfStringUntilNow.contains(palindromeCandidateForCurrentNode)) {
          Node newNode = new Node(i + 1, palindromeCandidateForCurrentNode, palindromeCandidateForCurrentNode.length());
          allNodes.put(newNode.id, newNode);
          newNode.insertionLinkIncomingNode = traversedNode;
          newNode.insertionLinkIncomingValue = currentLetter;

          currentNode = newNode;
          traversedNode.insertionLinkOutgoingNodes.put(currentLetter, newNode);

          while (true) {
            if (traversedNode.id == IMAGINARY_NODE_ID) {
              newNode.suffixLinkOutgoingNode = allNodes.get(0);
              break;

            } else if (isProperSuffixOfValue(palindromeCandidateForCurrentNode, newNode.value)) {
              newNode.suffixLinkOutgoingNode = traversedNode.insertionLinkOutgoingNodes.get(currentLetter);
              break;

            } else {
              traversedNode = traversedNode.suffixLinkOutgoingNode;
            }
          }

          if (newNode.length > 1) {
            allPalindromes.add(newNode.value);
          }
          break;

        } else {
          traversedNode = traversedNode.suffixLinkOutgoingNode;
        }
      }
    }
    return allPalindromes;
  }

  private static Map<Integer, Node> preinitializeNodesMap() {
    Node imaginaryNode = new Node(IMAGINARY_NODE_ID, EMPTY_STRING, -1);
    imaginaryNode.suffixLinkOutgoingNode = imaginaryNode;

    Node nullStringNode = new Node(NULL_NODE_ID, EMPTY_STRING, 0);
    nullStringNode.suffixLinkOutgoingNode = imaginaryNode;

    Map<Integer, Node> map = new HashMap<>();
    map.put(-1, imaginaryNode);
    map.put(0, nullStringNode);

    return map;
  }

  private static String setProperCandidate(Node traversedNode, String currentLetter) {
    if (traversedNode.id == IMAGINARY_NODE_ID) {
      return currentLetter;
    } else {
      return currentLetter + traversedNode.value + currentLetter;
    }
  }

  /**
   * Verifies whether a suffix parameter is a proper suffix of value parameter
   * (proper means shorter than the value string itself).
   * Examples:
   * s="ab", v="cab" -> TRUE
   * s="b", v="cab" -> TRUE
   * s="ab", v="ab" -> FALSE
   * s="ba", v="cab" -> FALSE
   *
   * @param suffix suffix that we want to examine
   * @param value  value in which we examine existence of a suffix
   * @return TRUE if suffix is a proper suffix string of the value string
   */
  private static boolean isProperSuffixOfValue(String suffix, String value) {
    char[] charArrayValue = value.toCharArray();
    char[] charArraySuffix = suffix.toCharArray();

    if (charArraySuffix.length >= charArrayValue.length) {
      return false;
    }

    for (int i = charArrayValue.length - 1, j = charArraySuffix.length - 1; j >= 0; i--, j--) {
      if (charArrayValue[i] != charArraySuffix[j]) {
        return false;
      }
    }
    return true;

  }

  @Test
  public void test_01() {
    assertEquals(new ArrayList<>(asList("ee", "rtr", "ertre", "eertree")), solution("eertree"));
    assertEquals(new ArrayList<>(asList("ee", "tt", "rttr", "erttre", "eerttree", "tt")), solution("dwqeerttreeghtyh"));
    assertEquals(new ArrayList<>(), solution("ab"));
    assertEquals(new ArrayList<>(asList("bb", "abba")), solution("abba"));
    assertEquals(new ArrayList<>(asList("wlw", "owlwo", "rowlwor", "mrowlworm")), solution("mrowlworm"));
    assertEquals(new ArrayList<>(), solution("a"));
    assertNotEquals(new ArrayList<>(), solution("aa"));
  }

  /**
   * Class representing single node of the palindromic tree. Each node consists of:
   * - single incoming insertion link (labeled with insertion letter)
   * - multiple outgoing insertion links (labeled with the insertion letter), stored in a Map
   * - single outgoing suffix link (unlabeled)
   */
  static class Node {
    int id;
    String value;
    int length;
    Node insertionLinkIncomingNode;
    String insertionLinkIncomingValue;
    Map<String, Node> insertionLinkOutgoingNodes;
    Node suffixLinkOutgoingNode;

    Node(int id, String value, int length) {
      this.id = id;
      this.value = value;
      this.length = length;
      this.insertionLinkOutgoingNodes = new HashMap<>();
    }

  }

}
