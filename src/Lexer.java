import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;
import java.util.Queue;

public class Lexer
{
    //private BufferedReader br = null;
    private StringBuilder str = new StringBuilder();
    private Queue<Token> tokenList = new PriorityQueue<Token>();

    public Lexer(String filepath) throws IOException
    {
        //BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
        //this.br = br;

        readfile(filepath);
        //processString();
    }

    private void readfile(String filepath) throws IOException
    {
        byte[] rawdata = Files.readAllBytes(Paths.get(filepath));
        String rawstring = new String(rawdata);

        //remove all whitespaces and new lines
        rawstring = rawstring.trim().replace("\n"," $ "); //adds $ for every line in the program
        //.replace(" ","") (removed creating spaces b/w each word)
        str.append(rawstring);

        //test
        System.out.println(str.toString());
    }

    //this parses the string formed in readfile and pushes tokens in the queue
    private void processString()
    {
        //assume the structure of the program is good for now. if its not, create errors and exceptions later
        tokenList.add(new Token(TokenType.PROGRAM_BEGIN,"{begin}"));

        str.replace(0,6,""); //take the begin token out after parsing it

    }
}