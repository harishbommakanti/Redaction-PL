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
                                                    new Token("let"),new Token("radius"),new Token("="),new Token("6"),
                                                    new Token("int"), new Token("let"),new Token("area"), new Token("="),
                                                    new Token("pi"),new Token("*"),new Token("radius"),new Token("*"),
                                                    new Token("radius"), new Token("double"), new Token("if"),new Token("("),
                                                    new Token("radius"),new Token("<="),new Token("5"),new Token("and"),
                                                    new Token("area"),new Token("=="),new Token("213"),new Token(")"),new Token("{"),
                                                    new Token("let"),new Token("c"),new Token("="),new Token("5"),new Token("+"),
                                                    new Token("3"),new Token("}"), new Token("func"),new Token("add"),
                                                    new Token("("),new Token(")"),new Token("{"),new Token("let"),new Token("sum"),new Token("="),
                                                    new Token("25"),new Token("-"),new Token("7"),new Token("}"),new Token("{end}")));
        if(compareList(tokenList, unitTestKey))
            System.out.println("Test 1 : Passed!");
        else
            System.out.println("Test 1 : Failed!");
    }


    private static boolean compareList(List<Token>list1, List<Token>list2)
    {
        for(int i = 0; i < list1.size(); i++)
        {
            if(!(Token.toString(list1.get(i)).equals(Token.toString(list2.get(i)))))
                return false;
        }
        return true;
    }

}