import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Lexer
{
    //for holding a 1 line representation of the source code
    private StringBuilder str = new StringBuilder();
    List<String> list = new ArrayList<String>();

    //the result of Lexer.java
    private ArrayList<Token> tokenList = new ArrayList<Token>();

    public Lexer(String filepath) throws IOException
    {
        readSourceCode(filepath);
        preProcessString();
        processList();
        printTokenization();
    }

    private void readSourceCode(String filepath) throws IOException
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
                if(Token.mapping.containsKey(arr[j]))
                {
                    //it is an IDENTIFIER, INT_LITERAL, CHAR_LITERAL, or STRING_LITERAL
                    //deal with the latter 2 later

                    //case of it being an integer
                    boolean isInt = false;
                    try {
                        int int_literal = Integer.parseInt(arr[j]);
                        isInt = true; //if it gets to this point, there was no error, so isInt = true
                    } catch (NumberFormatException e) {}

                    if (isInt)
                    {
                        tokenList.add(new Token("INT_LITERAL",arr[j]));
                    } else
                    {
                        tokenList.add(new Token("IDENTIFIER",arr[j]));
                    }

                    //TODO deal with cases of it being a char literal or string literal
                }
                else 
                {
                    //it was found in the mapping --> simple search of the hashmap in the token class
                    tokenList.add(new Token(arr[j]));
                }
            }
        }
    }

    private void printTokenization()
    {
        //print out index, token, and token identity
        for(Token t : tokenList)
        {
            System.out.println("name: " + t.name + "\tcontent: " + t.content);
        }

        //Test to print queue key and values
        //System.out.println(map.toString());
        //System.out.println(tokenList.toString());
    }
}