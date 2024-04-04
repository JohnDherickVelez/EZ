package code.model;

public enum TokenType {
    // Basic types
    DATATYPE,

    // Identifiers
    IDENTIFIER, INT, FLOAT,

    // Comments
    COMMENT,

    // Operators
    PLUS, MINUS, MULTIPLY, DIVIDE, // ... add others (MODULO, RELATIONAL, LOGICAL)

    // Special characters
    LPAREN, RPAREN, ASSIGN, DOLLAR, AMPERSAND, LBRACKET, RBRACKET,
    // ... others ($, &, etc.)

    // Keywords
    BEGIN, CODE, END, DISPLAY, CONTENT,// ... others if you add more

    // Literals
    INT_LITERAL, CHAR_LITERAL, BOOL_LITERAL, STRING_LITERAL,

    OPERATOR, // End of File
    VARIABLE, ENDLINE, EOF
}