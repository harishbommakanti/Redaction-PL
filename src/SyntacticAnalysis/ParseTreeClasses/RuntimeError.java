package SyntacticAnalysis.ParseTreeClasses;

import LexicalAnalysis.Token;

public class RuntimeError extends RuntimeException
{
    final Token token;
  
    RuntimeError(Token token, String message)
    {
        super(message);
        System.out.println(message);
        this.token = token;
    }
}