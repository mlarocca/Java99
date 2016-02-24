package mlarocca.java99.strings;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {

  protected static final Pattern DigitString = Pattern.compile("\\d+");
  protected static final Map<Character, String> DigitToWord = new HashMap<>(10);
  static {
    DigitToWord.put('0', "zero");
    DigitToWord.put('1', "one");
    DigitToWord.put('2', "two");
    DigitToWord.put('3', "three");
    DigitToWord.put('4', "four");
    DigitToWord.put('5', "five");
    DigitToWord.put('6', "six");
    DigitToWord.put('7', "seven");
    DigitToWord.put('8', "eight");
    DigitToWord.put('9', "nine");
  }
  
  public static String digitsToWords(String digits) throws IllegalArgumentException {
    Matcher m = DigitString.matcher(digits);
    String[] results = new String[digits.length()];
    
    if (m.matches()) {
      for (int i = 0; i < digits.length(); i++) {
        char c = digits.charAt(i);
        results[i] = DigitToWord.get(c);
      }
    } else {
      throw new IllegalArgumentException(String.format("Invalid input string %s", digits));
    }
    return String.join(" ", results);
  }
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub
     {
      System.out.println(digitsToWords("35001922"));
    }
  }

}
