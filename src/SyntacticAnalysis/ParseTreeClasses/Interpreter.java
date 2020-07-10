package SyntacticAnalysis.ParseTreeClasses;

import SyntacticAnalysis.ParserRecursiveDescent;

import LexicalAnalysis.Token;

public class Interpreter implements Expression.Visitor<Object>
{
    //public method available to the driver class to interpret an expression
    public void interpret(Expression expression)
    {
        try
        {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        }
        catch (RuntimeError error)
        {
            System.out.println("Runtime error in Interpreter.java");
            //ParserRecursiveDescent.runtimeError(error);
        }
    }

    @Override
    public Object visitBinaryExpression(Expression.Binary expr)
    {
        //binary is like 4+3, 3-(4*x), so you need to break it into left, operator, right
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        //for each case of the operator
        switch (expr.operator.name)
        {
            case "GREATER_THAN":
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            case "GREATER_OR_EQUAL_TO":
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            case "LESS_THAN":
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            case "LESS_OR_EQUAL_TO":
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
            case "MINUS":
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            case "PLUS":
                if (left instanceof Double && right instanceof Double)
                {
                    return (double)left + (double)right;
                }
                if (left instanceof String && right instanceof String)
                {
                  return (String)left + (String)right;
                }
                throw new RuntimeError(expr.operator,"Operands must be two numbers or two strings.");
            case "DIVIDE":
                checkNumberOperands(expr.operator, left, right);
                return (double)left / (double)right;
            case "MULTIPLY":
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            case "NOT_EQUAL_TO":
                return !isEqual(left, right);
            case "CONDITIONAL_EQUALS":
                return isEqual(left, right);
        }
        // Unreachable.
        return null;
    }

    //also trivial, return the evaluation of the inside expression
    @Override
    public Object visitGroupingExpression(Expression.Grouping expr)
    {
        return evaluate(expr.expression);
    }

    //trivial, just return the value for a literal
    @Override
    public Object visitLiteralExpression(Expression.Literal expr)
    {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpression(Expression.Unary expr)
    {
        //unary is like - 4 or -(6+3), need to take the right side as an expression and evaluate it
        //operator is the - or + or !, so have a case for each of those
        Object right = evaluate(expr.right);

        switch (expr.operator.name)
        {
            case "NOT":
                return !isTruthy(right);
            case "MINUS":
                checkNumberOperand(expr.operator, right);
                return -(double) right;
        }

        // Unreachable.
        return null;
    }

    private void checkNumberOperand(Token operator, Object operand)
    {
        if (operand instanceof Double)
            return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator,Object left, Object right)
    {
        if (left instanceof Double && right instanceof Double)
            return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    //an object to see if an object is 'falsey' or 'truthey', handwavey definitions to define empty/null/bool values
    private boolean isTruthy(Object object)
    {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    //simple utility methods to check if 2 objects are equal to eachother
    private boolean isEqual(Object a, Object b)
    {
        // nil is only equal to nil.
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }

    //returns the string value evaluation of a current expression
    private String stringify(Object object)
    {
        if (object == null) return "null";
        // Hack. Work around Java adding ".0" to integer-valued doubles.
        if (object instanceof Double)
        {
            String text = object.toString();
            if (text.endsWith(".0"))
            {
                text = text.substring(0, text.length() - 2); //just show the integer part of it
            }
            return text;
        }

        return object.toString();
    }

    //follows visitor design pattern and evaluates the curr expression
    private Object evaluate(Expression expr)
    {
        return expr.accept(this);
    }
}