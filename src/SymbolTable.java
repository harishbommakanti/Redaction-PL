import java.util.*;

public class SymbolTable
{
    //the parent symbol table from which it inherits variables and functions
    public SymbolTable parent = null;

    //the children of the current table: nested functions which have access to this table's data
    //entries will have a key of their name (func1, func2 etc) and the associated data will be the symbol table
    public Map<String,SymbolTable> children = null;

    //the name, ie 'Global' or the function name for scope
    public String name = null;

    //each entry will be the identifier (var/function name) and pieces of data alongside it: for now its type and value
    //the type and value will be stored in a hashmap relating to the name
    public Map<String,Map<String,String>> data = new HashMap();
    //          ^name     ^"value": ^"4" is an example

    public SymbolTable(String name)
    {
        this.name = name;
    }
}