package Parser;
import java.io.IOError;
import java.util.*;
import LexicalAnalysis.*;

public class parsing {
    private static List<Token> li;
    private static ListIterator<Token> iterator;
    private static Token currentToken;

    public parsing(List<Token> list)
    {
        li = list;
        iterator = li.listIterator();
        currentToken = iterator.next();

        /* verifying that the iterator works ok
        System.out.println(Token.comparisonForString(currentToken));
        while(iterator.hasNext())
        {
            System.out.println(Token.comparisonForString(iterator.next()));
        }
        */

        /*verifying that consumeToken()/error() work ok
        consumeToken(new Token("IDENTIFIER","a")); //works fine, IDENTIFIER is matched
        consumeToken(new Token("=")); //throws exception, good
        System.out.println(Token.comparisonForString(currentToken));
        */
    }

    public static void error()
    {
        try
        {
            throw new Exception("Invalid Syntax");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void consumeToken(Token inputToken)
    {
        /*
        compare current token with the input token
        assign a new token if it matches, or throws an exception
         */
        if (currentToken.name.equals(inputToken.name))
            currentToken = iterator.next();
        else
            error();
    }


    /*

    def factor(self):
        """Return an INTEGER token value.

        factor : INTEGER
        """
        token = self.current_token
        self.eat(INTEGER)
        return token.value

    def expr(self):
        """Arithmetic expression parser / interpreter.

        expr   : factor ((MUL | DIV) factor)*
        factor : INTEGER
        """
        result = self.factor()

        while self.current_token.type in (MUL, DIV):
            token = self.current_token
            if token.type == MUL:
                self.eat(MUL)
                result = result * self.factor()
            elif token.type == DIV:
                self.eat(DIV)
                result = result / self.factor()

        return result */




}