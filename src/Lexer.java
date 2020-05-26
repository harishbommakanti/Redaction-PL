import java.io.*;

public class Lexer
{
    private BufferedReader br = null;

    public Lexer(String filepath) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
        this.br = br;
    }
}