package SyntacticAnalysis;

import LexicalAnalysis.Token;
import SyntacticAnalysis.ParseTreeClasses.AstPrinter;
import SyntacticAnalysis.ParseTreeClasses.Expression;

import java.util.*;

public class ParserRecursiveDescent
{
    private final List<Token> tokens; //tokenlist
    private int current = 0;

    public ParserRecursiveDescent(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    public static void main(String[] args)
    {
        testASTPrinter();
    }

    private static void testASTPrinter()
    {
        //driver code to test ASTPrinter.java
        //translates to the expression '-123 * (45.67)'
        Expression expr = new Expression.Binary(
            new Expression.Unary(
                    new Token("-"),
                    new Expression.Literal(123)),
            new Token("*"),
            new Expression.Grouping(
                    new Expression.Literal(45.67)
        ));
        //works out fine

        System.out.println(new AstPrinter().print(expr));
    }
}