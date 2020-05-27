import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Lexer
{
    //for holding a 1 line representation of the source code
    private StringBuilder str = new StringBuilder();
    List<String> list = new ArrayList<String>();
    Map<String,String> mapping = Token.retMap();

    //the result of Lexer.java
    private ArrayList<Token> tokenList = new ArrayList<Token>();

    public Lexer(String filepath) throws IOException
    {
        readSourceCode(filepath);
        preProcessString();
        processList();
        //printTokenization();
    }

    private void readSourceCode(String filepath) throws IOException
    {
        byte[] rawdata = Files.readAllBytes(Paths.get(filepath));
        String rawstring = new String(rawdata);
        //remove all whitespaces
        rawstring = rawstring.trim();
        str.append(rawstring);    
    }

    //Converts StringBuilder String into an ArrayList to be tokenized
    private void preProcessString() throws IOException
    {
        String[] s = str.toString().split("\n");
        Collections.addAll(list,s);
    }

    //this parses the string formed in readfile and pushes tokens in the queue
    private void processList()
    {        
        //adds all tokens to arraylist
        for(int i = 0; i < list.size(); i++) 
        { 
            String [] arr = list.get(i).split(" ");
            for(int j = 0; j < arr.length; j++) 
            {
                if(!mapping.containsValue(arr[j]))
                {
                    //case of it being an integer
                    boolean isInt = false;
                    try {
                        int int_literal = Integer.parseInt(arr[j]);
                        isInt = true; //if it gets to this point, there was no error, so isInt = true
                    } catch (NumberFormatException e) {}
                    if (isInt)
                    {
                        tokenList.add(new Token("INT_LITERAL",arr[j]));
                    } 
                    //case of it being a String
                    else if(arr[j].charAt(0) == '"' && arr[j].charAt(arr[j].length()-1) == '"')
                    {
                        tokenList.add(new Token("STRING_LITERAL",arr[j]));
                    }
                    //case of it being a char                   
                    else if(arr[j].charAt(0) == '\'' && arr[j].charAt(arr[j].length()-1) == '\'')
                    {
                        tokenList.add(new Token("CHAR_LITERAL",arr[j]));
                    }
                    else
                    {
                        //if not identified as an int, String, or char, it must be a indentifier
                        //however, logic to be added to verify if identifier name is valid
                        tokenList.add(new Token("IDENTIFIER",arr[j]));
                    }
                }
                else 
                {
                    //it was found in the mapping --> simple search of the hashmap in the token class
                    tokenList.add(new Token(arr[j]));
                }
            }
        }
    }

    //print out index, token, and token identity
    private void printTokenization()
    {
       for(Token t : tokenList)
        {
            System.out.println("name: " + t.name + "\tcontent: " + t.content);
        }
    }

    //the result of the lexing phase: returns a list of tokens
    public ArrayList<Token> tokenize()
    {
        return tokenList;
    }

    //will go through the token list and inert variable/functions names wherever appropriate
    public void insertAllSymbolTables(SymbolTable st)
    {
    }
}