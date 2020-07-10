import LexicalAnalysis.Lexer;
import LexicalAnalysis.Token;
import SyntacticAnalysis.*;
import SyntacticAnalysis.ParseTreeClasses.AstPrinter;
import SyntacticAnalysis.ParseTreeClasses.Expression;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Main class for calling functions from other classes like Lexer and Parser
//The 'landing page' of the compiler/interpreter(byte)
public class Main
{
    public Map<String, Map<String,String>> symbolTable = new HashMap<String, Map<String, String>>();
    /*each entry will be of the following format:
      key: the name of the identifier
      value: a mapping of other satellite data for the variable, for now its just type and scope*/

    public static void main(String[] args) throws IOException
    {
        //stage 1 of the compiler is the Lexer, which converts the input program into a sequence of tokens
        String filepath = "src/test1.redact";
        Lexer lex = new Lexer(filepath);
        List<Token> tokenList = lex.tokenize();
  

        //stage 2 is the parser, manually going through the token list to build the symbol table
        //and check for syntactical errors

        //ParserNaive prs = new ParserNaive(tokenList);
        //prs.parse();

        ParserRecursiveDescent parser = new ParserRecursiveDescent(tokenList);
        Expression exp = parser.parse();

        if (parser.hadError) return; //there was a syntax/parsing error

        System.out.println(new AstPrinter().print(exp));
    }
} 