/**
 * William Kendall
 * CMSC 330
 *
 *Token class provides a data structure for nodes.
 *a token is represented by the type and data of the token
 */
public class Token {
    public String tokenType;
    public String data;

    Token(String type, String data) {
      this.tokenType = type;
      this.data = data;
    }

    public int compairTo(String tokenType, String data) {
      int results = -1;
      if (this.tokenType.compareTo(tokenType) == 0 && this.data.compareTo(data) == 0)
        results = 0;
      return results;
    }

    public int compairTo(Token token) {
      return this.compairTo(token.tokenType, token.data);
    }

    public boolean type(String tokenType) {
      if (tokenType.compareTo(this.tokenType) == 0)
        return true;
      return false;
    }

    public String toString() {
      return "{Token Type: " + this.tokenType + ", Token Data: " + this.data + "}";
    }
  }