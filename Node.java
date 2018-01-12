/**
 * William Kendall
 * CMSCM 330
 * 
 * Node class provides data that is needed for the parse graph
 * A node stores a list of child nodes along with a token that represents its node
 */

import java.util.LinkedList;


public class Node {
  public LinkedList<Node> children;
  public Token token;

  Node(Token token) {
    this.children = new LinkedList<Node>();
    this.token = token;
  }// end constructor

  private String _tabtoString(int tab) {
    String spaces = "";
    for (int i = 0; i < tab; i++)
      spaces += " ";
    String result = "";
    result += this.token.toString() + "{";
    for (int i = 0; i < this.children.size(); i++) {
      result += "\n" + spaces + "Node(" + i + "): " + this.children.get(i)._tabtoString(tab + 4);
    }
    return result + "}";
  }

  public String toString() {
    return this._tabtoString(0);
  }
  public int count()
  {
    int result = this.children.size();
    for(Node n : this.children)
    {
      result+=n.count();
    }
    return result;
  }
}// end class
