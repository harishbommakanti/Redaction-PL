package SyntacticAnalysis.ParseTreeClasses;

import LexicalAnalysis.Token;

public abstract class Expression
{
    /*interface for the visitor style OOP design pattern
    TLDR of visitor: allows you to both define new classes (binary, unary etc)
    AS WELL AS define new methods to each class, efficiently. other paradigms are usually one or the other
    */
    abstract <R> R accept(Visitor<R> visitor);

    interface Visitor<R>
    {
        R visitBinaryExpression(Binary expr);
        R visitGroupingExpression(Grouping expr);
        R visitLiteralExpression(Literal expr);
        R visitUnaryExpression(Unary expr);
    }

    //using grammers.txt, define subclasses here inside that represent
    //ways a numerical expression can be formed
    public static class Binary extends Expression
    {
        final Expression left;
        final Token operator;
        final Expression right;

        public Binary(Expression left, Token operator, Expression right)
        {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitBinaryExpression(this);
        }
    }

    public static class Grouping extends Expression
    {
        final Expression expression;
        public Grouping(Expression expression)
        {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitGroupingExpression(this);
        }
    }

    public static class Literal extends Expression
    {
        final Object value;

        public Literal(Object value)
        {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitLiteralExpression(this);
        }
    }

    public static class Unary extends Expression
    {
        final Token operator;
        final Expression right;

        public Unary(Token operator, Expression right)
        {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitUnaryExpression(this);
        }
    }
}