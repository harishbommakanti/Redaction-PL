import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Lexer
{
    private StringBuilder str = new StringBuilder();
    private ArrayList<Token> tokenList = new ArrayList<Token>();
    List<String> list = new ArrayList<String>();
    
    Map<String,Token> map = Token.getMap();

    public Lexer(String filepath) throws IOException
    {
        readfile(filepath);
        preProcessString();
        processList();
    }

    private void readfile(String filepath) throws IOException
    {
        byte[] rawdata = Files.readAllBytes(Paths.get(filepath));
        String rawstring = new String(rawdata);

        //remove all whitespaces
        rawstring = rawstring.trim();
        str.append(rawstring);    
        /*test
        System.out.println(str.toString());*/
    }

    //Converts StringBuilder String into an ArrayList to be tokenized
    private void preProcessString() throws IOException
    {
        String[] s = str.toString().split("\n");
        Collections.addAll(list,s);
        
        /*for(String a : list)
            System.out.println(a); */
    }

    //this parses the string formed in readfile and pushes tokens in the queue
    private void processList()
    {
        //assume the structure of the program is good for now. if its not, create errors and exceptions later
        
        //adds all tokens to arraylist
        for(int i = 0; i < list.size(); i++) 
        { 
            String [] arr = list.get(i).split(" ");
            for(int j = 0; j < arr.length; j++) 
            {
                if(map.get(arr[j]) == null)
                {
                    //logic to be added
                    tokenList.add(Token.IDENTIFIER);    
                }
                else 
                {
                    tokenList.add(map.get(arr[j]));
                }
            }
        }
       
        //print out index, token, and token identity
        for(int z = 0; z < tokenList.size(); z++) 
        {
            System.out.println(z+ ") "+ tokenList.get(z)+ " : "+ Token.retIdentity(tokenList.get(z)));
        }

        //Test to print queue key and values
        //System.out.println(map.toString());
        //System.out.println(tokenList.toString());
    }
}