package Parser;

//meant to be the data structure that holds the auxiliary data for a variable/method inside a symbol table
public class IdentifierData
{
    /* pieces of data to store:
        - variable value/method return value
        - variable/return type
        - (scope is inferred from the name the curr node is)
        - constant or not boolean value
     */

    //if its a variable: initialized to false so that the only things initialized in the constructor are true
    //so that later on, if you do variable.isMethod, you get false
    public boolean isVariable=false;
    public String variableValue=null;
    public String variableType=null;
    public boolean isVariableConstant=false;

    //if its a method
    public boolean isMethod=false;
    public boolean isVoid=true;
    public String returnVal=null;
    public String returnType=null;

    //the variable constructor
    public IdentifierData(String variableValue,String variableType,boolean isVariableConstant)
    {
        isVariable = true;
        this.variableValue = variableValue;
        this.variableType = variableType;
        this.isVariableConstant = isVariableConstant;
    }

    //the method constructor
    public IdentifierData(boolean isVoid, String returnVal, String returnType)
    {
        isMethod = true;
        this.isVoid = isVoid;
        this.returnVal = returnVal;
        this.returnType = returnType;
    }
}