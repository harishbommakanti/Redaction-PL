package SyntacticAnalysis;
import java.util.*;
/*
Due to scoping, we will need a tree-like structure
for the symbol table. The most specific scopes (if statements, method calls etc,
will be the leaves while the most general scopes (global) will be near/at the root
 */

public class SymbolTable
{
    //the parent symbol table from which it inherits variables and functions
    public SymbolTable parent;

    //the children of the current table: nested functions which have access to this table's data
    public List<SymbolTable> children;

    //the name, ie 'Global' or the function name for scope
    public String scopeName = null;

    //each entry will be the identifier (var/function name) and pieces of data alongside it
    public Map<String,IdentifierData> data = new HashMap();
    //          ^varname  ^"value": ^"4" is an example

    public SymbolTable(String scopeName, SymbolTable parent)
    {
        this.scopeName = scopeName;
        this.parent = parent;
    }
}