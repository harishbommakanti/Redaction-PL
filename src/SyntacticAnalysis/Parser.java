package SyntacticAnalysis;
import java.util.*;
import LexicalAnalysis.*;

public class Parser
{
    private static List<Token> tokenlist;
    private static int listsize;
    private static ListIterator<Token> iterator;

    public Parser(List<Token> list)
    {
        tokenlist = list;
        listsize = tokenlist.size();
        iterator = tokenlist.listIterator();
    }

    //will manually go through the tokens, build a symbol table, and if the structure doesn't match with what is
    //expected, an error (desirably a specific error like missing ')') will be thrown to the java console
    /*
        1) Assignment: let <identifier> = <numerical expression/string expression> type <type>
            - here we must check that since 'let' is used, the said var is not already in the symbol table
                - if it is in the symbol table, throw error
            - if the syntax doesn't match, ie there is a == instead of = or smth, throw error
                - if it says 'type string' but theres a numerical expression inside, throw error
            - else, create an entry in the symbol table with the scope, type, variable name, variable value
            - if a value is created as a constant, it should only be able to be assigned once

        2) Update: <identifier> = <numerical expression/string expression>
            - check in symbol table for the variable
                - if its not in it yet, throw error. will need to check for scope too
            - check for the type to be the same
                - else throw error
            - check if the variable is final and if an update is trying to happen
                - if it is, throw error
            - else, update the variable in the symbol table

        3) Method: func <identifier>() { <statementlist> }
            - will start with seeing if the first few tokens are 'func', identifier, (,),{ and the last is }
            - will prolly create a new variable/entry or however we decide to do it for new scope in the symbol table
            - will split the inside statementlist up by ;
                - if not, throw error
            - if it has a return value, will use the 'value' attribute of the symbol table for storing the return value
                - if return isn't called, 'value' will be null

        4) IF statements: if (<boolean expression>) { <statementlist> }
            - check if tokens are in right order
                - else throw error
            - need to check if the expression inside the parenthesis is a boolean expression
                - else throw error
            - need to parse the statementlist inside by splitting up by ;
            - will need to deal with ELIF/Else statements, maybe by using manual java switch, idk

        5) loop statements: loop <integer identifier> from <int literal start> to <int literal end> {<statementlist>}
            - check if tokens are in right order
                - else throw error
            - integer identifier must either already be in the symbol table or it must be added, scope=currscope,value = <int literal start>,type = int
            - parse the statement list inside

        6) Method Calls: <identifier>()
            - check in the symbol table if the identifier is there
                - if not, throw error
            - if it has a return value, execute the method (with parameters? mb thats a later addition)
                - if it returns a value of type __, have to check that the thing receiving it is also of type __
            - if there is no return value, execute the method inside (which prolly has a void method like println, otherwise the method is useless)
     */
    public static void parse()
    {
        checkForStartAndEndTokens();
        Token curr = iterator.next(); //move on to other indices

        while(iterator.hasNext())
        {
            if (curr.name.equals("LET"))
            {
                //parse for assignment statement until ';'
            } else if (curr.name.equals("IDENTIFIER"))
            {
                //parse for an update statement
            } else if (curr.name.equals("FUNCTION"))
            {
                //parse for the function statements
            } else if (curr.name.equals("IF"))
            {
                //parse for if statements
            } else if (curr.name.equals("LOOP"))
            {
                //parse for a FOR loop
            } else
            {
                //TODO: parse for method calls, etc
                error("");
            }
        }
    }

    private static void checkForStartAndEndTokens()
    {
        if (!(tokenlist.get(0).name.equals("PROGRAM_BEGIN") && tokenlist.get(tokenlist.size()-1).name.equals("PROGRAM_END")))
            error("{begin} and/or {end} tokens expected.");
    }

    private static void error(String message)
    {
        try
        {
            throw new Exception("Invalid syntax: "+message);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}