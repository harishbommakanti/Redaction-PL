package SyntacticAnalysis.ParseTreeClasses;

import SyntacticAnalysis.ParserRecursiveDescent;
import SyntacticAnalysis.ParseTreeClasses.Expression.Binary;
import SyntacticAnalysis.ParseTreeClasses.Expression.Grouping;
import SyntacticAnalysis.ParseTreeClasses.Expression.Literal;
import SyntacticAnalysis.ParseTreeClasses.Expression.Unary;

import LexicalAnalysis.Token;

public class Interpreter implements Expression.Visitor<Object> {

    void interpret(Expression expression) {
        try {
          Object value = evaluate(expression);
          System.out.println(stringify(value));
        } catch (RuntimeError error) {
          //ParserRecursiveDescent.runtimeError(error);
        }
      }

    @Override
    public Object visitBinaryExpression(Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right); 

        switch (expr.operator.name) {
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
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                } 
                if (left instanceof String && right instanceof String) {
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

    @Override
    public Object visitGroupingExpression(Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpression(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpression(Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.name) {
        case "NOT":
            return !isTruthy(right);
          case "MINUS":
            checkNumberOperand(expr.operator, right);
            return -(double)right;
        }
    
        // Unreachable.
        return null;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) 
            return;
        throw new RuntimeError(operator, "Operand must be a number.");
      }

    private void checkNumberOperands(Token operator,Object left, Object right) {
        if (left instanceof Double && right instanceof Double) 
            return;
        throw new RuntimeError(operator, "Operands must be numbers.");
        }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
      }

    private boolean isEqual(Object a, Object b) {
        // nil is only equal to nil.
        if (a == null && b == null) return true;
        if (a == null) return false;
    
        return a.equals(b);
      }

      private String stringify(Object object) {
        if (object == null) return "nil";
    
        // Hack. Work around Java adding ".0" to integer-valued doubles.
        if (object instanceof Double) {
          String text = object.toString();
          if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
          }
          return text;
        }
    
        return object.toString();
      }

    private Object evaluate(Expression expr) {
        return expr.accept(this);
      }
    
}