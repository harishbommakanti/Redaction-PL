package SyntacticAnalysis;

import LexicalAnalysis.Token;
import SyntacticAnalysis.ParseTreeClasses.AstPrinter;
import SyntacticAnalysis.ParseTreeClasses.Expression;

import java.util.*;

public class ParserRecursiveDescent
{
    private final List<Token> tokens; //tokenlist
    private int current = 0; //an index tracker of tokenlist to go through the entire list

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


    //below will be methods directly translating each rule to a function

    // <expression> ::= <equality> --> very simple
    private Expression expression()
    {
        return equality();
    }

    // <equality> ::= <comparison> ( ( "!=" | "==" ) comparison )*
    private Expression equality()
    {
        //starter token on the very left
        Expression expr = comparison();

        //* on the right of the grammar means while there is still a symbol in the expression
        while(doesMatch("NOT_EQUAL_TO","CONDITIONAL_EQUALS"))
        {
            //the 'middle' of the grammar
            Token operator = previous();

            //the last section of the grammar
            Expression right = comparison();

            //the expression is now designated as a binary due to != or ==, recursion pog
            expr = new Expression.Binary(expr, operator, right);
        }

        //the expression at this point will have included or be (recursion) all the binaries
        return expr;
    }

    private boolean match(String... tokenTypes)
    {
        for (String s: tokenTypes)
        {
            //if the current token is any of the types, return true
            if (check(s))
            {
                advance();
                return true;
            }
        }

        //current token is not any of the types
        return false;
    }

    //checks if the current token is of type input parameter
    private boolean check(String tokenType)
    {
        //if it is at the end of the file, return false: no token there
        if (isAtEnd()) return false;

        return tokens.get(current).name.equals(tokenType);
    }

    //moves the 'current' tokenlist pointer to the right
    private Token advance()
    {
        if (!isAtEnd()) current++;
        return previous();
    }

    //utility methods
    private boolean isAtEnd()
    {
        return peek().name.equals("PROGRAM_END");
    }

    //returns the current token
    private Token peek()
    {
        return tokens.get(current);
    }

    private Token previous()
    {
        return tokens.get(current - 1);
    }


}