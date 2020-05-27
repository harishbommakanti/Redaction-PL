import java.util.HashMap;
import java.util.Map;

public enum Token
{
    IDENTIFIER(" "), //this is like function names or var names
                        //not functional at the moment

    //Variable assignment
    LET("let"),
    INT("int"),
    CHAR("char"),
    CONST("const"),
    TYPE("type"),
    ASSIGNMENT_EQUALS("="),
    LITERAL("5"),

    //Operators
    PLUS("+"),
    MINUS("-"),
    DIVIDE("/"),
    MULTIPLY("*"),
    MODULUS("%"),

    //Conditionals
    GREATER_THAN(">"),
    GREATER_OR_EQUAL_TO(">="),
    LESS_THAN("<"),
    LESS_OR_EQUAL_TO("<="),
    NOT("!"),
    CONDITIONAL_EQUALS("=="),

    //Program control
    IF("if"),
    ELSE("else"),
    ELIF("elif"),
    LOOP("loop"),
    WHILE("while"),
    PROGRAM_BEGIN("{begin}"),
    PROGRAM_END("{end}"),
    COMMENT_START("/*"),
    COMMENT_END("*/"),

    //Miscellaneous symbols
    DOLLAR_SIGN("$"),
    OPEN_BRACKET("{"),
    CLOSE_BRACKET("}"),
    LEFT_PAREN("("),
    RIGHT_PAREN(")");

    private String identifier;
    private static final Map<String, Token> map = new HashMap<>();

    private Token(String identifier) {
        this.identifier = identifier;
    }

    static {
        for (Token toke : Token.values()) {
            map.put(toke.getIdentifier(), toke);
        }
    }

    public static Map<String, Token> getMap() {
        return map;
    }

    private String getIdentifier() {
        return identifier;
    }

    public static String retIdentity(Token t) {
        return t.identifier;
    }



}