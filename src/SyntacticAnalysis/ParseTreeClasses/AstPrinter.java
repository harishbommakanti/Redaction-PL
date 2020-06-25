package SyntacticAnalysis.ParseTreeClasses;

import SyntacticAnalysis.ParserRecursiveDescent;

//Will be used throughout debugging the syntax tree producer by printing the output of the program
public class AstPrinter implements Expression.Visitor<String>
{
    //implements an interface, needs to provide implementations for each
    public String print(Expression expr)
    {
        //implements the visitor interface
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpression(Expression.Binary expr) {
        //print in the following form: '2+3' --> '+ 2 3', called prefix notation
        return parenthesize(expr.operator.content, expr.left, expr.right);

        //notice how the visitor class is working. visitBinaryNumExpr is defined as taking
        //in a Binary expr, so now when you call on expr, you are routed towards the Binary subclass. neato
    }

    @Override
    public String visitGroupingExpression(Expression.Grouping expr) {
        // '(42.5)' would be printed as 'group (42.5)'
        return parenthesize("group",expr.expression);
    }

    @Override
    public String visitLiteralExpression(Expression.Literal expr) {
        //simple, a literal is very basic so either print it or not
        if (expr.value == null) return "null";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpression(Expression.Unary expr) {
        //unary is like -10, - is the operator and 10 is the expression
        return parenthesize(expr.operator.content,expr.right);
    }

    private String parenthesize(String name, Expression... exprs)
    {
        StringBuilder build = new StringBuilder();

        //start off with a ( and the name of the string
        build.append("(").append(name);

        for (Expression expr : exprs)
        {
            build.append(" ");
            build.append(expr.accept(this));
            //^ this method routes to ASTPrinter's own implementation of visitExpression, very cool
        }

        build.append(")");

        return build.toString();

        //final output is like '1+2' --> (+ 1 2)
    }
}
