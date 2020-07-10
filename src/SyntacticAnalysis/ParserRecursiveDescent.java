package SyntacticAnalysis;

import LexicalAnalysis.Token;
import SyntacticAnalysis.ParseTreeClasses.AstPrinter;
import SyntacticAnalysis.ParseTreeClasses.Expression;

import java.util.*;

public class ParserRecursiveDescent
{
    private final List<Token> tokens; //tokenlist
    private int current = 0; //an index tracker of tokenlist to go through the entire list
    private static class ParseError extends RuntimeException{} //to handle parse errors

    public ParserRecursiveDescent(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    public static void main(String[] args)
    {
        testASTPrinter();
    }

    //returns a huge expression encompassing all other expressions of the language, or null if theres an error
    public Expression parse(String errorStr)
    {
        try
        {
            return expression();
        } catch (ParseError error)
        {
            errorStr = "true";
            return null;
        }
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
        while(match("NOT_EQUAL_TO","CONDITIONAL_EQUALS"))
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

    //<comparison> ::= <addition> ( ( ">" | ">=" | "<" | "<=" ) <addition> )*
    private Expression comparison()
    {
        //almost exact structure to <equality> : check for expression on left,
        //then enter into a while loop which allocates tokens and a right addition token

        //first token on the left
        Expression expr = addition();

        while(match(">",">=","<","<="))
        {
            //allocate tokens while there is still an operator in the sequence
            Token operator = previous();
            Expression right = addition();
            expr = new Expression.Binary(expr, operator, right);
        }

        return expr;
    }

    //<addition> ::= <multiplication> ( ( "-" | "+" ) <multiplication> )*
    private Expression addition()
    {
        //again, virtually identical to the last two methods, comparison() and equality()
        Expression expr = multiplication();

        while(match("-","+"))
        {
            Token operator = previous();
            Expression right = multiplication();
            expr = new Expression.Binary(expr,operator,right);
        }

        return expr;
    }

    //<multiplication> ::= <unary> ( ( "/" | "*" ) <unary> )*
    private Expression multiplication()
    {
        //no surprise here...
        Expression expr = unary();

        while(match("/","*"))
        {
            Token operator = previous();
            Expression right = unary();
            expr = new Expression.Binary(expr,operator,right);
        }

        return expr;
    }

    //<unary> ::= ( "!" | "-" ) <unary> | <primary>
    private Expression unary()
    {
        //since there is a |, its an if-else situation

        //if the statement has a negation or minus:
        if(match("!","-"))
        {
            //there is a Token operator followed by another unary
            Token operator = previous();
            Expression right = unary();
            return new Expression.Unary(operator,right);
        }

        //else, its just a primary
        return primary();
    }

    //<primary> ::= NUM_LITERAL | STRING_LITERAL | <boolean literal> | "(" <expression> ")"
    private Expression primary()
    {
        //stuff you have to watch out for
        if (match("FALSE")) return new Expression.Literal(false);
        if (match("TRUE")) return new Expression.Literal(true);
        if (match("NULL")) return new Expression.Literal(null);

        //the other types
        if (match("INT_LITERAL","STRING_LITERAL"))
            return new Expression.Literal(previous().content);

        //a little more complicated, need to have error handling too
        if (match("LEFT_PAREN"))
        {
            Object j = false;
            //you have a regular expression in the middle
            Expression expr = expression();

            //this is the first time we *need* a ), in other methods we just checked
            //so therefore we need to have error checking
            consume("RIGHT_PAREN","Expect ')' after expression.");
            return new Expression.Grouping(expr);
        }

        //instead of returning null, throw an error saying you expect an expression
        throw error(peek().name, "Expect expression.");
    }











    /*Error checking: when an error is detected, the program will jump into 'Panic mode'
    which means that itll discard tokens until it returns to a properly defined grammar. Although
    this hides possible errors in the discarded tokens, it also avoids seeing nonsense cascaded errors, so its good.
    * */
    private Token consume(String tokenType, String message)
    {
        //if the current token is of the input type, move on: everything is good
        if (check(tokenType)) return advance();

        //else, throw an error with a message
        throw error(tokenType, message);
    }

    //small function to handle reporting an error
    private ParseError error(String tokenType, String message)
    {
        handleError(tokenType,message);
        return new ParseError();
    }

    //handles a specific error
    static void handleError(String tokenType, String message)
    {
        reportErrorToUser("Error at '" + tokenType + "'," + message);
    }

    //displays the errors to the user
    static void reportErrorToUser(String... messages)
    {
        StringBuilder curr = new StringBuilder();
        for (String i:messages)
            curr.append(i);

        System.out.println(curr.toString());
    }


    //for when we want to 'return' to a grammar rule state after an error is thrown, part of 'Panic Mode'
    //this means discarding tokens until we get to the beginning of the next statement

    //after a semicolon, we're done with a statement, only other cases are keywords: for, if, return, var...
    private void synchronize()
    {
        advance(); //move on to the next token to see what tokens to discard/where you can resume parsing

        while(!isAtEnd())
        {
            //if there was a syntax error in 1 LOC, you can just break out once you reach the semicolon
            if (previous().content.equals(";")) return;

            //until it reaches one of the keywords, the loop keeps going. then, it advances and the parsing restarts
            switch(peek().name)
            {
                case "FUNCTION":
                case "LET":
                case "FOR":
                case "IF":
                case "WHILE":
                case "PRINT":
                case "RETURN": return;
            }

            advance();
        }
    }













    //utility methods
    private boolean match(String... tokenTypes)
    {
        for (String s: tokenTypes)
        {
            //if the current token is any of the types, return true
            if (check(s))
            {
                advance(); //move on so there is no infinite loop in the place match() was called
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

    //returns whether the parser has reached the end of the file
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