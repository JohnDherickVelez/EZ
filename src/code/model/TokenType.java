package code.model;



public enum TokenType {
    // Basic types
    INT, CHAR, BOOL, FLOAT,

    // Identifiers
    IDENTIFIER,

    // Comments
    COMMENT,

    // Operators
    PLUS, MINUS, MULTIPLY, DIVIDE, // ... add others (MODULO, RELATIONAL, LOGICAL)

    // Special characters
    LPAREN, RPAREN, ASSIGNMENT,DOLLAR, AMPERSAND, LBRACKET, RBRACKET,
     // ... others ($, &, etc.)

    // Keywords
    BEGIN, CODE, END, DISPLAY, // ... others if you add more

    // Literals
    INT_LITERAL, CHAR_LITERAL, BOOL_LITERAL, STRING_LITERAL,

    // End of File
    EOF
}