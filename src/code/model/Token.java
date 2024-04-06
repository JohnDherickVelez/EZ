package code.model;

public class Token {
    private final TokenType type;
    private final String value;
    private boolean isReservedKey = false; // to handle reserved tokens later

    public Token(TokenType type, String value, boolean isReservedKey) {
        this.type = type;
        this.value = value;
        this.isReservedKey = isReservedKey;
    }
    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token {" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }

    public enum TokenType {
        BEGIN_CODE,
        DATATYPE,
        VARIABLE,
        ENDLINE,
        IDENTIFIER,
        OPERATOR,
        DELIMITER,
        COMMENT,
        WHITESPACE,
        OTHER
    }
    }

//    private TokenType type;
//    private String value; // Stores the actual text of the token
//    private int lineNumber; // For error reporting
//
//    public Token(TokenType type, String value, int lineNumber) {
//        this.type = type;
//        this.value = value;
//        this.lineNumber = lineNumber;
//    }
//
//    // Getters for type, value, lineNumber here
//
//    public TokenType getType() {
//        return type;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public int getLineNumber() {
//        return lineNumber;
//    }
