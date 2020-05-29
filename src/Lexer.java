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
        printTokenization();
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
                    recurse(arr[j], 1);                    
                }
                else 
                {
                    //it was found in the mapping --> simple search of the hashmap in the token class
                    tokenList.add(new Token(arr[j]));
                }
            }
        }
    }

    private void addToken(String str)
    {
        boolean isInt = false;
        try 
        {
            int int_literal = Integer.parseInt(str);
            isInt = true; //if it gets to this point, there was no error, so isInt = true
        } 
        catch (NumberFormatException e) {}
        if (isInt)
        {
            tokenList.add(new Token("INT_LITERAL",str));
        } 
        //case of it being a String
        else if(str.charAt(0) == '"' && str.charAt(str.length()-1) == '"')
        {
            tokenList.add(new Token("STRING_LITERAL",str));
        }
        //case of it being a char                   
        else if(str.charAt(0) == '\'' && str.charAt(str.length()-1) == '\'')
        {
            tokenList.add(new Token("CHAR_LITERAL",str));
        }
        else
        {
            //if not identified as an int, String, or char, it must be a indentifier
            //however, logic to be added to verify if identifier name is valid
            tokenList.add(new Token("IDENTIFIER",str));
        }
    }


    private void recurse(String str, int index)
    {
        if (index == -1)
        {
            addToken(str);
            return;
        } 
        else
        {
            recurse(str.substring(0,index),findIndexOfSymbol(str.substring(0,index)));
            addToken(str);
            recurse(str.substring(index+1),findIndexOfSymbol(str.substring(index+1)));
        }
    }


    private int findIndexOfSymbol(String str)
    {
        for(int i = 0; i < str.length(); i++)
        {
            if(mapping.containsValue(str.substring(i,i+1)))
                return i;
        }
        return -1;
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

















































    // \/ \/ \/ is part of the Parser, not even needed in the Lexer
    /*
    //will go through the token list and inert variable/functions names wherever appropriate
    public void insertAllSymbolTables(SymbolTable st)
    {
        SymbolTable currentSymbolTable = st;
        for (int i = 0; i < tokenList.size(); i++)
        {
            Token currToken = tokenList.get(i);
            if (currToken.name.equals("PROGRAM_BEGIN") || currToken.name.equals("PROGRAM_END")) //these tokens aren't really important for symbol tables
                continue;

            //initialization of a variable
            if (currToken.name.equals("LET"))
            {
                Token identifierToken = tokenList.get(i+1);
                String identifierName = identifierToken.name;
                //token at i+2 will be an = sign, irrelevant

                //it may be like 'let a = b + c / d' : the value of the array would be more than just a literal etc
                List<Token> identifierValueTokens = new ArrayList<Token>();
                int tempIndex = i+3;
                Token tempToken = tokenList.get(tempIndex);
                while(!tempToken.name.equals("TYPE")) //add value tokens to the identifierValueTokens list while it doesnt move on to the type signature
                {
                    identifierValueTokens.add(tempToken);
                    tempIndex++;
                }

                //now, identifierValueTokens has all the tokens to assign to the current identifier
                //token at tempIndex will now be TYPE, so look at index (tempIndex+1)
                String type = tokenList.get(tempIndex+1).name;

                //if the type is a char literal or string,
                String value = "";
                if (type.equals("CHAR"))
                    value = calculateCharExpression(identifierValueTokens); //TODO
                else if (type.equals("STRING"))
                    value = calculateStringExpression(identifierValueTokens); //TODO
                else //its an it
                    value = calculateMathExpression(identifierValueTokens); //TODO



                //at the very end, after all that parsing is done, add it to the current symbol table
                Map<String,String> newEntry = new HashMap();
                newEntry.put("type",type);
                newEntry.put("value",value);
                currentSymbolTable.data.put(identifierName,newEntry);

                //update i to go to the next statement
                i = tempIndex + 2;
            }
        }
    }
     */
}