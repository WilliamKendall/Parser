/**
 * William Kendall
 * CMSC330
 * 
 * Parser class
 * Class use to parse a list of tokens that are built from the Lexer class
 * The parser also requires a XML file for the command instructions
 */
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Parser {
  private HashMap<String, LinkedList<LinkedList<Token>>> parseInstructions =
      new HashMap<String, LinkedList<LinkedList<Token>>>();
  
  private String _xmlFile;
  
  private int _lastcomplete;
public Parser(String file) {
  this._xmlFile = file;
}
  
  /**
   * Helper class
   * _rparser needs to return more data in recursive calls 
   */
  private class ParserNode {
    public int tokensUsed;
    public Node node;
    public boolean fail;

    public ParserNode(Token t) {
      this.tokensUsed = 0;
      this.node = new Node(t);
    }
  }
  

  /**
   * Xml parser to parse the instructions for the parser. First time I have done XML, so I really
   * don't know what I am doing. This method does assume valid data
   */
private void buildParseInstructions() {
    try {

      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(new File(this._xmlFile));

      org.w3c.dom.Node parse = doc.getDocumentElement();

      NodeList nonterms = parse.getChildNodes();

      for (int i = 0; i < nonterms.getLength(); i++) {
        if (nonterms.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
            && nonterms.item(i).getNodeName().compareTo("nonterm") == 0) {

          // have nonterminal
          String termName = nonterms.item(i).getAttributes().getNamedItem("name").getNodeValue();
          System.out.println("Terminal: " + termName);
          LinkedList<LinkedList<Token>> llCommand = new LinkedList<LinkedList<Token>>();
          NodeList nlCommands = nonterms.item(i).getChildNodes();
          for (int c = 0; c < nlCommands.getLength(); c++) {
            if (nlCommands.item(c).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
                && nlCommands.item(c).getNodeName().compareTo("command") == 0) {

              // have command
              LinkedList<Token> llToken = new LinkedList<Token>();
              NodeList nlTokens = nlCommands.item(c).getChildNodes();
              for (int t = 0; t < nlTokens.getLength(); t++) {
                if (nlTokens.item(t).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
                    && nlTokens.item(t).getNodeName().compareTo("token") == 0) {

                  // have token
                  String tokenType =
                      nlTokens.item(t).getAttributes().getNamedItem("type").getNodeValue();
                  String tokenData = nlTokens.item(t).getTextContent().trim();
                  System.out.println("Token: " + tokenType + " : " + tokenData);
                  llToken.add(new Token(tokenType, tokenData));

                } // end token if
              } // end token search
              llCommand.add(llToken);
            } // end command if
          } // end command search
          parseInstructions.put(termName, llCommand);
        } // end nonterm if
      } // end nonterm search

    } catch (Exception e) {
      // I don't know what kinds of errors xml throws out
      // or how to handle a xml error
      // so this just prints them all out
      e.printStackTrace();
    }
    
  }// end build instructions

/**
 * _rparse should only be called by Parser.parse
 * This function descends the parse tree by recursive calls
 * @param nonTerminal recursive call nonterminal to access the non terminal
 * @param ltokens list of tokens to parse
 * @return ParserNode, will return null if command was not successful
 */
 private ParserNode _rparser(String nonTerminal, LinkedList<Token> ltokens, int depth) {
    System.out.println("Parser Scan: " + nonTerminal);
    ParserNode result = new ParserNode(new Token("ROOT", nonTerminal));
    LinkedList<LinkedList<Token>> nonterm = parseInstructions.get(nonTerminal);
    Node currentSymbol = null;

    int tokenPointer = depth;

    // command list

    for (int c = 0; c < nonterm.size(); c++) {
      boolean commandFail = false;
      tokenPointer = depth;
      result.node.children.clear();
      LinkedList<Token> commands = nonterm.get(c);
      // token list
      for (int k = 0; k < commands.size() && commandFail == false; k++) {
        if (tokenPointer >= ltokens.size()) {
          commandFail = true;
          break;
        }
        Token token = commands.get(k); // token is a parser instruction
        System.out.println(
            "Parser compaire: " + token.tokenType + ":" + ltokens.get(tokenPointer).tokenType + "  "
                + token.data + ":" + ltokens.get(tokenPointer).data);

        if(this._lastcomplete < tokenPointer) this._lastcomplete = tokenPointer-1;
        
        
        // looks at parser instructions add a node if match
        switch (token.tokenType) {
          case "NONTERM": {
            ParserNode n = _rparser(token.data, ltokens, tokenPointer);
            if (n.fail == true) {
              commandFail = true;
              break;
            }
            tokenPointer = n.tokensUsed;

           
            for (int i = 0; i < n.node.children.size(); i++) {
              if (currentSymbol != null)
                currentSymbol.children.add(n.node.children.get(i));
              else
                result.node.children.add(n.node.children.get(i));
            }
            break;
          }
          case "SYMBOL": {
            if (ltokens.get(tokenPointer).tokenType.compareTo(token.tokenType) == 0
                && ltokens.get(tokenPointer).data.compareTo(token.data) == 0) {
              currentSymbol = new Node(
                  new Token(ltokens.get(tokenPointer).tokenType, ltokens.get(tokenPointer).data));
              result.node.children.add(currentSymbol);
              tokenPointer++;
              break;
            } else {
              commandFail = true;
              break;
            }
          }
          case "NUMBER":
          case "STRING": {
            if (ltokens.get(tokenPointer).tokenType.compareTo(token.tokenType) == 0) {
              currentSymbol.children.add(new Node(
                  new Token(ltokens.get(tokenPointer).tokenType, ltokens.get(tokenPointer).data)));
              tokenPointer++;
              break;
            } else {
              commandFail = true;
              break;
            }
          }
          default: {
            if (ltokens.get(tokenPointer).tokenType.compareTo(token.tokenType) == 0) {
              // dont need to save special tokens
              // currentSymbol.children.add(new Node(new
              // Token(ltokens.get(tokenPointer).tokenType,ltokens.get(tokenPointer).data)));
              tokenPointer++;
              break;
            } else {
              commandFail = true;
              break;
            }
          }
        }// end switch
      } // end for k
      if (commandFail == false) {
        // found a match for the instruction
        result.tokensUsed = tokenPointer;
        result.fail = false;
        System.out.println("Token Pointer at: "+tokenPointer);

        return result;
      } else {
        System.out.println("Command Failed: " + nonTerminal + ": " + c);
      }
    } // end for c
    result.fail=true;
    System.out.println("Parse Failed");
    return result;
  }

 /**
  * if the parser returns null, getLastValid returns the last good token
  * @return last valid token
  */
 public int getLastValid()
 {
   return this._lastcomplete;
 }
 /**
  * parses a list of tokens and returns a graph of the parsed data
  * @param parserXML xml file that tells the parser what to do
  * @param ltokens List of tokens to parse
  * @return returns a graph of parsed data
  */
 public Node parse(LinkedList<Token> ltokens)
 {
   this._lastcomplete=-1;
   buildParseInstructions();
   ParserNode pNode = this._rparser("gui", ltokens,0);
   
   if(pNode.fail == true) return null;
   
   return pNode.node;
 }
 
}