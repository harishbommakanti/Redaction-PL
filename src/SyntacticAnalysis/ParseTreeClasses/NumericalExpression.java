package SyntacticAnalysis.ParseTreeClasses;

import LexicalAnalysis.Token;

public abstract class NumericalExpression
{
    /*interface for the visitor style OOP design pattern
    TLDR of visitor: allows you to both define new classes (binary, unary etc)
    AS WELL AS define new methods to each class, efficiently. other paradigms are usually one or the other
    */
    abstract <R> R accept(Visitor<R> visitor);

    interface Visitor<R>
    {
        R visitBinaryNumericalExpression(Binary expr);
        R visitGroupingNumericalExpression(Grouping expr);
        R visitLiteralNumericalExpression(Literal expr);
        R visitUnaryNumericalExpression(Unary expr);
    }

    //using grammers.txt, define subclasses here inside that represent
    //ways a numerical expression can be formed
    public static class Binary extends NumericalExpression
    {
        final NumericalExpression left;
        final Token operator;
        final NumericalExpression right;

        public Binary(NumericalExpression left, Token operator, NumericalExpression right)
        {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitBinaryNumericalExpression(this);
        }
    }

    public static class Grouping extends NumericalExpression
    {
        final NumericalExpression expression;
        public Grouping(NumericalExpression expression)
        {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitGroupingNumericalExpression(this);
        }
    }

    public static class Literal extends NumericalExpression
    {
        final Object value;

        public Literal(Object value)
        {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitLiteralNumericalExpression(this);
        }
    }

    public static class Unary extends NumericalExpression
    {
        final Token operator;
        final NumericalExpression right;

        public Unary(Token operator, NumericalExpression right)
        {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitUnaryNumericalExpression(this);
        }
    }
}