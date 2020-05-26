public enum TokenType
{
    IDENTIFIER, //this is like function names or var names

    //Variable assignment
    LET,
    INT,
    CHAR,
    CONST,
    TYPE,
    ASSIGNMENT_EQUALS,

    //Operators
    PLUS,
    MINUS,
    DIVIDE,
    MULTIPLY,
    MODULUS,

    //Conditionals
    GREATER_THAN,
    GREATER_OR_EQUAL_TO,
    LESS_THAN,
    LESS_OR_EQUAL_TO,
    NOT,
    CONDITIONAL_EQUALS,

    //Program control
    IF,
    ELSE,
    ELIF,
    LOOP,
    WHILE,
    PROGRAM_BEGIN,
    PROGRAM_END,
    COMMENT_START,
    COMMENT_END,

    //Miscellaneous symbols
    DOLLAR_SIGN,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    LEFT_PAREN,
    RIGHT_PAREN,
}