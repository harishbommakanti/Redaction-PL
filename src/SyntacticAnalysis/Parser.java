package SyntacticAnalysis;
import java.util.*;
import LexicalAnalysis.*;

public class Parser
{
    private static List<Token> tokenlist;
    private static int listsize;
    private static ListIterator<Token> iterator;
    private static int iteratorIndex = 0;

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
        //checks for {begin} and {end}
        checkForStartAndEndTokens();
        iteratorIndex = 1; //starts from token #2

        //go on until end of tokenlist
        while(iteratorIndex != listsize-1/*iterator.hasNext()*/)
        {
            Token curr = tokenlist.get(iteratorIndex)/*iterator.next()*/; //move on to other indices

            if (curr.name.equals("LET")) //parse for assignment
            {
                parseAndCheckAssignmentStatement();
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

    //tokenlist and iterator index are global vars, no params needed
    private static void parseAndCheckAssignmentStatement()
    {
        //desired structure: let <identifier> = <numerical expression/string expression> type <type>
        // or let const <identifier> = <numerical expression/string expression> type <type>
        //parse for assignment statement until ';'

        int tempindex = iteratorIndex; //string at this index is let (otherwise we wouldn't be in this method), no need to check errors here
        boolean isConst = false;

        Token errorChecker = tokenlist.get(++iteratorIndex);
        //if its a constant, set the flag to true and iterate the index: const should be right after let
        if (errorChecker.name.equals("CONST"))
        {
            isConst = true;
            iteratorIndex++;
        }

        String identifierName = "";
        //check for the <identifier> and '=' part
        if (!errorChecker.name.equals("IDENTIFIER"))
            error("identifier expected.");
        else
            identifierName = errorChecker.content;
        errorChecker = tokenlist.get(++iteratorIndex);
        if (!errorChecker.content.equals("="))
            error("equal sign expected.");

        //now, iteratorIndex should be at the start of the expression. if theres not a literal or identifier here, error
        errorChecker = tokenlist.get(++iteratorIndex);
        if (!(errorChecker.name.equals("INT_LITERAL") || errorChecker.name.equals("STRING_LITERAL")) || errorChecker.name.equals("IDENTIFIER"))
            error("expression expected.");

        //now, errorChecker should be the first token of the numerical/string expression
        Token currAssignmentToken = errorChecker;
        List<Token> expression = new ArrayList<Token>();
        while(!currAssignmentToken.name.equals("TYPE")) //go until the 'type __'
        {
            expression.add(currAssignmentToken);
            currAssignmentToken = tokenlist.get(++iteratorIndex);
        }

        //for assigning the type of the variable, is after ...'type'.. and before ';'
        errorChecker = tokenlist.get(++iteratorIndex);
        String varType = "";
        if (!(errorChecker.name.equals("INT") || errorChecker.name.equals("STRING") || errorChecker.name.equals("DOUBLE")))
            error("type of variable assignment expected.");
        else
            varType = errorChecker.name;

        //check for semicolon
        errorChecker = tokenlist.get(++iteratorIndex);
        if (!errorChecker.content.equals(";"))
            error("semicolon expected.");


        //expression is stored in List<Token> expression, need to parse it to get its true value
        //ParseTree expParser = new ParseTree(expression); //TODO: create ParseTree class and do this
        String varValue = ""/*expParser.parse()*/;

        //using the constructor for variables, consult IdentiferData.java to see
        IdentifierData vardata = new IdentifierData(varValue,varType,isConst);

        //TODO: add a symbol table way above in this file in the constructor prolly and have a way to track scope, best way is prolly LL
        //symboltable.put(identifierName,IdentifierData) or smth like that
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