/**
 * William Kendall
 * CMSC330
 * Project 1
 * 
 * This program uses two files
 * 1: parsegui.txt this is the gui data that that program will create
 * 2: parser.xml   this file tells the parser how to parse the data
 * 
 * the program will take the two input files and build a gui
 * that is described in parsegui.txt
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Project1 {


  public static void main(String[] args) {

    //read file to build gui from
    String file;
    try {
      Scanner scanner = new Scanner(new File("parsegui.txt"));
      if(scanner.hasNext() == false)
      {
        //File is empty
        System.out.println("gui file is empty");
        scanner.close();
        return;
      }
      file = scanner.useDelimiter("\\Z").next();
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("File: parsegui.txt could not be found" );
      return;
    }
    
    //tokenize the file
    Lexer lexer = new Lexer();
    LinkedList<Token> tokens = lexer.lex(file);
    
    //parser file
    Parser parser = new Parser("parser.xml");
    Node graph = parser.parse(tokens);

    //check that parse was successful
    //error tokens start at 1 and not 0
    //if error token= 1, that is the first word in the file 
    //so parser.getLastValid()+2 the +2 is need +1 for zero index, +1 because 
    //getLastValid() is the last GOOD value
    if(graph==null)
    {
      int et = parser.getLastValid()+1;
      System.out.println("Error on token: "+ (et+1));
     for(int i = 0; i < tokens.size();i++)
     {
       if (i < et + 5 && i > et-5)
       {
         //print a range of the error tokens
         if(i==et)
           System.out.print(" *"+tokens.get(i).data+"*");
         else
           System.out.print(" "+tokens.get(i).data);
           
       }
     }
      
      return;
    }
    
    //evaluate graph
    Evaluator eval = new Evaluator();
    eval.guiEvaluator(graph, null);
   
    
  }

}
