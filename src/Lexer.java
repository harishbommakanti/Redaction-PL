import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lexer
{
    //private BufferedReader br = null;
    private StringBuilder str = new StringBuilder();

    public Lexer(String filepath) throws IOException
    {
        //BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
        //this.br = br;

        readfile(filepath);
    }

    private void readfile(String filepath) throws IOException
    {
        byte[] rawdata = Files.readAllBytes(Paths.get(filepath));
        String rawstring = new String(rawdata);

        //remove all whitespaces and new lines
        rawstring = rawstring.replace(" ","").trim().replace("\n","");

        str.append(rawstring);

        //test
        //System.out.println(str.toString());
    }
}