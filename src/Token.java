import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Token
{
    public String name = null;
    public String content = null;

    //for loading in the mapping from tokenMapping.txt
    public static Map<String,String> mapping = new HashMap();

    //constructor for user defined tokens like function names or literals
    public Token(String name, String content)
    {
        /*User defined values or statements
        IDENTIFIER
        INT_LITERAL
        CHAR_LITERAL
        STRING_LITERAL */

        this.name = name;
        this.content = content;
    }

    //constructor for hardcoded tokens like program syntax
    public Token(String symbol)
    {
        //the symbol is found in the hashmap
        this.content = symbol;
        this.name = mapping.get(symbol);
    }

    static {
        //initialize the hashmap
        String filepath = "src/tokenMapping.txt";
        try {
            setTokenMapping(filepath);
        } catch (IOException e) {}
    }

    private static void setTokenMapping(String filepath) throws IOException
    {
        byte[] rawdata = Files.readAllBytes(Paths.get(filepath));
        String rawstring = new String(rawdata);

        String[] lineDelimited = rawstring.split("\n");

        for (String line: lineDelimited)
        {
            line = line.trim();
            if (line.startsWith("#") || line.length()==0) continue; // # is like a comment in the text file, skip blank lines as well

            String tokenContent = line.split(" ~ ")[0];
            String tokenName = line.split(" ~ ")[1];

            mapping.put(tokenName,tokenContent);
        }

        //   check
        for (String s:mapping.keySet())
            System.out.println("key: " + s + "\t value: " + mapping.get(s));
    }
}