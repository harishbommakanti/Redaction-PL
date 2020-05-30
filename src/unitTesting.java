import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class unitTesting {
    static String filepath;
    static Lexer lex;
    static List<Token> tokenList;
    static List<Token> unitTestKey;
    public static void main(String[] args) throws IOException 
    {
        test1();

        

        
    }

    private static void test1() throws IOException
    {
        filepath = "src/unitTest1.redact";
        lex = new Lexer(filepath);
        tokenList = lex.tokenize();
        unitTestKey = new ArrayList<>(Arrays.asList(new Token("{begin}"),
                                                    new Token("let"),new Token("IDENTIFIER","radius"),new Token("="),new Token("INT_LITERAL","6"),
                                                    new Token("int"), new Token("let"),new Token("IDENTIFIER","area"), new Token("="),
                                                    new Token("IDENTIFIER","pi"),new Token("*"),new Token("IDENTIFIER","radius"),new Token("*"),
                                                    new Token("IDENTIFIER","radius"), new Token("double"), new Token("if"),new Token("("),
                                                    new Token("IDENTIFIER","radius"),new Token("<="),new Token("INT_LITERAL","5"),new Token("and"),
                                                    new Token("IDENTIFIER","area"),new Token("=="),new Token("INT_LITERAL","213"),new Token(")"),new Token("{"),
                                                    new Token("let"),new Token("IDENTIFIER","c"),new Token("="),new Token("INT_LITERAL","5"),new Token("+"),
                                                    new Token("INT_LITERAL","3"),new Token("}"), new Token("func"),new Token("IDENTIFIER","add"),
                                                    new Token("("),new Token(")"),new Token("{"),new Token("let"),new Token("IDENTIFIER","sum"),new Token("="),
                                                    new Token("INT_LITERAL","25"),new Token("-"),new Token("INT_LITERAL","7"),new Token("}"),new Token("{end}")));
        if(compareList(tokenList, unitTestKey))
            System.out.println("Test 1 : Passed!");
        else
            System.out.println("Test 1 : Failed!");
    }


    private static boolean compareList(List<Token>list1, List<Token>list2)
    {
        for(int i = 0; i < list1.size(); i++)
        {
            boolean b1 = !(Token.toString1(list1.get(i)).equals(Token.toString1(list2.get(i))));
            boolean b2 = !(Token.toString2(list1.get(i)).equals(Token.toString2(list2.get(i))));
            if(b1 || b2)
                return false;
        }
        return true;
    }

}