import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class Token
{
    public String name = null;
    public String content = null;

    //for loading in the names from tokenNames.txt
    private static Set<String> tokenNames = new HashSet();

    public Token(String name, String content)
    {
        this.name = name;
        this.content = content;
    }

    static {
        //initialize the hashset
        String filepath = "src/tokenNames.txt";
        try {
            setTokenNames(filepath);
        } catch (IOException e) {}
    }

    private static void setTokenNames(String filepath) throws IOException
    {
        byte[] rawdata = Files.readAllBytes(Paths.get(filepath));
        String rawstring = new String(rawdata);

        String[] lineDelimited = rawstring.split("\n");

        for (String line: lineDelimited)
        {
            line = line.trim();
            if (line.startsWith("#") || line.length()==0) continue; // # is like a comment in the text file, skip blank lines as well

            tokenNames.add(line);
        }

        /*   check
        for (String s:tokenNames)
            System.out.println(s);
         */
    }
}