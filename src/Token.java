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
    private static Map<String,String> mapping = new HashMap<String,String>();

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
        Object o = symbol;
        this.name = getKeyFromValue(mapping,o);
    }

    static {
        //initialize the hashmap
        String filepath = "src/tokenMapping.txt";
        try {
            setTokenMapping(filepath);
        } catch (IOException e) {}
    }

    //sets the map to have the key and value in the correct spot
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
    }
    //allows to get key from give value, O(n)
    private static String getKeyFromValue(Map<String, String> mapping2, Object value) {
        for (Object o : mapping2.keySet()) {
            if (mapping2.get(o).equals(value))
            return o.toString();
        }
        return null;
    }

    //returns map to Lexer class
    public static Map<String,String> retMap()
    {
        return mapping;
    }

}