package code.model;

public class Token {
    private TokenType type;
    private String value; // Stores the actual text of the token
    private int lineNumber; // For error reporting

    public Token(TokenType type, String value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    // Getters for type, value, lineNumber here

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
