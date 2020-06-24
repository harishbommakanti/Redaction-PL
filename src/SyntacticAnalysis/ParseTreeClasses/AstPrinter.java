package SyntacticAnalysis.ParseTreeClasses;

//Will be used throughout debugging the syntax tree producer by printing the output of the program
class AstPrinter implements NumericalExpression.Visitor<String>
{
    //implements an interface, needs to provide implementations for each
    public String print(NumericalExpression expr)
    {
        //implements the visitor interface
        return expr.accept(this);
    }
}
