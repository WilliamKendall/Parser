/**
 * William Kendall
 * CMSC330
 * 
 * The lexer class is used to read in a string of data,
 * and return a linked list of tokens. 
 * the lexer ignores unknown symbols  
 */
import java.util.LinkedList;

public class Lexer {

  /**
   * asciiType uses the ascii code of a char to find out where it is on the ascii table
   * 
   * @param b Any char value
   * @return returns 3 if input matches A-Z.returns 2 if decimal point. returns 3 if input matches
   *         a-z. returns 4 if input matches 0-9. returns 0 if none of these types are matched.
   */
private static int asciiType(char b) {

    if (b >= 48 && b <= 57)
      return 1;
    if (b == 46)
      return 2;
    if (b >= 97 && b <= 122)
      return 3;
    if (b >= 65 && b <= 90)
      return 4;

    return 0;
  }

  
  
  /**
   * lex is the lexing method
   * lex takes any string of data
   * @param input any string
   * @return a linked list of tokens
   */
  public LinkedList<Token> lex(String input) {
    LinkedList<Token> results = new LinkedList<Token>();

    char[] iBytes = input.toCharArray();
    for (int i = 0; i < input.length(); i++) {

      // search for number
      if (asciiType(iBytes[i]) == 1) {
        String number = "" + iBytes[i];
        while ((i + 1) < input.length() && asciiType(iBytes[i + 1]) <= 2
            && asciiType(iBytes[i + 1]) > 0) {
          i++;
          number += iBytes[i];
        }
        results.add(new Token("NUMBER", number));
        System.out.println("number: " + number);
        continue;
      }
      // search for symbol
      if (asciiType(iBytes[i]) > 2) {
        String symbol = "" + iBytes[i];
        while ((i + 1) < input.length() && asciiType(iBytes[i + 1]) > 2) {
          i++;
          symbol += iBytes[i];
        }
        results.add(new Token("SYMBOL", symbol));
        System.out.println("symbol: " + symbol);
        continue;
      }

      // search for other stuff
      switch (iBytes[i]) {
        // String with "
        case '\"': {
          i++; // get rid of first quote
          String string = "";
          while ((i) < input.length() && iBytes[i] != '\"') {
            string += iBytes[i];
            i++;
          }
          results.add(new Token("STRING", string));
          System.out.println("string: " + string);
          break;
        }

        // string with '
        case '\'': {
          i++; // get rid of first quote
          String string = "";
          while ((i) < input.length() && iBytes[i] != '\'') {
            string += iBytes[i];
            i++;
          }
          results.add(new Token("STRING", string));
          System.out.println("string: " + string);
          break;
        }

        // special
        case '(':
        case ')':
        case ',':
        case ';':
        case '.':
        case ':': {
          String sp = String.valueOf(iBytes[i]);
          results.add(new Token(sp, sp));
          System.out.println("special: " + sp);

          break;
        }

        // Irrelevant cases
        case ' ':
        case '\n':
        case '\t':
          break;
        default: {
          System.out.println("Lexer Error!");
          break;
        }
      }// end switch

    } // end for

    return results;
  } // end lexer

}
