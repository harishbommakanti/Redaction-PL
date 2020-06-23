package Parser;
import java.io.IOError;
import java.util.*;
import LexicalAnalysis.*;

public class parsing {
    private static List<Token> tokenlist;
    private static int iteratorIndex = 0;

    public parsing(List<Token> list)
    {
        tokenlist = list;
        beginParsing();
    }

    //will manually go through the tokens, build a symbol table, and if the structure doesn't match with what is
    //expected, an error (desirably a specific error like missing ')') will be thrown to the java console
    private static void beginParsing()
    {
        //first, must check for beginning/ending tokens
        checkForStartAndEndTokens();
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