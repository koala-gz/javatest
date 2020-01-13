package com.eden;


import com.eden.RecursiveNumberLetterConverter;
import com.eden.NumberLetterConverter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RecursiveNumberLetterConverterTest {

  @Before
  public void setUp() throws Exception {
  }


  private static boolean ConfirmLetterCombination(final int[] arrInputData,
                                                  final String expectedValues)
          throws Exception {

    NumberLetterConverter converter = new RecursiveNumberLetterConverter();
    List<String> combinations = converter.convert(arrInputData);
    Set<String> actualCombinations = new HashSet<String>(combinations);
    String actualValues = concatToString(actualCombinations);

    Set<String> expectedCombinations = new HashSet<String>(wordsToList(expectedValues));

    // both are empty string
    if(isEmpty(actualValues) && isEmpty(expectedValues)){
      return true;
    }

    if(!actualCombinations.equals(expectedCombinations)){
      return false;
    }
    return true;
  }

  @Test
  public void testAllNormalCases() throws Exception {
    assertThat(ConfirmLetterCombination(new int[]{2, 3, 4},
            "adg adh adi aeg aeh aei afg afh afi bdg bdh bdi beg beh bei bfg bfh bfi cdg cdh cdi ceg ceh cei cfg cfh cfi"),
            is(true));

    assertThat(ConfirmLetterCombination(new int[]{2, 3},
            "ad ae af bd be bf cd ce cf"),
            is(true));

    assertThat(ConfirmLetterCombination(new int[]{9},
            "w x y z"),
            is(true));

    assertThat(ConfirmLetterCombination(new int[]{0},
            ""),
            is(true));

    assertThat(ConfirmLetterCombination(new int[]{1},
            ""),
            is(true));

    assertThat(ConfirmLetterCombination(new int[]{0, 2},
            "a b c"),
            is(true));

    assertThat(ConfirmLetterCombination(new int[]{2, 0},
            "a b c"),
            is(true));

    assertThat(ConfirmLetterCombination(new int[]{1, 3},
            "d e f"),
            is(true));

    assertThat(ConfirmLetterCombination(new int[]{3, 1},
            "d e f"),
            is(true));
  }

  @Test
  public void maxIndexCombination() throws Exception {
    assertThat(ConfirmLetterCombination(new int[]{99},
            ""),
            is(true));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void shouldBeErrorExceedsMaxIndex() throws Exception {
    final int[] arr = {100};
    NumberLetterConverter converter = new RecursiveNumberLetterConverter();
    List<String> combinations = converter.convert(arr);
  }

  /**
   *  Convert the digits from 0 to 9 into letters
   *  Input: arr[] = {2, 3}
   *  Output: ad ae af bd be bf cd ce cf
   * */
  @Test
  public void simpleLetterCombination() throws Exception {
    final int[] arr = {2, 3};
    NumberLetterConverter converter = new RecursiveNumberLetterConverter();
    List<String> combinations = converter.convert(arr);
    Set<String> actualCombinations = new HashSet<String>(combinations);

    final String expectedValues = "ad ae af bd be bf cd ce cf";
    Set<String> expectedCombinations = new HashSet<String>(wordsToList(expectedValues));

    // size & items are equal
    assertThat(actualCombinations.size() == expectedCombinations.size(), is(true));
    assertThat(actualCombinations.equals(expectedCombinations), is(true));
  }


  /**
   *  Convert the digits from 0 to 9 into letters
   *  Input: arr[] = {9}
   *  Output: w x y z
   * */
  @Test
  public void singleLetterCombination() throws Exception {
    final int[] arr = {9};
    NumberLetterConverter converter = new RecursiveNumberLetterConverter();
    List<String> combinations = converter.convert(arr);
    Set<String> actualCombinations = new HashSet<String>(combinations);

    final String expectedValues = "w x y z";
    Set<String> expectedCombinations = new HashSet<String>(wordsToList(expectedValues));

    // size & items are equal
    assertThat(actualCombinations.size() == expectedCombinations.size(), is(true));
    assertThat(actualCombinations.equals(expectedCombinations), is(true));
  }

  /**
   *  Convert the digits from 0 to 9 into letters
   *  Input: arr[] = {9}
   *  Output: w x y z
   * */
  @Test
  public void shouldBeNoLetterCombination() throws Exception {
    final int[] arr = {0};
    NumberLetterConverter converter = new RecursiveNumberLetterConverter();
    List<String> combinations = converter.convert(arr);
    Set<String> actualCombinations = new HashSet<String>(combinations);
    String actualValues = concatToString(actualCombinations);

    final String expectedValues = null;
    Set<String> expectedCombinations = new HashSet<String>(wordsToList(expectedValues));

    // both are empty string
    if(isEmpty(actualValues) || isEmpty(expectedValues)){
      assertThat(isEmpty(actualValues) && isEmpty(expectedValues), is(true));
      return;
    }

    // size & items are equal
    assertThat(actualCombinations.size() == expectedCombinations.size(), is(true));
    assertThat(actualCombinations.equals(expectedCombinations), is(true));
  }

  // {A,B} => "AB"
  private static String concatToString(Collection<String> values){
    return concatToString(values, "");
  }

  // {A,B,C},delimeter=" " ===> "A B C"
  private static String concatToString(Collection<String> values, String delimeter){
    StringBuilder sb = new StringBuilder();

    if(values == null || values.isEmpty()) {
      return "";
    }

    for (String val : values) {
      sb.append(val).append(delimeter);
    }
    return sb.toString().trim();
  }

  /**
   *  Convert words with spaces to List
   *  Input :"a b"
   *  Output : List ["a", "b"]
   * */
  static private List<String> wordsToList(String expectedValues) {
    if (isEmpty(expectedValues) || expectedValues.matches("\\s+")){
      return new ArrayList<String>(0);
    }

    return Arrays.asList(expectedValues.split("\\s+"));
  }

  private static boolean isEmpty(String value) {
    return value == null || value.length() <= 0;
  }

}
