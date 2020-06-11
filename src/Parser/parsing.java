package Parser;
import java.io.IOError;
import java.util.*;
import LexicalAnalysis.*;

public class parsing {
    private List<Token> li;
    private static ListIterator<Token> iterator;

    public parsing(List<Token> list) {
        li = list;
        iterator = li.listIterator();

    }

    public static void error() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compareTokens(Token token)
    {
        if(token == iterator.next())
            if(iterator != null)
                //iterator = iterator.next();
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