import java.io.*;

//Main class for calling functions from other classes like Lexer and Parser
//The 'landing page' of the compiler/interpreter(byte)
public class Main
{
    public static void main(String[] args) throws IOException
    {
        //stage 1 of the compiler is the Lexer, which converts the input program into a sequence of tokens
        String filepath = "src/testcode.redact";
        Lexer lex = new Lexer(filepath);
    }
}