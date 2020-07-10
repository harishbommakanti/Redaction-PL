package LexicalAnalysis;

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

    //loads source code data into str
    private void readSourceCode(String filepath) throws IOException
    {
        byte[] rawdata = Files.readAllBytes(Paths.get(filepath));
        String rawstring = new String(rawdata);

        //take all the comments out with regex magic
        rawstring = rawstring.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
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
            String [] arr = list.get(i).split(" "); //split tokens based on " " to seperate things like 'type' and 'int' and identifiers/literals
            for(int j = 0; j < arr.length; j++) 
            {

                String curr = arr[j];
                //special cases to consider before anything else: operators with length 2 like ==, <=, >=
                //assume the programmer has simple code and only one of these operators appear per statement
                int symbolIndex = -1;
                String symbol = "";
                if (curr.contains("=="))
                {
                    symbolIndex = curr.indexOf("==");
                    symbol = "==";
                } else if (curr.contains(">="))
                {
                    symbolIndex = curr.indexOf(">=");
                    symbol = ">=";
                } else if (curr.contains("<="))
                {
                    symbolIndex = curr.indexOf("<=");
                    symbol = "<=";
                }

                if (symbolIndex != -1) //if one of the 3 special symbols was found
                {
                    detectForSymbolsAndBreakDownExp(curr.substring(0,symbolIndex)); //looks at left substring
                    tokenList.add(new Token(symbol)); //add token for symbol
                    detectForSymbolsAndBreakDownExp(curr.substring(symbolIndex+2)); //looks at right substring, needs +2 as the length of the character is 2

                    continue; //don't do the other conditional checks, move on to next token
                }


                if(!mapping.containsValue(curr)) //if the token isn't in the mapping: its a identifier or literal
                {
                    //need to deal with the case of it being a complex expression: aka `let a = b+c/d type int`

                    detectForSymbolsAndBreakDownExp(curr);
                }
                else //case for 1 character symbols
                {
                    //it was found in the mapping --> simple search of the hashmap in the token class
                    tokenList.add(new Token(curr));
                }
            }
        }
    }

    //need to deal with the case of it being a complex expression: aka `let a = b+c/d type int`
    private void detectForSymbolsAndBreakDownExp(String curr)
    {
        int indexOfFirstSymbol = findIndexOfSymbol(curr); //find index of first symbol
        curr = curr.replace(" ",""); //remove whitespace within the string to make life easier
        breakDownExpression(curr, indexOfFirstSymbol); //basically do in order transversal to get tokens in the right order
        //if no symbol is found, indexOfFirstSymbol will be -1, in which case breakDownExpression will just assign 1 token
        //and terminate with no further recursion
    }

    //meant for looking at user created expressions (identifiers/character literals) and adding them to tokenList
    private void addIdentifierOrLiteral(String str)
    {
        if (str.length() == 0) return; //case of "" being passed in as a string

        //case of an integer literal
        boolean isInt = false;
        try
        {
            int int_literal = Integer.parseInt(str);
            isInt = true; //if it gets to this point, there was no error, so isInt = true
        } catch (NumberFormatException e) {}
        if (isInt)
        {
            tokenList.add(new Token("INT_LITERAL", str));
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


    //keeps splitting the input string into 2 parts and recurse over each part until only strings
    //without symbols are found. the symbols are added to the token list and the identifiers/literals are
    //also added in correct order
    private void breakDownExpression(String str, int index)
    {
        if (index == -1)
        {
            addIdentifierOrLiteral(str);
            return;
        } 
        else
        {
            breakDownExpression(str.substring(0,index),findIndexOfSymbol(str.substring(0,index)));
            tokenList.add(new Token(str.substring(index,index+1)));
            breakDownExpression(str.substring(index+1),findIndexOfSymbol(str.substring(index+1)));
        }
    }

    //returns the index of the first occurence of a symbol, or -1 if no index was found
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
        //the printTokenization method is just for debugging, not really needed after lexical phase
        //printTokenization();
        return tokenList;
    }
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