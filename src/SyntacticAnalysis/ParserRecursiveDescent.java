package SyntacticAnalysis;

import LexicalAnalysis.Token;

import java.util.*;

public class ParserRecursiveDescent
{
    private final List<Token> tokens; //tokenlist
    private int current = 0;

    public ParserRecursiveDescent(List<Token> tokens)
    {
        this.tokens = tokens;
    }
}